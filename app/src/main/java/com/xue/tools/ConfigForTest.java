package com.xue.tools;

class ConfigForTest implements Config {

    ConfigForTest() {
    }

    @Override
    public boolean isTestApi() {
        return true;
    }

    @Override
    public boolean isDebug() {
        return true;
    }

    @Override
    public String getHttpBaseUrl() {
        return "http://qa.market.wmdev2.lsh123.com/";
    }

    @Override
    public String getAliPayNotifyUrl() {
        return "http://qa.market.wmdev2.lsh123.com/pay/alinotify";
    }

}
