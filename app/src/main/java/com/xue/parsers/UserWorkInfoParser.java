package com.xue.parsers;

import com.xue.bean.UserWorkInfo;

import org.json.JSONArray;
import org.json.JSONObject;

public class UserWorkInfoParser extends MasterParser<UserWorkInfo> {

    @Override
    public UserWorkInfo parse(JSONObject data) throws Exception {
        UserWorkInfo userWorkInfo = null;
        JSONArray array = optJSONArray(data, "work_list");
        int len = getLength(array);

        userWorkInfo = new UserWorkInfo();
        if (len > 0) {
            UserWorkParser userWorkParser = new UserWorkParser();
            for (int i = 0; i < len; i++) {
                JSONObject object = optJSONObject(array, i);
                UserWorkInfo.Work work = userWorkParser.parse(object);
                if (work != null) {
                    userWorkInfo.add(work);
                }
            }
        }
        return userWorkInfo;
    }
}
