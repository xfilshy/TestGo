package com.xue.support.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.xue.support.R;

public class SimpleProgressBar extends View {

    private float mProgress = 0f;

    private int mProgressColor = 0xff000000;

    private int mBackgroundColor = 0x00000000;

    private Paint mPaint;

    private Paint mBackgroundPaint;

    public SimpleProgressBar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        if (attrs != null) {
            TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.SimpleProgressBar);
            mProgressColor = typedArray.getColor(R.styleable.SimpleProgressBar_sp_progressColor, mProgressColor);
            mBackgroundColor = typedArray.getColor(R.styleable.SimpleProgressBar_sp_backgroundColor, mBackgroundColor);
            mProgress = typedArray.getFloat(R.styleable.SimpleProgressBar_sp_progress, mProgress);
            if (mProgress < 0) {
                mProgress = 0;
            }

            if (mProgress > 1) {
                mProgress = 1;
            }
            typedArray.recycle();
        }

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(mProgressColor);
        mPaint.setStyle(Paint.Style.FILL_AND_STROKE);

        mBackgroundPaint = new Paint();
        mBackgroundPaint.setAntiAlias(true);
        mBackgroundPaint.setColor(mBackgroundColor);
        mBackgroundPaint.setStyle(Paint.Style.FILL_AND_STROKE);
    }

    public void setProgress(float progress) {
        mProgress = progress;
        if (mProgress < 0) {
            mProgress = 0;
        }

        if (mProgress > 1) {
            mProgress = 1;
        }

        invalidate();
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        RectF rectF = new RectF(0, 0, getWidth(), getHeight());
        canvas.drawRoundRect(rectF, getWidth() / 2, getWidth() / 2, mBackgroundPaint);

        float tmp = mProgress * getWidth();
        if (tmp < getHeight()) {
            tmp = getHeight();
        }

        rectF = new RectF(0, 0, tmp, getHeight());
        canvas.drawRoundRect(rectF, getWidth() / 2, getWidth() / 2, mPaint);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = getMySize(0, widthMeasureSpec);
        int height = getMySize(0, heightMeasureSpec);

        setMeasuredDimension(width, height);
    }

    private int getMySize(int defaultSize, int measureSpec) {
        int mySize = defaultSize;

        int mode = MeasureSpec.getMode(measureSpec);
        int size = MeasureSpec.getSize(measureSpec);

        switch (mode) {
            case MeasureSpec.UNSPECIFIED: {//如果没有指定大小，就设置为默认大小
                mySize = defaultSize;
                break;
            }
            case MeasureSpec.AT_MOST: {//如果测量模式是最大取值为size
                //我们将大小取最大值,你也可以取其他值
                mySize = size;
                break;
            }
            case MeasureSpec.EXACTLY: {//如果是固定的大小，那就不要去改变它
                mySize = size;
                break;
            }
        }
        return mySize;
    }
}
