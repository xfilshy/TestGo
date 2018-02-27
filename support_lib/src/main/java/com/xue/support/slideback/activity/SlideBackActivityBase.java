package com.xue.support.slideback.activity;

import com.xue.support.slideback.SlideBackLayout;

public interface SlideBackActivityBase {
    /**
     * @return the SlideBackLayout associated with this activity.
     */
    SlideBackLayout getSlideBackLayout();

    void setSlideBackEnabled(boolean enable);

    /**
     * Scroll out contentView and finish the activity
     */
    void scrollToFinishActivity();
}
