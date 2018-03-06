package com.xue.http.impl;

import android.text.TextUtils;

import com.xue.http.java.JavaHttpParameter;
import com.xue.http.hook.BaseKVP;
import com.xue.http.parse.BaseParser;

import java.net.HttpURLConnection;
import java.net.URLEncoder;
import java.util.List;

/**
 * 动态请求参数类
 */
public abstract class JavaHttpDynamicParameter<B> extends JavaHttpParameter<B> {

    public JavaHttpDynamicParameter(String baseUrl, List<BaseKVP> headers, List<BaseKVP> params, int type, BaseParser<B, ?> parser, int updateId, String secretKey) {
        super(baseUrl, headers, params, type, parser, updateId, secretKey);
    }

    public StringBuilder encodeUrl() {
        StringBuilder sb = new StringBuilder();
        if (getParams() == null) {
            return sb;
        }
        boolean first = true;

        for (BaseKVP kvpBean : getParams()) {
            if (!TextUtils.isEmpty(kvpBean.getKey()) && !TextUtils.isEmpty(kvpBean.getValue())) {
                if (first) {
                    if (getType() == Type.GET) {
                        sb.append("?");
                    }
                    first = false;
                } else {
                    sb.append("&");
                }
                sb.append(kvpBean.getKey() + "=" + URLEncoder.encode(kvpBean.getValue()));
            }
        }
        return sb;
    }


    @Override
    public String buildParameter(HttpURLConnection httpURLConnection) {
        return encodeUrl().toString();
    }
}