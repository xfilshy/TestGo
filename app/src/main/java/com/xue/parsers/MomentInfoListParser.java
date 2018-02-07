package com.xue.parsers;

import com.xue.bean.MomentInfoList;

import org.json.JSONArray;
import org.json.JSONObject;

public class MomentInfoListParser extends MasterParser<MomentInfoList> {
    @Override
    public MomentInfoList parse(JSONObject data) throws Exception {
        MomentInfoList momentInfoList = null;
        JSONArray array = optJSONArray(data, "moment_list");

        int len = getLength(array);
        if (len > 0) {
            momentInfoList = new MomentInfoList();
            MomentInfoParser momentInfoParser = new MomentInfoParser();
            for (int i = 0; i < len; i++) {
                MomentInfoList.MomentInfo momentInfo = momentInfoParser.parse(optJSONObject(array, i));

                if (momentInfo != null) {
                    momentInfoList.add(momentInfo);
                }
            }
        }
        return momentInfoList;
    }
}
