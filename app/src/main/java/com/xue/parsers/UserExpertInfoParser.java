package com.xue.parsers;

import com.xue.bean.UserExpertInfo;

import org.json.JSONException;
import org.json.JSONObject;

public class UserExpertInfoParser extends MasterParser<UserExpertInfo> {

    @Override
    public UserExpertInfo parse(JSONObject data) throws Exception {

        if (has(data, "expert_info")) {
            return parseBase(optJSONObject(data, "expert_info"));
        }

        return null;
    }

    public UserExpertInfo parseBase(JSONObject data) throws JSONException {
        UserExpertInfo userExpertInfo = null;

        if (data != null) {
            String signature = optString(data, "signature");
            int serviceFee = optInt(data, "service_fee");
            String status = optString(data, "status");
            String workCardImg = optString(data, "work_card_img");
            String workCardAuth = optString(data, "work_card_auth");
            String businessCardImg = optString(data, "busine_card_img");
            String businessCardAuth = optString(data, "busine_card_auth");

            if (serviceFee > 0) {
                userExpertInfo = new UserExpertInfo();
                userExpertInfo.setSignature(signature);
                userExpertInfo.setStatus(status);
                userExpertInfo.setServiceFee(serviceFee);
                userExpertInfo.setWorkCardImg(workCardImg);
                userExpertInfo.setWorkCardAuth(workCardAuth);
                userExpertInfo.setBusinessCardImg(businessCardImg);
                userExpertInfo.setBusinessCardAuth(businessCardAuth);
            }
        }

        return userExpertInfo;
    }
}
