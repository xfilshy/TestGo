package com.xue.support.slideback.utils;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.util.DisplayMetrics;

import static android.os.Build.VERSION.SDK_INT;
import static android.os.Build.VERSION_CODES.JELLY_BEAN_MR1;

public class DensityUtil {
    private DensityUtil() throws IllegalAccessException {
        throw new IllegalAccessException("no instance!");
    }

    public static int dp2px(@NonNull Context context, float dpValue) {
        final float density = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * density + 0.5f);
    }

    public static int px2dp(@NonNull Context context, float pxValue) {
        final float density = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / density + 0.5f);
    }

    public static int getScreenWidth(@NonNull Activity activity) {
        DisplayMetrics dm = activity.getResources().getDisplayMetrics();
        if (SDK_INT >= JELLY_BEAN_MR1) {
            activity.getWindowManager().getDefaultDisplay().getRealMetrics(dm);
        }
        return dm.widthPixels;
    }

    public static int getScreenHeight(@NonNull Activity activity) {
        DisplayMetrics dm = activity.getResources().getDisplayMetrics();
        if (SDK_INT >= JELLY_BEAN_MR1) {
            activity.getWindowManager().getDefaultDisplay().getRealMetrics(dm);
        }
        return dm.heightPixels;
    }
}
