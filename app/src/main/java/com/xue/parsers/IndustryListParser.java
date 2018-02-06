package com.xue.parsers;

import com.xue.bean.IndustryList;

import org.json.JSONArray;
import org.json.JSONObject;

public class IndustryListParser extends MasterParser<IndustryList> {
    @Override
    public IndustryList parse(JSONObject data) throws Exception {
        JSONArray array = optJSONArray(data, "industry_list");
        int len = getLength(array);
        IndustryList industryList = null;
        if (len > 0) {
            industryList = new IndustryList();
            IndustryParser industryParser = new IndustryParser();
            for (int i = 0; i < len; i++) {
                IndustryList.Industry academic = industryParser.parse(optJSONObject(array, i));

                if (academic != null) {
                    industryList.add(academic);
                }
            }
        }

        return industryList;
    }
}
