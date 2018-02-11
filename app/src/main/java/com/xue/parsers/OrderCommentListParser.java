package com.xue.parsers;

import com.xue.bean.OrderCommentList;

import org.json.JSONArray;
import org.json.JSONObject;

public class OrderCommentListParser extends MasterParser<OrderCommentList> {

    @Override
    public OrderCommentList parse(JSONObject data) throws Exception {
        OrderCommentList commentList = null;
        JSONArray array = optJSONArray(data, "comment_list");
        int len = getLength(array);

        if (len > 0) {
            commentList = new OrderCommentList();
            OrderCommentParser orderCommentParser = new OrderCommentParser();
            for (int i = 0; i < len; i++) {
                OrderCommentList.Comment comment = orderCommentParser.parse(optJSONObject(array, i));

                if (comment != null) {
                    commentList.add(comment);
                }
            }
        }

        return commentList;
    }
}