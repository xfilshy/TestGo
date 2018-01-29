package com.xue.support.view;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class GridItemDecoration extends RecyclerView.ItemDecoration {

    private int mSpace;

    private int mSpanCount;

    public GridItemDecoration(int space, int spanCount) {
        this.mSpace = space;
        this.mSpanCount = spanCount;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        int flag = 0;
        int pos = parent.getChildLayoutPosition(view);
        if (pos % mSpanCount == 0) { // 最后一列
            flag += 1;
        }

        int count = parent.getAdapter().getItemCount();
        int m = count % mSpanCount;
        if (m == 0) {
            m = mSpanCount;
        }
        if (pos >= count - m) { // 最后一行
            outRect.bottom = 0;
            flag += 2;
        }

        if (flag == 0) {
            outRect.set(0, 0, mSpace, mSpace);
        } else if (flag == 1) {
            outRect.set(0, 0, 0, mSpace);
        } else if (flag == 2) {
            outRect.set(0, 0, mSpace, 0);
        } else if (flag == 3) {
            outRect.set(0, 0, 0, 0);
        }
    }
}
