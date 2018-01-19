package com.xue.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

/**
 * Created by xfilshy on 2018/1/18.
 */

public class BaseFragment extends Fragment {

    private Callback callback;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (callback != null) {
            callback.activityCreated();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        callback = null;
    }

    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    public interface Callback {
        public void activityCreated();
    }
}
