package com.xue.adapter;

import android.support.v7.widget.GridLayoutManager;

import com.xue.bean.User;
import com.xue.bean.UserList;
import com.xue.support.view.HeaderFooterGridAdapter;

public class HomeFooterGridAdapter extends HeaderFooterGridAdapter<HomeGridAdapter> {

    public HomeFooterGridAdapter(HomeGridAdapter base, GridLayoutManager gridLayoutManager, int spanCount) {
        super(base, gridLayoutManager, spanCount);
    }

    public void setDataList(UserList dataList) {
        getWrappedAdapter().setDataList(dataList);
    }

    public void setCallback(AdapterOnItemClickCallback<User> callback) {
        getWrappedAdapter().setCallback(callback);
    }
}