package com.xue.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

public class DetailListAdapter extends RecyclerView.Adapter<DetailListAdapter.BaseViewHolder> {

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public static class BaseViewHolder extends RecyclerView.ViewHolder{

        public BaseViewHolder(View itemView) {
            super(itemView);
        }
    }
}