
package com.xue.support.slideback.activity;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.xue.support.slideback.SlideBackLayout;

public class SlideBackActivity extends AppCompatActivity implements SlideBackActivityBase {
    private final SlideBackActivityHelper mHelper;

    public SlideBackActivity() {
        mHelper = new SlideBackActivityHelper(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mHelper.onCreate();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mHelper.onPostCreate();
    }

    @Override
    public void onBackPressed() {
        mHelper.onBackPressed();
    }

    @Override
    public <V extends View> V findViewById(@IdRes int id) {
        V v = super.findViewById(id);
        if (v == null)
            return mHelper.findViewById(id);
        return v;
    }

    @Override
    public SlideBackLayout getSlideBackLayout() {
        return mHelper.getSlideBackLayout();
    }

    @Override
    public void setSlideBackEnabled(boolean enable) {
        getSlideBackLayout().setGestureEnabled(enable);
    }

    @Override
    public void scrollToFinishActivity() {
        getSlideBackLayout().scrollToFinishActivity2();
    }
}
