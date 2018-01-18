package com.xue.ui.activity;

import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.elianshang.tools.WeakReferenceHandler;
import com.xue.BaseApplication;
import com.xue.R;

/**
 * Created by xfilshy on 2018/1/17.
 */

public class WelcomeActivity extends BaseActivity {


    private WeakReferenceHandler<WelcomeActivity> handler = new WeakReferenceHandler<WelcomeActivity>(this) {
        @Override
        public void HandleMessage(WelcomeActivity activity, Message msg) {
            if (backFlag) {
                return;
            }
            if (msg.what == 1) {
                LoginActivity.launch(activity);
                activity.finish();
            } else if (msg.what == 2) {
                MainActivity.launch(activity);
                activity.finish();
            }
        }
    };

    private boolean backFlag = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_welcome);

        if (checkLogin()) {
            handler.sendEmptyMessageDelayed(2, 2000);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        backFlag = true;
        finish();

        handler.removeMessages(1);
        handler.removeMessages(2);
    }

    private boolean checkLogin() {
        if (TextUtils.isEmpty(BaseApplication.get().getUserId())) {
            handler.sendEmptyMessageDelayed(1, 2000);
            return false;
        }

        return true;
    }
}
