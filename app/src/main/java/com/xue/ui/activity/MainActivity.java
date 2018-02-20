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

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.NIMSDK;
import com.netease.nimlib.sdk.Observer;
import com.netease.nimlib.sdk.StatusCode;
import com.netease.nimlib.sdk.auth.AuthServiceObserver;
import com.netease.nimlib.sdk.avchat.AVChatManager;
import com.netease.nimlib.sdk.avchat.model.AVChatData;
import com.netease.nimlib.sdk.msg.model.RecentContact;
import com.xue.R;
import com.xue.imagecache.ImageCacheMannager;
import com.xue.ui.fragment.HomeFragment;

import java.util.List;

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
        intent.putExtra("flag", 1);
        context.startActivity(intent);
    }

    public static void logout(Context context) {
        if (context == null) {
            return;
        }

        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra("flag", 2);
        context.startActivity(intent);
    }

    private ImageView mActionLogoImageView;

    private View mActionDotView;

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        int flag = intent.getIntExtra("flag", 0);
        if (flag == 1) {
            finish();
        } else if (flag == 2) {
            finish();
            WelcomeActivity.launch(this);
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initActionBar();
        initSliding();
        addHomePage();

        AVChatManager.getInstance().observeIncomingCall(mIncomingCallObserver, true);
    }

    private void initActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM); //Enable自定义的View
            actionBar.setCustomView(R.layout.actionbar_home);//设置自定义的布局：actionbar_custom
            mActionLogoImageView = actionBar.getCustomView().findViewById(R.id.actionAccount);
            mActionDotView = actionBar.getCustomView().findViewById(R.id.actionDot);
            mActionLogoImageView.setOnClickListener(this);

            ImageCacheMannager.loadImage(this, R.drawable.photo_test, mActionLogoImageView, true);
        }
    }

    private void initSliding() {
        SlidingMenu menu = new SlidingMenu(this);
        menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        menu.setShadowWidth(0);
//        menu.setShadowDrawable(R.drawable.shadow);
        menu.setBehindOffset(200);
        menu.setFadeDegree(0.35f);
        menu.attachToActivity(this, SlidingMenu.SLIDING_WINDOW);
        menu.setMenu(R.layout.activity_my);
    }

    private void findView() {

    }


    @Override
    protected void onResume() {
        super.onResume();
        checkUnread();
        registerObserves();
    }

    @Override
    protected void onPause() {
        super.onPause();
        unRegisterObserves();
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

    private void registerObserves() {
        NIMSDK.getMsgServiceObserve().observeRecentContact(mRecentContactObserver, true);
        NIMSDK.getAuthServiceObserve().observeOnlineStatus(mOnlineStatusObserver, true);

    }

    private void unRegisterObserves() {
        NIMSDK.getMsgServiceObserve().observeRecentContact(mRecentContactObserver, false);
        NIMSDK.getAuthServiceObserve().observeOnlineStatus(mOnlineStatusObserver, false);
    }

    private void checkUnread() {
        int count = NIMSDK.getMsgService().getTotalUnreadCount();
        if (count > 0) {
            mActionDotView.setVisibility(View.VISIBLE);
        } else {
            mActionDotView.setVisibility(View.INVISIBLE);
        }
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
        if (v == mActionLogoImageView) {
            MyActivity.launch(this);
        }
    }

    private Observer<List<RecentContact>> mRecentContactObserver = new Observer<List<RecentContact>>() {

        @Override
        public void onEvent(List<RecentContact> recentContacts) {
            checkUnread();
        }
    };
}
