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
import com.xue.adapter.RechargeHistoryListAdapter;
import com.xue.asyns.HttpAsyncTask;
import com.xue.bean.WalletTradeList;
import com.xue.http.HttpApi;
import com.xue.http.impl.DataHull;
import com.xue.support.view.DividerItemDecoration;

public class RechargeHistoryActivity extends BaseActivity implements View.OnClickListener {

    public static void launch(Context context) {
        Intent intent = new Intent(context, RechargeHistoryActivity.class);
        context.startActivity(intent);
    }

    private ImageView mBackImageView;

    private TextView mTitleTextView;

    private RecyclerView mRecyclerView;

    private RechargeHistoryListAdapter mAdapter;

    private WalletTradeList mWalletTradeList;

    private int limit = 20;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_payments_history);
        initActionBar();
        findView();

        request(true, true);
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
        mRecyclerView = findViewById(R.id.recyclerView);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL, 1, R.color.grey_light));
        if (mAdapter == null) {
            mAdapter = new RechargeHistoryListAdapter();
            mRecyclerView.setAdapter(mAdapter);
        }
    }

    private void initRecyclerView() {

    }

    @Override
    public void onClick(View v) {
        if (mBackImageView == v) {
            finish();
        }
    }

    private void request(boolean isNew, boolean showLoading) {
        int offset = mWalletTradeList == null ? 0 : mWalletTradeList.size();
        new GetTradeListTask(this, String.valueOf(offset), String.valueOf(limit), isNew, showLoading).start();
    }

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
        }

        @Override
        public DataHull<WalletTradeList> doInBackground() {
            return HttpApi.getWalletTradeList("1", offset, limit);
        }

        @Override
        public void onPostExecute(int updateId, WalletTradeList result) {

        }
    }
}
