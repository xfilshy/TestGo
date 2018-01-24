package com.xue.support.view;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

public abstract class HeaderFooterGridAdapter<T extends RecyclerView.Adapter> extends HeaderFooterRecyclerViewAdapter<T> {

    public HeaderFooterGridAdapter(T base, GridLayoutManager gridLayoutManager, final int spanCount) {
        super(base);
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (isHeader(getItemViewType(position))) {
                    return spanCount;
                } else if (isFooter(getItemViewType(position))) {
                    return spanCount;
                } else {
                    return 1;
                }
            }
        });
    }
}
