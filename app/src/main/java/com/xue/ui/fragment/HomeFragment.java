package com.xue.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.elianshang.tools.UITool;
import com.xue.R;
import com.xue.adapter.AdapterOnItemClickCallback;
import com.xue.adapter.HomeFooterGridAdapter;
import com.xue.adapter.HomeGridAdapter;
import com.xue.asyns.HttpAsyncTask;
import com.xue.bean.User;
import com.xue.bean.UserList;
import com.xue.http.HttpApi;
import com.xue.http.impl.DataHull;
import com.xue.support.view.GridItemDecoration;
import com.xue.ui.activity.DetailActivity;

/**
 * Created by xfilshy on 2018/1/17.
 */
public class HomeFragment extends BaseFragment implements AdapterOnItemClickCallback<User> {

    private RecyclerView mRecyclerView;

    private UserList mDataList;

    private HomeFooterGridAdapter mAdapter;

    private View mFooterView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        findView();
        new ListTask(getActivity()).start();
    }

    private void findView() {
        mRecyclerView = getView().findViewById(R.id.recyclerView);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2);
        mRecyclerView.setLayoutManager(gridLayoutManager);
        mRecyclerView.addItemDecoration(new GridItemDecoration(UITool.dipToPx(getActivity(), 3)));
        mFooterView = View.inflate(getActivity(), R.layout.footer_home_grid, null);

        if (mAdapter == null) {
            mAdapter = new HomeFooterGridAdapter(new HomeGridAdapter(), gridLayoutManager, 2);
            mRecyclerView.setAdapter(mAdapter);
            mAdapter.setCallback(this);
        }
    }

    private void fillData() {
        if (getActivity() == null) {
            return;
        }

        if (mDataList != null && mDataList.size() > 0) {
            if (mFooterView.getParent() == null) {
                mAdapter.addFooter(mFooterView);
            }
        } else {
            if (mFooterView.getParent() != null) {
                mAdapter.removeFooter(mFooterView);
            }
        }
        mAdapter.setDataList(mDataList);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(User user, View view) {
        DetailActivity.launch(getActivity(), user.getUserBase().getUid());
    }

    private class ListTask extends HttpAsyncTask<UserList> {

        public ListTask(Context context) {
            super(context);
        }

        @Override
        public DataHull<UserList> doInBackground() {
            return HttpApi.recommendList();
        }

        @Override
        public void onPostExecute(int updateId, UserList result) {
            mDataList = result;
            fillData();
        }
    }
}
