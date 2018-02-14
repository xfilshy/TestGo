package com.xue.bean;

import com.xue.http.hook.BaseBean;

import java.util.ArrayList;

public class RechargeList extends ArrayList<RechargeList.Recharge> implements BaseBean{


    @Override
    public void setDataKey(String dataKey) {

    }

    @Override
    public String getDataKey() {
        return null;
    }

    public static class Recharge implements BaseBean {

        private String id ;

        private String diamond ;

        private String price ;

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

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
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
