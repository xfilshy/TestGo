package com.xue.parsers;

import android.text.TextUtils;

import com.xue.bean.UserWorkInfo;

import org.json.JSONObject;

public class UserWorkParser extends MasterParser<UserWorkInfo.Work> {

    @Override
    public UserWorkInfo.Work parse(JSONObject data) throws Exception {
        UserWorkInfo.Work work = null;
        if (data != null) {

            String id = optString(data, "id");
            String companyName = optString(data, "company_name");
            String industryId = optString(data, "industry_id");
            String industryName = optString(data, "industry_name");
            String directionName = optString(data, "direction_name");
            String positionName = optString(data, "position_name");
            String describe = optString(data, "describe");
            String beginAt = optString(data, "begin_at");
            String endAt = optString(data, "end_at");
            if (!TextUtils.isEmpty(companyName) && !TextUtils.isEmpty(directionName) && !TextUtils.isEmpty(industryId) && !TextUtils.isEmpty(positionName)
                    && !TextUtils.isEmpty(beginAt) && !TextUtils.isEmpty(endAt)) {
                work = new UserWorkInfo.Work();

                work.setId(id);
                work.setCompanyName(companyName);
                work.setIndustryId(industryId);
                work.setIndustryName(industryName);
                work.setDirectionName(directionName);
                work.setPositionName(positionName);
                work.setDescribe(describe);
                work.setBeginAt(beginAt);
                work.setEndAt(endAt);
            }
        }

        return work;
    }
}
