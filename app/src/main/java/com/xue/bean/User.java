package com.xue.bean;

import com.xue.http.hook.BaseBean;

import org.json.JSONException;
import org.json.JSONObject;

public class User implements BaseBean {

    private UserBase userBase;

    private UserDetailInfo userDetailInfo;

    private UserExpertInfo userExpertInfo;

    private UserConfigInfo userConfigInfo;

    private UserEducationInfo userEducationInfo;

    private UserWorkInfo userWorkInfo;

    private UserTagInfo userTagInfo;

    public User(UserBase userBase) {
        this.userBase = userBase;
    }

    public UserBase getUserBase() {
        return userBase;
    }

    public UserDetailInfo getUserDetailInfo() {
        return userDetailInfo;
    }

    public void setUserDetailInfo(UserDetailInfo userDetailInfo) {
        this.userDetailInfo = userDetailInfo;
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

    public UserConfigInfo getUserConfigInfo() {
        return userConfigInfo;
    }

    public void setUserConfigInfo(UserConfigInfo userConfigInfo) {
        this.userConfigInfo = userConfigInfo;
    }

    public UserEducationInfo getUserEducationInfo() {
        return userEducationInfo;
    }

    public void setUserEducationInfo(UserEducationInfo userEducationInfo) {
        this.userEducationInfo = userEducationInfo;
    }

    public UserWorkInfo getUserWorkInfo() {
        return userWorkInfo;
    }

    public void setUserWorkInfo(UserWorkInfo userWorkInfo) {
        this.userWorkInfo = userWorkInfo;
    }

    public UserTagInfo getUserTagInfo() {
        return userTagInfo;
    }

    public void setUserTagInfo(UserTagInfo userTagInfo) {
        this.userTagInfo = userTagInfo;
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
            if (userDetailInfo != null) {
                object.put("detail_info", new JSONObject(userDetailInfo.toString()));
            }
            if (userConfigInfo != null) {
                object.put("conf_info", new JSONObject(userConfigInfo.toString()));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return object == null ? null : object.toString();
    }
}
