package com.xue.http.impl;

import com.xue.http.hook.BaseKVP;

/**
 * 键值对
 */
public class DefaultKVPBean implements BaseKVP {

    /**
     * 键
     */
    private String key;

    /**
     * 值
     */
    private String value;


    public DefaultKVPBean() {

    }

    public DefaultKVPBean(String key, String value) {
        this.key = key;
        this.value = value;
    }

    @Override
    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    @Override
    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public int compareTo(BaseKVP another) {
        return key.compareToIgnoreCase(another.getKey());
    }
}
