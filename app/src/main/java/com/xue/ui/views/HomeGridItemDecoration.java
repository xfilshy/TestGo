package com.xue.ui.views;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class HomeGridItemDecoration extends RecyclerView.ItemDecoration {

    private int mSpace;

    private int mSpanCount;

    public HomeGridItemDecoration(int space, int spanCount) {
        this.mSpace = space;
        this.mSpanCount = spanCount;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        outRect.left = mSpace;
        outRect.bottom = mSpace;
        if (parent.getChildLayoutPosition(view) % mSpanCount == 0) {
            outRect.left = 0;
        }
    }
}
