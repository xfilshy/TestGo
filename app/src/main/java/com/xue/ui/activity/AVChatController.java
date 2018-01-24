package com.xue.ui.activity;

import android.content.Context;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.elianshang.tools.WeakReferenceHandler;
import com.netease.nimlib.sdk.Observer;
import com.netease.nimlib.sdk.auth.ClientType;
import com.netease.nimlib.sdk.avchat.AVChatCallback;
import com.netease.nimlib.sdk.avchat.AVChatManager;
import com.netease.nimlib.sdk.avchat.AVChatStateObserver;
import com.netease.nimlib.sdk.avchat.constant.AVChatChannelProfile;
import com.netease.nimlib.sdk.avchat.constant.AVChatEventType;
import com.netease.nimlib.sdk.avchat.constant.AVChatType;
import com.netease.nimlib.sdk.avchat.model.AVChatCalleeAckEvent;
import com.netease.nimlib.sdk.avchat.model.AVChatCameraCapturer;
import com.netease.nimlib.sdk.avchat.model.AVChatCommonEvent;
import com.netease.nimlib.sdk.avchat.model.AVChatControlEvent;
import com.netease.nimlib.sdk.avchat.model.AVChatData;
import com.netease.nimlib.sdk.avchat.model.AVChatNotifyOption;
import com.netease.nimlib.sdk.avchat.model.AVChatOnlineAckEvent;
import com.netease.nimlib.sdk.avchat.model.AVChatVideoCapturerFactory;
import com.xue.BaseApplication;
import com.xue.netease.AVChatConfigs;
import com.xue.netease.AVChatControlCommand;
import com.xue.netease.AVChatSoundPlayer;
import com.xue.netease.SimpleAVChatStateObserver;

/**
 * Created by xfilshy on 2018/1/18.
 */
public class AVChatController implements View.OnClickListener {

    private final int actionHangUp = 1;

    private WeakReferenceHandler<AVChatController> mHandler = new WeakReferenceHandler<AVChatController>(this) {
        @Override
        public void HandleMessage(AVChatController avChatController, Message msg) {
            if (msg.what == actionHangUp) {
                avChatController.hangUp();
            }
        }
    };

    private AVChatData mAVChatData;

    private AVChatCameraCapturer mAVChatVideoCapturer;

    private AVChatConfigs mAVChatConfigs;

    private AVChatControllerCallback mCallback;

    private AVChatControllerVideoModeCallback mVideoModeCallback;

    private boolean needRestoreLocalVideo = false;

    private boolean needRestoreLocalAudio = false;

    /**
     * 0 本地预览
     * 1 大小视窗，优先预览对方
     * 2 优先预览，优先预览自己
     */
    private int videoMode = 0;

    /**
     * 默认视频聊天模式
     */
    private int defaultVideoMode = 1;

    /**
     * 用户是否加入
     */
    private boolean isUserJoined = false;

    public AVChatController(Context context, AVChatData avChatData) {
        registerObserves(true);
        mAVChatConfigs = new AVChatConfigs(context);
        this.mAVChatData = avChatData;
    }

    public void setCallback(AVChatControllerCallback callback) {
        this.mCallback = callback;
    }

    public void setVideoModeCallback(AVChatControllerVideoModeCallback callback) {
        this.mVideoModeCallback = callback;
    }

    public AVChatData getAVChatData() {
        return mAVChatData;
    }

    public int getVideoMode() {
        return videoMode;
    }

    public boolean isUserJoined() {
        return isUserJoined;
    }

    //恢复视频和语音发送
    public void resumeVideo() {
        if (needRestoreLocalVideo) {
            AVChatManager.getInstance().muteLocalVideo(false);
            needRestoreLocalVideo = false;
        }

        if (needRestoreLocalAudio) {
            AVChatManager.getInstance().muteLocalAudio(false);
            needRestoreLocalAudio = false;
        }
    }

