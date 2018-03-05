package com.xue.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.netease.nimlib.sdk.InvocationFuture;
import com.netease.nimlib.sdk.NIMSDK;
import com.netease.nimlib.sdk.Observer;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.msg.MessageBuilder;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.netease.nimlib.sdk.msg.model.IMMessage;
import com.netease.nimlib.sdk.msg.model.QueryDirectionEnum;
import com.xue.BaseApplication;
import com.xue.R;
import com.xue.adapter.ChatListAdapter;

import java.util.List;

public class ChatActivity extends BaseActivity implements View.OnClickListener {

    public static void launch(Context context, String account) {
        Intent intent = new Intent(context, ChatActivity.class);
        intent.putExtra("account", account);
        context.startActivity(intent);
    }

    private TextView mTitleTextView;

    private String mAccount;

    private RecyclerView mRecyclerView;

    private ChatListAdapter mChatListAdapter;

    private List<IMMessage> mIMMessageList;

    private EditText mTextEditText;

    private TextView mSendTextView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        readExtra();
        initActionBar();
        findView();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        NIMSDK.getMsgServiceObserve().observeReceiveMessage(mReceiveMessageObserver, true);
        NIMSDK.getMsgServiceObserve().observeMsgStatus(mMsgStatusObserver, true);

        IMMessage emptyMessage = MessageBuilder.createEmptyMessage(mAccount, SessionTypeEnum.P2P, 0);
        emptyMessage.setFromAccount(BaseApplication.get().getUserId());
        InvocationFuture<List<IMMessage>> invocationFuture = NIMSDK.getMsgService().queryMessageListEx(emptyMessage, QueryDirectionEnum.QUERY_NEW, 100, true);
        invocationFuture.setCallback(new RequestCallback<List<IMMessage>>() {
            @Override
            public void onSuccess(List<IMMessage> imMessages) {
                mIMMessageList = imMessages;

                fillMessageList();
            }

            @Override
            public void onFailed(int i) {
            }

            @Override
            public void onException(Throwable throwable) {
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        NIMSDK.getMsgService().clearUnreadCount(mAccount, SessionTypeEnum.P2P);
    }

    private void findView() {
        mTextEditText = findViewById(R.id.text);
        mSendTextView = findViewById(R.id.send);

        mRecyclerView = findViewById(R.id.recyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);//列表再底部开始展示，反转后由上面开始展示
//        linearLayoutManager.setReverseLayout(true);//列表翻转
        mRecyclerView.setLayoutManager(linearLayoutManager);

        mSendTextView.setOnClickListener(this);
    }

    private void initActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM); //Enable自定义的View
            actionBar.setCustomView(R.layout.actionbar_simple_primary);//设置自定义的布局：actionbar_custom
            mTitleTextView = actionBar.getCustomView().findViewById(R.id.title);

            mTitleTextView.setText("聊天");
        }
    }

    private void fillMessageList() {
        if (mChatListAdapter == null) {
            mChatListAdapter = new ChatListAdapter();
            mRecyclerView.setAdapter(mChatListAdapter);
        }

        mChatListAdapter.setDataList(mIMMessageList);
        mChatListAdapter.notifyDataSetChanged();
    }

    private void readExtra() {
        mAccount = getIntent().getStringExtra("account");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        NIMSDK.getMsgServiceObserve().observeReceiveMessage(mReceiveMessageObserver, false);
        NIMSDK.getMsgServiceObserve().observeMsgStatus(mMsgStatusObserver, false);
    }

    private Observer<IMMessage> mMsgStatusObserver = new Observer<IMMessage>() {

        @Override
        public void onEvent(IMMessage imMessage) {
            Log.e("xue", "onEvent imMessages getMsgType == " + imMessage.getMsgType());
            Log.e("xue", "onEvent imMessages getMsgType == " + imMessage.getContent());

        }
    };

    private Observer<List<IMMessage>> mReceiveMessageObserver = new Observer<List<IMMessage>>() {

        @Override
        public void onEvent(List<IMMessage> imMessages) {
            mIMMessageList.addAll(imMessages);
            fillMessageList();
            mRecyclerView.scrollToPosition(mIMMessageList.size() - 1);
        }
    };

    @Override
    public void onClick(View v) {
        if (mSendTextView == v) {
            String msg = mTextEditText.getText().toString();
            if (!TextUtils.isEmpty(msg)) {
                final IMMessage imMessage = MessageBuilder.createTextMessage(mAccount, SessionTypeEnum.P2P, msg);
                mIMMessageList.add(imMessage);
                fillMessageList();
                mRecyclerView.scrollToPosition(mIMMessageList.size() - 1);
                mTextEditText.setText(null);

                NIMSDK.getMsgService().sendMessage(imMessage, false).setCallback(new RequestCallback<Void>() {
                    @Override
                    public void onSuccess(Void v) {
                        Log.e("xue", "发送成功");
                    }

                    @Override
                    public void onFailed(int i) {
                        Log.e("xue", "发送成功");
                    }

                    @Override
                    public void onException(Throwable throwable) {
                        Log.e("xue", "发送成功");
                    }
                });
            }
        }
    }
}
