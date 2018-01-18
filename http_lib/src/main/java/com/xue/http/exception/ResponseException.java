package com.xue.http.exception;

/**
 * 请求返回状态非200异常
 */
public class ResponseException extends BaseException {

    public ResponseException() {
        super("http response code is not 200");
    }
}
