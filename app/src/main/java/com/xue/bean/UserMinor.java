package com.xue.bean;

/**
 * 较小的用户数据，在UserBase基础上多了一些 如标签 职业 评价等字段，适合列表使用
 */

public class UserMinor {

    UserBase userBase;

    public UserMinor(String id) {
        userBase = new UserBase();
        userBase.setId(id);
    }

    public UserBase getUserBase() {
        return userBase;
    }

    public String getId() {
        return userBase.getId();
    }
}
