package com.xue.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.xue.BaseApplication;
import com.xue.R;
import com.xue.adapter.AdapterOnItemClickCallback;
import com.xue.adapter.EducationFooterListAdapter;
import com.xue.adapter.EducationListAdapter;
import com.xue.bean.UserEducationInfo;

public class EducationListActivity extends BaseActivity implements View.OnClickListener, AdapterOnItemClickCallback<UserEducationInfo.Education> {

    public static void launch(Context context) {
        Intent intent = new Intent(context, EducationListActivity.class);
        context.startActivity(intent);
    }

    private TextView mTitleTextView;

    private TextView mRightTextView;

    private TextView mEmptyTextView;

    private RecyclerView mRecyclerView;

    private EducationFooterListAdapter mAdapter;

    private View mFooterView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_education_list);

        initActionBar();
        findView();
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void initActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM); //Enable自定义的View
            actionBar.setCustomView(R.layout.actionbar_simple);//设置自定义的布局：actionbar_custom
            mTitleTextView = actionBar.getCustomView().findViewById(R.id.title);
            mRightTextView = actionBar.getCustomView().findViewById(R.id.right);

            mRightTextView.setVisibility(View.VISIBLE);
            mRightTextView.setOnClickListener(this);

            mTitleTextView.setText("教育经历");
            mRightTextView.setText("添加");
        }
    }

    private void findView() {
        mEmptyTextView = findViewById(R.id.empty);
        mRecyclerView = findViewById(R.id.recyclerView);
        mFooterView = View.inflate(this, R.layout.footer_educationa_work, null);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void init() {
        UserEducationInfo userEducationInfo = BaseApplication.get().getUser().getUserEducationInfo();

        if (userEducationInfo != null && userEducationInfo.size() > 0) {
            mRecyclerView.setVisibility(View.VISIBLE);
            mEmptyTextView.setVisibility(View.GONE);

            fillList(userEducationInfo);
        } else {
            mRecyclerView.setVisibility(View.GONE);
            mEmptyTextView.setVisibility(View.VISIBLE);
        }
    }

    private void fillList(UserEducationInfo userEducationInfo) {
        if (mAdapter == null) {
            mAdapter = new EducationFooterListAdapter(new EducationListAdapter());
            mAdapter.setCallback(this);
            mAdapter.addFooter(mFooterView);
            mRecyclerView.setAdapter(mAdapter);
        }

        mAdapter.setDataList(userEducationInfo);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        if (mRightTextView == v) {
            EducationActivity.launch(this, null);
        }
    }

    @Override
    public void onItemClick(UserEducationInfo.Education education , View view) {
        EducationActivity.launch(this, education);
    }
}
