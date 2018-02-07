package com.xue.bean;

import com.xue.http.hook.BaseBean;

import java.util.ArrayList;

public class MomentInfoList extends ArrayList<MomentInfoList.MomentInfo> implements BaseBean {

    @Override
    public void setDataKey(String dataKey) {

    }

    @Override
    public String getDataKey() {
        return null;
    }

    public static class MomentInfo implements BaseBean {

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

        @Override
        public void setDataKey(String dataKey) {

        }

        @Override
        public String getDataKey() {
            return null;
        }
    }

    public static class MomentRes implements BaseBean {

        private String type;

        private String url;

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

        @Override
        public void setDataKey(String dataKey) {

        }

        @Override
        public String getDataKey() {
            return null;
        }
    }
}
