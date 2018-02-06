package com.xue.adapter;

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.xue.R;
import com.xue.bean.UserWorkInfo;

public class WorkListAdapter extends RecyclerView.Adapter<WorkListAdapter.ViewHolder> {

    private UserWorkInfo dataList;

    private AdapterOnItemClickCallback<UserWorkInfo.Work> callback;

    public void setDataList(UserWorkInfo dataList) {
        this.dataList = dataList;
    }

    public void setCallback(AdapterOnItemClickCallback<UserWorkInfo.Work> callback) {
        this.callback = callback;
    }

    @Override
    public WorkListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(parent);
    }

    @Override
    public void onBindViewHolder(WorkListAdapter.ViewHolder holder, int position) {
        holder.fillData(dataList.get(position), callback);
    }

    @Override
    public int getItemCount() {
        if (dataList == null) {
            return 0;
        }
        return dataList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView company;

        private TextView industry;

        private TextView position;

        private TextView direction;

        private TextView describe;

        private TextView date;

        public ViewHolder(ViewGroup itemView) {
            super(LayoutInflater.from(itemView.getContext()).inflate(R.layout.item_work_timeline, itemView, false));
            findView();
        }

        private void findView() {
            company = itemView.findViewById(R.id.company);
            industry = itemView.findViewById(R.id.industry);
            position = itemView.findViewById(R.id.position);
            direction = itemView.findViewById(R.id.direction);
            describe = itemView.findViewById(R.id.describe);
            date = itemView.findViewById(R.id.date);
        }

        private void fillData(final UserWorkInfo.Work work, final AdapterOnItemClickCallback<UserWorkInfo.Work> callback) {
            date.setText(work.getBeginAt() + " - " + work.getEndAt());
            company.setText(work.getCompanyName());
            industry.setText(work.getIndustryName());
            position.setText(work.getPositionName());
            direction.setText(work.getDirectionName());

            if (!TextUtils.isEmpty(work.getDescribe())) {
                describe.setVisibility(View.VISIBLE);
                describe.setText(work.getDescribe());
            } else {
                describe.setVisibility(View.GONE);
            }
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (callback != null) {
                        callback.onItemClick(work , itemView);
                    }
                }
            });
        }
    }
}
