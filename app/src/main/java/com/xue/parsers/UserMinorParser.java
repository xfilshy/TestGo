package com.xue.parsers;

import com.xue.bean.UserBase;
import com.xue.bean.UserMinor;

import org.json.JSONObject;

public class UserMinorParser extends MasterParser<UserMinor> {

    @Override
    public UserMinor parse(JSONObject data) throws Exception {
        UserMinor userMinor = null;
        if (data != null) {
            UserBaseParser userBaseParser = new UserBaseParser();
            UserBase userBase = userBaseParser.parseBase(data);
            if (userBase != null) {
                userMinor = new UserMinor();
                userMinor.setUserBase(userBase);
            }

        }
        return userMinor;
    }
}
