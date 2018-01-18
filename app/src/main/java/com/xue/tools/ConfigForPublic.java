package com.xue.tools;


import com.xue.BaseApplication;

class ConfigForPublic implements Config {

    ConfigForPublic() {
    }

    @Override
    public boolean isTestApi() {
        return false;
    }

    @Override
    public boolean isDebug() {
        return false;
    }

    @Override
    public String getHttpBaseUrl() {
        return SecretTool.getBaseUrl(BaseApplication.get());
    }

    @Override
    public String getAliPayNotifyUrl() {
        return SecretTool.getAliPayNotifyUrl(BaseApplication.get());
    }
}
