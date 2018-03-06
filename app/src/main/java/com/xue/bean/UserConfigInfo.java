package com.xue.bean;

import org.json.JSONException;
import org.json.JSONObject;

public class UserConfigInfo {

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
}
