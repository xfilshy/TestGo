package com.xue.parsers;

import com.xue.bean.UserTagInfo;

import org.json.JSONArray;
import org.json.JSONObject;

public class UserTagInfoParser extends MasterParser<UserTagInfo> {

    @Override
    public UserTagInfo parse(JSONObject data) throws Exception {
        UserTagInfo userTagInfo = new UserTagInfo();
        JSONArray array = optJSONArray(data, "tag_list");
        int len = getLength(array);

        if (len > 0) {
            UserTagParser userTagParser = new UserTagParser();
            for (int i = 0; i < len; i++) {
                UserTagInfo.Tag tag = userTagParser.parse(optJSONObject(array, i));

                if (tag != null) {
                    userTagInfo.add(tag);
                }
            }
        }

        return userTagInfo;
    }
}
