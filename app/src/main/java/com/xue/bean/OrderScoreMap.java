package com.xue.bean;

import java.util.HashMap;

public class OrderScoreMap extends HashMap<String, Integer> {

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
}
