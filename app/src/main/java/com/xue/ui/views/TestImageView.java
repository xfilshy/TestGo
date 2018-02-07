package com.xue.ui.views;

import android.content.Context;
import android.graphics.Canvas;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;

public class TestImageView extends AppCompatImageView {


    public TestImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public TestImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    @Override
    protected boolean setFrame(int l, int t, int r, int b) {
        boolean changed = super.setFrame(l, t, r, b);
//        if (changed) {
//            attacher.update();
//        }
        return changed;
    }
}
