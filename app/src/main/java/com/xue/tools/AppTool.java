package com.xue.tools;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;

public class AppTool {


    /**
     * sdk版本
     *
     * @return
     */
    public static int getSDKVersionNumber() {
        int sdkVersion;
        try {
            sdkVersion = Integer.valueOf(Build.VERSION.SDK_INT);
        } catch (NumberFormatException e) {
            sdkVersion = 0;
        }
        return sdkVersion;
    }

    /**
     * 获取客户端版本
     *
     * @param context
     * @return
     */
    public static String getAppVersion(Context context) {
        String version = "";
        try {
            version = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return version;
    }


    /**
     * 获取客户端版本
     *
     * @param context
     * @return
     */
    public static int getVersionCode(Context context) {
        int vCode = 0;
        try {
            vCode = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return vCode;
    }


    /**
     * 获取分包渠道
     *
     * @param context
     * @return
     */
    public static String getAppKey(Context context) {

        return "123";
    }


    public static String getMetaValue(Context context, String metaKey) {
        Bundle metaData = null;
        String apiKey = null;
        if (context == null || metaKey == null) {
            return null;
        }
        try {
            ApplicationInfo ai = context.getPackageManager()
                    .getApplicationInfo(context.getPackageName(),
                            PackageManager.GET_META_DATA);
            if (null != ai) {
                metaData = ai.metaData;
            }
            if (null != metaData) {
                apiKey = metaData.getString(metaKey);
            }
        } catch (PackageManager.NameNotFoundException e) {

        }
        return apiKey;
    }

    public static int color2Int(String color) {
        int colorInt = 0;
        if (!TextUtils.isEmpty(color)) {

            int len = color.length();

            if (len == 6) {
                color = "FF" + color;
            }

            if (len == 8) {
                color = "#" + color;
                try {
                    colorInt = Color.parseColor(color);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        return colorInt;
    }

}
