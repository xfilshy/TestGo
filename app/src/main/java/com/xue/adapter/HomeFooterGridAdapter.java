package com.xue.adapter;

import android.support.v7.widget.GridLayoutManager;

import com.xue.support.view.HeaderFooterGridAdapter;

public class HomeFooterGridAdapter extends HeaderFooterGridAdapter<HomeGridAdapter> {

    public HomeFooterGridAdapter(HomeGridAdapter base, GridLayoutManager gridLayoutManager, int spanCount) {
        super(base, gridLayoutManager, spanCount);
    }
}