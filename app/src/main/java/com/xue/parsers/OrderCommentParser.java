package com.xue.parsers;

import android.text.TextUtils;

import com.xue.bean.OrderCommentList;

import org.json.JSONObject;

public class OrderCommentParser extends MasterParser<OrderCommentList.Comment> {

    @Override
    public OrderCommentList.Comment parse(JSONObject data) throws Exception {
        OrderCommentList.Comment comment = null;
        if (data != null) {
            String id = optString(data, "id");
            String orderId = optString(data, "order_id");
            String uid = optString(data, "uid");
            String fromUid = optString(data, "from_uid");
            String fromRealname = optString(data, "from_realname");
            String fromProfile = optString(data, "from_profile");
            String score = optString(data, "score");
            String content = optString(data, "content");

            if (!TextUtils.isEmpty(fromUid) && !TextUtils.isEmpty(score)) {
                comment = new OrderCommentList.Comment();

                comment.setId(id);
                comment.setUid(uid);
                comment.setOrderId(orderId);
                comment.setFromUid(fromUid);
                comment.setFromRealName(fromRealname);
                comment.setFromProfile(fromProfile);
                comment.setScore(score);
                comment.setContent(content);
            }
        }

        return comment;
    }
}