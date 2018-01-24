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
import com.xue.adapter.HomeFooterGridAdapter;
import com.xue.adapter.HomeGridAdapter;
import com.xue.asyns.HttpAsyncTask;
import com.xue.bean.UserMinor;
import com.xue.bean.UserMinorList;
import com.xue.http.HttpApi;
import com.xue.http.impl.DataHull;
import com.xue.ui.views.HomeGridItemDecoration;

import java.util.List;

/**
 * Created by xfilshy on 2018/1/17.
 */
public class HomeFragment extends BaseFragment {

    private RecyclerView mRecyclerView;

//    private HomeGridAdapter mAdapter;

    private List<UserMinor> mDataList;

    private HomeFooterGridAdapter mAdapter;

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
        mRecyclerView = getView().findViewById(R.id.recyclerview);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2);
        mRecyclerView.setLayoutManager(gridLayoutManager);
        mRecyclerView.addItemDecoration(new HomeGridItemDecoration(UITool.dipToPx(getActivity(), 3), 2));

        if (mAdapter == null) {
            mAdapter = new HomeFooterGridAdapter(new HomeGridAdapter(), gridLayoutManager, 2);
            mRecyclerView.setAdapter(mAdapter);

            mAdapter.addFooter(View.inflate(getActivity(), R.layout.footer_home_grid, null));
        }
    }

    private void fillRecyclerView() {
        if (getActivity() == null) {
            return;
        }

        mAdapter.getWrappedAdapter().setDataList(mDataList);
        mAdapter.notifyDataSetChanged();
    }

    private class ListTask extends HttpAsyncTask<UserMinorList> {

        public ListTask(Context context) {
            super(context);
        }

        @Override
        public DataHull<UserMinorList> doInBackground() {
            return HttpApi.recommendList();
        }

        @Override
        public void onPostExecute(int updateId, UserMinorList result) {
            mDataList = result;
            fillRecyclerView();
        }
    }
}
