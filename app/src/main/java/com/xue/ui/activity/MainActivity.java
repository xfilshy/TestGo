package com.xue.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;

import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.Observer;
import com.netease.nimlib.sdk.StatusCode;
import com.netease.nimlib.sdk.auth.AuthServiceObserver;
import com.netease.nimlib.sdk.avchat.AVChatManager;
import com.netease.nimlib.sdk.avchat.model.AVChatData;
import com.xue.R;
import com.xue.ui.fragment.HomeFragment;

/**
 * Created by xfilshy on 2018/1/17.
 */

public class MainActivity extends BaseActivity {

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
        addHomePage();

        NIMClient.getService(AuthServiceObserver.class).observeOnlineStatus(mOnlineStatusObserver, true);
        AVChatManager.getInstance().observeIncomingCall(mIncomingCallObserver, true);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        NIMClient.getService(AuthServiceObserver.class).observeOnlineStatus(mOnlineStatusObserver, false);
        AVChatManager.getInstance().observeIncomingCall(mIncomingCallObserver, false);
    }

    private void addHomePage() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.fragment_root, new HomeFragment());
        transaction.commit();
    }


    private Observer<StatusCode> mOnlineStatusObserver = new Observer<StatusCode>() {
        @Override
        public void onEvent(StatusCode statusCode) {
            Log.i("tag", "User status changed to: " + statusCode);
            if (statusCode.wontAutoLogin()) {
                // 被踢出、账号被禁用、密码错误等情况，自动登录失败，需要返回到登录界面进行重新登录操作
            }
        }
    };

    private Observer<AVChatData> mIncomingCallObserver = new Observer<AVChatData>() {
        @Override
        public void onEvent(AVChatData avChatData) {
            String extra = avChatData.getExtra();
            Log.e("Extra", "Extra Message->" + extra);
            Log.e("Extra", "Extra Message->" + avChatData.getChatType());
            Log.e("Extra", "Extra Message->" + avChatData.getChatId());
            AVChatActivity.launchVideoAccept(MainActivity.this, avChatData);
        }
    };
}
