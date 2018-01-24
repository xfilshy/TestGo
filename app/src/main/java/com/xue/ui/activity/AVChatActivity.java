package com.xue.ui.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.netease.nimlib.sdk.avchat.AVChatManager;
import com.netease.nimlib.sdk.avchat.constant.AVChatType;
import com.netease.nimlib.sdk.avchat.model.AVChatData;
import com.xue.R;
import com.xue.imagecache.ImageCacheMannager;
import com.xue.netease.SimpleAVChatData;
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

    public static void launchAccept(Context context, AVChatData avChatData) {
        Intent intent = new Intent(context, AVChatActivity.class);
        intent.putExtra("avChatData", avChatData);
        intent.putExtra("action", "accept");
        context.startActivity(intent);
    }

    private AVChatData mAVChatData;

    private String mAction;

    private AVChatController mAVChatController;

    private VideoChatFragment mVideoChatFragment;

    private ImageView mForegroundImageView;

    private LinearLayout mAccountLargeLayout;

    private LinearLayout mAccountSmallLayout;

    private ImageView mCutImageView;

    private ImageView mChangeImageView;

    private ImageView mMicImageView;

    private ImageView mRingImageView;

    private ImageView mVolumeImageView;

    private ImageView mHangupButton;

    private ImageView mAcceptButton;

    private TextView mTimeTextView;

    /**
     * 当前页面标记  1 视频通话 ， 2 音频通话
     */
    private int flag = -1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dismissKeyguard();
        setContentView(R.layout.activity_avchat);
        readExtra(getIntent());
        findView();
        init();
        start();
    }

    // 设置窗口flag，亮屏并且解锁/覆盖在锁屏界面上
    private void dismissKeyguard() {
        getWindow().addFlags(
                WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
                        WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD |
                        WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON |
                        WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON
        );
    }

    @Override
    protected void onResume() {
        super.onResume();
        mAVChatController.resumeVideo();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mAVChatController.pauseVideo();
    }

    private void init() {
        mAVChatController = new AVChatController(this, mAVChatData);
        mAVChatController.setCallback(this);

        mAVChatController.preview();
        if (TextUtils.equals("call", mAction)) {
            mAVChatController.call();
        } else if (TextUtils.equals("accept", mAction)) {
            mAVChatController.callIn();
        }
    }

    private void readExtra(Intent intent) {
        mAVChatData = (AVChatData) intent.getSerializableExtra("avChatData");
        mAction = intent.getStringExtra("action");
    }

    private void findView() {
        mCutImageView = findViewById(R.id.cut);
        mChangeImageView = findViewById(R.id.change);
        mRingImageView = findViewById(R.id.ring);
        mMicImageView = findViewById(R.id.mic);
        mVolumeImageView = findViewById(R.id.volume);
        mAcceptButton = findViewById(R.id.accept);
        mHangupButton = findViewById(R.id.hangUp);
        mTimeTextView = findViewById(R.id.time);
        mForegroundImageView = findViewById(R.id.foreground);
        mAccountLargeLayout = findViewById(R.id.accountLargeLayout);
        mAccountSmallLayout = findViewById(R.id.accountSmallLayout);

        mChangeImageView.setOnClickListener(this);
        mRingImageView.setOnClickListener(this);
        mMicImageView.setOnClickListener(this);
        mVolumeImageView.setOnClickListener(this);
        mCutImageView.setOnClickListener(this);
        mAcceptButton.setOnClickListener(this);
        mHangupButton.setOnClickListener(this);
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
            mVideoChatFragment = new VideoChatFragment(mAVChatController);
        }

        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.fragment_root, mVideoChatFragment);
        transaction.commit();

        flag = 1;
    }

    /**
     * 去音频聊天页
     */
    private void goAudioFragment() {
        mForegroundImageView.setVisibility(View.VISIBLE);
        mAccountLargeLayout.setVisibility(View.VISIBLE);
        mAccountSmallLayout.setVisibility(View.GONE);
        mCutImageView.setVisibility(View.GONE);

        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.remove(mVideoChatFragment);
        transaction.commit();

        flag = 2;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
    }

    @Override
    public void onClick(View view) {
        if (mChangeImageView == view) {
            if (flag == 1) {
                mAVChatController.switchVideo2Audio();
            } else if (flag == 2) {
                mAVChatController.switchAudio2Video();
            }
        } else if (mMicImageView == view) {
            if (mAVChatController.switchMic()) {
                ImageCacheMannager.loadImage(this, R.drawable.icon_mic_off, mMicImageView);
            } else {
                ImageCacheMannager.loadImage(this, R.drawable.icon_mic_up, mMicImageView);
            }
        } else if (mRingImageView == view) {
            mAVChatController.stopRing();
            ImageCacheMannager.loadImage(this, R.drawable.icon_ring_off, mRingImageView);
            mRingImageView.setClickable(false);
        } else if (mVolumeImageView == view) {
            if (mAVChatController.switchSpeaker()) {
                ImageCacheMannager.loadImage(this, R.drawable.icon_volume_off, mVolumeImageView);
            } else {
                ImageCacheMannager.loadImage(this, R.drawable.icon_volume_up, mVolumeImageView);
            }
        } else if (mCutImageView == view) {
            mAVChatController.switchCamera();
        } else if (mHangupButton == view) {
//            if (!mAVChatController.isUserJoined() && TextUtils.equals("accept", mAction)) {
//                mAVChatController.busy();
//            } else {
                mAVChatController.hangUp();
//            }
        } else if (mAcceptButton == view) {
            mAVChatController.accept();
        }
    }

    @Override
    public void preview() {
    }

    @Override
    public void call() {
        mCutImageView.setVisibility(View.GONE);
        mChangeImageView.setVisibility(View.GONE);
        mRingImageView.setVisibility(View.VISIBLE);
        mMicImageView.setVisibility(View.GONE);
        mVolumeImageView.setVisibility(View.GONE);
        mHangupButton.setVisibility(View.VISIBLE);
        mAcceptButton.setVisibility(View.GONE);
        mTimeTextView.setVisibility(View.GONE);
    }

    @Override
    public void callSuccess() {
    }

    @Override
    public void callReject() {
    }

    @Override
    public void callBusy() {
    }

    @Override
    public void callIn() {
        mCutImageView.setVisibility(View.GONE);
        mChangeImageView.setVisibility(View.GONE);
        mRingImageView.setVisibility(View.VISIBLE);
        mMicImageView.setVisibility(View.GONE);
        mVolumeImageView.setVisibility(View.GONE);
        mHangupButton.setVisibility(View.VISIBLE);
        mAcceptButton.setVisibility(View.VISIBLE);
        mTimeTextView.setVisibility(View.GONE);
    }

    @Override
    public void accept() {
    }

    @Override
    public void acceptSuccess() {
        mCutImageView.setVisibility(View.VISIBLE);
        mChangeImageView.setVisibility(View.VISIBLE);
        mRingImageView.setVisibility(View.GONE);
        mMicImageView.setVisibility(View.VISIBLE);
        mVolumeImageView.setVisibility(View.VISIBLE);
        mHangupButton.setVisibility(View.VISIBLE);
        mAcceptButton.setVisibility(View.GONE);
        mTimeTextView.setVisibility(View.VISIBLE);
    }

    @Override
    public void firstFrame() {
        mForegroundImageView.setVisibility(View.GONE);
        if (!mAVChatController.isUserJoined() && TextUtils.equals("call", mAction)) {
            mAccountSmallLayout.setVisibility(View.VISIBLE);
            mAccountLargeLayout.setVisibility(View.GONE);
        } else {
            mAccountSmallLayout.setVisibility(View.GONE);
            mAccountLargeLayout.setVisibility(View.GONE);
        }
    }

    @Override
    public void userJoined() {
        if (flag == 1) {
            mCutImageView.setVisibility(View.VISIBLE);
            mChangeImageView.setVisibility(View.VISIBLE);
            mRingImageView.setVisibility(View.GONE);
            mMicImageView.setVisibility(View.VISIBLE);
            mVolumeImageView.setVisibility(View.VISIBLE);
            mHangupButton.setVisibility(View.VISIBLE);
            mAcceptButton.setVisibility(View.GONE);
            mTimeTextView.setVisibility(View.VISIBLE);
            mAccountLargeLayout.setVisibility(View.GONE);
            mAccountSmallLayout.setVisibility(View.GONE);
        } else if (flag == 2) {
            mCutImageView.setVisibility(View.GONE);
            mChangeImageView.setVisibility(View.VISIBLE);
            mRingImageView.setVisibility(View.GONE);
            mMicImageView.setVisibility(View.VISIBLE);
            mVolumeImageView.setVisibility(View.VISIBLE);
            mHangupButton.setVisibility(View.VISIBLE);
            mAcceptButton.setVisibility(View.GONE);
            mTimeTextView.setVisibility(View.VISIBLE);
            mAccountLargeLayout.setVisibility(View.VISIBLE);
            mAccountSmallLayout.setVisibility(View.GONE);
        }
    }

    @Override
    public void userLeave() {
    }

    @Override
    public void hangup() {
        finish();
    }

    @Override
    public void hangupSuccess() {
    }

    @Override
    public void switchVideo2Audio() {
        AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setTitle("询问")
                .setMessage("对方请求将视频通话切换为语音通话")
                .setNegativeButton("拒绝", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.e("xue", "发送  拒绝请求");
                        mAVChatController.rejectVideo2Audio();
                    }
                })
                .setPositiveButton("同意", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.e("xue", "发送  同意请求");
                        mAVChatController.agreetVideo2Audio();
                    }
                })
                .setCancelable(false)
                .create();
        alertDialog.show();
    }

    @Override
    public void switchVideo2AudioAgree(boolean self) {
        if (!self) {
            Toast.makeText(this, "对方接受切换为语音聊天", Toast.LENGTH_SHORT).show();
        }

        goAudioFragment();
        AVChatManager.getInstance().stopVideoPreview();
        AVChatManager.getInstance().disableVideo();
    }

    @Override
    public void switchVideo2AudioReject(boolean self) {
        if (!self) {
            Toast.makeText(this, "对方拒绝切换为语音聊天", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void switchAudio2Video() {
        AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setTitle("询问")
                .setMessage("对方请求将视频通话切换为语音通话")
                .setNegativeButton("拒绝", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.e("xue", "发送  拒绝请求");
                        mAVChatController.rejectAudio2Video();
                    }
                })
                .setPositiveButton("同意", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.e("xue", "发送  同意请求");
                        mAVChatController.agreeAudio2Video();
                    }
                })
                .setCancelable(false)
                .create();
        alertDialog.show();
    }

    @Override
    public void switchAudio2VideoAgree(boolean self) {
        if (!self) {
            Toast.makeText(this, "对方接受切换为视频聊天", Toast.LENGTH_SHORT).show();
        }

        goVideoFragment();
        AVChatManager.getInstance().enableVideo();
        AVChatManager.getInstance().startVideoPreview();
    }

    @Override
    public void switchAudio2VideoReject(boolean self) {
        if (!self) {
            Toast.makeText(this, "对方拒绝切换为视频聊天", Toast.LENGTH_SHORT).show();
        }
    }
}