    //关闭视频和语音发送.
    public void pauseVideo() {
        if (!AVChatManager.getInstance().isLocalVideoMuted()) {
            AVChatManager.getInstance().muteLocalVideo(true);
            needRestoreLocalVideo = true;
        }

        if (!AVChatManager.getInstance().isLocalAudioMuted()) {
            AVChatManager.getInstance().muteLocalAudio(true);
            needRestoreLocalAudio = true;
        }
    }

    public void preview() {
        initAVCharManager();
        if (mCallback != null) {
            mCallback.preview();
        }
    }

    public void call() {
        AVChatSoundPlayer.instance().play(AVChatSoundPlayer.RingerTypeEnum.CONNECTING);
        AVChatManager.getInstance().call2(mAVChatData.getAccount(), mAVChatData.getChatType(), getAVChatNotifyOption(), mCallCallback);
        if (mCallback != null) {
            mCallback.call();
        }
    }

    public void callIn() {
        AVChatSoundPlayer.instance().play(AVChatSoundPlayer.RingerTypeEnum.RING);
        if (mCallback != null) {
            mCallback.callIn();
        }
    }

    public void accept() {
        AVChatManager.getInstance().accept2(mAVChatData.getChatId(), mAcceptCallback);
        if (mCallback != null) {
            mCallback.accept();
        }
    }

    public void hangUp() {
        Log.d("xue", "主动调用 hangUp");
        AVChatManager.getInstance().hangUp2(mAVChatData.getChatId(), mHangUpCallback);
        destroy();
        if (mCallback != null) {
            mCallback.hangup();
        }
    }

    public void busy() {
        AVChatManager.getInstance().sendControlCommand(mAVChatData.getChatId(), com.netease.nimlib.sdk.avchat.constant.AVChatControlCommand.BUSY, mHangUpCallback);
        destroy();
        if (mCallback != null) {
            mCallback.hangup();
        }
    }

    public void switchVideo2Audio() {
        Log.e("xue", "发起了切换请求");
        AVChatManager.getInstance().sendControlCommand(mAVChatData.getChatId(), AVChatControlCommand.SWITCH_VIDEO_TO_AUDIO, new AVChatCallback<Void>() {

            @Override
            public void onSuccess(Void aVoid) {
                Log.e("xue", "切换请求 成功送达");
            }

            @Override
            public void onFailed(int i) {
                Log.e("xue", "切换请求 成功失败");
            }

            @Override
            public void onException(Throwable throwable) {
                Log.e("xue", "切换请求 成功异常");
            }
        });
    }

    public void switchAudio2Video() {
        Log.e("xue", "发起了切换请求");
        AVChatManager.getInstance().sendControlCommand(mAVChatData.getChatId(), AVChatControlCommand.SWITCH_AUDIO_TO_VIDEO, new AVChatCallback<Void>() {

            @Override
            public void onSuccess(Void aVoid) {
                Log.e("xue", "切换请求 成功送达");
            }

            @Override
            public void onFailed(int i) {
                Log.e("xue", "切换请求 成功失败");
            }

            @Override
            public void onException(Throwable throwable) {
                Log.e("xue", "切换请求 成功异常");
            }
        });
    }

