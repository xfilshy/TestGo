package com.xue.bean;

import com.xue.http.hook.BaseBean;

public class FollowResult implements BaseBean {

    private int fansCount;

    public int getFansCount() {
        return fansCount;
    }

    public void setFansCount(int fansCount) {
        this.fansCount = fansCount;
    }

    @Override
    public void setDataKey(String dataKey) {

    }

    @Override
    public String getDataKey() {
        return null;
    }
}
