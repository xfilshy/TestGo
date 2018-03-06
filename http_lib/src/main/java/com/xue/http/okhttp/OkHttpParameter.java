package com.xue.http.okhttp;

import android.text.TextUtils;

import com.xue.http.HttpLogTool;
import com.xue.http.hook.BaseKVP;
import com.xue.http.hook.HttpParameter;
import com.xue.http.parse.BaseParser;

import java.util.List;

import okhttp3.Request;

/**
 * 动态请求参数类
 */
public abstract class OkHttpParameter<B> extends HttpParameter<Request.Builder, B, List<BaseKVP>> {

    /**
     * 公钥
     */
    protected String secretKey = "";

    public OkHttpParameter(String baseUrl, List<BaseKVP> headers, List<BaseKVP> params, int type, BaseParser<B, ?> parser, int updateId, String secretKey) {
        super(baseUrl, headers, params, type, parser, updateId);
        this.secretKey = secretKey;
    }

    @Override
    public String buildHeader(Request.Builder requestBuilder) {
        if (requestBuilder != null && getHeaders() != null && getHeaders().size() > 0) {
            HttpLogTool.log("--------------Header--------------");
            for (BaseKVP baseKVP : getHeaders()) {
                if (baseKVP != null && !TextUtils.isEmpty(baseKVP.getKey()) && !TextUtils.isEmpty(baseKVP.getValue())) {
                    requestBuilder.addHeader(baseKVP.getKey(), baseKVP.getValue());
                    HttpLogTool.log(baseKVP.getKey() + ": " + baseKVP.getValue());
                }
            }

            if (!TextUtils.isEmpty(secretKey)) {
                sign(requestBuilder);
            }
        }

        return null;
    }
}