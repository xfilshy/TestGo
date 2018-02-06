package com.xue.bean;

import com.xue.http.hook.BaseBean;

import java.util.ArrayList;

public class IndustryList extends ArrayList<IndustryList.Industry> implements BaseBean {

    public static class Industry implements BaseBean {

        private String id;

        private String name;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        @Override
        public void setDataKey(String dataKey) {

        }

        @Override
        public String getDataKey() {
            return null;
        }
    }

    @Override
    public void setDataKey(String dataKey) {

    }

    @Override
    public String getDataKey() {
        return null;
    }
}
