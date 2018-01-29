package com.xue.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;

import com.netease.nimlib.sdk.NIMSDK;
import com.netease.nimlib.sdk.Observer;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.msg.MessageBuilder;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.netease.nimlib.sdk.msg.model.IMMessage;

import java.util.List;

public class ChatActivity extends BaseActivity {

    public static void launch(Context context, String account) {
        Intent intent = new Intent(context, ChatActivity.class);
        intent.putExtra("account", account);
        context.startActivity(intent);
    }

    private String mAccount;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        findView();
        readExtra();

        NIMSDK.getMsgServiceObserve().observeReceiveMessage(mReceiveMessageObserver, true);
        NIMSDK.getMsgServiceObserve().observeMsgStatus(mMsgStatusObserver, true);

        IMMessage imMessage = MessageBuilder.createTextMessage(mAccount, SessionTypeEnum.P2P, "我就是试试");
        NIMSDK.getMsgService().sendMessage(imMessage, false).setCallback(new RequestCallback<Void>() {
            @Override
            public void onSuccess(Void imMessage) {
                Log.e("xue", "RequestCallback onSuccess");
            }

            @Override
            public void onFailed(int i) {
                Log.e("xue", "RequestCallback onFailed = " + i);
            }

            @Override
            public void onException(Throwable throwable) {
                Log.e("xue", "RequestCallback onSuccess");
            }
        });
    }

    private void findView() {
    }

    private void readExtra() {
        mAccount = getIntent().getStringExtra("account");
    }

    private Observer<IMMessage> mMsgStatusObserver = new Observer<IMMessage>() {

        @Override
        public void onEvent(IMMessage imMessage) {
            Log.e("xue", "mReceiveMessageObserver imMessages == " + imMessage);
        }
    };

    private Observer<List<IMMessage>> mReceiveMessageObserver = new Observer<List<IMMessage>>() {

        @Override
        public void onEvent(List<IMMessage> imMessages) {
            Log.e("xue", "mReceiveMessageObserver imMessages == " + imMessages);
        }
    };
}
