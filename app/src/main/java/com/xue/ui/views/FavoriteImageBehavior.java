package com.xue.ui.views;

import android.content.Context;
import android.os.Build;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;

import com.elianshang.tools.UITool;

public class FavoriteImageBehavior extends CoordinatorLayout.Behavior<ImageView> {
    private int maxScrollDistance;
    private float maxChildWidth;
    private float minChildWidth;

    private int toolbarHeight;
    private int statusBarHeight;
    private int appbarStartPoint;
    private int marginRight;

    private ImageView mFinalView;

    public FavoriteImageBehavior(Context context, ImageView finalView, AttributeSet attrs) {
        super(context, attrs);
        //计算出头像的最小宽度
        minChildWidth = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24, context.getResources().getDisplayMetrics());
        //计算出toolbar的高度
        toolbarHeight = context.getResources().getDimensionPixelSize(android.support.design.R.dimen.abc_action_bar_default_height_material);
        //计算出状态栏的高度
        statusBarHeight = getStatusBarHeight(context);
        //计算出头像居右的距离
        marginRight = UITool.dipToPx(context, 55);

        this.mFinalView = finalView;
    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, ImageView child, View dependency) {
//        Log.d(TAG, "layoutDependsOn");
        //确定依赖关系，这里我们用作头像的ImageButton相依赖的是AppBarLayout，也就是ImageButton跟着AppBarLayout的变化而变化。
        return dependency instanceof AppBarLayout || dependency instanceof CollapsingToolbarLayout;
    }

    private int startX;
    private int startY;

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, ImageView child, View dependency) {
        //这里的dependency就是布局中的AppBarLayout，child即显示的头像
        if (maxScrollDistance == 0) {
            //也就是第一次进来时，计算出AppBarLayout的最大垂直变化距离
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
                maxScrollDistance = dependency.getBottom() - toolbarHeight - statusBarHeight;
            else
                maxScrollDistance = dependency.getBottom() - toolbarHeight;
        }
        //计算出appbar的开始的y坐标
        if (appbarStartPoint == 0)
            appbarStartPoint = dependency.getBottom();

        //计算出头像的宽度
        if (maxChildWidth == 0)
            maxChildWidth = Math.min(child.getWidth(), child.getHeight());

        //计算出头像的起始x坐标
        if (startX == 0)
            startX = (int) (dependency.getWidth() / 2 - maxChildWidth - UITool.dipToPx(parent.getContext(), 20));
        //计算出头像的起始y坐标
        if (startY == 0)
            startY = (int) (dependency.getBottom() - maxChildWidth - UITool.dipToPx(parent.getContext(), 10));

        //计算出appbar已经变化距离的百分比，起始位置y减去当前位置y，然后除以最大距离
        float expandedPercentageFactor = (appbarStartPoint - dependency.getBottom()) * 1.0f / (maxScrollDistance * 1.0f);

        if (mFinalView != null) {
            if (expandedPercentageFactor == 1) {
                mFinalView.setVisibility(View.VISIBLE);
            } else {
                mFinalView.setVisibility(View.GONE);
            }
        }

        //根据上面计算出的百分比，计算出头像应该移动的y距离,通过百分比乘以最大距离
        float moveY = expandedPercentageFactor * (startY - toolbarHeight / 2 - minChildWidth / 2);
        //根据上面计算出的百分比，计算出头像应该移动的y距离
        float moveX = expandedPercentageFactor * (dependency.getWidth() - startX - marginRight - 3 * minChildWidth);
        //更新头像的位置
        child.setX(startX + moveX);
        child.setY(startY - moveY);
        //计算出当前头像的宽度
//          float nowWidth = maxChildWidth - ((maxChildWidth - minChildWidth) * expandedPercentageFactor);
        //更新头像的宽高
//          CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) child.getLayoutParams();
//          params.height = params.width = (int) nowWidth;
//          child.setLayoutParams(params);


        return true;
    }

    private int getStatusBarHeight(Context context) {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }
}