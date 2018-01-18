package com.xue.bean;

import com.xue.http.hook.BaseBean;

/**
 * 用户全暑假，适用于详情页
 */

public class UserMajor implements BaseBean {

    private UserMinor userMinor;

    @Override
    public void setDataKey(String dataKey) {

    }

    @Override
    public String getDataKey() {
        return null;
    }
}
