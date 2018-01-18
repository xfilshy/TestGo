package com.xue.http.java;

import android.text.TextUtils;

import com.xue.http.hook.HttpParameter;
import com.xue.http.hook.BaseBean;
import com.xue.http.hook.BaseKVP;
import com.xue.http.parse.BaseParser;

import java.net.HttpURLConnection;
import java.util.List;

/**
 * 动态请求参数类
 */
public abstract class JavaHttpParameter<PR extends BaseParser<? extends BaseBean, ?>> extends HttpParameter<HttpURLConnection, PR, List<BaseKVP>> {

    /**
     * 公钥
     */
    protected String secretKey = "";

    public JavaHttpParameter(String baseUrl, List<BaseKVP> headers, List<BaseKVP> params, int type, PR parser, int updataId, String secretKey) {
        super(baseUrl, headers, params, type, parser, updataId);
        this.secretKey = secretKey;
    }


    @Override
    public String buildHeader(HttpURLConnection httpURLConnection) {
        if (httpURLConnection != null && getHeaders() != null) {
            for (BaseKVP baseKVP : getHeaders()) {
                if (baseKVP != null && !TextUtils.isEmpty(baseKVP.getKey())) {
                    httpURLConnection.setRequestProperty(baseKVP.getKey(), baseKVP.getValue());
                }
            }

            sign(httpURLConnection);
        }

        return null;
    }
}