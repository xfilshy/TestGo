package com.xue.http.java;

import com.xue.http.HttpConstant;
import com.xue.http.HttpLogTool;
import com.xue.http.exception.ResponseException;
import com.xue.http.hook.BaseBean;
import com.xue.http.hook.HttpHandler;
import com.xue.http.parse.BaseParser;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;


/**
 * 网络请求处理类，java实现
 */

public final class JavaHttpHandler<B extends BaseBean> extends HttpHandler<JavaHttpParameter<? extends BaseParser<B, ?>>, B> {

    private int code = -2;

    /**
     * GET 请求
     */
    @Override
    public final String doGet(JavaHttpParameter params) throws ResponseException, IOException {
        if (params == null) {
            return null;
        }

        String baseUrl;
        URL url;
        String response = null;
        HttpURLConnection httpConnection = null;
        InputStream is = null;

        try {
            baseUrl = params.getBaseUrl() + params.buildParameter(null);

            HttpLogTool.log("GET  " + baseUrl);

            url = new URL(baseUrl);
            httpConnection = (HttpURLConnection) url.openConnection();
            httpConnection.setDoInput(true);

            params.buildHeader(httpConnection);

            httpConnection.setRequestMethod("GET");
            httpConnection.setReadTimeout(HttpConstant.READ_TIMEOUT);
            httpConnection.setConnectTimeout(HttpConstant.CONNECT_TIMEOUT);
            httpConnection.connect();
            int responseCode = httpConnection.getResponseCode();
            if (responseCode == 200) {
                is = httpConnection.getInputStream();
                response = read(is);
                HttpLogTool.log(response);

                return response;
            }

            code = responseCode;
            throw new ResponseException();

        } finally {
            httpConnection.disconnect();
            if (is != null) {
                try {
                    is.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            url = null;
            response = null;
            httpConnection = null;
            is = null;
        }
    }


    /**
     * POST 请求
     */
    @Override
    public final String doPost(JavaHttpParameter params) throws ResponseException, IOException {
        if (params == null) {
            return null;
        }

        URL url;
        String response = null;
        HttpURLConnection httpConnection = null;
        InputStream is = null;

        try {
            url = new URL(params.getBaseUrl());
            httpConnection = (HttpURLConnection) url.openConnection();

            httpConnection.setRequestMethod("POST");
            httpConnection.setDoOutput(true);
            httpConnection.setDoInput(true);
            httpConnection.setUseCaches(false);
            httpConnection.setReadTimeout(HttpConstant.READ_TIMEOUT);
            httpConnection.setConnectTimeout(HttpConstant.CONNECT_TIMEOUT);

            params.buildHeader(httpConnection);

            httpConnection.connect();
            String p = params.buildParameter(null);

            HttpLogTool.log("POST  " + params.getBaseUrl() + "?" + p);
            OutputStream outputStream = httpConnection.getOutputStream();
            outputStream.write(p.toString().getBytes());
            outputStream.flush();
            outputStream.close();

            int responseCode = httpConnection.getResponseCode();
            if (responseCode == 200) {
                is = httpConnection.getInputStream();
                response = read(is);
                HttpLogTool.log(response);

                return response;
            }

            code = responseCode;
            throw new ResponseException();

        } finally {
            httpConnection.disconnect();
            if (is != null) {
                try {
                    is.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            url = null;
            response = null;
            httpConnection = null;
            is = null;
        }
    }

    /**
     * 将回来的IO流转化为字符串
     */
    private final String read(InputStream in) throws IOException {
        if (in == null) {
            return null;
        }
        InputStreamReader r = null;
        String result = null;
        char[] buf = new char[100];
        try {
            StringBuilder sb = new StringBuilder();
            r = new InputStreamReader(in);
            int len = 0;
            while ((len = r.read(buf)) != -1) {
                sb.append(buf, 0, len);
            }

            result = sb.toString();

            return result;
        } finally {
            if (r != null) {
                r.close();
            }

            r = null;
            result = null;
            buf = null;
            in = null;
        }
    }

    @Override
    public int getResponseCode() {
        return code;
    }
}
