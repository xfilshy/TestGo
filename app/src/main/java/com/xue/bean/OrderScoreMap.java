package com.xue.bean;

import com.xue.http.hook.BaseBean;

import java.util.HashMap;

public class OrderScoreMap extends HashMap<String, Integer> implements BaseBean {

    private String avgScore;

    private int total;

    public String getAvgScore() {
        return avgScore;
    }

    public void setAvgScore(String avgScore) {
        this.avgScore = avgScore;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    @Override
    public void setDataKey(String dataKey) {

    }

    @Override
    public String getDataKey() {
        return null;
    }
}
