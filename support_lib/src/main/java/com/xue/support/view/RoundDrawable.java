package com.xue.support.view;

import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.RectF;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;

public class RoundDrawable extends Drawable {

    private Paint mPaint;

    private RectF rectF;

    private int mRadius;

    private int intrinsicWidth;

    private int intrinsicHeight;

    public RoundDrawable(Drawable drawable, int radius) {
        Bitmap bitmap = null;
        int color = 0xff000000;
        mRadius = radius;
        if (drawable != null) {
            intrinsicWidth = drawable.getIntrinsicWidth();
            intrinsicHeight = drawable.getIntrinsicHeight();

            if (drawable instanceof BitmapDrawable) {
                BitmapDrawable bd = (BitmapDrawable) drawable;
                bitmap = bd.getBitmap();
            } else if (drawable instanceof ColorDrawable) {
                color = ((ColorDrawable) drawable).getColor();
            } else if (drawable instanceof TransitionDrawable) {
                TransitionDrawable transitionDrawable = ((TransitionDrawable) drawable);
                for (int i = 0; i < transitionDrawable.getNumberOfLayers(); i++) {
                    if (transitionDrawable.getDrawable(i) instanceof BitmapDrawable) {
                        BitmapDrawable bd = (BitmapDrawable) transitionDrawable.getDrawable(i);
                        bitmap = bd.getBitmap();
                    }
                }
            }
        }

        if (drawable instanceof RoundDrawable) {
            mPaint = ((RoundDrawable) drawable).getPaint();
        } else {
            mPaint = new Paint();
            mPaint.setAntiAlias(true);
            mPaint.setColor(color);
            if (bitmap != null) {
                BitmapShader bitmapShader = new BitmapShader(bitmap, TileMode.CLAMP, TileMode.CLAMP);
                mPaint.setShader(bitmapShader);
            }
        }
    }

    @Override
    public void setBounds(int left, int top, int right, int bottom) {
        super.setBounds(left, top, right, bottom);
        rectF = new RectF(left, top, right, bottom);
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawRoundRect(rectF, mRadius, mRadius, mPaint);
    }

    @Override
    public int getIntrinsicWidth() {
        return intrinsicWidth;
    }

    @Override
    public int getIntrinsicHeight() {
        return intrinsicHeight;
    }

    @Override
    public void setAlpha(int alpha) {
        mPaint.setAlpha(alpha);
    }

    @Override
    public void setColorFilter(ColorFilter cf) {
        mPaint.setColorFilter(cf);
    }

    @Override
    public int getOpacity() {
        return PixelFormat.TRANSLUCENT;
    }

    public Paint getPaint() {
        return mPaint;
    }
}