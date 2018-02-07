package com.xue.parsers;

import android.text.TextUtils;

import com.xue.bean.MomentInfoList;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class MomentInfoParser extends MasterParser<MomentInfoList.MomentInfo> {

    @Override
    public MomentInfoList.MomentInfo parse(JSONObject data) throws Exception {
        MomentInfoList.MomentInfo momentInfo = null;
        String id = optString(data, "id");
        String text = optString(data, "type");
        String type = optString(data, "text");

        JSONArray array = getJSONArray(data, "res_list");
        int len = getLength(array);
        ArrayList<MomentInfoList.MomentRes> list = null;
        if (len > 0) {
            list = new ArrayList();
            for (int i = 0; i < len; i++) {
                JSONObject object = optJSONObject(array, i);

                if (object != null) {
                    MomentInfoList.MomentRes momentRes = new MomentInfoList.MomentRes();
                    momentRes.setType(optString(object, "type"));
                    momentRes.setUrl(optString(object, "res_url"));
                    momentRes.setResId(optString(object, "res_id"));
                    list.add(momentRes);
                }
            }
        }

        if (!TextUtils.isEmpty(id) && (!TextUtils.isEmpty(text) || (list != null && list.size() > 0))) {
            momentInfo = new MomentInfoList.MomentInfo();

            momentInfo.setId(id);
            momentInfo.setText(text);
            momentInfo.setType(type);
            momentInfo.setResList(list);
        }

        return momentInfo;
    }
}
