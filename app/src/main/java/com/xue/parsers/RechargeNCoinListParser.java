package com.xue.parsers;

import android.text.TextUtils;

import com.xue.bean.RechargeNCoinList;

import org.json.JSONArray;
import org.json.JSONObject;

public class RechargeNCoinListParser extends MasterParser<RechargeNCoinList> {

    @Override
    public RechargeNCoinList parse(JSONObject data) throws Exception {
        RechargeNCoinList rechargeNCoinList = null;
        JSONArray array = optJSONArray(data, "exchange_list");
        int len = getLength(array);

        if (len > 0) {
            rechargeNCoinList = new RechargeNCoinList();

            for (int i = 0; i < len; i++) {
                JSONObject object = optJSONObject(array, i);

                String id = optString(object, "id");
                String diamond = optString(object, "diamond");
                String ncoin = optString(object, "ncoin");

                if (!TextUtils.isEmpty(id) && !TextUtils.isEmpty(diamond) && !TextUtils.isEmpty(ncoin)) {
                    RechargeNCoinList.RechargeNCoin recharge = new RechargeNCoinList.RechargeNCoin();
                    recharge.setId(id);
                    recharge.setDiamond(diamond);
                    recharge.setnCoin(ncoin);

                    rechargeNCoinList.add(recharge);
                }
            }
        }

        return rechargeNCoinList;
    }


}
