package com.xue.ui.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.netease.nimlib.sdk.NIMSDK;
import com.netease.nimlib.sdk.Observer;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.msg.model.RecentContact;
import com.xue.R;
import com.xue.adapter.AdapterOnItemClickCallback;
import com.xue.adapter.AdapterOnItemLongClickCallback;
import com.xue.adapter.SessionListAdapter;
import com.xue.netease.NeteaseUserInfoCache;
import com.xue.support.view.DividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

public class SessionListActivity extends BaseActivity implements AdapterOnItemClickCallback<RecentContact>, AdapterOnItemLongClickCallback<RecentContact> {

    public static void launch(Context context) {
        Intent intent = new Intent(context, SessionListActivity.class);
        context.startActivity(intent);
    }

    private TextView mTitleTextView;

    private RecyclerView mRecyclerView;

    private SessionListAdapter mAdapter;

    private List<RecentContact> mRecentContacts;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_session_list);
        initActionBar();
        findView();

        fillData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerObserves();
    }

    @Override
    protected void onPause() {
        super.onPause();
        unRegisterObserves();
    }

    private void findView() {
        mRecyclerView = findViewById(R.id.recyclerView);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayout.VERTICAL, 1, R.color.grey_light));
    }


    private void registerObserves() {
        NIMSDK.getMsgService().queryRecentContacts().setCallback(mQueryCallback);
        NeteaseUserInfoCache.get().setCallback(mCacheChangeCallback);
        NIMSDK.getMsgServiceObserve().observeRecentContact(mRecentContactObserver, true);
    }

    private void unRegisterObserves() {
        NeteaseUserInfoCache.get().setCallback(null);
        NIMSDK.getMsgServiceObserve().observeRecentContact(mRecentContactObserver, false);
    }

    private ArrayList<String> toAccounts() {
        if (mRecentContacts != null) {
            ArrayList<String> accounts = new ArrayList();
            for (RecentContact recentContact : mRecentContacts) {
                accounts.add(recentContact.getContactId());
            }

            return accounts;
        }

        return null;
    }

    private synchronized void changeRecentContacts(List<RecentContact> recentContacts, boolean isChange) {
        if (mRecentContacts == null || !isChange) {
            mRecentContacts = recentContacts;
        } else {
            ArrayList<RecentContact> tmp = new ArrayList();
            for (RecentContact rc1 : recentContacts) {
                for (RecentContact rc2 : mRecentContacts) {
                    if (TextUtils.equals(rc1.getContactId(), rc2.getContactId())) {
                        tmp.add(rc2);
                        continue;
                    }
                }
            }

            mRecentContacts.removeAll(tmp);
            mRecentContacts.addAll(0, recentContacts);
        }
    }

    private void fillData() {
        if (mAdapter == null) {
            mAdapter = new SessionListAdapter();
            mAdapter.setCallback(this);
            mAdapter.setLongClickCallback(this);
            mRecyclerView.setAdapter(mAdapter);
        }

        mAdapter.setDataList(mRecentContacts);
        mAdapter.notifyDataSetChanged();
    }

    private void initActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM); //Enable自定义的View
            actionBar.setCustomView(R.layout.actionbar_simple);//设置自定义的布局：actionbar_custom
            mTitleTextView = actionBar.getCustomView().findViewById(R.id.title);

            mTitleTextView.setText("消息");
        }
    }

    @Override
    public void onItemClick(RecentContact recentContact, View view) {
        ChatActivity.launch(this, recentContact.getContactId());
    }

    @Override
    public void onItemLongClick(final RecentContact recentContact, View view) {
        AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setTitle("提示")
                .setMessage("确认删除这个会话")
                .setNegativeButton("取消", null)
                .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        NIMSDK.getMsgService().deleteRecentContact(recentContact);
                        mRecentContacts.remove(recentContact);
                        fillData();
                    }
                }).create();
        alertDialog.show();
    }


    private NeteaseUserInfoCache.CacheChangeCallback mCacheChangeCallback = new NeteaseUserInfoCache.CacheChangeCallback() {

        @Override
        public void onChange() {
            fillData();
        }
    };

    private RequestCallback<List<RecentContact>> mQueryCallback = new RequestCallback<List<RecentContact>>() {

        @Override
        public void onSuccess(List<RecentContact> recentContacts) {
            changeRecentContacts(recentContacts, false);
            if (NeteaseUserInfoCache.get().update(toAccounts())) {
                fillData();
            }
        }

        @Override
        public void onFailed(int i) {
        }

        @Override
        public void onException(Throwable throwable) {
        }
    };

    private Observer<List<RecentContact>> mRecentContactObserver = new Observer<List<RecentContact>>() {

        @Override
        public void onEvent(List<RecentContact> recentContacts) {
            changeRecentContacts(recentContacts, true);
            fillData();
        }
    };
}