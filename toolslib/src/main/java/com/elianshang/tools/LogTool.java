package com.elianshang.tools;

import android.util.Log;

/**
 * 日志工具
 */
public class LogTool {

    private static boolean isDebug = true;

    public static void setDebug(boolean isDebug) {
        LogTool.isDebug = isDebug;
    }

    public static boolean isDebug() {
        return isDebug;
    }

    /**
     * 错误级日志
     */
    public static void e(String tag, String logMsg) {
        if (LogLevelTool.curLevel.getCode() <= LogLevelTool.ERROR.getCode() && isDebug)
            Log.e(tag, logMsg);
    }


    /**
     * 错误级日志
     */
    public static void e(String logMsg) {
        if (LogLevelTool.curLevel.getCode() <= LogLevelTool.ERROR.getCode() && isDebug)
            e(LogTool.class.getName(), logMsg);
    }

    /**
     * 警告级日志
     */
    public static void w(String tag, String logMsg) {
        if (LogLevelTool.curLevel.getCode() <= LogLevelTool.WARN.getCode() && isDebug)
            Log.w(tag, logMsg);
    }


    /**
     * 警告级日志
     */
    public static void w(String logMsg) {
        if (LogLevelTool.curLevel.getCode() <= LogLevelTool.WARN.getCode() && isDebug)
            w(LogTool.class.getName(), logMsg);
    }

    /**
     * 调试级日志
     */
    public static void d(String tag, String logMsg) {
        if (LogLevelTool.curLevel.getCode() <= LogLevelTool.DEBUG.getCode() && isDebug)
            Log.d(tag, logMsg);
    }

    /**
     * 调试级日志
     */
    public static void d(String logMsg) {
        if (LogLevelTool.curLevel.getCode() <= LogLevelTool.DEBUG.getCode() && isDebug)
            d(LogTool.class.getName(), logMsg);
    }

    /**
     * 明细级日志
     */
    public static void i(String tag, String logMsg) {
        if (LogLevelTool.curLevel.getCode() <= LogLevelTool.DEBUG.getCode() && isDebug)
            Log.i(tag, logMsg);
    }


    /**
     * 明细级日志
     */
    public static void i(String logMsg) {
        if (LogLevelTool.curLevel.getCode() <= LogLevelTool.DEBUG.getCode() && isDebug)
            i(LogTool.class.getName(), logMsg);
    }
}
