package com.xue.bean;

import com.xue.http.hook.BaseBean;

public class UserInfoDetail implements BaseBean {

    /**
     * 性别 id
     */
    private String gender;

    /**
     *
     * */
    private String regionIds;

    /**
     * 真实名
     */
    private String realName;

    /**
     * 头像
     */
    private String profile;

    /**
     * 封面
     */
    private String cover;

    /**
     * 独白
     */
    private String intro;

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getRegionIds() {
        return regionIds;
    }

    public void setRegionIds(String regionIds) {
        this.regionIds = regionIds;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    @Override
    public void setDataKey(String dataKey) {

    }

    @Override
    public String getDataKey() {
        return null;
    }
}
