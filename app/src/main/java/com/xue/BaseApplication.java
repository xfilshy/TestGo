package com.xue;

import android.app.Application;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.auth.LoginInfo;
import com.netease.nimlib.sdk.util.NIMUtil;
import com.xue.asyns.UserSaveTask;
import com.xue.bean.User;
import com.xue.bean.UserBase;
import com.xue.bean.UserInfoDetail;
import com.xue.netease.NimSDKOptionConfig;
import com.xue.preference.PreferencesManager;

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
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        Log.e("xue", "getUserId() == " + getUserId());
        Log.e("xue", "getUserToken() == " + getNeteaseToken());

        NIMClient.init(this, new LoginInfo(getUserId(), getNeteaseToken()), NimSDKOptionConfig.getSDKOptions(this));

        if (NIMUtil.isMainProcess(this)) {

        }
    }

    public static BaseApplication get() {
        return instance;
    }

    private synchronized User getUser() {
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

    public void setUserInfoDetail(UserInfoDetail userInfoDetail) {
        getUser().setUserInfoDetail(userInfoDetail);
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
