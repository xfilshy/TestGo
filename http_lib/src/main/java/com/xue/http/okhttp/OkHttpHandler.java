package com.xue.http.okhttp;

import com.xue.http.HttpLogTool;
import com.xue.http.exception.ResponseException;
import com.xue.http.hook.HttpHandler;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 网络请求处理类，okhttp实现
 */

public final class OkHttpHandler<B> extends HttpHandler<OkHttpParameter<B>, B> {

    private int code;

    private static String opensslSecret;

    public static void setOpensslSecret(String opensslSecret) {
        OkHttpHandler.opensslSecret = opensslSecret;
    }

    @Override
    public String doGet(OkHttpParameter params) throws ResponseException, IOException {

        OkHttpClient okHttpClient = OkHttpClientBuidler.get(opensslSecret).getOkHttpClient();
        Request.Builder requestBuilder = new Request.Builder();
        String baseUrl = params.getBaseUrl() + params.buildParameter(null);
        HttpLogTool.log("GET  " + baseUrl);
        requestBuilder.url(baseUrl);
        params.buildHeader(requestBuilder);

        Response response = okHttpClient.newCall(requestBuilder.build()).execute();
        if (response.isSuccessful()) {
            String data = response.body().string();
            response.body().close();
            return data;
        }

        code = response.code();

        throw new ResponseException();
    }

    @Override
    public String doPost(OkHttpParameter params) throws ResponseException, IOException {

        OkHttpClient okHttpClient = OkHttpClientBuidler.get(opensslSecret).getOkHttpClient();
        Request.Builder requestBuilder = new Request.Builder();
        String baseUrl = params.getBaseUrl();
        HttpLogTool.log("POST  " + baseUrl);
        requestBuilder.url(baseUrl);
        params.buildHeader(requestBuilder);
        params.buildParameter(requestBuilder);

        Response response = okHttpClient.newCall(requestBuilder.build()).execute();
        if (response.isSuccessful()) {
            String data = response.body().string();
            response.body().close();
            return data;
        }

        code = response.code();

        throw new ResponseException();
    }

    @Override
    public int getResponseCode() {
        return code;
    }
}