    public void rejectVideo2Audio() {
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

    public void agreetVideo2Audio() {
        AVChatManager.getInstance().sendControlCommand(AVChatManager.getInstance().getCurrentChatId(), AVChatControlCommand.SWITCH_VIDEO_TO_AUDIO_AGREE, new AVChatCallback<Void>() {

            @Override
            public void onSuccess(Void aVoid) {
                Log.e("xue", "发送  同意请求 成功");
                if (mCallback != null) {
                    mCallback.switchVideo2AudioAgree(true);
                }
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

    public void rejectAudio2Video() {
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

    public void agreeAudio2Video() {
        AVChatManager.getInstance().sendControlCommand(AVChatManager.getInstance().getCurrentChatId(), AVChatControlCommand.SWITCH_AUDIO_TO_VIDEO_AGREE, new AVChatCallback<Void>() {

            @Override
            public void onSuccess(Void aVoid) {
                Log.e("xue", "发送  同意请求 成功");
                if (mCallback != null) {
                    mCallback.switchAudio2VideoAgree(true);
                }
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


    public boolean switchMic() {
        boolean flag = !AVChatManager.getInstance().isMicrophoneMute();
        AVChatManager.getInstance().setMicrophoneMute(flag);
        return flag;
    }

    public boolean switchSpeaker() {
        boolean flag = !AVChatManager.getInstance().speakerEnabled();
        AVChatManager.getInstance().setSpeaker(!flag);
        return flag;
    }

    public void switchCamera() {
        if (mAVChatVideoCapturer != null) {
            mAVChatVideoCapturer.switchCamera();
        }
    }

    public void stopRing() {
        AVChatSoundPlayer.instance().stop();
    }

    private void initAVCharManager() {
        AVChatManager.getInstance().enableRtc();
        AVChatManager.getInstance().setChannelProfile(AVChatChannelProfile.CHANNEL_PROFILE_DEFAULT);
        AVChatManager.getInstance().setParameters(mAVChatConfigs.getAvChatParameters());
        AVChatManager.getInstance().setVideoQualityStrategy(true);
        if (mAVChatVideoCapturer == null) {
            mAVChatVideoCapturer = AVChatVideoCapturerFactory.createCameraCapturer();
            AVChatManager.getInstance().setupVideoCapturer(mAVChatVideoCapturer);
        }

        if (mAVChatData.getChatType() == AVChatType.VIDEO) {
            AVChatManager.getInstance().enableVideo();
            AVChatManager.getInstance().startVideoPreview();
            //开启预处理
//            AVChatManager.getInstance().setParameter(AVChatParameters.KEY_VIDEO_FRAME_FILTER, true);
        }
    }

    private void destroy() {
        AVChatSoundPlayer.instance().stop();
        if (mAVChatData.getChatType() == AVChatType.VIDEO) {
            AVChatManager.getInstance().stopVideoPreview();
            AVChatManager.getInstance().disableVideo();
        }
        AVChatManager.getInstance().disableRtc();
        registerObserves(false);
    }

    private AVChatNotifyOption getAVChatNotifyOption() {
        AVChatNotifyOption avChatNotifyOption = new AVChatNotifyOption();
        avChatNotifyOption.apnsContent = "你薛大爷";
        avChatNotifyOption.extendMessage = "快接电话啊";
        avChatNotifyOption.webRTCCompat = false;
        avChatNotifyOption.forceKeepCalling = true;

        return avChatNotifyOption;
    }

    private void registerObserves(boolean register) {
        AVChatManager.getInstance().observeAVChatState(mAVChatStateObserver, register);
        AVChatManager.getInstance().observeHangUpNotification(mCallHangupObserver, register);
        AVChatManager.getInstance().observeCalleeAckNotification(mCalleeAckNotification, register);
        AVChatManager.getInstance().observeControlNotification(mControlNotification, register);
        AVChatManager.getInstance().observeOnlineAckNotification(mOnlineAckNotification, register);
    }

    /**
     * 点击事件
     */
    @Override
    public void onClick(View view) {

    }

    /**
     * 监听打出
     */
    private AVChatCallback<AVChatData> mCallCallback = new AVChatCallback<AVChatData>() {
        @Override
        public void onSuccess(AVChatData avChatData) {
            Log.e("xue", "拨打通了");
            mAVChatData = avChatData;
            if (mCallback != null) {
                mCallback.callSuccess();
            }
        }

        @Override
        public void onFailed(int i) {
            Log.e("xue", "拨打失败 " + i);
        }

        @Override
        public void onException(Throwable throwable) {
            Log.e("xue", "拨打异常");
        }
    };

    /**
     * 监听接听
     */
    private AVChatCallback<Void> mAcceptCallback = new AVChatCallback<Void>() {

        @Override
        public void onSuccess(Void aVoid) {
            Log.e("xue", "接听成功");
            if (mCallback != null) {
                mCallback.acceptSuccess();
            }
        }

        @Override
        public void onFailed(int i) {
            Log.e("xue", "接听失败 " + i);
        }

        @Override
        public void onException(Throwable throwable) {
            Log.e("xue", "接听异常");
        }
    };

    /**
     * 监听挂断
     */
    private AVChatCallback<Void> mHangUpCallback = new AVChatCallback<Void>() {

        @Override
        public void onSuccess(Void aVoid) {
            Log.e("xue", "mHangUpCallback 挂断成功");
            if (mCallback != null) {
                mCallback.hangupSuccess();
            }
        }

        @Override
        public void onFailed(int i) {
            Log.e("xue", "挂断失败");
        }

        @Override
        public void onException(Throwable throwable) {
            Log.e("xue", "挂断异常");
        }
    };

    /**
     * 所有通话状态
     */
    private AVChatStateObserver mAVChatStateObserver = new SimpleAVChatStateObserver() {

        @Override
        public void onDeviceEvent(int i, String s) {
            super.onDeviceEvent(i, s);
            Log.e("xue", "onDeviceEvent == " + i + "  " + s);
        }

        @Override
        public void onFirstVideoFrameAvailable(String s) {
            super.onFirstVideoFrameAvailable(s);
            Log.e("xue", "onFirstVideoFrameRendered == " + s);
        }

        @Override
        public void onFirstVideoFrameRendered(String s) {
            super.onFirstVideoFrameRendered(s);
            Log.e("xue", "onFirstVideoFrameRendered == " + s);
            if (TextUtils.equals(BaseApplication.get().getUserId(), s)) {
                if (mCallback != null) {
                    mCallback.firstFrame();
                }
            }
        }

        @Override
        public void onUserJoined(String s) {
            super.onUserJoined(s);
            isUserJoined = true;

            AVChatSoundPlayer.instance().stop();
            Log.e("xue", "onUserJoined == " + s);
            if (mCallback != null) {
                mCallback.userJoined();
            }
            videoMode = defaultVideoMode;
            if (mVideoModeCallback != null) {
                mVideoModeCallback.change(videoMode);
            }
        }

        @Override
        public void onUserLeave(String s, int i) {
            super.onUserLeave(s, i);
            isUserJoined = false;

            Log.e("xue", "onUserLeave == " + s);
            if (mCallback != null) {
                mCallback.userLeave();
            }
        }

        @Override
        public void onDisconnectServer(int i) {
            super.onDisconnectServer(i);
            Log.e("xue", "onDisconnectServer == " + i);
        }
    };

    /**
     * 监听对方的挂断通知
     */
    private Observer<AVChatCommonEvent> mCallHangupObserver = new Observer<AVChatCommonEvent>() {

        @Override
        public void onEvent(AVChatCommonEvent avChatCommonEvent) {
            if (avChatCommonEvent.getEvent() == AVChatEventType.PEER_HANG_UP) {
                Log.d("xue", "对方挂断");
                hangUp();
            }
        }
    };

    /**
     * 呼叫发起后，监听对方的接听结果
     */
    private Observer<AVChatCalleeAckEvent> mCalleeAckNotification = new Observer<AVChatCalleeAckEvent>() {

        @Override
        public void onEvent(AVChatCalleeAckEvent avChatCalleeAckEvent) {
            if (avChatCalleeAckEvent.getEvent() == AVChatEventType.CALLEE_ACK_AGREE) {//同意接通
                Log.e("xue", "对方同意连接");
                if (mCallback != null) {
                    mCallback.acceptSuccess();
                }
            } else if (avChatCalleeAckEvent.getEvent() == AVChatEventType.CALLEE_ACK_REJECT) {//拒绝接听
                Log.e("xue", "对方拒绝");

                AVChatSoundPlayer.instance().play(AVChatSoundPlayer.RingerTypeEnum.PEER_REJECT);
                if (mCallback != null) {
                    mCallback.callReject();
                }
                mHandler.sendEmptyMessageDelayed(actionHangUp, 4200);
            } else if (avChatCalleeAckEvent.getEvent() == AVChatEventType.CALLEE_ACK_BUSY) {//接通忙
                Log.e("xue", "对方忙");

                AVChatSoundPlayer.instance().play(AVChatSoundPlayer.RingerTypeEnum.PEER_BUSY);
                if (mCallback != null) {
                    mCallback.callBusy();
                }
                mHandler.sendEmptyMessageDelayed(actionHangUp, 3200);
            }
        }
    };

    /**
     * 所有控制指令的监听接口
     */
    private Observer<AVChatControlEvent> mControlNotification = new Observer<AVChatControlEvent>() {

        @Override
        public void onEvent(AVChatControlEvent avChatControlEvent) {
            byte command = avChatControlEvent.getControlCommand();
            Log.e("xue", "command  == " + command);

            if (command == AVChatControlCommand.SWITCH_VIDEO_TO_AUDIO) {
                Log.e("xue", "接到 切换请求");
                if (mCallback != null) {
                    mCallback.switchVideo2Audio();
                }
            } else if (command == AVChatControlCommand.SWITCH_VIDEO_TO_AUDIO_AGREE) {
                Log.e("xue", "接到 切换同意请求");
                if (mCallback != null) {
                    mCallback.switchVideo2AudioAgree(false);
                }
            } else if (command == AVChatControlCommand.SWITCH_VIDEO_TO_AUDIO_REJECT) {
                Log.e("xue", "接到 切换拒绝请求");

                if (mCallback != null) {
                    mCallback.switchVideo2AudioReject(false);
                }
            } else if (command == AVChatControlCommand.SWITCH_AUDIO_TO_VIDEO) {
                Log.e("xue", "接到 切换请求");
                if (mCallback != null) {
                    mCallback.switchAudio2Video();
                }
            } else if (command == AVChatControlCommand.SWITCH_AUDIO_TO_VIDEO_AGREE) {
                Log.e("xue", "接到 切换同意请求");

                if (mCallback != null) {
                    mCallback.switchAudio2VideoAgree(false);
                }
            } else if (command == AVChatControlCommand.SWITCH_AUDIO_TO_VIDEO_REJECT) {
                Log.e("xue", "接到 切换拒绝请求");

                if (mCallback != null) {
                    mCallback.switchAudio2VideoReject(false);
                }
            }
        }
    };

    /**
     * 监听多端登陆时其他端处理
     */
    private Observer<AVChatOnlineAckEvent> mOnlineAckNotification = new Observer<AVChatOnlineAckEvent>() {

        @Override
        public void onEvent(AVChatOnlineAckEvent avChatOnlineAckEvent) {
            if (mAVChatData != null && mAVChatData.getChatId() == avChatOnlineAckEvent.getChatId()) {
                AVChatSoundPlayer.instance().stop();

                String client = null;
                switch (avChatOnlineAckEvent.getClientType()) {
                    case ClientType.Web:
                        client = "Web";
                        break;
                    case ClientType.Windows:
                        client = "Windows";
                        break;
                    case ClientType.Android:
                        client = "Android";
                        break;
                    case ClientType.iOS:
                        client = "iOS";
                        break;
                    case ClientType.MAC:
                        client = "Mac";
                        break;
                    default:
                        break;
                }

                //TODO 多端情况  需要处理
                if (client != null) {
                    String option = avChatOnlineAckEvent.getEvent() == AVChatEventType.CALLEE_ONLINE_CLIENT_ACK_AGREE ? "接听！" : "拒绝！";
//                    Toast.makeText(AVChatActivity.this, "通话已在" + client + "端被" + option, Toast.LENGTH_SHORT).show();
                }
//                finish();
        }
    };
}