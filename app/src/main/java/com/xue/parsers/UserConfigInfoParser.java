package com.xue.parsers;

import com.xue.bean.UserConfigInfo;

import org.json.JSONException;
import org.json.JSONObject;

public class UserConfigInfoParser extends MasterParser<UserConfigInfo> {

    @Override
    public UserConfigInfo parse(JSONObject data) throws Exception {

        if (has(data, "conf_info")) {
            return parseBase(optJSONObject(data, "conf_info"));
        }

        return null;
    }

    public UserConfigInfo parseBase(JSONObject data) throws JSONException {
        UserConfigInfo userConfigInfo = null;

        if (data != null) {
            int feeMin = optInt(data, "fee_min");
            int feeMax = optInt(data, "fee_max");
            int feeDefault = optInt(data, "fee_default");

            if (feeMin > 0 && feeMax > 0 && feeDefault > 0) {
                userConfigInfo = new UserConfigInfo();
                userConfigInfo.setFeeMin(feeMin);
                userConfigInfo.setFeeMax(feeMax);
                userConfigInfo.setFeeDefault(feeDefault);
            }
        }

        return userConfigInfo;
    }
}
