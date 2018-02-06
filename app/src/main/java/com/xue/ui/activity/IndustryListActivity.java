package com.xue.ui.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.xue.R;
import com.xue.adapter.AdapterOnItemClickCallback;
import com.xue.adapter.IndustryListAdapter;
import com.xue.asyns.HttpAsyncTask;
import com.xue.bean.IndustryList;
import com.xue.http.HttpApi;
import com.xue.http.impl.DataHull;
import com.xue.support.view.DividerItemDecoration;

public class IndustryListActivity extends BaseActivity implements AdapterOnItemClickCallback<IndustryList.Industry> {

    public static void launch(Activity activity) {
        Intent intent = new Intent(activity, IndustryListActivity.class);
        activity.startActivityForResult(intent, 1);
    }

    private TextView mTitleTextView;

    private RecyclerView mRecyclerView;

    private IndustryListAdapter mAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_academic_list);
        initActionBar();
        findView();
        new GetTask(this).start();
    }

    private void initActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM); //Enable自定义的View
            actionBar.setCustomView(R.layout.actionbar_simple);//设置自定义的布局：actionbar_custom
            mTitleTextView = actionBar.getCustomView().findViewById(R.id.title);

            mTitleTextView.setText("行业列表");
        }
    }

    private void findView() {
        mRecyclerView = findViewById(R.id.recyclerView);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL, 1, R.color.grey_light));
    }

    private void fillData(IndustryList industryList) {
        if (mAdapter == null) {
            mAdapter = new IndustryListAdapter();
            mRecyclerView.setAdapter(mAdapter);
            mAdapter.setCallback(this);
        }

        mAdapter.setDataList(industryList);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(IndustryList.Industry industry) {
        Intent intent = new Intent();
        intent.putExtra("industry", industry);
        setResult(RESULT_OK, intent);
        finish();
    }

    private class GetTask extends HttpAsyncTask<IndustryList> {

        public GetTask(Context context) {
            super(context);
        }

        @Override
        public DataHull<IndustryList> doInBackground() {
            return HttpApi.getIndustryList();
        }

        @Override
        public void onPostExecute(int updateId, IndustryList result) {
            fillData(result);
        }
    }
}
