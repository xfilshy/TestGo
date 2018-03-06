package com.xue.http;

import android.text.TextUtils;

import com.elianshang.tools.MD5Tool;
import com.xue.http.hook.BaseKVP;
import com.xue.http.impl.OkHttpDynamicParameter;
import com.xue.http.parse.BaseParser;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import okhttp3.Request;

public class HttpDynamicParameter<B> extends OkHttpDynamicParameter<B> {

    public HttpDynamicParameter(String baseUrl, List headers, List params, int type, BaseParser<B, ?> parser, int updateId, String secretKey) {
        super(baseUrl, headers, params, type, parser, updateId, secretKey);
    }

    @Override
    public String sign(Request.Builder builder) {

        if (TextUtils.isEmpty(secretKey) || getParams() == null) {
            return null;
        }

        StringBuilder sb = new StringBuilder();
        Collections.sort(getParams());
        int count = 0;
        for (BaseKVP kvpBean : getParams()) {
            if (!TextUtils.isEmpty(kvpBean.getKey()) && !TextUtils.isEmpty(kvpBean.getValue())) {
                if (count != 0) {
                    sb.append("&");
                }
                sb.append(kvpBean.getKey() + "=" + URLEncoder.encode(kvpBean.getValue()));

                count++;
            }
        }
        sb.append(secretKey);

        String si = MD5Tool.toMd5(sb.toString());

        builder.addHeader("sign", si);

        return si;
    }

    public void addParameter(BaseKVP kvp) {
        if (params == null) {
            params = new ArrayList();
        }

        getParams().add(kvp);
    }
}
