package com.xue;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;
import android.text.TextUtils;
import android.util.Log;

import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.auth.LoginInfo;
import com.netease.nimlib.sdk.util.NIMUtil;
import com.previewlibrary.ZoomMediaLoader;
import com.xue.asyns.UserSaveTask;
import com.xue.bean.User;
import com.xue.bean.UserBase;
import com.xue.bean.UserConfigInfo;
import com.xue.bean.UserDetailInfo;
import com.xue.bean.UserEducationInfo;
import com.xue.bean.UserExpertInfo;
import com.xue.bean.UserTagInfo;
import com.xue.bean.UserWorkInfo;
import com.xue.netease.NimSDKOptionConfig;
import com.xue.preference.PreferencesManager;
import com.xue.support.slideback.SwipeBackHelper;
import com.xue.tools.PreviewImageLoader;

/**
 * Created by xfilshy on 2018/1/17.
 */
public class BaseApplication extends Application {

    private static BaseApplication instance;

    private User mUser;

    /**
     * 标记只会第一次获取 从本地读取
     */
    private boolean mUserInitFlag = false;

    private String deviceId;

    private String imei;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(newBase);
        MultiDex.install(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        Log.e("xue", "getUserId() == " + getUserId());
        Log.e("xue", "getUserToken() == " + getNeteaseToken());

        NIMClient.init(this, new LoginInfo(getUserId(), getNeteaseToken()), NimSDKOptionConfig.getSDKOptions(this));

        if (NIMUtil.isMainProcess(this)) {
            /**
             * 初始化查看图片
             * */
            ZoomMediaLoader.getInstance().init(new PreviewImageLoader());
            SwipeBackHelper.init(this, null);
        }
    }

    public static BaseApplication get() {
        return instance;
    }

    public synchronized User getUser() {
        if (mUser == null) {
            if (!mUserInitFlag) {
                mUser = PreferencesManager.get().getUser();
                mUserInitFlag = true;
            }
            if (mUser == null) {//这个逻辑代表是初始化过
                mUser = new User(new UserBase());
            }
        }

        return mUser;
    }

    public void setUser(User user, boolean init) {
        mUser = user;
        new UserSaveTask(mUser).start();
        if (init) {
            NIMClient.init(this, new LoginInfo(getUserId(), getNeteaseToken()), NimSDKOptionConfig.getSDKOptions(this));
        }
    }

    public String getUserId() {
        return getUser().getUserBase().getUid();
    }

    public String getUserToken() {
        return getUser().getUserBase().getToken();
    }

    public String getNeteaseToken() {
        return getUser().getUserBase().getNeteaseToken();
    }

    public void setUserDetailInfo(UserDetailInfo userDetailInfo) {
        getUser().setUserDetailInfo(userDetailInfo);
        new UserSaveTask(mUser).start();
    }

    public void setUserExpertInfo(UserExpertInfo userExpertInfo) {
        getUser().setUserExpertInfo(userExpertInfo);
    }

    public void setUserEducationInfo(UserEducationInfo userEducationInfo) {
        getUser().setUserEducationInfo(userEducationInfo);
    }

    public void setUserWorkInfo(UserWorkInfo userWorkInfo) {
        getUser().setUserWorkInfo(userWorkInfo);
    }

    public void setUserTagInfo(UserTagInfo userTagInfo) {
        getUser().setUserTagInfo(userTagInfo);
    }

    public void setUserConfigInfo(UserConfigInfo userConfigInfo) {
        getUser().setUserConfigInfo(userConfigInfo);
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
        if (!TextUtils.isEmpty(imei)) {
            PreferencesManager.get().writeIMEI(imei);
        }
    }
}
