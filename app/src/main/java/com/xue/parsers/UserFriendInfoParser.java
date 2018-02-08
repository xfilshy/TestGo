package com.xue.parsers;

import com.xue.bean.UserFriendInfo;

import org.json.JSONObject;

public class UserFriendInfoParser extends MasterParser<UserFriendInfo> {

    @Override
    public UserFriendInfo parse(JSONObject data) throws Exception {
        UserFriendInfo userFriendInfo = null;
        data = optJSONObject(data, "friend_info");
        if (data != null) {
            int fanCount = optInt(data, "fans_count");
            boolean isFollow = optBoolean(data, "is_follow");

            userFriendInfo = new UserFriendInfo();
            userFriendInfo.setFansCount(fanCount);
            userFriendInfo.setFollow(isFollow);
        }

        return userFriendInfo;
    }
}
