package com.xue.bean;

import android.text.TextUtils;

import java.io.Serializable;
import java.util.ArrayList;

public class UserTagInfo extends ArrayList<UserTagInfo.Tag> {

    public Tag findTagByName(String tagName) {
        for (Tag tag : this) {
            if (TextUtils.equals(tagName, tag.getTagName())) {
                return tag;
            }
        }

        return null;
    }

    public String[] toStringArray() {
        String[] ss = new String[size()];

        for (int i = 0; i < size(); i++) {
            ss[i] = get(i).tagName;
        }

        return ss;
    }

    public static class Tag implements Serializable{

        private String tagId;

        private String tagName;

        public String getTagId() {
            return tagId;
        }

        public void setTagId(String tagId) {
            this.tagId = tagId;
        }

        public String getTagName() {
            return tagName;
        }

        public void setTagName(String tagName) {
            this.tagName = tagName;
        }

    }
}
