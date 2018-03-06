package com.xue.bean;

import java.util.ArrayList;

public class RechargeNCoinList extends ArrayList<RechargeNCoinList.RechargeNCoin> {

    public static class RechargeNCoin {

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

    }
}
