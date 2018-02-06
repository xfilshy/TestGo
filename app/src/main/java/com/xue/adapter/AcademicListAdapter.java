package com.xue.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.xue.R;
import com.xue.bean.AcademicList;

public class AcademicListAdapter extends RecyclerView.Adapter<AcademicListAdapter.ViewHolder> {

    private AcademicList dataList;

    private AdapterOnItemClickCallback<AcademicList.Academic> callback;

    public void setDataList(AcademicList dataList) {
        this.dataList = dataList;
    }

    public void setCallback(AdapterOnItemClickCallback<AcademicList.Academic> callback) {
        this.callback = callback;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(parent);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
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

        private TextView name;

        public ViewHolder(ViewGroup itemView) {
            super(LayoutInflater.from(itemView.getContext()).inflate(R.layout.item_academic, itemView, false));
            findView();
        }

        private void findView() {
            name = itemView.findViewById(R.id.name);
        }

        private void fillData(final AcademicList.Academic academic, final AdapterOnItemClickCallback<AcademicList.Academic> callback) {
            name.setText(academic.getName());
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (callback != null) {
                        callback.onItemClick(academic , itemView);
                    }
                }
            });
        }
    }
}
