package com.xue.ui.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.Observer;
import com.netease.nimlib.sdk.StatusCode;
import com.netease.nimlib.sdk.auth.AuthServiceObserver;
import com.netease.nimlib.sdk.avchat.AVChatManager;
import com.netease.nimlib.sdk.avchat.model.AVChatData;
import com.xue.R;
import com.xue.imagecache.ImageCacheMannager;
import com.xue.ui.fragment.HomeFragment;

/**
 * Created by xfilshy on 2018/1/17.
 */

public class MainActivity extends BaseActivity implements View.OnClickListener {

    public static void launch(Activity activity) {
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

    private ImageView mActionLogoImageView;

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

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM); //Enable自定义的View
            actionBar.setCustomView(R.layout.actionbar_home);//设置自定义的布局：actionbar_custom
            mActionLogoImageView = actionBar.getCustomView().findViewById(R.id.action_account);
            mActionLogoImageView.setOnClickListener(this);

            ImageCacheMannager.loadImage(this, R.drawable.photo_test, mActionLogoImageView, true);
        }

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


    /**
     * 登录状态监听
     */
    private Observer<StatusCode> mOnlineStatusObserver = new Observer<StatusCode>() {
        @Override
        public void onEvent(StatusCode statusCode) {
            Log.i("xue", "登录状态: " + statusCode);
            if (statusCode.wontAutoLogin()) {
                // 被踢出、账号被禁用、密码错误等情况，自动登录失败，需要返回到登录界面进行重新登录操作
            }
        }
    };

    private Observer<AVChatData> mIncomingCallObserver = new Observer<AVChatData>() {
        @Override
        public void onEvent(AVChatData avChatData) {
            String extra = avChatData.getExtra();
            AVChatActivity.launchAccept(MainActivity.this, avChatData);
        }
    };

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.action_account) {
            MyActivity.launch(this);
        }
    }
}
