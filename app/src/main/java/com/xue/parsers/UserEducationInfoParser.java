package com.xue.parsers;

import com.xue.bean.UserEducationInfo;

import org.json.JSONArray;
import org.json.JSONObject;

public class UserEducationInfoParser extends MasterParser<UserEducationInfo> {

    @Override
    public UserEducationInfo parse(JSONObject data) throws Exception {
        UserEducationInfo userEducationInfo = null;
        JSONArray array = optJSONArray(data, "education_list");
        int len = getLength(array);

        if (len > 0) {
            userEducationInfo = new UserEducationInfo();
            UserEducationParser userEducationParser = new UserEducationParser();
            for (int i = 0; i < len; i++) {
                JSONObject object = optJSONObject(array, i);
                UserEducationInfo.Education education = userEducationParser.parse(object);
                if (education != null) {
                    userEducationInfo.add(education);
                }
            }
        }
        return userEducationInfo;
    }
}
