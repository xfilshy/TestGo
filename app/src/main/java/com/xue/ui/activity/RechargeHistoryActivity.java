package com.xue.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.xue.R;
import com.xue.adapter.RechargeHistoryFooterListAdapter;
import com.xue.adapter.RechargeHistoryListAdapter;
import com.xue.asyns.HttpAsyncTask;
import com.xue.bean.WalletTradeList;
import com.xue.http.HttpApi;
import com.xue.http.impl.DataHull;
import com.xue.support.view.DividerItemDecoration;
import com.xue.ui.views.ListFootView;

public class RechargeHistoryActivity extends BaseActivity implements View.OnClickListener {

    public static void launch(Context context) {
        Intent intent = new Intent(context, RechargeHistoryActivity.class);
        context.startActivity(intent);
    }

    private ImageView mBackImageView;

    private TextView mTitleTextView;

    private TextView mEmptyTextView;

    private RecyclerView mRecyclerView;

    private RechargeHistoryFooterListAdapter mAdapter;

    private LinearLayoutManager mLinearLayoutManager;

    private WalletTradeList mWalletTradeList;

    private ListFootView mListFootView;

    private int limit = 20;

    private boolean isLoading;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_recharge_history);
        initActionBar();
        findView();

        getTradeList(true, true);
    }

    private void initActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM); //Enable自定义的View
            actionBar.setCustomView(R.layout.actionbar_simple);//设置自定义的布局：actionbar_custom
            mBackImageView = actionBar.getCustomView().findViewById(R.id.back);
            mTitleTextView = actionBar.getCustomView().findViewById(R.id.title);
            mBackImageView.setOnClickListener(this);

            mTitleTextView.setText("收益记录");
        }
    }

    private void findView() {
        mEmptyTextView = findViewById(R.id.empty);
        mRecyclerView = findViewById(R.id.recyclerView);
        mListFootView = new ListFootView(this);

        mLinearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL, 1, R.color.grey_light));
    }

    private void fillData() {
        if (mWalletTradeList == null || mWalletTradeList.size() == 0) {
            mEmptyTextView.setVisibility(View.VISIBLE);
            mRecyclerView.setVisibility(View.GONE);
        } else {
            if (mAdapter == null) {
                mAdapter = new RechargeHistoryFooterListAdapter(new RechargeHistoryListAdapter());
                mAdapter.addFooter(mListFootView);
                mRecyclerView.setAdapter(mAdapter);

                mRecyclerView.addOnScrollListener(mOnScrollListener);
            }

            mAdapter.setDataList(mWalletTradeList);
            mAdapter.notifyDataSetChanged();

            mEmptyTextView.setVisibility(View.GONE);
            mRecyclerView.setVisibility(View.VISIBLE);

            if (mWalletTradeList.getTotal() == mWalletTradeList.size()) {
                mListFootView.showFinish();
            } else {
                mListFootView.showLoading();
            }
        }
    }

    private void getTradeList(boolean isNew, boolean showLoading) {
        int offset = mWalletTradeList == null ? 0 : mWalletTradeList.size();
        new GetTradeListTask(this, String.valueOf(offset), String.valueOf(limit), isNew, showLoading).start();
    }

    @Override
    public void onClick(View v) {
        if (mBackImageView == v) {
            finish();
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
            if (!isLoading && newState == RecyclerView.SCROLL_STATE_IDLE && mLastVisibleItem - 2 == mWalletTradeList.size() && mWalletTradeList.size() < mWalletTradeList.getTotal()) {
                getTradeList(false, false);
            }
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            mLastVisibleItem = mLinearLayoutManager.findLastVisibleItemPosition();
        }
    };


    private class GetTradeListTask extends HttpAsyncTask<WalletTradeList> {

        private String offset;

        private String limit;

        private boolean isNew;

        private boolean showLoading;

        public GetTradeListTask(Context context, String offset, String limit, boolean isNew, boolean showLoading) {
            super(context, true, true);
            this.offset = offset;
            this.limit = limit;
            this.isNew = isNew;
            this.showLoading = showLoading;

            isLoading = true;
        }

        @Override
        public DataHull<WalletTradeList> doInBackground() {
            return HttpApi.getWalletTradeList("1", offset, limit);
        }

        @Override
        public void onPostExecute(int updateId, WalletTradeList result) {
            if (mWalletTradeList == null) {
                mWalletTradeList = result;
            } else {
                if (isNew) {
                    mWalletTradeList = result;
                } else {
                    mWalletTradeList.addAll(result);
                }
            }

            fillData();
        }

        @Override
        public void finish() {
            super.finish();
            isLoading = false;
        }
    }
}
