package com.xue.adapter;

import com.xue.bean.WalletTradeList;
import com.xue.support.view.HeaderFooterRecyclerViewAdapter;

public class RechargeHistoryFooterListAdapter extends HeaderFooterRecyclerViewAdapter<RechargeHistoryListAdapter> {

    public RechargeHistoryFooterListAdapter(RechargeHistoryListAdapter base) {
        super(base);
    }

    public void setDataList(WalletTradeList dataList) {
        getWrappedAdapter().setDataList(dataList);
    }
}
