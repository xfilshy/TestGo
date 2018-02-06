package com.xue.bean;

import com.xue.http.hook.BaseBean;

import java.util.ArrayList;

public class Gallery extends ArrayList<Gallery.Picture> implements BaseBean {

    @Override
    public void setDataKey(String dataKey) {

    }

    @Override
    public String getDataKey() {
        return null;
    }

    public static class Picture implements BaseBean {

        private String id;

        private String url;

        @Override
        public void setDataKey(String dataKey) {

        }

        @Override
        public String getDataKey() {
            return null;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        @Override
        public String toString() {
            if (id != null) {
                return id;
            } else {
                return url;
            }
        }
    }


}
