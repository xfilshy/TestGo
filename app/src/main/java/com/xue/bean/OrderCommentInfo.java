package com.xue.bean;

import com.xue.http.hook.BaseBean;

public class OrderCommentInfo implements BaseBean {

    private OrderScoreMap orderScoreMap;

    private OrderCommentList orderCommentList;

    public OrderScoreMap getOrderScoreMap() {
        return orderScoreMap;
    }

    public void setOrderScoreMap(OrderScoreMap orderScoreMap) {
        this.orderScoreMap = orderScoreMap;
    }

    public OrderCommentList getOrderCommentList() {
        return orderCommentList;
    }

    public void setOrderCommentList(OrderCommentList orderCommentList) {
        this.orderCommentList = orderCommentList;
    }

    @Override
    public void setDataKey(String dataKey) {

    }

    @Override
    public String getDataKey() {
        return null;
    }
}
