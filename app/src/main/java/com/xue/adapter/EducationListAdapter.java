package com.xue.adapter;

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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

        private TextView school;

        private TextView major;

        private TextView academic;

        private TextView describe;

        private TextView date;

        public ViewHolder(ViewGroup itemView) {
            super(LayoutInflater.from(itemView.getContext()).inflate(R.layout.item_educational_timeline, itemView, false));
            findView();
        }

        private void findView() {
            school = itemView.findViewById(R.id.school);
            major = itemView.findViewById(R.id.major);
            academic = itemView.findViewById(R.id.academic);
            describe = itemView.findViewById(R.id.describe);
            date = itemView.findViewById(R.id.date);
        }

        void fillData(final UserEducationInfo.Education education, final AdapterOnItemClickCallback<UserEducationInfo.Education> callback) {
            date.setText(education.getBeginAt() + " - " + education.getEndAt());
            school.setText("学校：" + education.getSchoolName());
            major.setText("专业：" + education.getMajorName());
            academic.setText("学历：" + education.getAcademicName());
            if (!TextUtils.isEmpty(education.getDescribe())) {
                describe.setVisibility(View.VISIBLE);
                describe.setText(education.getDescribe());
            } else {
                describe.setVisibility(View.GONE);
            }

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
