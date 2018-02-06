package com.xue.adapter;

import com.xue.bean.UserWorkInfo;
import com.xue.support.view.HeaderFooterRecyclerViewAdapter;

public class WorkFooterListAdapter extends HeaderFooterRecyclerViewAdapter<WorkListAdapter> {

    public WorkFooterListAdapter(WorkListAdapter base) {
        super(base);
    }

    public void setDataList(UserWorkInfo dataList) {
        getWrappedAdapter().setDataList(dataList);
    }

    public void setCallback(AdapterOnItemClickCallback<UserWorkInfo.Work> callback) {
        getWrappedAdapter().setCallback(callback);
    }
}