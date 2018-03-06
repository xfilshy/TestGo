package com.xue.ui.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xue.R;
import com.xue.support.slideback.SwipeBackHelper;
import com.xue.ui.views.SoftInputLinearLayout;

/**
 * Created by xfilshy on 2018/1/17.
 */
public abstract class SwipeBackBaseActivity extends BaseActivity implements SwipeBackHelper.Delegate {

    enum Style {
        Primary,
        White
    }

    private LinearLayout mRootView;

    private ImageView mActionBackImageView;

    private TextView mActionTitleTextView;

    private TextView mActionRightTextView;

    protected SwipeBackHelper mSwipeBackHelper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        // 「必须在 Application 的 onCreate 方法中执行 BGASwipeBackHelper.init 来初始化滑动返回」
        // 在 super.onCreate(savedInstanceState) 之前调用该方法
        initSwipeBackFinish();
        super.onCreate(savedInstanceState);
        mSwipeBackHelper.executeForwardAnim();
    }

    private void createRootView() {
        SoftInputLinearLayout linearLayout = new SoftInputLinearLayout(this);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.setBackgroundResource(R.color.grey_50);
        linearLayout.setFitsSystemWindows(true);

        Toolbar toolbar = null;
        if (actionBarStyle() == Style.Primary) {
            toolbar = (Toolbar) LayoutInflater.from(this).inflate(R.layout.actionbar_simple_primary, null, false);
        } else if (actionBarStyle() == Style.White) {
            toolbar = (Toolbar) LayoutInflater.from(this).inflate(R.layout.actionbar_simple_white, null, false);
        }
        toolbar.setPadding(0, getStatusBarHeight(this), 0, 0);
        initActionBar(toolbar, actionBarTitle(), actionBarRight());
        linearLayout.addView(toolbar);

        mRootView = linearLayout;
    }

    private void initActionBar(Toolbar toolbar, String title, String right) {
        setSupportActionBar(toolbar);
        mActionBackImageView = toolbar.findViewById(R.id.back);
        mActionTitleTextView = toolbar.findViewById(R.id.title);
        mActionRightTextView = toolbar.findViewById(R.id.right);

        mActionTitleTextView.setText(title);
        mActionRightTextView.setText(right);
    }

    protected abstract boolean hasActionBar();

    protected abstract String actionBarTitle();

    protected abstract String actionBarRight();

    protected Style actionBarStyle() {
        return Style.Primary;
    }

    protected void setActionTitle(String titleText) {
        if (mActionTitleTextView != null) {
            mActionTitleTextView.setText(titleText);
        }
    }

    protected void setActionRight(String rightText) {
        if (mActionRightTextView != null) {
            mActionRightTextView.setText(rightText);
        }
    }

    protected void setActionRightVisibility(int visibility) {
        if (mActionRightTextView != null) {
            mActionRightTextView.setVisibility(visibility);
        }
    }

    public void closeActivity(View view) {
        onBackPressed();
    }

    public void rightAction(View view) {

    }

    public void setRequestDisallowInterceptTouchEventEnable(boolean isEnable) {
        mSwipeBackHelper.setRequestDisallowInterceptTouchEventEnable(isEnable);
    }

    @Override
    public void setContentView(View view, ViewGroup.LayoutParams params) {
        if (hasActionBar()) {
            createRootView();
            super.setContentView(mRootView, params);
        } else {
            super.setContentView(view, params);
        }
    }

    @Override
    public void setContentView(View view) {
        if (hasActionBar()) {
            createRootView();
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            mRootView.addView(view, layoutParams);
            super.setContentView(mRootView);
        } else {
            super.setContentView(view);
        }
    }

    @Override
    public void setContentView(int layoutResID) {
        View view = LayoutInflater.from(this).inflate(layoutResID, null, false);

        setContentView(view);
    }

    /**
     * 初始化滑动返回。在 super.onCreate(savedInstanceState) 之前调用该方法
     */
    private void initSwipeBackFinish() {
        mSwipeBackHelper = new SwipeBackHelper(this, this);

        // 设置滑动返回是否可用。默认值为 true
        mSwipeBackHelper.setSwipeBackEnable(true);
        // 设置是否仅仅跟踪左侧边缘的滑动返回。默认值为 true
        mSwipeBackHelper.setIsOnlyTrackingLeftEdge(true);
        // 设置是否是微信滑动返回样式。默认值为 true
        mSwipeBackHelper.setIsWeChatStyle(true);
        // 设置阴影资源 id。默认值为 R.drawable.bga_sbl_shadow
        mSwipeBackHelper.setShadowResId(R.drawable.bga_sbl_shadow);
        // 设置是否显示滑动返回的阴影效果。默认值为 true
        mSwipeBackHelper.setIsNeedShowShadow(true);
        // 设置阴影区域的透明度是否根据滑动的距离渐变。默认值为 true
        mSwipeBackHelper.setIsShadowAlphaGradient(true);
        // 设置触发释放后自动滑动返回的阈值，默认值为 0.3f
        mSwipeBackHelper.setSwipeBackThreshold(0.3f);
        // 设置底部导航条是否悬浮在内容上，默认值为 false
        mSwipeBackHelper.setIsWeChatStyle(false);
        mSwipeBackHelper.setIsNavigationBarOverlap(false);
        mSwipeBackHelper.setIsOnlyTrackingLeftEdge(false);
    }

    /**
     * 是否支持滑动返回。这里在父类中默认返回 true 来支持滑动返回，如果某个界面不想支持滑动返回则重写该方法返回 false 即可
     *
     * @return
     */
    @Override
    public boolean isSupportSwipeBack() {
        return true;
    }

    /**
     * 正在滑动返回
     *
     * @param slideOffset 从 0 到 1
     */
    @Override
    public void onSwipeBackLayoutSlide(float slideOffset) {
    }

    /**
     * 没达到滑动返回的阈值，取消滑动返回动作，回到默认状态
     */
    @Override
    public void onSwipeBackLayoutCancel() {
    }

    /**
     * 滑动返回执行完毕，销毁当前 Activity
     */
    @Override
    public void onSwipeBackLayoutExecuted() {
        mSwipeBackHelper.swipeBackward();
    }

    @Override
    public void onBackPressed() {
        // 正在滑动返回的时候取消返回按钮事件
        if (mSwipeBackHelper.isSliding()) {
            return;
        }
        mSwipeBackHelper.backward();
    }

    //获取状态栏高度
    private static int getStatusBarHeight(Context context) {
        int statusBarHeight = dip2px(context, 25);
        try {
            Class<?> clazz = Class.forName("com.android.internal.R$dimen");
            Object object = clazz.newInstance();
            int height = Integer.parseInt(clazz.getField("status_bar_height").get(object).toString());
            statusBarHeight = context.getResources().getDimensionPixelSize(height);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return statusBarHeight;
    }

    //根据手机的分辨率从 dp 的单位 转成为 px(像素)
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

}