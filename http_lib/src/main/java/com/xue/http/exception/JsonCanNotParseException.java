package com.xue.http.exception;

/**
 * 数据头不能正常解析异常
 */

public class JsonCanNotParseException extends BaseException {

    public JsonCanNotParseException() {
        super("canParse is return false");
    }

}
