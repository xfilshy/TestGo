package com.xue.http.exception;

/**
 * 请求数据为空的异常
 */

public class DataIsNullException extends BaseException {

    public DataIsNullException() {
        super("json string is null");
    }

}
