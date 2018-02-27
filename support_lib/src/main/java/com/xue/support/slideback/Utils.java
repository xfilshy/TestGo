
package com.xue.support.slideback;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityOptions;
import android.support.annotation.RequiresApi;

import java.lang.reflect.Method;

import static android.os.Build.VERSION.SDK_INT;
import static android.os.Build.VERSION_CODES.LOLLIPOP;

@SuppressLint("PrivateApi")
public class Utils {
    private Utils() throws IllegalAccessException {
        throw new IllegalAccessException("no instance!");
    }

    /**
     * Convert a translucent themed Activity
     * {@link android.R.attr#windowIsTranslucent} to a fullscreen opaque
     * Activity.
     * <p>
     * Call this whenever the background of a translucent Activity has changed
     * to become opaque. Doing so will allow the {@link android.view.Surface} of
     * the Activity behind to be released.
     * <p>
     * This call has no effect on non-translucent activities or on activities
     * with the {@link android.R.attr#windowIsFloating} attribute.
     */
    public static void convertActivityToOpaque(Activity activity) {
        try {
            Method method = Activity.class.getDeclaredMethod("convertFromTranslucent");
            method.setAccessible(true);
            method.invoke(activity);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Convert a translucent themed Activity
     * {@link android.R.attr#windowIsTranslucent} back from opaque to
     * translucent following a call to
     * {@link #convertActivityToOpaque(Activity)} .
     * <p>
     * Calling this allows the Activity behind this one to be seen again. Once
     * all such Activities have been redrawn
     * <p>
     * This call has no effect on non-translucent activities or on activities
     * with the {@link android.R.attr#windowIsFloating} attribute.
     */
    public static void convertActivityToTranslucent(Activity activity) {
        try {
            if (SDK_INT >= LOLLIPOP) {
                convertActivityToTranslucentSinceL(activity);
            } else {
                convertActivityToTranslucentBeforeL(activity);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Calling the convertToTranslucent method on platforms before Android 5.0
     *
     * @before Android 5.0
     */
    private static void convertActivityToTranslucentBeforeL(Activity activity) throws Exception {
        Class<?>[] classes = Activity.class.getDeclaredClasses();
        Class<?> translucentConversionListenerClass = null;
        for (Class clazz : classes) {
            if ("TranslucentConversionListener".equals(clazz.getSimpleName())) {
                translucentConversionListenerClass = clazz;
                break;
            }
        }
        Method method = Activity.class.getDeclaredMethod("convertToTranslucent",
                translucentConversionListenerClass);
        method.setAccessible(true);
        method.invoke(activity, (Object[]) null);
    }

    /**
     * Calling the convertToTranslucent method on platforms since Android 5.0
     *
     * @since Android 5.0
     */
    @RequiresApi(api = LOLLIPOP)
    private static void convertActivityToTranslucentSinceL(Activity activity) throws Exception {
        Method getActivityOptions = Activity.class.getDeclaredMethod("getActivityOptions");
        getActivityOptions.setAccessible(true);
        Object options = getActivityOptions.invoke(activity);

        Class<?>[] classes = Activity.class.getDeclaredClasses();
        Class<?> translucentConversionListenerClass = null;
        for (Class clazz : classes) {
            if ("TranslucentConversionListener".equals(clazz.getSimpleName())) {
                translucentConversionListenerClass = clazz;
                break;
            }
        }
        Method convertToTranslucent = Activity.class.getDeclaredMethod("convertToTranslucent",
                translucentConversionListenerClass, ActivityOptions.class);
        convertToTranslucent.setAccessible(true);
        convertToTranslucent.invoke(activity, null, options);
    }
}
