package com.xue.bean;

import com.xue.http.hook.BaseBean;

import org.json.JSONException;
import org.json.JSONObject;

public class UserConfigInfo implements BaseBean {

    private int feeMin;

    private int feeMax;

    private int feeDefault;

    public int getFeeMin() {
        return feeMin;
    }

    public void setFeeMin(int feeMin) {
        this.feeMin = feeMin;
    }

    public int getFeeMax() {
        return feeMax;
    }

    public void setFeeMax(int feeMax) {
        this.feeMax = feeMax;
    }

    public int getFeeDefault() {
        return feeDefault;
    }

    public void setFeeDefault(int feeDefault) {
        this.feeDefault = feeDefault;
    }

    @Override
    public String toString() {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("fee_min", feeMin);
            jsonObject.put("fee_max", feeMax);
            jsonObject.put("fee_default", feeDefault);

            return jsonObject.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public void setDataKey(String dataKey) {

    }

    @Override
    public String getDataKey() {
        return null;
    }
}
