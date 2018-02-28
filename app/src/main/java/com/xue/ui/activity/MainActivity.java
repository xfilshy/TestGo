package com.xue.ui.activity;

import android.animation.ArgbEvaluator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.elianshang.tools.ToastTool;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.NIMSDK;
import com.netease.nimlib.sdk.Observer;
import com.netease.nimlib.sdk.StatusCode;
import com.netease.nimlib.sdk.auth.AuthServiceObserver;
import com.netease.nimlib.sdk.avchat.AVChatManager;
import com.netease.nimlib.sdk.avchat.model.AVChatData;
import com.netease.nimlib.sdk.msg.model.RecentContact;
import com.xue.BaseApplication;
import com.xue.R;
import com.xue.asyns.HttpAsyncTask;
import com.xue.bean.User;
import com.xue.bean.UserConfigInfo;
import com.xue.bean.UserDetailInfo;
import com.xue.bean.UserExpertInfo;
import com.xue.http.HttpApi;
import com.xue.http.impl.DataHull;
import com.xue.imagecache.ImageCacheMannager;
import com.xue.oss.OssManager;
import com.xue.oss.SimpleOssManagerCallback;
import com.xue.preference.PreferencesManager;
import com.xue.tools.GlideImageLoader;
import com.xue.tools.SimplePickHandlerCallBack;
import com.xue.ui.fragment.HomeFragment;
import com.yancy.gallerypick.config.GalleryConfig;
import com.yancy.gallerypick.config.GalleryPick;
import com.yarolegovich.slidingrootnav.SlidingRootNav;
import com.yarolegovich.slidingrootnav.SlidingRootNavBuilder;
import com.yarolegovich.slidingrootnav.callback.DragListener;

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

    private SlidingRootNav mSlidingRootNav;

    private Toolbar mToolbar;

    private ImageView mActionLogoImageView;

    private View mActionDotView;

    private LinearLayout mTipLinearLayout;

    private ImageView mPhotoImageView;

    private TextView mRealNameTextView;

    private ImageView mGenderImageView;

    private TextView mUidTextView;

    private TextView mSignatureTextView;

    private TextView mFeeTextView;

    private TextView mAuthTextView;

    private long mUserTimeStamp;

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
        initSliding(savedInstanceState);
        findView();
        addHomePage();

        checkUserTip();
        new UserInfoTask(this).start();

        AVChatManager.getInstance().observeIncomingCall(mIncomingCallObserver, true);
    }

    private void initActionBar() {
        mToolbar = findViewById(R.id.toolbar);
        mActionLogoImageView = mToolbar.findViewById(R.id.actionAccount);
        mActionDotView = mToolbar.findViewById(R.id.actionDot);

        setSupportActionBar(mToolbar);

        ImageCacheMannager.loadImage(this, R.drawable.photo_test, mActionLogoImageView, true);
    }

    private void initSliding(@Nullable Bundle savedInstanceState) {
        mSlidingRootNav = new SlidingRootNavBuilder(this)
                .withDragDistance(236)
                .withToolbarMenuToggle(mToolbar)
                .withMenuOpened(false)
                .withContentClickableWhenMenuOpened(false)
                .withSavedState(savedInstanceState)
                .withMenuLayout(R.layout.activity_my)
                .addDragListener(new DragListener() {
                    @Override
                    public void onDrag(float progress) {
                        changeStatusBar(progress);
                    }
                })
                .inject();
    }

    private void findView() {
        mTipLinearLayout = mSlidingRootNav.getLayout().findViewById(R.id.tip);
        mPhotoImageView = mSlidingRootNav.getLayout().findViewById(R.id.photo);
        mRealNameTextView = mSlidingRootNav.getLayout().findViewById(R.id.realName);
        mGenderImageView = mSlidingRootNav.getLayout().findViewById(R.id.gender);
        mUidTextView = mSlidingRootNav.getLayout().findViewById(R.id.uid);
        mSignatureTextView = mSlidingRootNav.getLayout().findViewById(R.id.signature);
        mFeeTextView = mSlidingRootNav.getLayout().findViewById(R.id.fee);
        mAuthTextView = mSlidingRootNav.getLayout().findViewById(R.id.auth);
    }

    private void checkUserTip() {
        boolean isShowUserTip = PreferencesManager.get().isShowUserTip();
        if (isShowUserTip) {
            mTipLinearLayout.setVisibility(View.VISIBLE);
        } else {
            mTipLinearLayout.setVisibility(View.GONE);
        }
    }

    private void fillUser() {
        User user = BaseApplication.get().getUser();
        if (mUserTimeStamp != user.getTimeStamp()) {
            mUidTextView.setText(user.getUserBase().getUid());

            UserDetailInfo userDetailInfo = user.getUserDetailInfo();
            if (userDetailInfo != null) {
                ImageCacheMannager.loadImage(this, user.getUserDetailInfo().getProfile(), mPhotoImageView, true);
                mRealNameTextView.setText(userDetailInfo.getRealName());
                if (TextUtils.equals("1", userDetailInfo.getGender())) {
                    ImageCacheMannager.loadImage(this, R.drawable.icon_male, mGenderImageView, false);
                } else if (TextUtils.equals("2", userDetailInfo.getGender())) {
                    ImageCacheMannager.loadImage(this, R.drawable.icon_female, mGenderImageView, false);
                }
            }

            UserConfigInfo userConfigInfo = user.getUserConfigInfo();
            if (userConfigInfo != null) {
                mFeeTextView.setVisibility(View.VISIBLE);
                mFeeTextView.setText(userConfigInfo.getFeeDefault() + "钻/分钟");
            }

            UserExpertInfo userExpertInfo = user.getUserExpertInfo();
            if (userExpertInfo != null) {
                if (!TextUtils.isEmpty(userExpertInfo.getSignature())) {
                    mSignatureTextView.setVisibility(View.VISIBLE);
                    mSignatureTextView.setText(userExpertInfo.getSignature());
                } else {
                    mSignatureTextView.setVisibility(View.GONE);
                }

                if (userExpertInfo.getServiceFee() > 0) {
                    mFeeTextView.setVisibility(View.VISIBLE);
                    mFeeTextView.setText(userExpertInfo.getServiceFee() + "钻/分钟");
                } else {
                    mFeeTextView.setVisibility(View.GONE);
                }

                if (TextUtils.equals("0", userExpertInfo.getStatus())) {
                    mAuthTextView.setText("未认证");
                } else if (TextUtils.equals("1", userExpertInfo.getStatus())) {
                    mAuthTextView.setText("认证中");
                } else if (TextUtils.equals("2", userExpertInfo.getStatus())) {
                    mAuthTextView.setText("认证通过");
                } else if (TextUtils.equals("3", userExpertInfo.getStatus())) {
                    mAuthTextView.setText("认证失败");
                }
            } else {
                mSignatureTextView.setVisibility(View.GONE);
            }
        }

        mUserTimeStamp = user.getTimeStamp();
    }

    private void changeStatusBar(float progress) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Window window = getWindow();
                ArgbEvaluator argbEvaluator = new ArgbEvaluator();
                int tmp = (Integer) argbEvaluator.evaluate(progress, getResources().getColor(R.color.colorPrimary), getResources().getColor(R.color.black_tr_80));
                window.setStatusBarColor(tmp);
                //window.setNavigationBarColor(activity.getResources().getColor(colorResId));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        fillUser();
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


    public void closeTip(View view) {
        mTipLinearLayout.setVisibility(View.GONE);
        PreferencesManager.get().closeUserTip();
    }

    public void goPrice(View view) {
        FeeSettingActivity.launch(this);
    }

    public void goInfo(View view) {
        InfoActivity.launch(this);
    }

    public void goCertify(View view) {
        CertifyActivity.launch(this);
    }

    public void goRecharge(View view) {
        RechargeActivity.launch(this);
    }

    public void goPayments(View view) {
        PaymentsActivity.launch(this);
    }

    public void goSetting(View view) {
        SettingActivity.launch(this);
    }

    public void goPickPhoto(View view) {
        SimplePickHandlerCallBack iHandlerCallBack = new SimplePickHandlerCallBack() {

            @Override
            public void onSuccess(List<String> photoList) {
                if (photoList != null && photoList.size() > 0) {
                    final String photoPath = photoList.get(0);
                    ImageCacheMannager.loadImage(MainActivity.this, photoPath, mPhotoImageView, true);

                    new UploadTask(MainActivity.this, photoPath);
                }
            }
        };
        GalleryConfig galleryConfig = new GalleryConfig.Builder()
                .imageLoader(new GlideImageLoader())
                .iHandlerCallBack(iHandlerCallBack)
                .provider("com.xue.fileprovider")
                .crop(true, 1, 1, 1000, 1000)
                .isShowCamera(true)
                .filePath("/Gallery/Pictures")
                .build();

        GalleryPick.getInstance().setGalleryConfig(galleryConfig).open(this);
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

    private class UserInfoTask extends HttpAsyncTask<User> {

        public UserInfoTask(Context context) {
            super(context, true, true);
        }

        @Override
        public DataHull<User> doInBackground() {
            return HttpApi.userInfo();
        }

        @Override
        public void onPostExecute(int updateId, User result) {
            BaseApplication.get().setUser(result, false);
            fillUser();
        }
    }

    private static class UploadTask extends HttpAsyncTask<UserDetailInfo> {

        private String resultPath;

        public UploadTask(Context context, String cover) {
            super(context, true, true);
            showLoadingDialog();
            OssManager.get().upload(cover, callback, true);
        }

        @Override
        public DataHull<UserDetailInfo> doInBackground() {
            return HttpApi.updateUserDetailInfo(null, null, null, resultPath, null, null);
        }

        @Override
        public void onPostExecute(int updateId, UserDetailInfo result) {
            ToastTool.show(context, "头像上传成功");
            BaseApplication.get().setUserDetailInfo(result);
        }

        @Override
        public void dataNull(int updateId, String errMsg) {
            super.dataNull(updateId, errMsg);
            ToastTool.show(context, "头像上传失败");
        }

        @Override
        public void netNull() {
            super.netNull();
            ToastTool.show(context, "头像上传失败");
        }

        @Override
        public void netErr(int updateId, String errMsg) {
            super.netErr(updateId, errMsg);
            ToastTool.show(context, "头像上传失败");
        }

        private OssManager.Callback callback = new SimpleOssManagerCallback() {

            @Override
            public void onSuccess(String file, String resultName) {
                UploadTask.this.resultPath = resultName;
                UploadTask.this.start();
            }

            @Override
            public void onInitFailure() {
                ToastTool.show(context, "初始化上传头像失败");
            }

            @Override
            public void onFailure(String file, int code) {
                ToastTool.show(context, "头像上传失败");
            }
        };

    }
}
