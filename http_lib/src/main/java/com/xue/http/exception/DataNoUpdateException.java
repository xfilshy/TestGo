package com.xue.http.exception;

/**
 * 数据无更新异常
 */

public class DataNoUpdateException extends BaseException {

    public DataNoUpdateException() {
        super("data has not update");
    }

}
