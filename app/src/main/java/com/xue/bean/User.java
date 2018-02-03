package com.xue.bean;

import com.xue.http.hook.BaseBean;

import org.json.JSONException;
import org.json.JSONObject;

public class User implements BaseBean {

    private UserBase userBase;

    private UserInfoDetail userInfoDetail;

    private UserExpertInfo userExpertInfo;

    public User(UserBase userBase) {
        this.userBase = userBase;
    }

    public UserBase getUserBase() {
        return userBase;
    }

    public UserInfoDetail getUserInfoDetail() {
        return userInfoDetail;
    }

    public void setUserInfoDetail(UserInfoDetail userInfoDetail) {
        this.userInfoDetail = userInfoDetail;
    }

    public void setUserBase(UserBase userBase) {
        this.userBase = userBase;
    }

    public UserExpertInfo getUserExpertInfo() {
        return userExpertInfo;
    }

    public void setUserExpertInfo(UserExpertInfo userExpertInfo) {
        this.userExpertInfo = userExpertInfo;
    }

    @Override
    public void setDataKey(String dataKey) {

    }

    @Override
    public String getDataKey() {
        return null;
    }

    @Override
    public String toString() {
        JSONObject object = null;
        try {
            object = new JSONObject();
            object.put("user_info", new JSONObject(userBase.toString()));
            if (userInfoDetail != null) {
                object.put("detail_info", new JSONObject(userInfoDetail.toString()));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return object == null ? null : object.toString();
    }
}
