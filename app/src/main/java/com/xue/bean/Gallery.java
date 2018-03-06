package com.xue.bean;

import java.util.ArrayList;

public class Gallery extends ArrayList<Gallery.Picture> {


    public static class Picture {

        private String id;

        private String url;

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
