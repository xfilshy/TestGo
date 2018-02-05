package com.xue.bean;

import com.xue.http.hook.BaseBean;

import java.util.ArrayList;

public class UserWorkInfo extends ArrayList<UserWorkInfo.Work> implements BaseBean {


    @Override
    public void setDataKey(String dataKey) {

    }

    @Override
    public String getDataKey() {
        return null;
    }

    public static class Work {

        /**
         * id
         */
        private String id;

        /**
         * 公司
         */
        private String company;

        /**
         * 行业
         */
        private String industry;

        /**
         * 方向
         */
        private String direction;

        /**
         * 职位
         */
        private String position;

        /**
         * 描述
         */
        private String describe;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getCompany() {
            return company;
        }

        public void setCompany(String company) {
            this.company = company;
        }

        public String getIndustry() {
            return industry;
        }

        public void setIndustry(String industry) {
            this.industry = industry;
        }

        public String getDirection() {
            return direction;
        }

        public void setDirection(String direction) {
            this.direction = direction;
        }

        public String getPosition() {
            return position;
        }

        public void setPosition(String position) {
            this.position = position;
        }

        public String getDescribe() {
            return describe;
        }

        public void setDescribe(String describe) {
            this.describe = describe;
        }
    }
}
