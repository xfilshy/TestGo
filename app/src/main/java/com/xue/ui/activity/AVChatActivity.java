package com.xue.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.Button;

import com.netease.nimlib.sdk.avchat.constant.AVChatType;
import com.netease.nimlib.sdk.avchat.model.AVChatData;
import com.xue.R;
import com.xue.netease.SimpleAVChatData;
import com.xue.ui.fragment.AudioChatFragment;
import com.xue.ui.fragment.VideoChatFragment;


/**
 * Created by xfilshy on 2018/1/18.
 */

public class AVChatActivity extends BaseActivity implements AVChatControllerCallback, View.OnClickListener {

    public static void launchVideoCall(Context context, String account) {
        Intent intent = new Intent(context, AVChatActivity.class);
        AVChatData avChatData = new SimpleAVChatData(account, AVChatType.VIDEO);
        intent.putExtra("avChatData", avChatData);
        intent.putExtra("action", "call");
        context.startActivity(intent);
    }

    public static void launchAudioCall(Context context, String account) {
        Intent intent = new Intent(context, AVChatActivity.class);
        AVChatData avChatData = new SimpleAVChatData(account, AVChatType.AUDIO);
        intent.putExtra("avChatData", avChatData);
        intent.putExtra("action", "call");
        context.startActivity(intent);
    }

    public static void launchVideoAccept(Context context, AVChatData avChatData) {
        Intent intent = new Intent(context, AVChatActivity.class);
        intent.putExtra("avChatData", avChatData);
        intent.putExtra("action", "accept");
        context.startActivity(intent);
    }

    public static void launchAudioAccept(Context context, AVChatData avChatData) {
        Intent intent = new Intent(context, AVChatActivity.class);
        intent.putExtra("avChatData", avChatData);
        intent.putExtra("action", "accept");
        context.startActivity(intent);
    }

    private AVChatData mAVChatData;

    private String mAction;

    private AVChatController mAVChatController;

    private VideoChatFragment mVideoChatFragment;

    private AudioChatFragment mAudioChatFragment;

    private Button mChangeButton;

    private Button mHangupButton;

    private Button mAcceptButton;

