package com.xue.bean;

import com.xue.http.hook.BaseBean;

import org.json.JSONException;
import org.json.JSONObject;

public class UserInfoDetail implements BaseBean {

    /**
     * 性别 id
     */
    private String gender;

    /**
     * 性别文本
     */
    private String genderName;

    /**
     * 区域id
     */
    private String regionId;

    /**
     * 区域id
     */
    private String regionName;

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

    public String getRegionId() {
        return regionId;
    }

    public void setRegionId(String regionId) {
        this.regionId = regionId;
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

    public String getGenderName() {
        return genderName;
    }

    public void setGenderName(String genderName) {
        this.genderName = genderName;
    }

    public String getRegionName() {
        return regionName;
    }

    public void setRegionName(String regionName) {
        this.regionName = regionName;
    }

    @Override
    public String toString() {
        JSONObject object = null;
        try {
            object = new JSONObject();
            object.put("gender", gender);
            object.put("gender_name", genderName);
            object.put("region_id", regionId);
            object.put("region_name", regionName);
            object.put("realname", realName);
            object.put("profile", profile);
            object.put("cover", cover);
            object.put("intro", intro);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return object == null ? null : object.toString();
    }

    @Override
    public void setDataKey(String dataKey) {

    }

    @Override
    public String getDataKey() {
        return null;
    }
}
