package com.xue.parsers;

import android.text.TextUtils;

import com.xue.bean.UserDetailInfo;

import org.json.JSONException;
import org.json.JSONObject;

public class UserDetailInfoParser extends MasterParser<UserDetailInfo> {

    @Override
    public UserDetailInfo parse(JSONObject data) throws Exception {

        if (has(data, "detail_info")) {
            return parseBase(optJSONObject(data, "detail_info"));
        }

        return null;
    }

    public UserDetailInfo parseBase(JSONObject data) throws JSONException {
        UserDetailInfo userDetailInfo = null;
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
                userDetailInfo = new UserDetailInfo();
                userDetailInfo.setGender(gender);
                userDetailInfo.setGenderName(genderName);
                userDetailInfo.setHomeTown(hometown);
                userDetailInfo.setHomeTownName(hometownName);
                userDetailInfo.setRealName(realName);
                userDetailInfo.setProfile(profile);
                userDetailInfo.setCover(cover);
                userDetailInfo.setIntro(intro);
            }
        }
        return userDetailInfo;
    }
}
