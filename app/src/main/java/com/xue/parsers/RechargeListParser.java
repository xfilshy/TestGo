package com.xue.parsers;

import android.text.TextUtils;

import com.xue.bean.RechargeList;

import org.json.JSONArray;
import org.json.JSONObject;

public class RechargeListParser extends MasterParser<RechargeList> {

    @Override
    public RechargeList parse(JSONObject data) throws Exception {
        RechargeList rechargeList = null;
        JSONArray array = optJSONArray(data, "recharge_list");
        int len = getLength(array);

        if (len > 0) {
            rechargeList = new RechargeList();

            for (int i = 0; i < len; i++) {
                JSONObject object = optJSONObject(array, i);

                String id = optString(object, "id");
                String diamond = optString(object, "diamond");
                String price = optString(object, "price");

                if (!TextUtils.isEmpty(id) && !TextUtils.isEmpty(diamond) && !TextUtils.isEmpty(price)) {
                    RechargeList.Recharge recharge = new RechargeList.Recharge();
                    recharge.setId(id);
                    recharge.setDiamond(diamond);
                    recharge.setPrice(price);

                    rechargeList.add(recharge);
                }


            }
        }

        return rechargeList;
    }


}
