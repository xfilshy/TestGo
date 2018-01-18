package com.xue.http.hook;

import com.xue.http.parse.BaseParser;

public interface BaseHttpParameter<N, PR extends BaseParser<? extends BaseBean, ?>> {

    /**
     * 请求类型
     */
    interface Type {

        /**
         * post
         */
        int POST = 1;

        /**
         * get
         */
        int GET = 2;
    }

    int getType();

    int getDataId();

    String getBaseUrl();

    String buildHeader(N n);

    String buildParameter(N n);

    String sign(N n);

    PR getParser();

}