package com.xue.bean;

import java.io.Serializable;
import java.util.ArrayList;

public class UserEducationInfo extends ArrayList<UserEducationInfo.Education> {

    public static class Education implements Serializable{

        /**
         * id
         */
        private String id;

        /**
         * 学校
         */
        private String schoolName;

        /**
         * 专业
         */
        private String majorName;

        /**
         * 学历id
         */
        private String academicType;

        /**
         * 学历名
         */
        private String academicName;

        /**
         * 开始时间
         */
        private String beginAt;

        /**
         * 结束时间
         */
        private String endAt;

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

        public String getSchoolName() {
            return schoolName;
        }

        public void setSchoolName(String schoolName) {
            this.schoolName = schoolName;
        }

        public String getMajorName() {
            return majorName;
        }

        public void setMajorName(String majorName) {
            this.majorName = majorName;
        }

        public String getAcademicType() {
            return academicType;
        }

        public void setAcademicType(String academicType) {
            this.academicType = academicType;
        }

        public String getAcademicName() {
            return academicName;
        }

        public void setAcademicName(String academicName) {
            this.academicName = academicName;
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

        public String getDescribe() {
            return describe;
        }

        public void setDescribe(String describe) {
            this.describe = describe;
        }

    }
}
