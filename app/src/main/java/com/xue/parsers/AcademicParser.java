package com.xue.parsers;

import android.text.TextUtils;

import com.xue.bean.AcademicList;

import org.json.JSONObject;

public class AcademicParser extends MasterParser<AcademicList.Academic> {

    @Override
    public AcademicList.Academic parse(JSONObject data) throws Exception {
        AcademicList.Academic academic = null;

        if (data != null) {
            String id = optString(data, "id");
            String name = optString(data, "name");

            if (!TextUtils.isEmpty(id) && !TextUtils.isEmpty(name)) {
                academic = new AcademicList.Academic();
                academic.setId(id);
                academic.setName(name);
            }
        }
        return academic;
    }
}
