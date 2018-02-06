package com.xue.bean;

import android.text.TextUtils;

import com.xue.http.hook.BaseBean;

import java.util.ArrayList;

public class UserTagInfo extends ArrayList<UserTagInfo.Tag> implements BaseBean {

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

    @Override
    public void setDataKey(String dataKey) {

    }

    @Override
    public String getDataKey() {
        return null;
    }

    public static class Tag implements BaseBean {

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

        @Override
        public void setDataKey(String dataKey) {

        }

        @Override
        public String getDataKey() {
            return null;
        }
    }
}
