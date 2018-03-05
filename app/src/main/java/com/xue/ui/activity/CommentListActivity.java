package com.xue.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.xue.R;
import com.xue.adapter.CommentFooterListAdapter;
import com.xue.adapter.CommentListAdapter;
import com.xue.asyns.HttpAsyncTask;
import com.xue.bean.OrderCommentInfo;
import com.xue.http.HttpApi;
import com.xue.http.impl.DataHull;
import com.xue.support.view.DividerItemDecoration;
import com.xue.ui.views.ListFootView;

public class CommentListActivity extends SwipeBackBaseActivity {

    public static void launch(Context context, String uid) {
        Intent intent = new Intent(context, CommentListActivity.class);
        intent.putExtra("uid", uid);
        context.startActivity(intent);
    }

    private RecyclerView mRecyclerView = null;

    private TextView mEmptyTextView;

    private LinearLayoutManager mLinearLayoutManager;

    private String mUid;

    private OrderCommentInfo mOrderCommentInfo;

    private ListFootView mFooterView;

    private CommentFooterListAdapter mAdapter;

    private int mLimit = 10;

    private boolean isLoading = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment_list);

        readExtra();
        findView();

        getCommentInfo(true, true);
    }

    @Override
    protected boolean hasActionBar() {
        return true;
    }

    @Override
    protected String actionBarTitle() {
        return "评论";
    }

    @Override
    protected String actionBarRight() {
        return null;
    }

    private void readExtra() {
        mUid = getIntent().getStringExtra("uid");
    }

    private void findView() {
        mRecyclerView = findViewById(R.id.recyclerView);
        mEmptyTextView = findViewById(R.id.empty);
        mFooterView = new ListFootView(this);

        mLinearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL, 1, R.color.grey_light));
    }

    private void fillData() {
        if (mOrderCommentInfo == null) {
            mRecyclerView.setVisibility(View.GONE);
            mEmptyTextView.setVisibility(View.VISIBLE);
            return;
        }

        mRecyclerView.setVisibility(View.VISIBLE);
        mEmptyTextView.setVisibility(View.GONE);

        if (mAdapter == null) {
            mAdapter = new CommentFooterListAdapter(new CommentListAdapter());
            mAdapter.addFooter(mFooterView);

            mRecyclerView.setAdapter(mAdapter);
            mRecyclerView.addOnScrollListener(mOnScrollListener);
        }

        mAdapter.setDataList(mOrderCommentInfo);
        mAdapter.notifyDataSetChanged();

        if (mOrderCommentInfo.getOrderScoreMap().getTotal() == mOrderCommentInfo.getOrderCommentList().size()) {
            mFooterView.showFinish();
        } else {
            mFooterView.showLoading();
        }
    }

    private void getCommentInfo(boolean isNew, boolean showLoading) {
        if (!isLoading) {
            int offset = mOrderCommentInfo == null ? 0 : mOrderCommentInfo.getOrderCommentList().size();
            new GetCommentInfoTask(this, mUid, offset, mLimit, isNew, showLoading).start();
        }
    }

    private RecyclerView.OnScrollListener mOnScrollListener = new RecyclerView.OnScrollListener() {

        /**
         * 最后一个显示的item的pos
         */
        private int mLastVisibleItem;

        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            if (!isLoading && newState == RecyclerView.SCROLL_STATE_IDLE && mLastVisibleItem - 2 == mOrderCommentInfo.getOrderCommentList().size() && mOrderCommentInfo.getOrderCommentList().size() < mOrderCommentInfo.getOrderScoreMap().getTotal()) {
                getCommentInfo(false, false);
            }
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            mLastVisibleItem = mLinearLayoutManager.findLastVisibleItemPosition();
        }
    };

    private class GetCommentInfoTask extends HttpAsyncTask<OrderCommentInfo> {

        private String uid;

        private int offset;

        private int limit;

        private boolean isNew;

        private boolean showLoading;

        public GetCommentInfoTask(Context context, String uid, int offset, int limit, boolean isNew, boolean showLoading) {
            super(context, true, showLoading);
            isLoading = true;
            this.uid = uid;
            this.offset = offset;
            this.limit = limit;
            this.isNew = isNew;
            this.showLoading = showLoading;
        }

        @Override
        public DataHull<OrderCommentInfo> doInBackground() {
            return HttpApi.getOrderCommentInfo(uid, String.valueOf(offset), String.valueOf(limit));
        }

        @Override
        public void onPostExecute(int updateId, OrderCommentInfo result) {
            if (isNew) {
                mOrderCommentInfo = result;
            } else {
                mOrderCommentInfo.getOrderCommentList().addAll(result.getOrderCommentList());
                mOrderCommentInfo.setOrderScoreMap(result.getOrderScoreMap());
            }

            fillData();
        }

        @Override
        public void finished() {
            super.finished();
            isLoading = false;
        }
    }
}
