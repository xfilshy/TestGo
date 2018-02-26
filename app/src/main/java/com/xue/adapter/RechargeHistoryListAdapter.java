package com.xue.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.xue.R;
import com.xue.bean.WalletTradeList;

public class RechargeHistoryListAdapter extends RecyclerView.Adapter<RechargeHistoryListAdapter.HistoryViewHolder> {

    private WalletTradeList dataList;

    public void setDataList(WalletTradeList dataList) {
        this.dataList = dataList;
    }

    @Override
    public HistoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new HistoryViewHolder(parent);
    }

    @Override
    public void onBindViewHolder(HistoryViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 10;
    }

    public static class HistoryViewHolder extends RecyclerView.ViewHolder {

        public HistoryViewHolder(ViewGroup itemView) {
            super(LayoutInflater.from(itemView.getContext()).inflate(R.layout.item_recharge_history, itemView, false));
        }
    }
}
