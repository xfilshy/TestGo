package com.xue.parsers;

import com.xue.bean.OrderScoreMap;

import org.json.JSONArray;
import org.json.JSONObject;

public class OrderScoreMapParser extends MasterParser<OrderScoreMap> {

    @Override
    public OrderScoreMap parse(JSONObject data) throws Exception {
        OrderScoreMap orderScoreMap = null;
        String avgScore = optString(data, "avg_score");
        int total = optInt(data, "total");

        JSONArray array = optJSONArray(data, "score_list");
        int len = getLength(array);
        if (len > 0) {
            orderScoreMap = new OrderScoreMap();
            orderScoreMap.setAvgScore(avgScore);
            orderScoreMap.setTotal(total);

            for (int i = 0; i < len; i++) {
                JSONObject object = optJSONObject(array, i);
                String score = optString(object, "score");
                int count = optInt(object, "count");

                orderScoreMap.put(score, count);
            }
        }

        return orderScoreMap;
    }
}
