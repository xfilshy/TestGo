package com.xue.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xue.R;
import com.xue.bean.UserEducationInfo;

public class EducationListAdapter extends RecyclerView.Adapter<EducationListAdapter.ViewHolder> {

    private UserEducationInfo dataList;

    private AdapterOnItemClickCallback<UserEducationInfo.Education> callback;

    public void setDataList(UserEducationInfo dataList) {
        this.dataList = dataList;
    }

    public void setCallback(AdapterOnItemClickCallback<UserEducationInfo.Education> callback) {
        this.callback = callback;
    }

    @Override
    public EducationListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(parent);
    }

    @Override
    public void onBindViewHolder(EducationListAdapter.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        if (dataList == null) {
            return 0;
        }
        return dataList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(ViewGroup itemView) {
            super(LayoutInflater.from(itemView.getContext()).inflate(R.layout.item_educational_timeline, itemView, false));
        }

        void fillData(final UserEducationInfo.Education education, final AdapterOnItemClickCallback<UserEducationInfo.Education> callback) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (callback != null) {
                        callback.onItemClick(education);
                    }
                }
            });
        }
    }
}
