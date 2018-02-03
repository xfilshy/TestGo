package com.xue.bean;

import com.xue.http.hook.BaseBean;

import java.util.ArrayList;

public class CityList extends ArrayList<CityList.City> implements BaseBean {


    public static class City {

        private String id;

        private String name;

        private String showName;

        private String initial;

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

        public String getShowName() {
            return showName;
        }

        public void setShowName(String showName) {
            this.showName = showName;
        }

        public String getInitial() {
            return initial;
        }

        public void setInitial(String initial) {
            this.initial = initial;
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
