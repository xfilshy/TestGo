package com.xue.bean;

import com.xue.http.hook.BaseBean;

import java.util.ArrayList;

public class UserList extends ArrayList<User> implements BaseBean {

    @Override
    public void setDataKey(String dataKey) {

    }

    @Override
    public String getDataKey() {
        return null;
    }
}