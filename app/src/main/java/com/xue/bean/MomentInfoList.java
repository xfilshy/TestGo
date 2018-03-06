package com.xue.bean;

import java.util.ArrayList;

public class MomentInfoList extends ArrayList<MomentInfoList.MomentInfo> {

    public static class MomentInfo {

        private String id;

        private String type;

        private String text;

        private ArrayList<MomentRes> resList;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public ArrayList<MomentRes> getResList() {
            return resList;
        }

        public void setResList(ArrayList<MomentRes> resList) {
            this.resList = resList;
        }

    }

    public static class MomentRes {

        private String type;

        private String resId;

        private String url;

        public String getResId() {
            return resId;
        }

        public void setResId(String resId) {
            this.resId = resId;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }
}
