package com.xue.ui.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import com.elianshang.tools.UITool;
import com.xue.R;

public class ListFootView extends LinearLayout {

    private View loadingLayout;

    private View refreshLayout;

    private View finishLayout;

    public ListFootView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ListFootView(Context context) {
        super(context);
        init(context);
    }

    protected void init(Context context) {
        View view = UITool.inflate(context, R.layout.listview_foot, null);

        loadingLayout = view.findViewById(R.id.loading_layout);
        refreshLayout = view.findViewById(R.id.refresh_layout);
        finishLayout = view.findViewById(R.id.finish_layout);

        this.addView(view);
    }

    public void showLoading() {
        loadingLayout.setVisibility(View.VISIBLE);
        refreshLayout.setVisibility(View.INVISIBLE);
        finishLayout.setVisibility(INVISIBLE);
    }

    public void showRefresh() {
        loadingLayout.setVisibility(View.INVISIBLE);
        refreshLayout.setVisibility(View.VISIBLE);
        finishLayout.setVisibility(INVISIBLE);
    }

    public void showFinish() {
        loadingLayout.setVisibility(INVISIBLE);
        refreshLayout.setVisibility(INVISIBLE);
        finishLayout.setVisibility(VISIBLE);
    }

    public boolean isFinish() {
        return finishLayout.getVisibility() == VISIBLE;
    }

    public void destroy() {
        loadingLayout = null;
        refreshLayout = null;
        finishLayout = null;
        removeAllViewsInLayout();
    }
}
