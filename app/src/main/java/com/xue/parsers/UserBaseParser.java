package com.xue.parsers;

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
            data = data.getJSONObject("user_info");
            userBase = new UserBase();
            userBase.setId(getString(data, "uid"));
            userBase.setToken(getString(data, "token"));
            userBase.setName(getString(data, "cellphone"));
        }

        return userBase;
    }
}
