package com.xue.bean;

import com.xue.http.hook.BaseBean;

public class User implements BaseBean {

    private UserBase userBase;

    private UserInfoDetail userInfoDetail;

    public User(UserBase userBase) {
        this.userBase = userBase;
    }

    public UserBase getUserBase() {
        return userBase;
    }

    public UserInfoDetail getUserInfoDetail() {
        return userInfoDetail;
    }

    public void setUserInfoDetail(UserInfoDetail userInfoDetail) {
        this.userInfoDetail = userInfoDetail;
    }

    @Override
    public void setDataKey(String dataKey) {

    }

    @Override
    public String getDataKey() {
        return null;
    }
}
