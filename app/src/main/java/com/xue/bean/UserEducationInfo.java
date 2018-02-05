package com.xue.bean;

import com.xue.http.hook.BaseBean;

import java.util.ArrayList;

public class UserEducationInfo extends ArrayList<UserEducationInfo.Education> implements BaseBean {


    @Override
    public void setDataKey(String dataKey) {

    }

    @Override
    public String getDataKey() {
        return null;
    }

    public static class Education implements BaseBean {

        /**
         * id
         */
        private String id;

        /**
         * 学校
         */
        private String school;

        /**
         * 专业
         */
        private String speciality;

        /**
         * 学位
         */
        private String degree;

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

        public String getSchool() {
            return school;
        }

        public void setSchool(String school) {
            this.school = school;
        }

        public String getSpeciality() {
            return speciality;
        }

        public void setSpeciality(String speciality) {
            this.speciality = speciality;
        }

        public String getDegree() {
            return degree;
        }

        public void setDegree(String degree) {
            this.degree = degree;
        }

        public String getDescribe() {
            return describe;
        }

        public void setDescribe(String describe) {
            this.describe = describe;
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
