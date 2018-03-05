package com.xue.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.TextView;

import com.xue.R;
import com.xue.adapter.PaymentsHistoryListAdapter;
import com.xue.support.view.DividerItemDecoration;

public class PaymentsHistoryActivity extends SwipeBackBaseActivity {

    public static void launch(Context context) {
        Intent intent = new Intent(context, PaymentsHistoryActivity.class);
        context.startActivity(intent);
    }

    private ImageView mBackImageView;

    private TextView mTitleTextView;

    private RecyclerView mRecyclerView;

    private PaymentsHistoryListAdapter mAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_recharge_history);
        findView();
    }

    @Override
    protected boolean hasActionBar() {
        return true;
    }

    @Override
    protected String actionBarTitle() {
        return "收支记录";
    }

    @Override
    protected String actionBarRight() {
        return null;
    }

    private void findView() {
        mRecyclerView = findViewById(R.id.recyclerView);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL, 1, R.color.grey_light));
        if (mAdapter == null) {
            mAdapter = new PaymentsHistoryListAdapter();
            mRecyclerView.setAdapter(mAdapter);
        }
    }

    private void initRecyclerView() {

    }
}
