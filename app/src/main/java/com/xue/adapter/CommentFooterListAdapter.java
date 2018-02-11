package com.xue.adapter;

import com.xue.bean.OrderCommentInfo;
import com.xue.support.view.HeaderFooterRecyclerViewAdapter;

public class CommentFooterListAdapter extends HeaderFooterRecyclerViewAdapter<CommentListAdapter> {

    public CommentFooterListAdapter(CommentListAdapter base) {
        super(base);
    }

    public void setDataList(OrderCommentInfo dataList) {
        getWrappedAdapter().setDataList(dataList);
    }
}
