package com.xue.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.timehop.stickyheadersrecyclerview.StickyRecyclerHeadersDecoration;
import com.xue.BaseApplication;
import com.xue.R;
import com.xue.adapter.AdapterOnItemClickCallback;
import com.xue.adapter.CityListAdapter;
import com.xue.asyns.HttpAsyncTask;
import com.xue.bean.CityList;
import com.xue.bean.UserDetailInfo;
import com.xue.http.HttpApi;
import com.xue.http.impl.DataHull;
import com.xue.support.view.DividerItemDecoration;
import com.xue.support.view.SideBar;

import java.util.HashMap;

public class CityListActivity extends SwipeBackBaseActivity implements AdapterOnItemClickCallback<CityList.City>, SideBar.OnTouchingLetterChangedListener {

    public static final int HomeTown = 1;

    public static final int Location = 2;

    public static void launch(Context context, int type) {
        Intent intent = new Intent(context, CityListActivity.class);
        intent.putExtra("type", type);
        context.startActivity(intent);
    }

    private TextView mCityTitleTextView;

    private TextView mCityTextView;

    private RecyclerView mRecyclerView;

    private SideBar mSideBar;

    private CityListAdapter mAdapter;

    private CityList mCityList;

    private int mType;

    private LinearLayoutManager mLinearLayoutManager;

    private UserDetailInfo mUserDetailInfo;

    private CityList.City mCity;

    private HashMap<String, Integer> positions = new HashMap();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_list);

        readExtra();
        findView();
        init();

        new CityListTask(this).start();
    }

    @Override
    protected boolean hasActionBar() {
        return true;
    }

    @Override
    protected String actionBarTitle() {
        return "选择城市";
    }

    @Override
    protected String actionBarRight() {
        return "保存";
    }

    @Override
    public void rightAction(View view) {
        super.rightAction(view);
        if (mType == HomeTown) {
            new UploadTask(this, mCity.getId(), null).start();
        } else if (mType == Location) {
            new UploadTask(this, null, mCity.getId()).start();
        }
    }

    private void readExtra() {
        Intent intent = getIntent();
        mType = intent.getIntExtra("type", 0);
    }

    private void findView() {
        mRecyclerView = findViewById(R.id.recyclerView);
        mSideBar = findViewById(R.id.sidebar);
        mCityTitleTextView = findViewById(R.id.cityTitle);
        mCityTextView = findViewById(R.id.city);

        if (mType == HomeTown) {
            setActionTitle("选择家乡");
        } else if (mType == Location) {
            setActionTitle("选择现居地");
        }
    }

    private void init() {
        if (mType == HomeTown) {
            mCityTitleTextView.setText("家乡：");
        } else if (mType == Location) {
            mCityTitleTextView.setText("现居地：");
        }

        mUserDetailInfo = BaseApplication.get().getUser().getUserDetailInfo();

        if (!TextUtils.isEmpty(mUserDetailInfo.getHomeTownName())) {
            mCityTextView.setText(mUserDetailInfo.getHomeTownName());
        }

        mSideBar.setOnTouchingLetterChangedListener(this);

        if (mAdapter == null) {
            mAdapter = new CityListAdapter();
            mRecyclerView.setAdapter(mAdapter);
            mAdapter.setCallback(this);
        }

        mLinearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mRecyclerView.addItemDecoration(new StickyRecyclerHeadersDecoration(mAdapter));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL, 1, R.color.black_light));
    }

    private void fillData() {
        positions.clear();

        String s = null;
        for (int i = 0; i < mCityList.size(); i++) {
            CityList.City city = mCityList.get(i);
            if (!TextUtils.equals(s, city.getInitial())) {
                s = city.getInitial();
                positions.put(s, i);
            }
        }

        mAdapter.setDataList(mCityList);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(CityList.City city, View view) {
        mCity = city;
        mCityTextView.setText(city.getShowName());
        if (!TextUtils.equals(mUserDetailInfo.getHomeTown(), city.getId())) {
            setActionRightVisibility(View.VISIBLE);
        } else {
            setActionRightVisibility(View.GONE);
        }
    }

    @Override
    public void onTouchingLetterChanged(String s) {
        Integer integer = positions.get(s);
        if (integer != null) {
            mLinearLayoutManager.scrollToPositionWithOffset(integer, 0);
        }
    }

    private class CityListTask extends HttpAsyncTask<CityList> {

        public CityListTask(Context context) {
            super(context, true, true);
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

    private class UploadTask extends HttpAsyncTask<UserDetailInfo> {

        private String homeTown;

        private String location;

        public UploadTask(Context context, String homeTown, String location) {
            super(context, true, true);
            this.homeTown = homeTown;
            this.location = location;
        }

        @Override
        public DataHull<UserDetailInfo> doInBackground() {
            return HttpApi.updateUserDetailInfo(null, null, homeTown, null, null, null);
        }

        @Override
        public void onPostExecute(int updateId, UserDetailInfo result) {
            BaseApplication.get().setUserDetailInfo(result);
            CityListActivity.this.finish();
        }
    }
}