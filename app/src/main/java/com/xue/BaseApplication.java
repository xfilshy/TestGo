package com.xue;

import android.app.Application;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.auth.LoginInfo;
import com.netease.nimlib.sdk.util.NIMUtil;
import com.xue.asyns.UserSaveTask;
import com.xue.bean.UserBase;
import com.xue.netease.NimSDKOptionConfig;
import com.xue.preference.PreferencesManager;

/**
 * Created by xfilshy on 2018/1/17.
 */
public class BaseApplication extends Application {

    private static BaseApplication instance;

    private static UserBase mUserBase;

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
        Log.e("xue", "getUserToken() == " + getUserToken());

        NIMClient.init(this, new LoginInfo(getUserId(), getUserToken()), NimSDKOptionConfig.getSDKOptions(this));

        if (NIMUtil.isMainProcess(this)) {

        }
    }


    public static BaseApplication get() {
        return instance;
    }

    public UserBase getUser() {
        if (mUserBase == null) {
            mUserBase = PreferencesManager.get().getUser();
            if (mUserBase == null) {
                mUserBase = new UserBase();
            }
        }

        return mUserBase;
    }

    public void setUser(UserBase userBase) {
        mUserBase = userBase;
        NIMClient.init(this, new LoginInfo(getUserId(), getUserToken()), NimSDKOptionConfig.getSDKOptions(this));
        new UserSaveTask(userBase).start();
    }

    public String getUserId() {
        return getUser().getId();
    }

    public String getUserToken() {
        return getUser().getToken();
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
