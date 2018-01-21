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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.netease.nimlib.sdk.avchat.AVChatCallback;
import com.netease.nimlib.sdk.avchat.AVChatManager;
import com.netease.nimlib.sdk.avchat.constant.AVChatType;
import com.netease.nimlib.sdk.avchat.model.AVChatData;
import com.xue.R;
import com.xue.netease.AVChatControlCommand;
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

    private ImageView mForegroundImageView;

    private Button mChangeButton;

    private ImageView mHangupButton;

    private View mMiddleSpace;

    private ImageView mAcceptButton;

    private Button mRecordButton;

    private int flag = -1;

//    private AVChatControllerCallback mCallback;

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
        mChangeButton = findViewById(R.id.change);
        mAcceptButton = findViewById(R.id.accept);
        mMiddleSpace = findViewById(R.id.middleSpace);
        mHangupButton = findViewById(R.id.hangUp);
        mRecordButton = findViewById(R.id.record);
        mForegroundImageView = findViewById(R.id.foreground);

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
        if (mAudioChatFragment == null) {
            mAudioChatFragment = new AudioChatFragment(mAVChatController);
        }

        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.fragment_root, mAudioChatFragment);
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
        if (mChangeButton == view) {
            if (flag == 1) {
                mAVChatController.switchVideo2Audio();
            } else if (flag == 2) {
                mAVChatController.switchAudio2Video();
            }
        } else if (mHangupButton == view) {
            mAVChatController.hangUp();
        } else if (mAcceptButton == view) {
            mAVChatController.accept();
        } else if (mRecordButton == view) {
            AVChatManager.getInstance().muteLocalAudio(true);
        }
    }

    @Override
    public void preview() {
    }

    @Override
    public void call() {
        mChangeButton.setVisibility(View.GONE);
        mHangupButton.setVisibility(View.VISIBLE);
        mMiddleSpace.setVisibility(View.GONE);
        mAcceptButton.setVisibility(View.GONE);
        mRecordButton.setVisibility(View.GONE);
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
        mChangeButton.setVisibility(View.GONE);
        mHangupButton.setVisibility(View.VISIBLE);
        mMiddleSpace.setVisibility(View.VISIBLE);
        mAcceptButton.setVisibility(View.VISIBLE);
        mRecordButton.setVisibility(View.GONE);
    }

    @Override
    public void accept() {
    }

    @Override
    public void acceptSuccess() {
        mChangeButton.setVisibility(View.VISIBLE);
        mHangupButton.setVisibility(View.VISIBLE);
        mMiddleSpace.setVisibility(View.GONE);
        mAcceptButton.setVisibility(View.GONE);
        mRecordButton.setVisibility(View.VISIBLE);
    }

    @Override
    public void firstFrame() {
        mForegroundImageView.setVisibility(View.GONE);
    }

    @Override
    public void userJoined() {
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
                        Log.e("xue", "mAVChatData.getChatId() = " + mAVChatData.getChatId());
                        AVChatManager.getInstance().sendControlCommand(AVChatManager.getInstance().getCurrentChatId(), AVChatControlCommand.SWITCH_VIDEO_TO_AUDIO_REJECT, new AVChatCallback<Void>() {

                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.e("xue", "发送  拒绝请求 成功");
                            }

                            @Override
                            public void onFailed(int i) {
                                Log.e("xue", "发送  拒绝请求 失败 i = " + i);
                            }

                            @Override
                            public void onException(Throwable throwable) {
                                Log.e("xue", "发送  拒绝请求 异常");
                            }
                        });
                    }
                })
                .setPositiveButton("同意", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.e("xue", "发送  同意请求");
                        Log.e("xue", "mAVChatData.getChatId() = " + mAVChatData.getChatId());
                        AVChatManager.getInstance().sendControlCommand(AVChatManager.getInstance().getCurrentChatId(), AVChatControlCommand.SWITCH_VIDEO_TO_AUDIO_AGREE, new AVChatCallback<Void>() {

                            @Override
                            public void onSuccess(Void aVoid) {
                                //TODO 切换成语音 等待连接
                                Log.e("xue", "发送  同意请求 成功");

                                AVChatManager.getInstance().stopVideoPreview();
                                AVChatManager.getInstance().disableVideo();
                                goAudioFragment();
                            }

                            @Override
                            public void onFailed(int i) {
                                Log.e("xue", "发送  同意请求 失败 i = " + i);
                            }

                            @Override
                            public void onException(Throwable throwable) {
                                Log.e("xue", "发送  同意请求 异常");
                            }
                        });
                    }
                })
                .setCancelable(false)
                .create();
        alertDialog.show();
    }

    @Override
    public void switchVideo2AudioAgree() {
        Toast.makeText(this, "对方接受切换为语音聊天", Toast.LENGTH_SHORT).show();

        AVChatManager.getInstance().stopVideoPreview();
        AVChatManager.getInstance().disableVideo();
        goAudioFragment();
    }

    @Override
    public void switchVideo2AudioReject() {
        Toast.makeText(this, "对方拒绝切换为语音聊天", Toast.LENGTH_SHORT).show();
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
                        AVChatManager.getInstance().sendControlCommand(AVChatManager.getInstance().getCurrentChatId(), AVChatControlCommand.SWITCH_AUDIO_TO_VIDEO_REJECT, new AVChatCallback<Void>() {

                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.e("xue", "发送  拒绝请求 成功");
                            }

                            @Override
                            public void onFailed(int i) {
                                Log.e("xue", "发送  拒绝请求 失败 i = " + i);
                            }

                            @Override
                            public void onException(Throwable throwable) {
                                Log.e("xue", "发送  拒绝请求 异常");
                            }
                        });
                    }
                })
                .setPositiveButton("同意", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.e("xue", "发送  同意请求");
                        AVChatManager.getInstance().sendControlCommand(AVChatManager.getInstance().getCurrentChatId(), AVChatControlCommand.SWITCH_AUDIO_TO_VIDEO_AGREE, new AVChatCallback<Void>() {

                            @Override
                            public void onSuccess(Void aVoid) {
                                //TODO 切换成语音 等待连接
                                Log.e("xue", "发送  同意请求 成功");

                                AVChatManager.getInstance().enableVideo();
                                AVChatManager.getInstance().startVideoPreview();
                                goVideoFragment();
                            }

                            @Override
                            public void onFailed(int i) {
                                Log.e("xue", "发送  同意请求 失败 i = " + i);
                            }

                            @Override
                            public void onException(Throwable throwable) {
                                Log.e("xue", "发送  同意请求 异常");
                            }
                        });
                    }
                })
                .setCancelable(false)
                .create();
        alertDialog.show();
    }

    @Override
    public void switchAudio2VideoAgree() {
        Toast.makeText(this, "对方接受切换为视频聊天", Toast.LENGTH_SHORT).show();

        AVChatManager.getInstance().enableVideo();
        AVChatManager.getInstance().startVideoPreview();
        goVideoFragment();
    }

    @Override
    public void switchAudio2VideoReject() {
        Toast.makeText(this, "对方拒绝切换为视频聊天", Toast.LENGTH_SHORT).show();
    }
}
