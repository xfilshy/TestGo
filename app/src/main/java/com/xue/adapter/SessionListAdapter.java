package com.xue.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.elianshang.tools.DateTool;
import com.netease.nimlib.sdk.msg.model.RecentContact;
import com.xue.R;

import java.text.ParseException;
import java.util.List;

public class SessionListAdapter extends RecyclerView.Adapter<SessionListAdapter.SessionViewHolder> {

    private List<RecentContact> mDataList;

    private AdapterOnItemClickCallback<RecentContact> callback;

    private AdapterOnItemLongClickCallback<RecentContact> longClickCallback;

    public void setDataList(List<RecentContact> dataList) {
        this.mDataList = dataList;
    }

    public void setCallback(AdapterOnItemClickCallback<RecentContact> callback) {
        this.callback = callback;
    }

    public void setLongClickCallback(AdapterOnItemLongClickCallback<RecentContact> longClickCallback) {
        this.longClickCallback = longClickCallback;
    }

    @Override
    public SessionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new SessionViewHolder(parent);
    }

    @Override
    public void onBindViewHolder(SessionViewHolder holder, int position) {
        holder.fill(mDataList.get(position), callback, longClickCallback);
    }

    @Override
    public int getItemCount() {
        if (mDataList == null) {
            return 0;
        }
        return mDataList.size();
    }

    public static class SessionViewHolder extends RecyclerView.ViewHolder {

        private ImageView profile;

        private TextView number;

        private TextView name;

        private TextView message;

        private TextView date;

        public SessionViewHolder(ViewGroup itemView) {
            super(LayoutInflater.from(itemView.getContext()).inflate(R.layout.item_session_list_chat, itemView, false));

            findView();
        }

        private void findView() {
            profile = itemView.findViewById(R.id.profile);
            number = itemView.findViewById(R.id.number);
            name = itemView.findViewById(R.id.name);
            message = itemView.findViewById(R.id.message);
            date = itemView.findViewById(R.id.date);
        }

        private void fill(final RecentContact recentContact, final AdapterOnItemClickCallback<RecentContact> callback, final AdapterOnItemLongClickCallback<RecentContact> longClickCallback) {
            name.setText(recentContact.getContactId());
            int count = recentContact.getUnreadCount();
            if (count > 0) {
                String countString = count <= 99 ? String.valueOf(count) : "99+";
                if (countString.length() == 1) {
                    number.setTextSize(12);
                } else if (countString.length() == 2) {
                    number.setTextSize(11);
                } else {
                    number.setTextSize(9);
                }
                number.setText(countString);
                number.setVisibility(View.VISIBLE);
            } else {
                number.setVisibility(View.INVISIBLE);
            }
            message.setText(recentContact.getContent());
            try {
                date.setText(DateTool.longToString(recentContact.getTime(), "HH:mm"));
            } catch (ParseException e) {
                e.printStackTrace();
            }

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (callback != null) {
                        callback.onItemClick(recentContact, v);
                    }
                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (longClickCallback != null) {
                        longClickCallback.onItemLongClick(recentContact, v);
                    }
                    return true;
                }
            });
        }
    }
}
