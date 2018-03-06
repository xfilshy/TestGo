package com.xue.bean;

import java.io.Serializable;
import java.util.ArrayList;

public class AcademicList extends ArrayList<AcademicList.Academic> {

    public static class Academic implements Serializable{

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
    }
}
