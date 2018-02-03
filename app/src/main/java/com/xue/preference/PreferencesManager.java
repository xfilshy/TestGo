package com.xue.preference;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.xue.BaseApplication;
import com.xue.bean.User;
import com.xue.parsers.UserParser;

import org.json.JSONObject;


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

    public void setUser(User user) {
        if (user == null) {
            SharedPreferences sharedPreferences = mContext.getSharedPreferences(USER, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.clear();
            editor.commit();
        } else {
            SharedPreferences sharedPreferences = mContext.getSharedPreferences(USER, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("user", user.toString());
            editor.commit();
        }
    }

    public User getUser() {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(USER, Context.MODE_PRIVATE);
        String userString = sharedPreferences.getString("user", null);
        User user = null;
        if (!TextUtils.isEmpty(userString)) {
            UserParser userParser = new UserParser();
            try {
                user = userParser.parse(new JSONObject(userString));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return user;
    }
}
