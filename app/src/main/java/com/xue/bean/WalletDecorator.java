package com.xue.bean;

public class WalletDecorator {

    private WalletInfo walletInfo;

    private RechargeList rechargeList;

    private RechargeNCoinList rechargeNCoinList;

    public WalletInfo getWalletInfo() {
        return walletInfo;
    }

    public void setWalletInfo(WalletInfo walletInfo) {
        this.walletInfo = walletInfo;
    }

    public RechargeList getRechargeList() {
        return rechargeList;
    }

    public void setRechargeList(RechargeList rechargeList) {
        this.rechargeList = rechargeList;
    }

    public RechargeNCoinList getRechargeNCoinList() {
        return rechargeNCoinList;
    }

    public void setRechargeNCoinList(RechargeNCoinList rechargeNCoinList) {
        this.rechargeNCoinList = rechargeNCoinList;
    }
}
