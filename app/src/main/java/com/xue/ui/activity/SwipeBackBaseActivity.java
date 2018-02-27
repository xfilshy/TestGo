package com.xue.ui.activity;

import android.view.View;

import com.netease.nimlib.sdk.NIMSDK;
import com.xue.BaseApplication;
import com.xue.support.slideback.activity.SlideBackActivity;

/**
 * Created by xfilshy on 2018/1/17.
 */

public class SwipeBackBaseActivity extends SlideBackActivity {

    public void closeActivity(View view) {
        finish();
    }

    public void logout(View view) {
        BaseApplication.get().setUser(null, true);
        MainActivity.logout(view.getContext());
        NIMSDK.getAuthService().logout();
    }

    public void launchSessionListActivity(View view) {
        SessionListActivity.launch(this);
    }
}