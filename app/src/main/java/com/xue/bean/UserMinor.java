package com.xue.bean;

import com.xue.http.hook.BaseBean;

/**
 * 较小的用户数据，在UserBase基础上多了一些 如标签 职业 评价等字段，适合列表使用
 */

public class UserMinor implements BaseBean {

    UserBase userBase;

    public UserMinor() {
        userBase = new UserBase();
    }

    public UserBase getUserBase() {
        return userBase;
    }

    public void setUserBase(UserBase userBase) {
        this.userBase = userBase;
    }

    @Override
    public void setDataKey(String dataKey) {

    }

    @Override
    public String getDataKey() {
        return null;
    }
}
