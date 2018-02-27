package com.xue.support.slideback;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.annotation.FloatRange;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.annotation.Px;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.xue.support.R;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.List;

public class SlideBackLayout extends FrameLayout {
    // @formatter:off
    private static final String TAG = "SlideBackLayout";
    private static final boolean DEBUG = false;

    public static final int EDGE_LEFT = ViewDragHelper.EDGE_LEFT;
    public static final int EDGE_RIGHT = ViewDragHelper.EDGE_RIGHT;
    public static final int EDGE_BOTTOM = ViewDragHelper.EDGE_BOTTOM;
    public static final int EDGE_ALL = EDGE_LEFT | EDGE_RIGHT | EDGE_BOTTOM;

    @IntDef(value = {
            EDGE_LEFT, EDGE_RIGHT, EDGE_BOTTOM, EDGE_ALL
    })
    @Retention(RetentionPolicy.SOURCE)
    public @interface Edge {
    }

    @Edge
    private int mEdgeFlags;
    /** Edge being dragged */
    @Edge
    private int mTrackingEdge;

    public static final int STATE_IDLE = ViewDragHelper.STATE_IDLE;
    public static final int STATE_DRAGGING = ViewDragHelper.STATE_DRAGGING;
    public static final int STATE_SETTLING = ViewDragHelper.STATE_SETTLING;

    @IntDef(value = {
            STATE_IDLE, STATE_DRAGGING, STATE_SETTLING
    })
    @Retention(RetentionPolicy.SOURCE)
    public @interface State {
    }

    /** Minimum velocity that will be detected as a fling */
    private static final int MIN_FLING_VELOCITY = 400; // dips per second

    @Px
    private static final int OVERSCROLL_DISTANCE = 10;

    private Activity mActivity;

    /** The contentView that will be moved by user gesture */
    private View mContentView;

    private final ViewDragHelper mDragHelper;

    /** The set of listeners to be sent events through. */
    private List<SlideListener> mSlideListeners;

    private final Rect mContentRect = new Rect();

    private Drawable mShadowLeft;
    private Drawable mShadowRight;
    private Drawable mShadowBottom;

    private int mContentLeft;
    private int mContentTop;
    private boolean mIsLayingOut;

    @ColorInt
    private static final int DEFAULT_SCRIM_COLOR = 0x99000000;
    @ColorInt
    private int mScrimColor = DEFAULT_SCRIM_COLOR;
    private static final int FULL_ALPHA = 255;
    private float mScrimOpacity;

    /** Default threshold of scroll */
    private static final float DEFAULT_SCROLL_THRESHOLD = 0.3f;
    /** Threshold of scroll, we will close the activity, when scrollPercent over this value; */
    private float mScrollThreshold;
    private float mScrollPercent;

    private boolean mEnable = true;
    // @formatter:on

    public SlideBackLayout(Context context) {
        this(context, null);
    }

