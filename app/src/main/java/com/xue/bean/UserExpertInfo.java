package com.xue.bean;

import com.xue.http.hook.BaseBean;

public class UserExpertInfo implements BaseBean {

    private String signature;

    private String status;

    private int serviceFee;

    private String workCardImg;

    private String workCardAuth;

    private String businessCardImg;

    private String businessCardAuth;

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public int getServiceFee() {
        return serviceFee;
    }

    public void setServiceFee(int serviceFee) {
        this.serviceFee = serviceFee;
    }

    public String getWorkCardImg() {
        return workCardImg;
    }

    public void setWorkCardImg(String workCardImg) {
        this.workCardImg = workCardImg;
    }

    public String getBusinessCardImg() {
        return businessCardImg;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getWorkCardAuth() {
        return workCardAuth;
    }

    public void setWorkCardAuth(String workCardAuth) {
        this.workCardAuth = workCardAuth;
    }

    public String getBusinessCardAuth() {
        return businessCardAuth;
    }

    public void setBusinessCardAuth(String businessCardAuth) {
        this.businessCardAuth = businessCardAuth;
    }

    public void setBusinessCardImg(String businessCardImg) {
        this.businessCardImg = businessCardImg;
    }

    @Override
    public void setDataKey(String dataKey) {

    }

    @Override
    public String getDataKey() {
        return null;
    }
}
