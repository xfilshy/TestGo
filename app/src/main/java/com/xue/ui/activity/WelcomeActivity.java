package com.xue.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.View;

import com.elianshang.tools.WeakReferenceHandler;
import com.xue.BaseApplication;
import com.xue.R;
import com.xue.ui.fragment.LoginFragment;

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
                activity.loadLoginFragment();
            } else if (msg.what == 2) {
                MainActivity.launch(activity);
                activity.finish();
            }
        }
    };

    private boolean backFlag = false;

    private LoginFragment mLoginFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_welcome);

        if (checkLogin()) {
            handler.sendEmptyMessageDelayed(2, 2000);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (mLoginFragment != null && mLoginFragment.isAdded() && !mLoginFragment.isDetached()) {
            mLoginFragment.onActivityResult(requestCode, resultCode, data);
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

    public void goPhoneLogin(View view) {
        PhoneLoginActivity.launch(this);
    }

    private boolean checkLogin() {
        if (TextUtils.isEmpty(BaseApplication.get().getUserId())) {
            handler.sendEmptyMessageDelayed(1, 2000);
            return false;
        }

        return true;
    }

    private void loadLoginFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        mLoginFragment = new LoginFragment();
        transaction.replace(R.id.fragment_root, mLoginFragment);
        transaction.commitAllowingStateLoss();
    }
}
