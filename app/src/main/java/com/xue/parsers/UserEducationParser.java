package com.xue.parsers;

import android.text.TextUtils;

import com.xue.bean.UserEducationInfo;

import org.json.JSONObject;

public class UserEducationParser extends MasterParser<UserEducationInfo.Education> {

    @Override
    public UserEducationInfo.Education parse(JSONObject data) throws Exception {
        UserEducationInfo.Education education = null;
        if (data != null) {

            String schoolName = optString(data, "school_name");
            String majorName = optString(data, "major_name");
            String academicType = optString(data, "academic_type");
            String academicName = optString(data, "academic_name");
            String describe = optString(data, "describe");
            String beginAt = optString(data, "begin_at");
            String endAt = optString(data, "end_at");
            if (!TextUtils.isEmpty(schoolName) && !TextUtils.isEmpty(majorName) && !TextUtils.isEmpty(academicType)
                    && !TextUtils.isEmpty(beginAt) && !TextUtils.isEmpty(endAt)) {
                education = new UserEducationInfo.Education();

                education.setSchoolName(schoolName);
                education.setMajorName(majorName);
                education.setAcademicType(academicType);
                education.setAcademicName(academicName);
                education.setDescribe(describe);
                education.setBeginAt(beginAt);
                education.setBeginAt(endAt);
            }
        }

        return education;
    }
}
