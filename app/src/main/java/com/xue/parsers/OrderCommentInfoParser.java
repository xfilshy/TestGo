package com.xue.parsers;

import com.xue.bean.OrderCommentInfo;
import com.xue.bean.OrderCommentList;
import com.xue.bean.OrderScoreMap;

import org.json.JSONObject;

public class OrderCommentInfoParser extends MasterParser<OrderCommentInfo> {

    @Override
    public OrderCommentInfo parse(JSONObject data) throws Exception {
        OrderCommentInfo orderCommentInfo = null;

        OrderCommentList orderCommentList = new OrderCommentListParser().parse(data);
        OrderScoreMap orderScoreMap = new OrderScoreMapParser().parse(data);

        if (orderCommentList != null && orderScoreMap != null) {
            orderCommentInfo = new OrderCommentInfo();

            orderCommentInfo.setOrderCommentList(orderCommentList);
            orderCommentInfo.setOrderScoreMap(orderScoreMap);
        }

        return orderCommentInfo;
    }
}
