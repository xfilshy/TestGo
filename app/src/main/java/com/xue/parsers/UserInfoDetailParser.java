package com.xue.parsers;

import android.text.TextUtils;

import com.xue.bean.UserInfoDetail;

import org.json.JSONException;
import org.json.JSONObject;

public class UserInfoDetailParser extends MasterParser<UserInfoDetail> {

    @Override
    public UserInfoDetail parse(JSONObject data) throws Exception {

        if (has(data, "detail_info")) {
            return parseBase(optJSONObject(data, "detail_info"));
        }

        return null;
    }

    public UserInfoDetail parseBase(JSONObject data) throws JSONException {
        UserInfoDetail userInfoDetail = null;
        if (data != null) {
            String gender = optString(data, "gender");
            String genderName = optString(data, "gender_name");
            String hometown = optString(data, "hometown");
            String hometownName = optString(data, "hometown_name");
            String realName = optString(data, "realname");
            String profile = optString(data, "profile");
            String cover = optString(data, "cover");
            String intro = optString(data, "intro");

            if (!TextUtils.isEmpty(realName) && !TextUtils.isEmpty(profile) && !TextUtils.isEmpty(genderName)) {
                userInfoDetail = new UserInfoDetail();
                userInfoDetail.setGender(gender);
                userInfoDetail.setGenderName(genderName);
                userInfoDetail.setHomeTown(hometown);
                userInfoDetail.setHomeTownName(hometownName);
                userInfoDetail.setRealName(realName);
                userInfoDetail.setProfile(profile);
                userInfoDetail.setCover(cover);
                userInfoDetail.setIntro(intro);
            }
        }
        return userInfoDetail;
    }
}
