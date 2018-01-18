package com.xue.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;

import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.auth.AuthService;
import com.netease.nimlib.sdk.auth.LoginInfo;
import com.xue.BaseApplication;
import com.xue.R;

/**
 * Created by xfilshy on 2018/1/17.
 */
public class LoginActivity extends BaseActivity {

    public static void launch(BaseActivity activity) {
        if (activity == null) {
            return;
        }

        Intent intent = new Intent(activity, LoginActivity.class);
        activity.startActivity(intent);
    }

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
        RequestCallback<LoginInfo> callback = new RequestCallback<LoginInfo>() {
            @Override
            public void onSuccess(LoginInfo loginInfo) {
                Log.e("xue", "授权成功");
                setResult(RESULT_OK);
                finish();
            }

            @Override
            public void onFailed(int i) {
                Log.e("xue", "授权失败");
            }

            @Override
            public void onException(Throwable throwable) {
                Log.e("xue", "授权异常");
            }
            // 可以在此保存LoginInfo到本地，下次启动APP做自动登录用
        };
        NIMClient.getService(AuthService.class).login(info).setCallback(callback);
    }
}
