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

    public static class Work implements BaseBean {

        /**
         * id
         */
        private String id;

        /**
         * 公司
         */
        private String companyName;

        /**
         * 行业
         */
        private String industryName;

        /**
         * 行业id
         */
        private String industryId;

        /**
         * 方向
         */
        private String directionName;

        /**
         * 职位
         */
        private String positionName;

        /**
         * 描述
         */
        private String describe;

        /**
         * 开始时间
         */
        private String beginAt;

        /**
         * 结束时间
         */
        private String endAt;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getCompanyName() {
            return companyName;
        }

        public void setCompanyName(String companyName) {
            this.companyName = companyName;
        }

        public String getIndustryName() {
            return industryName;
        }

        public void setIndustryName(String industryName) {
            this.industryName = industryName;
        }

        public String getIndustryId() {
            return industryId;
        }

        public void setIndustryId(String industryId) {
            this.industryId = industryId;
        }

        public String getDirectionName() {
            return directionName;
        }

        public void setDirectionName(String directionName) {
            this.directionName = directionName;
        }

        public String getPositionName() {
            return positionName;
        }

        public void setPositionName(String positionName) {
            this.positionName = positionName;
        }

        public String getDescribe() {
            return describe;
        }

        public void setDescribe(String describe) {
            this.describe = describe;
        }

        public String getBeginAt() {
            return beginAt;
        }

        public void setBeginAt(String beginAt) {
            this.beginAt = beginAt;
        }

        public String getEndAt() {
            return endAt;
        }

        public void setEndAt(String endAt) {
            this.endAt = endAt;
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
