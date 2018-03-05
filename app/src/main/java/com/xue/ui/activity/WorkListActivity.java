package com.xue.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.xue.BaseApplication;
import com.xue.R;
import com.xue.adapter.AdapterOnItemClickCallback;
import com.xue.adapter.WorkFooterListAdapter;
import com.xue.adapter.WorkListAdapter;
import com.xue.bean.UserWorkInfo;

public class WorkListActivity extends SwipeBackBaseActivity implements AdapterOnItemClickCallback<UserWorkInfo.Work> {

    public static void launch(Context context) {
        Intent intent = new Intent(context, WorkListActivity.class);
        context.startActivity(intent);
    }

    private TextView mEmptyTextView;

    private RecyclerView mRecyclerView;

    private WorkFooterListAdapter mAdapter;

    private View mFooterView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work_list);

        findView();
    }

    @Override
    protected boolean hasActionBar() {
        return true;
    }

    @Override
    protected String actionBarTitle() {
        return "工作经历";
    }

    @Override
    protected String actionBarRight() {
        return "添加";
    }

    @Override
    public void rightAction(View view) {
        super.rightAction(view);
        WorkActivity.launch(this, null);
    }

    @Override
    protected void onResume() {
        super.onResume();
        init();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    private void findView() {
        mEmptyTextView = findViewById(R.id.empty);
        mRecyclerView = findViewById(R.id.recyclerView);
        mFooterView = View.inflate(this, R.layout.footer_educationa_work, null);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        setActionRightVisibility(View.VISIBLE);
    }

    private void init() {
        UserWorkInfo userWorkInfo = BaseApplication.get().getUser().getUserWorkInfo();

        if (userWorkInfo != null && userWorkInfo.size() > 0) {
            mRecyclerView.setVisibility(View.VISIBLE);
            mEmptyTextView.setVisibility(View.GONE);

            fillList(userWorkInfo);
        } else {
            mRecyclerView.setVisibility(View.GONE);
            mEmptyTextView.setVisibility(View.VISIBLE);
        }
    }

    private void fillList(UserWorkInfo userWorkInfo) {
        if (mAdapter == null) {
            mAdapter = new WorkFooterListAdapter(new WorkListAdapter());
            mAdapter.setCallback(this);
            mAdapter.addFooter(mFooterView);
            mRecyclerView.setAdapter(mAdapter);
        }

        mAdapter.setDataList(userWorkInfo);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(UserWorkInfo.Work work, View view) {
        WorkActivity.launch(this, work);
    }
}
