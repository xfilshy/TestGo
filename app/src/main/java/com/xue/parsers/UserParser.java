package com.xue.parsers;

import com.xue.bean.User;
import com.xue.bean.UserBase;
import com.xue.bean.UserInfoDetail;

import org.json.JSONObject;

public class UserParser extends MasterParser<User> {

    @Override
    public User parse(JSONObject data) throws Exception {
        User user = null;
        if (data != null) {
            UserBase userBase = new UserBaseParser().parse(data);
            UserInfoDetail userInfoDetail = new UserInfoDetailParser().parse(optJSONObject(data, "user_info"));

            if (userBase != null) {
                user = new User(userBase);
                user.setUserInfoDetail(userInfoDetail);
            }
        }

        return user;
    }
}
