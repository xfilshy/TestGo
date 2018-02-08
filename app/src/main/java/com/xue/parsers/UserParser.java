package com.xue.parsers;

import com.xue.bean.User;
import com.xue.bean.UserBase;
import com.xue.bean.UserConfigInfo;
import com.xue.bean.UserDetailInfo;
import com.xue.bean.UserEducationInfo;
import com.xue.bean.UserExpertInfo;
import com.xue.bean.UserFriendInfo;
import com.xue.bean.UserTagInfo;
import com.xue.bean.UserWorkInfo;

import org.json.JSONObject;

public class UserParser extends MasterParser<User> {

    @Override
    public User parse(JSONObject data) throws Exception {
        User user = null;
        if (data != null) {
            UserBase userBase = new UserBaseParser().parse(data);
            UserDetailInfo userDetailInfo = new UserDetailInfoParser().parse(data);
            UserConfigInfo userConfigInfo = new UserConfigInfoParser().parse(data);
            UserExpertInfo userExpertInfo = new UserExpertInfoParser().parse(data);
            UserEducationInfo userEducationInfo = new UserEducationInfoParser().parse(data);
            UserWorkInfo userWorkInfo = new UserWorkInfoParser().parse(data);
            UserTagInfo userTagInfo = new UserTagInfoParser().parse(data);
            UserFriendInfo userFriendInfo = new UserFriendInfoParser().parse(data);

            if (userBase != null) {
                user = new User(userBase);
                user.setUserDetailInfo(userDetailInfo);
                user.setUserConfigInfo(userConfigInfo);
                user.setUserExpertInfo(userExpertInfo);
                user.setUserEducationInfo(userEducationInfo);
                user.setUserWorkInfo(userWorkInfo);
                user.setUserTagInfo(userTagInfo);
                user.setUserFriendInfo(userFriendInfo);
            }
        }

        return user;
    }
}
