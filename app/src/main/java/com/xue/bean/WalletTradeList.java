package com.xue.bean;

import com.xue.http.hook.BaseBean;

import java.util.ArrayList;

public class WalletTradeList extends ArrayList<WalletTradeList.WalletTrade> implements BaseBean {

    private int total ;

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

    public static class WalletTrade implements BaseBean {

        @Override
        public void setDataKey(String dataKey) {

        }

        @Override
        public String getDataKey() {
            return null;
        }
    }
}