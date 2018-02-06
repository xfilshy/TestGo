package com.xue.parsers;

import android.text.TextUtils;

import com.xue.bean.IndustryList;

import org.json.JSONObject;

public class IndustryParser extends MasterParser<IndustryList.Industry> {

    @Override
    public IndustryList.Industry parse(JSONObject data) throws Exception {
        IndustryList.Industry industry = null;

        if (data != null) {
            String id = optString(data, "id");
            String name = optString(data, "name");

            if (!TextUtils.isEmpty(id) && !TextUtils.isEmpty(name)) {
                industry = new IndustryList.Industry();
                industry.setId(id);
                industry.setName(name);
            }
        }
        return industry;
    }
}
