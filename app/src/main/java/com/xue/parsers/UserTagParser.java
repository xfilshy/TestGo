package com.xue.parsers;

import android.text.TextUtils;

import com.xue.bean.UserTagInfo;

import org.json.JSONObject;

public class UserTagParser extends MasterParser<UserTagInfo.Tag> {

    @Override
    public UserTagInfo.Tag parse(JSONObject data) throws Exception {
        UserTagInfo.Tag tag = null;

        if (data != null) {
            String tagId = optString(data, "tag_id");
            String tagName = optString(data, "tag_name");

            if (!TextUtils.isEmpty(tagId) && !TextUtils.isEmpty(tagName)) {
                tag = new UserTagInfo.Tag();
                tag.setTagId(tagId);
                tag.setTagName(tagName);
            }
        }

        return tag;
    }
}
