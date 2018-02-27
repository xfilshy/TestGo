package com.xue.support.slideback.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Px;
import android.support.annotation.RequiresApi;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import java.lang.reflect.Method;

import static android.os.Build.VERSION.SDK_INT;
import static android.os.Build.VERSION_CODES.KITKAT;
import static android.os.Build.VERSION_CODES.LOLLIPOP;
import static android.os.Build.VERSION_CODES.M;

public class SystemBarUtil {
    private SystemBarUtil() throws IllegalAccessException {
        throw new IllegalAccessException("no instance!");
    }

    @Px
    public static int getStatusHeight(@NonNull Context context) {
        final int resId = context.getResources().getIdentifier("status_bar_height",
                "dimen", "android");
        if (resId > 0) {
            return context.getResources().getDimensionPixelSize(resId);
        }
        return 0;
    }

    @Px
    @RequiresApi(KITKAT)
    public static int getNavigationHeight(@NonNull Context context) {
        final int resId = context.getResources().getIdentifier("navigation_bar_height",
                "dimen", "android");
        if (resId > 0) {
            return context.getResources().getDimensionPixelSize(resId);
        }
        return 0;
    }

    /**
     * 判断是否有虚拟按键
     */
    @SuppressLint("PrivateApi")
    @RequiresApi(KITKAT)
    public static boolean hasNavigationBar(@NonNull Context context) {
        boolean hasNavigationBar = false;
        final int resId = context.getResources().getIdentifier("config_showNavigationBar",
                "bool", "android");
        if (resId > 0) {
            hasNavigationBar = context.getResources().getBoolean(resId);
        }

        try {
            Class<?> systemPropertiesClass = Class.forName("android.os.SystemProperties");
            Method m = systemPropertiesClass.getMethod("get", String.class);
            String navBarOverride = (String) m.invoke(systemPropertiesClass, "qemu.hw.mainkeys");

            if ("1".equals(navBarOverride)) {
                hasNavigationBar = false;
            } else if ("0".equals(navBarOverride)) {
                hasNavigationBar = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return hasNavigationBar;
    }

    /**
     * 设置显示或隐藏状态栏和虚拟按键
     * {@link View#SYSTEM_UI_FLAG_LAYOUT_STABLE}：使View的布局不变，隐藏状态栏或导航栏后，View不会被拉伸。
     * {@link View#SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN}：让View全屏显示，布局会被拉伸到状态栏下面，不包含虚拟按键。
     * {@link View#SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION}：让View全屏显示，布局会被拉伸到状态栏和导航栏下面。
     */
    public static void showSystemBars(@NonNull Activity activity, boolean show) {
        Window window = activity.getWindow();
        if (SDK_INT >= KITKAT) {
            View decorView = window.getDecorView();
            int flags = decorView.getSystemUiVisibility();
            if (show) {
                // This snippet shows the system bars.
                // It does this by removing all the flags.
                // Make the content resize and appear below status bar
                // and above navigation bar(if the device has).
                flags &= ~(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
            } else {
                // This snippet hides the system bars.
                flags |= View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        // Set the above 2 flags to make the content appear
                        // under the system bars so that the content doesn't resize
                        // when the system bars hide and show.
                        | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide navigation bar
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
                // Set the IMMERSIVE_STICKY flag to prevent the flags of
                // hiding navigation bar and hiding status bar from being force-cleared
                // by the system on any user interaction.
            }
            decorView.setSystemUiVisibility(flags);
        } else {
            if (show)
                window.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
            else
                window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
    }

    /**
     * 设置 半透明状态栏
     */
    @RequiresApi(KITKAT)
    public static void setTranslucentStatus(@NonNull Activity activity, boolean translucent) {
        Window window = activity.getWindow();
        WindowManager.LayoutParams winParams = window.getAttributes();
        final int statusFlag = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (translucent) {
            winParams.flags |= statusFlag;
        } else {
            winParams.flags &= ~statusFlag;
        }
        window.setAttributes(winParams);
    }

    /**
     * 设置 半透明虚拟按键
     */
    @RequiresApi(KITKAT)
    public static void setTranslucentNavigation(Activity activity, boolean translucent) {
        Window window = activity.getWindow();
        WindowManager.LayoutParams winParams = window.getAttributes();
        final int navigationFlag = WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION;
        if (translucent) {
            winParams.flags |= navigationFlag;
        } else {
            winParams.flags &= ~navigationFlag;
        }
        window.setAttributes(winParams);
    }

    /**
     * 改变状态栏字体颜色（黑/白）
     *
     * @see <a href="https://developer.android.com/reference/android/R.attr.html#windowLightStatusBar"></a>
     */
    @RequiresApi(api = M)
    public static void setLightStatus(Activity activity, boolean light) {
        Window window = activity.getWindow();
        View decorView = window.getDecorView();
        int flag = decorView.getSystemUiVisibility();
        if (light) {
            flag |= WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS
                    | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
        } else {
            flag &= ~(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS
                    | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
        decorView.setSystemUiVisibility(flag);
    }

    /**
     * 设置 透明状态栏
     */
    @RequiresApi(api = LOLLIPOP)
    public static void setTransparentStatus(Activity activity, boolean transparent) {
        Window window = activity.getWindow();
        View decorView = window.getDecorView();
        int flag = decorView.getSystemUiVisibility();
        if (transparent) {
            flag |= View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            window.setStatusBarColor(Color.TRANSPARENT);
        } else {
            flag &= ~(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            //window.setStatusBarColor(activity.getResources().getColor(R.color.colorPrimaryDark));
        }
        decorView.setSystemUiVisibility(flag);
    }

    /**
     * 设置 透明虚拟按键
     */
    @RequiresApi(api = LOLLIPOP)
    public static void setTransparentNavigation(Activity activity, boolean transparent) {
        Window window = activity.getWindow();
        View decorView = window.getDecorView();
        int flag = decorView.getSystemUiVisibility();
        if (transparent) {
            flag |= View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION;
            window.setStatusBarColor(Color.TRANSPARENT);
            window.setNavigationBarColor(Color.TRANSPARENT);
        } else {
            flag &= ~View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION;
            //window.setStatusBarColor(activity.getResources().getColor(R.color.colorPrimaryDark));
            //window.setNavigationBarColor();
        }
        decorView.setSystemUiVisibility(flag);
    }
}