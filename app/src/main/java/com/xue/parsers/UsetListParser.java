package com.xue.parsers;

import com.xue.bean.User;
import com.xue.bean.UserList;

import org.json.JSONArray;
import org.json.JSONObject;

public class UsetListParser extends MasterParser<UserList> {
    @Override
    public UserList parse(JSONObject data) throws Exception {
        UserList userList = null;
        JSONArray jsonArray = getJSONArray(data, "user_list");
        int len = getLength(jsonArray);
        if (len > 0) {
            userList = new UserList();
            UserParser userParser = new UserParser();
            for (int i = 0; i < len; i++) {
                User user = userParser.parse(optJSONObject(jsonArray, i));

                if (user != null) {
                    userList.add(user);
                }
            }

        }

        return userList;
    }
}
