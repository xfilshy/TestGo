package com.xue.http.exception;

import com.xue.http.HttpLogTool;

public class BaseException extends Exception {

    private String msg;

    public BaseException(String msg) {
        this.msg = msg;
    }

    @Override
    public void printStackTrace() {
        HttpLogTool.log(msg);
    }
}
