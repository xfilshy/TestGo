package com.elianshang.tools;

/**
 * 日志级别工具
 * */
public enum LogLevelTool {
    /**
     * 调试级，可见所有日志
     * */
    DEBUG(0) ,

    /**
     * 警告级，可见所有警告和错误
     * */
    WARN(1) ,

    /**
     * 错误级，可见错误
     * */
    ERROR(2) ,

    /**
     * 正常级，所有日志不可见
     * */
    NORMAL(3) ;

    private int code ;

    /**
     * 当前日志级别
     * */
    public static LogLevelTool curLevel = DEBUG ;

    LogLevelTool(int code){
        this.code = code ;
    }

    public int getCode(){
        return code ;
    }
}
