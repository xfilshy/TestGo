package com.xue.http.hook;

import com.xue.http.exception.ResponseException;
import com.xue.http.impl.DataHull;
import com.xue.http.parse.BaseParser;

import java.io.IOException;

/**
 * 请求描述接口
 */
public interface BaseHttpHandler<P extends BaseHttpParameter<?, ? extends BaseParser<B, ?>>, B extends BaseBean> {

    String doGet(P params) throws ResponseException, IOException;

    String doPost(P params) throws ResponseException, IOException;

    DataHull<B> requestData(P httpParameter);

    int getResponseCode();

}
