package com.xue.preference;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.xue.BaseApplication;
import com.xue.bean.UserBase;


public class PreferencesManager {

    private static PreferencesManager mInstance;
    private Context mContext;
    private static String USER = "user";

    private static String DEVICE = "device";

    private static String TACTIC = "tactic";

    private static String SHOPPING = "shopping";

    public static PreferencesManager get() {
        if (null == mInstance) {
            mInstance = new PreferencesManager(BaseApplication.get());
        }
        return mInstance;
    }

    private PreferencesManager(Context context) {
        this.mContext = context;
    }

    /**
     * 写入设备IMEI
     */
    public void writeIMEI(String deviceId) {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(DEVICE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        if (!TextUtils.isEmpty(deviceId)) {
            editor.putString("IMEI", deviceId).commit();
        }
    }

    /**
     * 读取设备IMEI
     */
    public String readIMEI() {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(DEVICE, Context.MODE_PRIVATE);
        return sharedPreferences.getString("IMEI", null);
    }

    public void setUser(UserBase userBase) {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(USER, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("id", userBase.getId());
        editor.putString("cellphone", userBase.getCellphone());
        editor.putString("token", userBase.getToken());
        editor.commit();
    }

    public UserBase getUser() {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(USER, Context.MODE_PRIVATE);
        String id = sharedPreferences.getString("id", null);
        String name = sharedPreferences.getString("cellphone", null);
        String token = sharedPreferences.getString("token", null);
        if (!TextUtils.isEmpty(id) && !TextUtils.isEmpty(name)) {
            UserBase userBase = new UserBase();
            userBase.setId(id);
            userBase.setCellphone(name);
            userBase.setToken(token);

            return userBase;
        }

        return null;
    }
}