    private Button mRecordButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_avchat);
        readExtra(getIntent());
        findView();
        init();
        start();
    }

    private void init() {
        mAVChatController = new AVChatController(this, mAVChatData);
        mAVChatController.setCallback(this);
    }

    private void readExtra(Intent intent) {
        mAVChatData = (AVChatData) intent.getSerializableExtra("avChatData");
        mAction = intent.getStringExtra("action");
    }

    private void findView() {
        mChangeButton = findViewById(R.id.change);
        mAcceptButton = findViewById(R.id.accept);
        mHangupButton = findViewById(R.id.hangUp);
        mRecordButton = findViewById(R.id.record);

        mChangeButton.setOnClickListener(this);
        mAcceptButton.setOnClickListener(this);
        mHangupButton.setOnClickListener(this);
        mRecordButton.setOnClickListener(this);
    }

    private void start() {
        if (mAVChatData.getChatType() == AVChatType.VIDEO) {
            goVideoFragment();
        } else if (mAVChatData.getChatType() == AVChatType.AUDIO) {
            goAudioFragment();
        }
    }

    /**
     * 去视频聊天页
     */
    private void goVideoFragment() {
        if (mVideoChatFragment == null) {
            mVideoChatFragment = new VideoChatFragment(mAVChatController, mAction);
        }

        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.fragment_root, mVideoChatFragment);
        transaction.commit();
    }

    /**
     * 去音频聊天页
     */
    private void goAudioFragment() {
        if (mAudioChatFragment == null) {
            mAudioChatFragment = new AudioChatFragment(mAVChatController, mAction);
        }

        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.fragment_root, mAudioChatFragment);
        transaction.commit();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onClick(View view) {
        if (mChangeButton == view) {

        } else if (mHangupButton == view) {
            mAVChatController.hangUp();
            finish();
        } else if (mAcceptButton == view) {

        } else if (mRecordButton == view) {

        }
    }

    @Override
    public void calling() {
        mChangeButton.setVisibility(View.GONE);
        mHangupButton.setVisibility(View.VISIBLE);
        mAcceptButton.setVisibility(View.GONE);
        mRecordButton.setVisibility(View.GONE);

        ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) mChangeButton.getLayoutParams();
        layoutParams.leftToLeft = -1;
        layoutParams.rightToRight = -1;
        layoutParams.rightToLeft = -1;
        layoutParams.leftToRight = -1;

        layoutParams = (ConstraintLayout.LayoutParams) mHangupButton.getLayoutParams();
        layoutParams.leftToLeft = ConstraintLayout.LayoutParams.PARENT_ID;
        layoutParams.rightToRight = ConstraintLayout.LayoutParams.PARENT_ID;
        layoutParams.rightToLeft = -1;
        layoutParams.leftToRight = -1;

        layoutParams = (ConstraintLayout.LayoutParams) mAcceptButton.getLayoutParams();
        layoutParams.leftToLeft = -1;
        layoutParams.rightToRight = -1;
        layoutParams.rightToLeft = -1;
        layoutParams.leftToRight = -1;

        layoutParams = (ConstraintLayout.LayoutParams) mRecordButton.getLayoutParams();
        layoutParams.leftToLeft = -1;
        layoutParams.rightToRight = -1;
        layoutParams.rightToLeft = -1;
        layoutParams.leftToRight = -1;
    }

    @Override
    public void callIn() {
        mChangeButton.setVisibility(View.GONE);
        mHangupButton.setVisibility(View.VISIBLE);
        mAcceptButton.setVisibility(View.VISIBLE);
        mRecordButton.setVisibility(View.GONE);

        ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) mChangeButton.getLayoutParams();
        layoutParams.leftToLeft = -1;
        layoutParams.rightToRight = -1;
        layoutParams.rightToLeft = -1;
        layoutParams.leftToRight = -1;

        layoutParams = (ConstraintLayout.LayoutParams) mHangupButton.getLayoutParams();
        layoutParams.leftToLeft = ConstraintLayout.LayoutParams.PARENT_ID;
        layoutParams.rightToRight = -1;
        layoutParams.rightToLeft = R.id.accept;
        layoutParams.leftToRight = -1;

        layoutParams = (ConstraintLayout.LayoutParams) mAcceptButton.getLayoutParams();
        layoutParams.leftToLeft = -1;
        layoutParams.rightToRight = ConstraintLayout.LayoutParams.PARENT_ID;
        layoutParams.rightToLeft = -1;
        layoutParams.leftToRight = R.id.hangUp;

        layoutParams = (ConstraintLayout.LayoutParams) mRecordButton.getLayoutParams();
        layoutParams.leftToLeft = -1;
        layoutParams.rightToRight = -1;
        layoutParams.rightToLeft = -1;
        layoutParams.leftToRight = -1;
    }

    @Override
    public void accept() {
        mChangeButton.setVisibility(View.VISIBLE);
        mHangupButton.setVisibility(View.VISIBLE);
        mAcceptButton.setVisibility(View.GONE);
        mRecordButton.setVisibility(View.VISIBLE);

        ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) mChangeButton.getLayoutParams();
        layoutParams.leftToLeft = ConstraintLayout.LayoutParams.PARENT_ID;
        layoutParams.rightToRight = -1;
        layoutParams.rightToLeft = R.id.hangUp;
        layoutParams.leftToRight = -1;

        layoutParams = (ConstraintLayout.LayoutParams) mHangupButton.getLayoutParams();
        layoutParams.leftToLeft = -1;
        layoutParams.rightToRight = -1;
        layoutParams.rightToLeft = R.id.record;
        layoutParams.leftToRight = R.id.change;

        layoutParams = (ConstraintLayout.LayoutParams) mAcceptButton.getLayoutParams();
        layoutParams.leftToLeft = -1;
        layoutParams.rightToRight = -1;
        layoutParams.rightToLeft = -1;
        layoutParams.leftToRight = -1;

        layoutParams = (ConstraintLayout.LayoutParams) mRecordButton.getLayoutParams();
        layoutParams.leftToLeft = -1;
        layoutParams.rightToRight = ConstraintLayout.LayoutParams.PARENT_ID;
        layoutParams.rightToLeft = -1;
        layoutParams.leftToRight = R.id.hangUp;
    }
}
