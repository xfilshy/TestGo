package com.xue.bean;

import com.xue.http.hook.BaseBean;

import java.util.ArrayList;

public class RechargeNCoinList extends ArrayList implements BaseBean {

    @Override
    public void setDataKey(String dataKey) {

    }

    @Override
    public String getDataKey() {
        return null;
    }

    public static class RechargeNCoin implements BaseBean {

        private String id;

        private String diamond;

        private String nCoin;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getDiamond() {
            return diamond;
        }

        public void setDiamond(String diamond) {
            this.diamond = diamond;
        }

        public String getnCoin() {
            return nCoin;
        }

        public void setnCoin(String nCoin) {
            this.nCoin = nCoin;
        }

        @Override
        public void setDataKey(String dataKey) {

        }

        @Override
        public String getDataKey() {
            return null;
        }
    }
}
