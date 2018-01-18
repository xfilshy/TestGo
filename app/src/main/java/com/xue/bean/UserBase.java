package com.xue.bean;

import com.xue.http.hook.BaseBean;

/**
 * 基础用户类，提供用户最基本的数据 如 id token name 等
 */

public class UserBase implements BaseBean, UserToken {

    private String id;

    private String name;

    private String token;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public void setDataKey(String dataKey) {

    }

    @Override
    public String getDataKey() {
        return null;
    }

    @Override
    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public String getToken() {
        return token;
    }
}
