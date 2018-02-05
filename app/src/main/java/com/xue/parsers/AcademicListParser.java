package com.xue.parsers;

import com.xue.bean.AcademicList;

import org.json.JSONArray;
import org.json.JSONObject;

public class AcademicListParser extends MasterParser<AcademicList> {
    @Override
    public AcademicList parse(JSONObject data) throws Exception {
        JSONArray array = optJSONArray(data, "academic_list");
        int len = getLength(array);
        AcademicList academicList = null;
        if (len > 0) {
            academicList = new AcademicList();
            AcademicParser academicParser = new AcademicParser();
            for (int i = 0; i < len; i++) {
                AcademicList.Academic academic = academicParser.parse(optJSONObject(array, i));

                if (academic != null) {
                    academicList.add(academic);
                }
            }
        }
        
        return academicList;
    }
}
