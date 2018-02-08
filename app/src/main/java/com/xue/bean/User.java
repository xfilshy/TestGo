package com.xue.bean;

import com.xue.http.hook.BaseBean;

import org.json.JSONException;
import org.json.JSONObject;

public class User implements BaseBean {

    private long timeStamp = System.currentTimeMillis();

    private UserBase userBase;

    private UserDetailInfo userDetailInfo;

    private UserExpertInfo userExpertInfo;

    private UserConfigInfo userConfigInfo;

    private UserEducationInfo userEducationInfo;

    private UserWorkInfo userWorkInfo;

    private UserTagInfo userTagInfo;

    private UserFriendInfo userFriendInfo;

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
        this.timeStamp = System.currentTimeMillis();
    }

    public void setUserBase(UserBase userBase) {
        this.userBase = userBase;
        this.timeStamp = System.currentTimeMillis();
    }

    public UserExpertInfo getUserExpertInfo() {
        return userExpertInfo;
    }

    public void setUserExpertInfo(UserExpertInfo userExpertInfo) {
        this.userExpertInfo = userExpertInfo;
        this.timeStamp = System.currentTimeMillis();
    }

    public UserConfigInfo getUserConfigInfo() {
        return userConfigInfo;
    }

    public void setUserConfigInfo(UserConfigInfo userConfigInfo) {
        this.userConfigInfo = userConfigInfo;
        this.timeStamp = System.currentTimeMillis();
    }

    public UserEducationInfo getUserEducationInfo() {
        return userEducationInfo;
    }

    public void setUserEducationInfo(UserEducationInfo userEducationInfo) {
        this.userEducationInfo = userEducationInfo;
        this.timeStamp = System.currentTimeMillis();
    }

    public UserWorkInfo getUserWorkInfo() {
        return userWorkInfo;
    }

    public void setUserWorkInfo(UserWorkInfo userWorkInfo) {
        this.userWorkInfo = userWorkInfo;
        this.timeStamp = System.currentTimeMillis();
    }

    public UserTagInfo getUserTagInfo() {
        return userTagInfo;
    }

    public void setUserTagInfo(UserTagInfo userTagInfo) {
        this.userTagInfo = userTagInfo;
        this.timeStamp = System.currentTimeMillis();
    }

    public UserFriendInfo getUserFriendInfo() {
        return userFriendInfo;
    }

    public void setUserFriendInfo(UserFriendInfo userFriendInfo) {
        this.userFriendInfo = userFriendInfo;
    }

    public long getTimeStamp() {
        return timeStamp;
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
