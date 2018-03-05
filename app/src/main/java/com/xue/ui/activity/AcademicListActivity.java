package com.xue.ui.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.xue.R;
import com.xue.adapter.AcademicListAdapter;
import com.xue.adapter.AdapterOnItemClickCallback;
import com.xue.asyns.HttpAsyncTask;
import com.xue.bean.AcademicList;
import com.xue.http.HttpApi;
import com.xue.http.impl.DataHull;
import com.xue.support.view.DividerItemDecoration;

public class AcademicListActivity extends SwipeBackBaseActivity implements AdapterOnItemClickCallback<AcademicList.Academic> {

    public static void launch(Activity activity) {
        Intent intent = new Intent(activity, AcademicListActivity.class);
        activity.startActivityForResult(intent, 1);
    }

    private RecyclerView mRecyclerView;

    private AcademicListAdapter mAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_academic_list);
        findView();
        new GetTask(this).start();
    }

    @Override
    protected boolean hasActionBar() {
        return true;
    }

    @Override
    protected String actionBarTitle() {
        return "学历列表";
    }

    @Override
    protected String actionBarRight() {
        return null;
    }

    private void findView() {
        mRecyclerView = findViewById(R.id.recyclerView);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL, 1, R.color.grey_light));
    }

    private void fillData(AcademicList academicList) {
        if (mAdapter == null) {
            mAdapter = new AcademicListAdapter();
            mRecyclerView.setAdapter(mAdapter);
            mAdapter.setCallback(this);
        }

        mAdapter.setDataList(academicList);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(AcademicList.Academic academic , View view) {
        Intent intent = new Intent();
        intent.putExtra("academic", academic);
        setResult(RESULT_OK , intent);
        finish();
    }

    private class GetTask extends HttpAsyncTask<AcademicList> {

        public GetTask(Context context) {
            super(context , true , true);
        }

        @Override
        public DataHull<AcademicList> doInBackground() {
            return HttpApi.getAcademicList();
        }

        @Override
        public void onPostExecute(int updateId, AcademicList result) {
            fillData(result);
        }
    }
}