    public SlideBackLayout(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.SlideBackLayoutStyle);
    }

    public SlideBackLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs);
        mDragHelper = ViewDragHelper.create(this, new ViewDragCallback());

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.SlideBackLayout, defStyle, R.style.SlideBackLayout);
        setEnabledEdges(a.getInt(R.styleable.SlideBackLayout_edgeFlags, EDGE_LEFT));
        setSensitivity(a.getFloat(R.styleable.SlideBackLayout_sensitivity, 1));
        setScrollThreshold(a.getFloat(R.styleable.SlideBackLayout_scrollThreshold, DEFAULT_SCROLL_THRESHOLD));
        setEdgeShadow(a.getResourceId(R.styleable.SlideBackLayout_shadow_left, R.drawable.shadow_left), EDGE_LEFT);
        setEdgeShadow(a.getResourceId(R.styleable.SlideBackLayout_shadow_right, R.drawable.shadow_right), EDGE_RIGHT);
        setEdgeShadow(a.getResourceId(R.styleable.SlideBackLayout_shadow_bottom, R.drawable.shadow_bottom), EDGE_BOTTOM);
        a.recycle();

        final float density = getResources().getDisplayMetrics().density;
        final float minVelocity = MIN_FLING_VELOCITY * density;
        mDragHelper.setMinVelocity(minVelocity);
        mDragHelper.setMaxVelocity(minVelocity * 2);
    }

    public void attachToActivity(Activity activity) {
        mActivity = activity;

        TypedArray a = activity.getTheme().obtainStyledAttributes(new int[]{
                android.R.attr.windowBackground
        });
        final int background = a.getResourceId(0, 0);
        a.recycle();

        ViewGroup decor = (ViewGroup) activity.getWindow().getDecorView();
        mContentView = decor.getChildAt(0);
        mContentView.setBackgroundResource(background);

        decor.removeView(mContentView);
        addView(mContentView);
        decor.addView(this);
    }

    /**
     * Enable edge tracking for the selected edges of the parent view.
     * The callback's
     * {@link ViewDragHelper.Callback#onEdgeTouched(int, int)}
     * and
     * {@link ViewDragHelper.Callback#onEdgeDragStarted(int, int)}
     * methods will only be invoked for edges for which edge tracking has been enabled.
     *
     * @param edgeFlags Combination of edge flags describing the edges to watch
     * @see #EDGE_LEFT
     * @see #EDGE_RIGHT
     * @see #EDGE_BOTTOM
     */
    public void setEnabledEdges(@Edge int edgeFlags) {
        mEdgeFlags = edgeFlags;
        mDragHelper.setEdgeTrackingEnabled(edgeFlags);
    }

    /**
     * Return the size of an edge. This is the range in pixels along the edges of this view
     * that will actively detect edge touches or drags if edge tracking is enabled.
     *
     * @return The size of an edge in pixels
     * @see #setEnabledEdges(int)
     */
    public int getEdgeSize() {
        return mDragHelper.getEdgeSize();
    }

    /**
     * Set a drawable used for edge shadow.
     *
     * @param resId    Resource of drawable to use
     * @param edgeFlag Combination of edge flags describing the edge to set
     * @see #EDGE_LEFT
     * @see #EDGE_RIGHT
     * @see #EDGE_BOTTOM
     */
    public void setEdgeShadow(@DrawableRes int resId, @Edge int edgeFlag) {
        setEdgeShadow(getResources().getDrawable(resId), edgeFlag);
    }

    /**
     * Set a drawable used for edge shadow.
     *
     * @param shadow   Drawable to use
     * @param edgeFlag Combination of edge flags describing the edge to set
     * @see #EDGE_LEFT
     * @see #EDGE_RIGHT
     * @see #EDGE_BOTTOM
     */
    public void setEdgeShadow(Drawable shadow, @Edge int edgeFlag) {
        if ((edgeFlag & EDGE_LEFT) != 0) {
            mShadowLeft = shadow;
        } else if ((edgeFlag & EDGE_RIGHT) != 0) {
            mShadowRight = shadow;
        } else if ((edgeFlag & EDGE_BOTTOM) != 0) {
            mShadowBottom = shadow;
        }
        // redraw edge shadows
        invalidate();
    }

    /**
     * Set a color to use for the scrim that obscures(遮盖) primary content while a
     * drawer is open.
     *
     * @param color Color to use in 0xAARRGGBB format.
     */
    public void setScrimColor(@ColorInt int color) {
        mScrimColor = color;
        // redraw scrim
        invalidate();
    }

    /**
     * Set the sensitivity of the Layout.
     *
     * @param sensitivity value between 0 and 1
     */
    public void setSensitivity(@FloatRange(from = 0.0, to = 1.0) float sensitivity) {
        float s = Math.max(0f, Math.min(1.0f, sensitivity));
        mDragHelper.setSensitivity(getContext(), s);
    }

    /**
     * Set scroll threshold, we will close the activity, when scrollPercent over
     * this value
     */
    public void setScrollThreshold(@FloatRange(from = 0.0, to = 1.0) float threshold) {
        if (threshold >= 1.0f || threshold <= 0) {
            throw new IllegalArgumentException("Threshold value should be between 0 and 1.0");
        }
        mScrollThreshold = threshold;
    }

    public void setGestureEnabled(boolean enable) {
        mEnable = enable;
    }

    /**
     * Register a callback to be invoked when a slide event is sent to this view.
     *
     * @param listener the slide listener to attach to this view
     * @deprecated use {@link #addSlideListener} instead
     */
    @Deprecated
    public void setSlideListener(SlideListener listener) {
        addSlideListener(listener);
    }

    /**
     * Add a callback to be invoked when a slide event is sent to this view.
     *
     * @param listener the slide listener to attach to this view
     */
    public void addSlideListener(SlideListener listener) {
        if (mSlideListeners == null) {
            mSlideListeners = new ArrayList<>();
        }
        mSlideListeners.add(listener);
    }

    private boolean existSlideListener() {
        return mSlideListeners != null && !mSlideListeners.isEmpty();
    }

    /**
     * Remove a listener from the set of listeners
     */
    public void removeSlideListener(SlideListener listener) {
        if (existSlideListener()) {
            mSlideListeners.remove(listener);
        }
    }

    /**
     * Remove all the listeners
     */
    public void clearSlideListeners() {
        if (existSlideListener()) {
            mSlideListeners.clear();
        }
    }

    public interface SlideListener {
        /**
         * Invoke when state change
         *
         * @param state         flag to describe scroll state
         * @param scrollPercent scroll percent of this view
         * @see #STATE_IDLE
         * @see #STATE_DRAGGING
         * @see #STATE_SETTLING
         */
        void onScrollStateChange(@State int state, float scrollPercent);

        /**
         * Invoke when edge touched
         *
         * @param edgeFlag edge flag describing the edge being touched
         * @see #EDGE_LEFT
         * @see #EDGE_RIGHT
         * @see #EDGE_BOTTOM
         */
        void onEdgeTouch(@Edge int edgeFlag);

        /**
         * Invoke when scroll percent over the threshold for the first time
         */
        void onScrollOverThreshold();
    }

    /**
     * Scroll out contentView and finish the activity
     *
     * @deprecated Since using this method may cause screen to be partly black and flash,
     * use {@link #scrollToFinishActivity2} for a better performance.
     */
    @Deprecated
    public void scrollToFinishActivity() {
        Utils.convertActivityToTranslucent(mActivity);

        final int childWidth = mContentView.getWidth();
        final int childHeight = mContentView.getHeight();

        int left = 0, top = 0;
        if ((mEdgeFlags & EDGE_LEFT) != 0) {
            left = childWidth + mShadowLeft.getIntrinsicWidth() + OVERSCROLL_DISTANCE;
            mTrackingEdge = EDGE_LEFT;
        } else if ((mEdgeFlags & EDGE_RIGHT) != 0) {
            left = -childWidth - mShadowRight.getIntrinsicWidth() - OVERSCROLL_DISTANCE;
            mTrackingEdge = EDGE_RIGHT;
        } else if ((mEdgeFlags & EDGE_BOTTOM) != 0) {
            top = -childHeight - mShadowBottom.getIntrinsicHeight() - OVERSCROLL_DISTANCE;
            mTrackingEdge = EDGE_BOTTOM;
        }

        mDragHelper.smoothSlideViewTo(mContentView, left, top);
        invalidate();
    }

    /**
     * Scroll out contentView and finish the activity
     */
    public void scrollToFinishActivity2() {
        if (mActivity.isFinishing()) {
            return;
        }
        mActivity.finish();
        if ((mEdgeFlags & EDGE_LEFT) != 0) {
            mActivity.overridePendingTransition(R.anim.anim_none, R.anim.anim_slide_out_from_left);
        } else if ((mEdgeFlags & EDGE_RIGHT) != 0) {
            mActivity.overridePendingTransition(R.anim.anim_none, R.anim.anim_slide_out_from_right);
        } else if ((mEdgeFlags & EDGE_BOTTOM) != 0) {
            mActivity.overridePendingTransition(R.anim.anim_none, R.anim.anim_slide_out_from_bottom);
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        if (!mEnable) {
            return false;
        }
        try {
            return mDragHelper.shouldInterceptTouchEvent(event);
        } catch (ArrayIndexOutOfBoundsException e) {
            // FIXME: handle exception
            // issues #9
            return false;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!mEnable) {
            return false;
        }
        mDragHelper.processTouchEvent(event);
        return true;
    }

    @Override
    public void requestLayout() {
        if (!mIsLayingOut) {
            super.requestLayout();
        }
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        mIsLayingOut = true;
        if (mContentView != null) {
            if (DEBUG) {
                Log.d(TAG, "layout child");
            }
            mContentView.layout(mContentLeft, mContentTop,
                    mContentLeft + mContentView.getMeasuredWidth(),
                    mContentTop + mContentView.getMeasuredHeight());
        }
        mIsLayingOut = false;
    }

    @Override
    protected boolean drawChild(Canvas canvas, View child, long drawingTime) {
        if (DEBUG) {
            Log.d(TAG, "draw child");
        }
        final boolean needDraw = child == mContentView;

        final boolean isInvalidateIssued = super.drawChild(canvas, child, drawingTime);
        if (needDraw && mScrimOpacity > 0
                && mDragHelper.getViewDragState() != ViewDragHelper.STATE_IDLE) {
            if (DEBUG) {
                Log.d(TAG, "draw shadows and scrim");
            }
            drawShadow(canvas, child);
            drawScrim(canvas, child);
        }
        return isInvalidateIssued;
    }

    private void drawShadow(Canvas canvas, View child) {
        child.getHitRect(mContentRect);

        if ((mEdgeFlags & EDGE_LEFT) != 0) {
            mShadowLeft.setBounds(mContentRect.left - mShadowLeft.getIntrinsicWidth(),
                    mContentRect.top, mContentRect.left, mContentRect.bottom);
            mShadowLeft.setAlpha((int) (mScrimOpacity * (float) FULL_ALPHA + 0.5f));
            mShadowLeft.draw(canvas);
        }

        if ((mEdgeFlags & EDGE_RIGHT) != 0) {
            mShadowRight.setBounds(mContentRect.right, mContentRect.top,
                    mContentRect.right + mShadowRight.getIntrinsicWidth(), mContentRect.bottom);
            mShadowRight.setAlpha((int) (mScrimOpacity * (float) FULL_ALPHA + 0.5f));
            mShadowRight.draw(canvas);
        }

        if ((mEdgeFlags & EDGE_BOTTOM) != 0) {
            mShadowBottom.setBounds(mContentRect.left, mContentRect.bottom, mContentRect.right,
                    mContentRect.bottom + mShadowBottom.getIntrinsicHeight());
            mShadowBottom.setAlpha((int) (mScrimOpacity * (float) FULL_ALPHA + 0.5f));
            mShadowBottom.draw(canvas);
        }
    }

    private void drawScrim(Canvas canvas, View child) {
        final int baseAlpha = (mScrimColor & 0xff000000) >>> 24;
        final int alpha = (int) ((float) baseAlpha * mScrimOpacity + 0.5f);
        final int color = alpha << 24 | (mScrimColor & 0xffffff);

        if ((mTrackingEdge & EDGE_LEFT) != 0) {
            canvas.clipRect(0, 0, child.getLeft(), getHeight());
        } else if ((mTrackingEdge & EDGE_RIGHT) != 0) {
            canvas.clipRect(child.getRight(), 0, getRight(), getHeight());
        } else if ((mTrackingEdge & EDGE_BOTTOM) != 0) {
            canvas.clipRect(getLeft(), child.getBottom(), getRight(), getHeight());
        }
        canvas.drawColor(color);
    }

    @Override
    public void computeScroll() {
        if (DEBUG) {
            Log.d(TAG, "computeScroll");
        }
        // 滑动时，背景透明度随滑动距离增大而增大
        mScrimOpacity = 1 - mScrollPercent;

        if (mDragHelper.continueSettling(true)) {
            if (DEBUG) {
                Log.d(TAG, "invalidate on animation");
            }
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }

    private class ViewDragCallback extends ViewDragHelper.Callback {

        @Override
        public boolean tryCaptureView(@NonNull View child, int pointerId) {
            if (DEBUG) {
                Log.d(TAG, "tryCaptureView");
            }
            if (mDragHelper.isEdgeTouched(mEdgeFlags, pointerId)) {
                if (mDragHelper.isEdgeTouched(EDGE_LEFT, pointerId)) {
                    mTrackingEdge = EDGE_LEFT;
                }
                if (mDragHelper.isEdgeTouched(EDGE_RIGHT, pointerId)) {
                    mTrackingEdge = EDGE_RIGHT;
                } else if (mDragHelper.isEdgeTouched(EDGE_BOTTOM, pointerId)) {
                    mTrackingEdge = EDGE_BOTTOM;
                }
                // 回调 SlideListener#onEdgeTouch(int)
                if (existSlideListener()) {
                    for (SlideListener listener : mSlideListeners) {
                        listener.onEdgeTouch(mTrackingEdge);
                    }
                }

                // 左右滑动返回时，竖直方向位移改变量(px)不能超过 ViewDragHelper#mTouchSlop
                if (mEdgeFlags == EDGE_LEFT || mEdgeFlags == EDGE_RIGHT) {
                    return !mDragHelper.checkTouchSlop(ViewDragHelper.DIRECTION_VERTICAL, pointerId);
                    // 上滑返回时，水平方向位移改变量(px)不能超过 ViewDragHelper#mTouchSlop
                } else if (mEdgeFlags == EDGE_BOTTOM) {
                    return !mDragHelper.checkTouchSlop(ViewDragHelper.DIRECTION_HORIZONTAL, pointerId);
                } else if (mEdgeFlags == EDGE_ALL) {
                    return true;
                }
            }

            return false;
        }

        @Override
        public int getViewHorizontalDragRange(@NonNull View child) {
            return mEdgeFlags & (EDGE_LEFT | EDGE_RIGHT);
        }

        @Override
        public int getViewVerticalDragRange(@NonNull View child) {
            return mEdgeFlags & EDGE_BOTTOM;
        }

        @Override
        public void onViewPositionChanged(@NonNull View changedView, int left, int top, int dx, int dy) {
            if (DEBUG) {
                Log.d(TAG, "onViewPositionChanged" +
                        "   " + "left" + left +
                        "   " + "top" + top +
                        "   " + "dx" + dx +
                        "   " + "dy" + dy);
            }
            if ((mTrackingEdge & EDGE_LEFT) != 0 || (mTrackingEdge & EDGE_RIGHT) != 0) {
                mScrollPercent = Math.abs((float) left
                        / (float) (mContentView.getWidth() + mShadowLeft.getIntrinsicWidth()));
            } else if ((mTrackingEdge & EDGE_BOTTOM) != 0) {
                mScrollPercent = Math.abs((float) top
                        / (float) (mContentView.getHeight() + mShadowBottom.getIntrinsicHeight()));
            }
            if (DEBUG) {
                Log.d(TAG, "scroll percent= " + mScrollPercent);
            }
            mContentLeft = left;
            mContentTop = top;
            invalidate();

            // 在滑动时，若 滑动距离 >= 使activity关闭时的最大滑动距离
            // 回调 SlideListener#onScrollOverThreshold()
            if (existSlideListener()
                    && mDragHelper.getViewDragState() == STATE_DRAGGING
                    && mScrollPercent > mScrollThreshold) {

                for (SlideListener listener : mSlideListeners) {
                    listener.onScrollOverThreshold();
                }
            }

            // 当activity被完全滑开时 --> finish()
            if (mScrollPercent >= 1 && !mActivity.isFinishing()) {
                mActivity.finish();
                mActivity.overridePendingTransition(0, 0);
                if (DEBUG) {
                    Log.d(TAG, "===== finish activity! =====");
                }
            }
        }

        @Override
        public void onViewReleased(@NonNull View releasedChild, float xvel, float yvel) {
            final int childWidth = releasedChild.getWidth();
            final int childHeight = releasedChild.getHeight();

            int left = 0, top = 0;
            if ((mTrackingEdge & EDGE_LEFT) != 0) {
                left = xvel > 0 || xvel == 0 && mScrollPercent > mScrollThreshold ? childWidth
                        + mShadowLeft.getIntrinsicWidth() + OVERSCROLL_DISTANCE : 0;
            } else if ((mTrackingEdge & EDGE_RIGHT) != 0) {
                left = xvel < 0 || xvel == 0 && mScrollPercent > mScrollThreshold ? -(childWidth
                        + mShadowLeft.getIntrinsicWidth() + OVERSCROLL_DISTANCE) : 0;
            } else if ((mTrackingEdge & EDGE_BOTTOM) != 0) {
                top = yvel < 0 || yvel == 0 && mScrollPercent > mScrollThreshold ? -(childHeight
                        + mShadowBottom.getIntrinsicHeight() + OVERSCROLL_DISTANCE) : 0;
            }

            if (DEBUG) {
                Log.d(TAG, "onViewReleased" +
                        "   " + "left= " + left +
                        "   " + "top= " + top);
            }
            mDragHelper.settleCapturedViewAt(left, top);
            invalidate();
        }

        @Override
        public int clampViewPositionHorizontal(@NonNull View child, int left, int dx) {
            int newClampedX = 0;
            if ((mTrackingEdge & EDGE_LEFT) != 0) {
                newClampedX = Math.min(child.getWidth(), Math.max(left, 0));
            } else if ((mTrackingEdge & EDGE_RIGHT) != 0) {
                newClampedX = Math.min(0, Math.max(left, -child.getWidth()));
            }
            if (DEBUG) {
                Log.d(TAG, "clampViewPositionHorizontal newClampedX= " + newClampedX +
                        "   " + "left= " + left +
                        "   " + "dx= " + dx);
            }
            return newClampedX;
        }

        @Override
        public int clampViewPositionVertical(@NonNull View child, int top, int dy) {
            int newClampedY = 0;
            if ((mTrackingEdge & EDGE_BOTTOM) != 0) {
                newClampedY = Math.min(0, Math.max(top, -child.getHeight()));
            }
            if (DEBUG) {
                Log.d(TAG, "clampViewPositionVertical newClampedX= " + newClampedY +
                        "   " + "top= " + top +
                        "   " + "dy= " + dy);
            }
            return newClampedY;
        }

        @Override
        public void onViewDragStateChanged(int state) {
            if (existSlideListener()) {
                for (SlideListener listener : mSlideListeners) {
                    listener.onScrollStateChange(state, mScrollPercent);
                }
            }
            if (DEBUG) {
                Log.d(TAG, "****** " + "onViewDragStateChanged state=" + state + " *****");
            }
        }
    }
}