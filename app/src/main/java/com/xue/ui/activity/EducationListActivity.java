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

import com.xue.R;
import com.xue.adapter.EducationFooterListAdapter;
import com.xue.adapter.EducationListAdapter;

public class EducationListActivity extends BaseActivity implements View.OnClickListener {

    public static void launch(Context context) {
        Intent intent = new Intent(context, EducationListActivity.class);
        context.startActivity(intent);
    }

    private TextView mTitleTextView;

    private RecyclerView mRecyclerView;

    private EducationFooterListAdapter mAdapter;

    private View mFooterView;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_education_list);

        initActionBar();
        findView();
        init();

        fillList();
    }

    private void initActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM); //Enable自定义的View
            actionBar.setCustomView(R.layout.actionbar_simple);//设置自定义的布局：actionbar_custom
            mTitleTextView = actionBar.getCustomView().findViewById(R.id.title);

            mTitleTextView.setText("教育经历");
        }
    }

    private void findView() {
        mRecyclerView = findViewById(R.id.recyclerView);
        mFooterView = View.inflate(this, R.layout.footer_educationa_work, null);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void init() {

    }

    private void fillList() {
        if (mAdapter == null) {
            mAdapter = new EducationFooterListAdapter(new EducationListAdapter());
            mAdapter.addFooter(mFooterView);
            mRecyclerView.setAdapter(mAdapter);
        }
    }

    @Override
    public void onClick(View v) {
    }
}
