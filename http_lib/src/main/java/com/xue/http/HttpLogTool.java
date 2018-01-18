package com.xue.http;

import android.util.Log;

public class HttpLogTool {

    private static boolean isDebug = true;

    private static final int LOG_LENGTH = 4000;

    public static void setDebug(boolean isDebug) {
        HttpLogTool.isDebug = isDebug ;
    }

    public static void log(String info) {
        if (!isDebug) {
            return;
        }

//        while(info.length() >= 0){
//            if(info.length() <= LOG_LENGTH){
//                Log.e(HttpConstant.LOG, info);
//                return;
//            }
//            Log.e(HttpConstant.LOG, info.substring(0, LOG_LENGTH));
//            info = info.substring(LOG_LENGTH);
//        }
        Log.e(HttpConstant.LOG,info);
    }
}
