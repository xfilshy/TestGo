package com.xue.ui.fragment;

import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.elianshang.tools.WeakReferenceHandler;
import com.xue.R;
import com.xue.adapter.HomeListAdapter;
import com.xue.bean.UserMinor;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xfilshy on 2018/1/17.
 */
public class HomeFragment extends BaseFragment {

    private WeakReferenceHandler<HomeFragment> handler = new WeakReferenceHandler<HomeFragment>(this) {
        @Override
        public void HandleMessage(HomeFragment fragment, Message msg) {
            if (fillRecyclerView == msg.what) {
                fragment.fillRecyclerView();
            }
        }
    };

    private RecyclerView mRecyclerView;

    private HomeListAdapter mAdapter;

    private List<UserMinor> mDataList;

    private int fillRecyclerView = 1;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        findView();

        mDataList = new ArrayList();
        mDataList.add(new UserMinor("3624602962994852973"));
        mDataList.add(new UserMinor("7387923401607860050"));
        mDataList.add(new UserMinor("2512354592515272805"));
        mDataList.add(new UserMinor("4270869509516842689"));
        mDataList.add(new UserMinor("2899945635449269424"));
        mDataList.add(new UserMinor("1215740112302612424"));
        mDataList.add(new UserMinor("9043699952115462624"));
        handler.sendEmptyMessage(1);
    }

    private void findView() {
        mRecyclerView = getView().findViewById(R.id.recyclerview);
    }

    private void fillRecyclerView() {
        if (getActivity() == null) {
            return;
        }
//        if (mAdapter == null) {
            mAdapter = new HomeListAdapter();
            mRecyclerView.setAdapter(mAdapter);
//        }

        mAdapter.setDataList(mDataList);
        mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        mAdapter.notifyDataSetChanged();
    }
}
