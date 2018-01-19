package com.xue.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;

import com.elianshang.tools.WeakReferenceHandler;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.auth.AuthService;
import com.netease.nimlib.sdk.auth.LoginInfo;
import com.xue.BaseApplication;
import com.xue.R;

/**
 * Created by xfilshy on 2018/1/17.
 */
public class LoginActivity extends BaseActivity implements RequestCallback<LoginInfo> {

    public static void launch(BaseActivity activity) {
        if (activity == null) {
            return;
        }

        Intent intent = new Intent(activity, LoginActivity.class);
        activity.startActivity(intent);
    }

    private WeakReferenceHandler<LoginActivity> handler = new WeakReferenceHandler<LoginActivity>(this) {
        @Override
        public void HandleMessage(LoginActivity loginActivity, Message msg) {
            super.HandleMessage(loginActivity, msg);
            if (msg.what == 1) {
                setResult(RESULT_OK);
                MainActivity.launch(loginActivity);
                finish();
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            doLogin(BaseApplication.get().getUserId(), BaseApplication.get().getUserToken());
        }
    }

    public void goPhoneLogin(View view) {
        PhoneLoginActivity.launch(this);
    }

    public void doLogin(String account, String token) {
        LoginInfo info = new LoginInfo(account, token);
        NIMClient.getService(AuthService.class).login(info).setCallback(this);
    }

    @Override
    public void onSuccess(LoginInfo loginInfo) {
        Log.e("xue", "授权成功");
        handler.sendEmptyMessage(1);
    }

    @Override
    public void onFailed(int i) {
        Log.e("xue", "授权失败");
    }

    @Override
    public void onException(Throwable throwable) {
        Log.e("xue", "授权异常");
    }
}
