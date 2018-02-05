package com.xue.adapter;

import com.xue.bean.UserEducationInfo;
import com.xue.support.view.HeaderFooterRecyclerViewAdapter;

public class EducationFooterListAdapter extends HeaderFooterRecyclerViewAdapter<EducationListAdapter> {

    public EducationFooterListAdapter(EducationListAdapter base) {
        super(base);
    }

    public void setDataList(UserEducationInfo dataList) {
        getWrappedAdapter().setDataList(dataList);
    }

    public void setCallback(AdapterOnItemClickCallback<UserEducationInfo.Education> callback) {
        getWrappedAdapter().setCallback(callback);
    }
}