package com.xue.ui.activity;

import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.netease.nimlib.sdk.NIMSDK;
import com.xue.BaseApplication;

/**
 * Created by xfilshy on 2018/1/17.
 */

public class BaseActivity extends AppCompatActivity {

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
