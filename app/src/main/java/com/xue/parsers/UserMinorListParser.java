package com.xue.parsers;

import android.text.TextUtils;

import com.xue.BaseApplication;
import com.xue.bean.UserMinor;
import com.xue.bean.UserMinorList;

import org.json.JSONArray;
import org.json.JSONObject;

public class UserMinorListParser extends MasterParser<UserMinorList> {

    @Override
    public UserMinorList parse(JSONObject data) throws Exception {

        UserMinorList userMinorList = null;
        JSONArray jsonArray = getJSONArray(data, "user_list");
        int len = getLength(jsonArray);
        if (len > 0) {
            userMinorList = new UserMinorList();
            UserMinorParser userMinorParser = new UserMinorParser();
            JSONObject object;
            for (int i = 0; i < len; i++) {
                object = getJSONObject(jsonArray, i);
                UserMinor userMinor = userMinorParser.parse(object);
                if (userMinor != null) {
                    //TODO 测试逻辑
                    if (!TextUtils.equals(BaseApplication.get().getUserId(), userMinor.getUserBase().getId())) {
                        userMinorList.add(userMinor);
                    }
                }
            }
        }

        return userMinorList;
    }
}
