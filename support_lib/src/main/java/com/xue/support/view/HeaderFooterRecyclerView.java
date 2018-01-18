package com.xue.support.view;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;

public class HeaderFooterRecyclerView extends RecyclerView {

    private HeaderFooterRecyclerViewAdapter<RecyclerView.Adapter> mHeaderFooterAdapter;

    public HeaderFooterRecyclerView(Context context) {
        super(context);
    }

    public HeaderFooterRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public HeaderFooterRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void setAdapter(RecyclerView.Adapter adapter) {
        if (mHeaderFooterAdapter == null) {
            mHeaderFooterAdapter = new HeaderFooterRecyclerViewAdapter(adapter);
        } else {
            HeaderFooterRecyclerViewAdapter<RecyclerView.Adapter> newAdapter = new HeaderFooterRecyclerViewAdapter(adapter);
            newAdapter.copyHeaders(mHeaderFooterAdapter.getHeaders());
            newAdapter.copyFooters(mHeaderFooterAdapter.getFooters());

            mHeaderFooterAdapter = newAdapter;
        }

        super.setAdapter(mHeaderFooterAdapter);
    }

    public void addHeaderView(View headerView) {
        if (mHeaderFooterAdapter != null) {
            mHeaderFooterAdapter.addHeader(headerView);
            mHeaderFooterAdapter.notifyDataSetChanged();
        }
    }

    public void addFooterView(View footerView) {
        if (mHeaderFooterAdapter != null) {
            mHeaderFooterAdapter.addFooter(footerView);
            mHeaderFooterAdapter.notifyDataSetChanged();
        }
    }

    public void removeHeaderView(View headerView) {
        if (mHeaderFooterAdapter != null) {
            mHeaderFooterAdapter.removeHeader(headerView);
            mHeaderFooterAdapter.notifyDataSetChanged();
        }
    }

    public void removeFooterView(View footerView) {
        if (mHeaderFooterAdapter != null) {
            mHeaderFooterAdapter.removeFooter(footerView);
            mHeaderFooterAdapter.notifyDataSetChanged();
        }
    }

    public int getHeaderViewCount() {
        if (mHeaderFooterAdapter != null) {
            return mHeaderFooterAdapter.getHeaderCount();
        }

        return 0;
    }

    public int getFooterViewCount() {
        if (mHeaderFooterAdapter != null) {
            return mHeaderFooterAdapter.getFooterCount();
        }

        return 0;
    }

    public void setHeaderVisibility(boolean shouldShow) {
        if (mHeaderFooterAdapter != null) {
            mHeaderFooterAdapter.setHeaderVisibility(shouldShow);
        }
    }

    public void setFooterVisibility(boolean shouldShow) {
        if (mHeaderFooterAdapter != null) {
            mHeaderFooterAdapter.setFooterVisibility(shouldShow);
        }
    }


}
