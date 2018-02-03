package com.xue.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.timehop.stickyheadersrecyclerview.StickyRecyclerHeadersDecoration;
import com.xue.BaseApplication;
import com.xue.R;
import com.xue.adapter.CityListAdapter;
import com.xue.asyns.HttpAsyncTask;
import com.xue.bean.CityList;
import com.xue.bean.UserInfoDetail;
import com.xue.http.HttpApi;
import com.xue.http.impl.DataHull;
import com.xue.support.view.DividerItemDecoration;

public class CityActivity extends BaseActivity implements View.OnClickListener, CityListAdapter.OnItemClickCallback {

    public static final int HomeTown = 1;

    public static final int Location = 2;

    public static void launch(Context context, int type) {
        Intent intent = new Intent(context, CityActivity.class);
        intent.putExtra("type", type);
        context.startActivity(intent);
    }

    private ImageView mBackImageView;

    private TextView mTitleTextView;

    private TextView mRightTextView;

    private TextView mCityTitleTextView;

    private TextView mCityTextView;

    private RecyclerView mRecyclerView;

    private CityListAdapter mAdapter;

    private CityList mCityList;

    private int mType;

    private UserInfoDetail mUserInfoDetail;

    private CityList.City mCity;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city);

        readExtra();
        initActionBar();
        findView();
        init();

        new CityListTask(this).start();
    }

    private void initActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM); //Enable自定义的View
            actionBar.setCustomView(R.layout.actionbar_simple);//设置自定义的布局：actionbar_custom
            mBackImageView = actionBar.getCustomView().findViewById(R.id.back);
            mTitleTextView = actionBar.getCustomView().findViewById(R.id.title);
            mRightTextView = actionBar.getCustomView().findViewById(R.id.right);
            mBackImageView.setOnClickListener(this);
            mRightTextView.setOnClickListener(this);

            if (mType == HomeTown) {
                mTitleTextView.setText("选择家乡");
            } else if (mType == Location) {
                mTitleTextView.setText("选择现居地");
            }
            mRightTextView.setText("保存");
        }
    }

    private void readExtra() {
        Intent intent = getIntent();
        mType = intent.getIntExtra("type", 0);
    }

    private void findView() {
        mRecyclerView = findViewById(R.id.recyclerView);
        mCityTitleTextView = findViewById(R.id.cityTitle);
        mCityTextView = findViewById(R.id.city);
    }

    private void init() {
        if (mType == HomeTown) {
            mCityTitleTextView.setText("家乡：");
        } else if (mType == Location) {
            mCityTitleTextView.setText("现居地：");
        }

        mUserInfoDetail = BaseApplication.get().getUser().getUserInfoDetail();

        if (!TextUtils.isEmpty(mUserInfoDetail.getHomeTownName())) {
            mCityTextView.setText(mUserInfoDetail.getHomeTownName());
        }

        if (mAdapter == null) {
            mAdapter = new CityListAdapter();
            mRecyclerView.setAdapter(mAdapter);
            mAdapter.setCallback(this);
        }

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.addItemDecoration(new StickyRecyclerHeadersDecoration(mAdapter));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL, 1, R.color.black_light));
    }

    private void fillData() {
        mAdapter.setDataList(mCityList);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        if (mRightTextView == v) {
            if (mType == HomeTown) {
                new UploadTask(this, mCity.getId(), null).start();
            } else if (mType == Location) {
                new UploadTask(this, null, mCity.getId()).start();
            }
        }
    }

    @Override
    public void onItemClick(CityList.City city) {
        mCity = city;
        if (!TextUtils.equals(mUserInfoDetail.getGender(), city.getId())) {
            mCityTextView.setText(city.getShowName());
            mRightTextView.setVisibility(View.VISIBLE);
        } else {
            mRightTextView.setVisibility(View.GONE);
        }
    }

    private class CityListTask extends HttpAsyncTask<CityList> {

        public CityListTask(Context context) {
            super(context);
        }

        @Override
        public DataHull<CityList> doInBackground() {
            return HttpApi.resCityList();
        }

        @Override
        public void onPostExecute(int updateId, CityList result) {
            mCityList = result;
            fillData();
        }
    }

    private class UploadTask extends HttpAsyncTask<UserInfoDetail> {

        private String homeTown;

        private String location;

        public UploadTask(Context context, String homeTown, String location) {
            super(context);
            this.homeTown = homeTown;
            this.location = location;
        }

        @Override
        public DataHull<UserInfoDetail> doInBackground() {
            return HttpApi.updateUserInfoDetail(null, null, homeTown, null, null, null);
        }

        @Override
        public void onPostExecute(int updateId, UserInfoDetail result) {
            BaseApplication.get().setUserInfoDetail(result);
            CityActivity.this.finish();
        }
    }
}