package com.xue.support.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.Path.Direction;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.Xfermode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;

import com.xue.support.R;


public class HoverImageView extends AppCompatImageView {
    private static final ScaleType SCALE_TYPE = ScaleType.CENTER_CROP;
    private static final PorterDuffXfermode duffMode = new PorterDuffXfermode(PorterDuff.Mode.SRC_IN);

    private boolean pressed;
    private int borderColor = 0x44000000;
    private int hoverColor = 0x22000000;
    private Paint borderPaint;
    private Path boundPath;
    private Path borderPath;
    private RectF rect = new RectF();
    private float borderWidth = 0f;

    public HoverImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setup(attrs);
    }

    public HoverImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setup(attrs);
    }

    public HoverImageView(Context context) {
        super(context);
        setup(null);
    }

    private void setup(AttributeSet attrs) {
        if (attrs != null) {
            TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.HoverImageView);
            borderColor = typedArray.getColor(R.styleable.HoverImageView_r_borderColor, borderColor);
            hoverColor = typedArray.getColor(R.styleable.HoverImageView_r_hoverColor, hoverColor);
            borderWidth = typedArray.getDimension(R.styleable.HoverImageView_r_borderWidth, borderWidth);
            typedArray.recycle();
        }

        borderPath = new Path();
        boundPath = new Path();
        borderPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        if (borderWidth > 0) {
            borderPaint.setStrokeWidth(borderWidth);
        }

        super.setScaleType(SCALE_TYPE);
    }

    @Override
    protected void onDetachedFromWindow() {
        setImageDrawable(null);
        super.onDetachedFromWindow();
    }

    protected void drawBorder(Canvas canvas) {
        if (borderWidth > 0) {
            borderPaint.setStyle(Style.STROKE);
            borderPaint.setColor(borderColor);
            canvas.drawPath(borderPath, borderPaint);
        }
    }

    protected void drawHover(Canvas canvas) {
        if (this.isClickable() && pressed) {
            borderPaint.setStyle(Style.FILL);
            borderPaint.setColor(hoverColor);
            canvas.drawPath(boundPath, borderPaint);
        }
    }

    public void buildBorderPath(Path borderPath) {
        final float halfBorderWidth = borderWidth * 0.5f;
        if (halfBorderWidth > 0) {
            borderPath.reset();
            boundPath.addRect(halfBorderWidth, halfBorderWidth,
                    getWidth() - halfBorderWidth, getHeight() - halfBorderWidth, Direction.CW);
        }
    }

    public void buildBoundPath(Path boundPath) {
        boundPath.reset();
        boundPath.addRect(0, 0, getWidth(), getHeight(), Direction.CW);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (changed) {
            buildBoundPath(boundPath);
            buildBorderPath(borderPath);
        }
    }


    @Override
    protected void onDraw(Canvas canvas) {

        Drawable maiDrawable = getDrawable();
        if (!isInEditMode() && maiDrawable instanceof BitmapDrawable) {
            Paint paint = ((BitmapDrawable) maiDrawable).getPaint();

            //Rect bitmapBounds = maiDrawable.getBounds();
            rect.set(0, 0, getWidth(), getHeight());

            int saveCount = canvas.saveLayer(rect, null,
                    Canvas.MATRIX_SAVE_FLAG |
                            Canvas.CLIP_SAVE_FLAG |
                            Canvas.HAS_ALPHA_LAYER_SAVE_FLAG |
                            Canvas.FULL_COLOR_LAYER_SAVE_FLAG |
                            Canvas.CLIP_TO_LAYER_SAVE_FLAG);
            getImageMatrix().mapRect(rect);

            paint.setAntiAlias(true);
            canvas.drawARGB(0, 0, 0, 0);
            final int color = 0xffffffff;
            paint.setColor(color);
            canvas.drawPath(boundPath, paint);

            Xfermode oldMode = paint.getXfermode();
            paint.setXfermode(duffMode);
            super.onDraw(canvas);
            paint.setXfermode(oldMode);
            canvas.restoreToCount(saveCount);

            drawHover(canvas);
            drawBorder(canvas);
        } else {
            super.onDraw(canvas);
        }
    }


//    @Override
//    public boolean onTouchEvent(MotionEvent event) {
//        final boolean touched = super.onTouchEvent(event);
//        if (touched) {
//            switch (event.getAction() & MotionEvent.ACTION_MASK) {
//                case MotionEvent.ACTION_DOWN:
//                    pressed = true;
//                    postInvalidate();
//                    break;
//                case MotionEvent.ACTION_CANCEL:
//                case MotionEvent.ACTION_UP:
//                    pressed = false;
//                    postInvalidate();
//                    break;
//                default:
//                    break;
//            }
//        }
//        return touched;
//    }

    public float getBorderWidth() {
        return borderWidth;
    }
}
