package com.xue.bean;

import com.xue.http.hook.BaseBean;

import java.util.ArrayList;

public class OrderCommentList extends ArrayList<OrderCommentList.Comment> implements BaseBean {

    @Override
    public void setDataKey(String dataKey) {

    }

    @Override
    public String getDataKey() {
        return null;
    }

    public static class Comment implements BaseBean {

        private String id;

        private String uid;

        private String orderId;

        private String fromUid;

        private String fromRealName;

        private String fromProfile;

        private String score;

        private String content;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
        }

        public String getOrderId() {
            return orderId;
        }

        public void setOrderId(String orderId) {
            this.orderId = orderId;
        }

        public String getFromUid() {
            return fromUid;
        }

        public void setFromUid(String fromUid) {
            this.fromUid = fromUid;
        }

        public String getFromRealName() {
            return fromRealName;
        }

        public void setFromRealName(String fromRealName) {
            this.fromRealName = fromRealName;
        }

        public String getFromProfile() {
            return fromProfile;
        }

        public void setFromProfile(String fromProfile) {
            this.fromProfile = fromProfile;
        }

        public String getScore() {
            return score;
        }

        public void setScore(String score) {
            this.score = score;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
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
