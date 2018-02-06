package com.xue.adapter;

import android.view.View;

public interface AdapterOnItemClickCallback<T> {

    public void onItemClick(T t, View view);
}
