package com.xue.bean;

import com.xue.http.hook.BaseBean;

/**
 * 基础用户类，提供用户最基本的数据 如 id token name 等
 */

public class UserBase implements BaseBean {

    private String uid;

    private String cellphone;

    private String neteaseToken;

    private String token;

    private String status;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getCellphone() {
        return cellphone;
    }

    public void setCellphone(String cellphone) {
        this.cellphone = cellphone;
    }

    public String getNeteaseToken() {
        return neteaseToken;
    }

    public void setNeteaseToken(String neteaseToken) {
        this.neteaseToken = neteaseToken;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public void setDataKey(String dataKey) {

    }

    @Override
    public String getDataKey() {
        return null;
    }
}
