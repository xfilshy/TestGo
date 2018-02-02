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
            String regionIds = optString(data, "region_ids");
            String realName = optString(data, "realname");
            String profile = optString(data, "profile");
            String cover = optString(data, "cover");
            String intro = optString(data, "intro");

            if (!TextUtils.isEmpty(realName) && !TextUtils.isEmpty(profile) && !TextUtils.isEmpty(gender)) {
                userInfoDetail = new UserInfoDetail();
                userInfoDetail.setGender(gender);
                userInfoDetail.setRegionIds(regionIds);
                userInfoDetail.setRealName(realName);
                userInfoDetail.setProfile(profile);
                userInfoDetail.setCover(cover);
                userInfoDetail.setIntro(intro);
            }
        }
        return userInfoDetail;
    }
}
