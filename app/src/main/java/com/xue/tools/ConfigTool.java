package com.xue.tools;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.text.TextUtils;
import android.util.Log;

import com.xue.BaseApplication;

public class ConfigTool {

    private static Config config;

    private static String pcode;

    private static void init() {
        if (config == null) {
            ApplicationInfo appInfo = null;
            try {
                Log.d("xue" , " BaseApplication.get().getPackageManager() == " +  BaseApplication.get().getPackageManager());
                appInfo = BaseApplication.get().getPackageManager().getApplicationInfo(BaseApplication.get().getPackageName(), PackageManager.GET_META_DATA);
                if (appInfo.metaData != null) {
                    String value = appInfo.metaData.getString("CONFIG");
                    if ("test".equals(value)) {
                        config = new ConfigForTest();
                    } else {
                        config = new ConfigForPublic();
                    }

                    value = appInfo.metaData.getString("PCODE");
                    if (TextUtils.isEmpty(value)) {
                        pcode = "unknown";
                    } else {
                        pcode = value;
                    }
                } else {
                    config = new ConfigForPublic();
                    pcode = "unknown";
                }
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    public static boolean isDebug() {
        init();
        return config.isDebug();
    }

    public static String getHttpBaseUrl() {
        init();
        return config.getHttpBaseUrl();
    }

    public static String getAliPayNotifyUrl() {
        init();
        return config.getAliPayNotifyUrl();
    }

    public static String getPcode() {
        init();
        return pcode;
    }
}
