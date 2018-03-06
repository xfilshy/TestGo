package com.xue.http.hook;

import com.xue.http.parse.BaseParser;

/**
 * 请求参数组装类
 * 封装：
 * 请求地址
 * 参数
 * 解析器
 * 刷新ID
 */
public abstract class HttpParameter<N, B, P> implements BaseHttpParameter<N, B> {

    /**
     * baseUrl请求地址
     */
    protected String baseUrl;

    /**
     * 参数
     */
    protected P params;

    /**
     * 头参数
     */
    protected P headers;

    /**
     * 请求完成后的更新ID
     */
    protected int dataId = -1;

    /**
     * 请求方式 post 或  get
     */
    protected int type;

    /**
     * 请求结束后的解析器
     */
    protected BaseParser<B, ?> parser;

    /**
     * 构造方法
     */
    public HttpParameter(String baseUrl, P headers, P params, int type, BaseParser<B, ?> parser, int dataId) {
        this.baseUrl = baseUrl;
        this.headers = headers;
        this.params = params;
        this.type = type;
        this.parser = parser;
        this.dataId = dataId;
    }

    /**
     * 得到baseUrl请求地址
     */
    @Override
    public String getBaseUrl() {
        return baseUrl;
    }

    /**
     * 得到参数
     */
    public P getParams() {
        return params;
    }

    /**
     * 得到头参数
     */
    public P getHeaders() {
        return headers;
    }

    /**
     * 得完成后的更新ID
     */
    public int getDataId() {
        return dataId;
    }

    /**
     * 得到请求结束后的解析器
     */
    public BaseParser<B, ?> getParser() {
        return parser;
    }

    /**
     * 得到请求类型
     */
    public int getType() {
        return type;
    }

}
