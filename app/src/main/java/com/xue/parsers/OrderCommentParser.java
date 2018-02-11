package com.xue.parsers;

import android.text.TextUtils;

import com.elianshang.tools.DateTool;
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
            String fromUsername = optString(data, "from_username");
            String fromProfile = optString(data, "from_profile");
            float score = optFloat(data, "score");
            String content = optString(data, "content");
            long time = optLong(data, "created_at");
            String createdAt = DateTool.longToString(time * 1000, "yyyy年MM月dd日 HH时mm分");

            if (!TextUtils.isEmpty(fromUid) && score > 0) {
                comment = new OrderCommentList.Comment();

                comment.setId(id);
                comment.setUid(uid);
                comment.setOrderId(orderId);
                comment.setFromUid(fromUid);
                comment.setFromUserName(fromUsername);
                comment.setFromProfile(fromProfile);
                comment.setScore(score);
                comment.setContent(content);
                comment.setCreatedAt(createdAt);
            }
        }

        return comment;
    }
}