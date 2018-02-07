package com.xue.parsers;

import android.text.TextUtils;

import com.xue.bean.UserBase;

import org.json.JSONObject;

/**
 * Created by xfilshy on 2018/1/17.
 */
public class UserBaseParser extends MasterParser<UserBase> {

    @Override
    public UserBase parse(JSONObject data) throws Exception {
        UserBase userBase = null;

        if (data.has("user_info")) {
            userBase = parseBase(data.optJSONObject("user_info"));
        }

        return userBase;
    }

    public UserBase parseBase(JSONObject data) throws Exception {
        UserBase userBase = null;
        if (data != null) {
            String uid = optString(data, "uid");
            String token = optString(data, "token");
            String cellphone = optString(data, "cellphone");
            String neteaseToken = optString(data, "netease_token");
            String status = optString(data, "status");

            if (!TextUtils.isEmpty(uid)) {
                userBase = new UserBase();
                userBase.setUid(uid);
                userBase.setToken(token);
                userBase.setCellphone(cellphone);
                userBase.setNeteaseToken(neteaseToken);
                userBase.setStatus(status);
            }
        }

        return userBase;
    }
}
