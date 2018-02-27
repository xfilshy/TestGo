package com.xue.support.slideback.activity;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.IdRes;
import android.view.View;

import com.xue.support.R;
import com.xue.support.slideback.SlideBackLayout;
import com.xue.support.slideback.Utils;

/*package*/ class SlideBackActivityHelper {
    private final Activity mActivity;

    private SlideBackLayout mSlideBackLayout;

    public SlideBackActivityHelper(Activity activity) {
        mActivity = activity;
    }

    public void onCreate() {
        // 将window和DecorView的背景置为透明
        mActivity.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mActivity.getWindow().getDecorView().setBackgroundDrawable(null);

        mSlideBackLayout = (SlideBackLayout) View.inflate(mActivity, R.layout.slideback_layout, null);
        mSlideBackLayout.addSlideListener(new SlideBackLayout.SlideListener() {
            @Override
            public void onScrollStateChange(@SlideBackLayout.State int state, float scrollPercent) {
            }

            @Override
            public void onEdgeTouch(@SlideBackLayout.Edge int edgeFlag) {
                Utils.convertActivityToTranslucent(mActivity);
            }

            @Override
            public void onScrollOverThreshold() {

            }
        });

        mActivity.overridePendingTransition(R.anim.anim_slide_in, R.anim.anim_none);
    }

    public void onPostCreate() {
        mSlideBackLayout.attachToActivity(mActivity);
    }

    public void onBackPressed() {
        mSlideBackLayout.scrollToFinishActivity2();
    }

    public <V extends View> V findViewById(@IdRes int id) {
        if (mSlideBackLayout != null) {
            return mSlideBackLayout.findViewById(id);
        }
        return null;
    }

    public SlideBackLayout getSlideBackLayout() {
        return mSlideBackLayout;
    }
}
