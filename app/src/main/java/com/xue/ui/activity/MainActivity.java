package com.xue.ui.activity;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;

import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.Observer;
import com.netease.nimlib.sdk.StatusCode;
import com.netease.nimlib.sdk.auth.AuthServiceObserver;
import com.xue.R;
import com.xue.ui.fragment.HomeFragment;

/**
 * Created by xfilshy on 2018/1/17.
 */

public class MainActivity extends BaseActivity implements Observer<StatusCode> {

    public static void launch(BaseActivity activity) {
        if (activity == null) {
            return;
        }

        Intent intent = new Intent(activity, MainActivity.class);
        activity.startActivity(intent);
    }

    public static void closeAll(Context context) {
        if (context == null) {
            return;
        }

        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra("flag", true);
        context.startActivity(intent);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        boolean flag = intent.getBooleanExtra("flag", false);
        if (flag) {
            finish();
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        addHomePage();

        NIMClient.getService(AuthServiceObserver.class).observeOnlineStatus(this, true);
    }

    private void addHomePage() {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.fragment_root, new HomeFragment());
        transaction.commit();
    }

    @Override
    public void onEvent(StatusCode statusCode) {
        Log.i("tag", "User status changed to: " + statusCode);
        if (statusCode.wontAutoLogin()) {
            // 被踢出、账号被禁用、密码错误等情况，自动登录失败，需要返回到登录界面进行重新登录操作
        }
    }
}
