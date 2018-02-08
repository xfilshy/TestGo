package com.xue.parsers;

import com.xue.bean.FollowResult;

import org.json.JSONObject;

public class FollowResultParser extends MasterParser<FollowResult> {

    @Override
    public FollowResult parse(JSONObject data) throws Exception {
        FollowResult followResult = null;
        int fanCount = optInt(data, "fans_count");

        if (fanCount >= 0) {
            followResult = new FollowResult();
            followResult.setFansCount(fanCount);
        }

        return followResult;
    }
}
