package com.xue.bean;

public class UserExpertInfo {

    private String signature;

    /**
     * 1 验证中
     * 2 验证通过
     * 3 验证失败
     */
    private String status;

    private int serviceFee;

    private String workCardImg;

    /**
     * 1 验证中
     * 2 验证通过
     * 3 验证失败
     */
    private String workCardAuth;

    private String businessCardImg;

    /**
     * 1 验证中
     * 2 验证通过
     * 3 验证失败
     */
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
}
