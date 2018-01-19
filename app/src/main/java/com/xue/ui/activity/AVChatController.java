package com.xue.ui.activity;

import android.content.Context;
import android.util.Log;
import android.view.View;

import com.netease.nimlib.sdk.Observer;
import com.netease.nimlib.sdk.avchat.AVChatCallback;
import com.netease.nimlib.sdk.avchat.AVChatManager;
import com.netease.nimlib.sdk.avchat.AVChatStateObserver;
import com.netease.nimlib.sdk.avchat.constant.AVChatChannelProfile;
import com.netease.nimlib.sdk.avchat.constant.AVChatEventType;
import com.netease.nimlib.sdk.avchat.constant.AVChatType;
import com.netease.nimlib.sdk.avchat.model.AVChatCalleeAckEvent;
import com.netease.nimlib.sdk.avchat.model.AVChatCommonEvent;
import com.netease.nimlib.sdk.avchat.model.AVChatControlEvent;
import com.netease.nimlib.sdk.avchat.model.AVChatData;
import com.netease.nimlib.sdk.avchat.model.AVChatNotifyOption;
import com.netease.nimlib.sdk.avchat.model.AVChatOnlineAckEvent;
import com.netease.nimlib.sdk.avchat.model.AVChatVideoCapturer;
import com.netease.nimlib.sdk.avchat.model.AVChatVideoCapturerFactory;
import com.xue.netease.AVChatConfigs;
import com.xue.netease.SimpleAVChatStateObserver;

/**
 * Created by xfilshy on 2018/1/18.
 */
public class AVChatController implements View.OnClickListener {

    private AVChatData mAVChatData;

    private AVChatVideoCapturer mAVChatVideoCapturer;

    private AVChatConfigs mAVChatConfigs;

    private AVChatControllerCallback mCallback;

    public AVChatController(Context context, AVChatData avChatData) {
        registerObserves(true);
        mAVChatConfigs = new AVChatConfigs(context);
        this.mAVChatData = avChatData;
    }

    public void setCallback(AVChatControllerCallback callback) {
        this.mCallback = callback;
    }

    public void call() {
        initAVCharManager();
        AVChatManager.getInstance().call2(mAVChatData.getAccount(), mAVChatData.getChatType(), getAVChatNotifyOption(), mCallCallback);
        if (mCallback != null) {
            mCallback.calling();
        }
    }

    public void accept() {
        initAVCharManager();
        AVChatManager.getInstance().accept2(mAVChatData.getChatId(), mAcceptCallback);
        if (mCallback != null) {
            mCallback.callIn();
        }
    }

    public void hangUp() {
        AVChatManager.getInstance().hangUp2(mAVChatData.getChatId(), mHangUpCallback);
        destroy();
    }

    private void initAVCharManager() {
        AVChatManager.getInstance().enableRtc();
        AVChatManager.getInstance().setChannelProfile(AVChatChannelProfile.CHANNEL_PROFILE_DEFAULT);
        AVChatManager.getInstance().setParameters(mAVChatConfigs.getAvChatParameters());
        if (mAVChatData.getChatType() == AVChatType.VIDEO) {
            AVChatManager.getInstance().enableVideo();
            if (mAVChatVideoCapturer == null) {
                mAVChatVideoCapturer = AVChatVideoCapturerFactory.createCameraCapturer();
                AVChatManager.getInstance().setupVideoCapturer(mAVChatVideoCapturer);
            }
            AVChatManager.getInstance().setVideoQualityStrategy(true);
            AVChatManager.getInstance().startVideoPreview();
            //开启预处理
//            AVChatManager.getInstance().setParameter(AVChatParameters.KEY_VIDEO_FRAME_FILTER, true);
        }
    }

    private void destroy() {
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
                mCallback.calling();
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
                mCallback.calling();
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
            Log.e("xue", "挂断成功");
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
        public void onFirstVideoFrameRendered(String s) {
            super.onFirstVideoFrameRendered(s);
            Log.e("xue", "onFirstVideoFrameRendered  == " + s);
        }
    };

    /**
     * 监听对方的挂断通知
     */
    private Observer<AVChatCommonEvent> mCallHangupObserver = new Observer<AVChatCommonEvent>() {

        @Override
        public void onEvent(AVChatCommonEvent avChatCommonEvent) {
            if (avChatCommonEvent.getEvent() == AVChatEventType.PEER_HANG_UP) {

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

            } else if (avChatCalleeAckEvent.getEvent() == AVChatEventType.CALLEE_ACK_REJECT) {//拒绝接听

            } else if (avChatCalleeAckEvent.getEvent() == AVChatEventType.CALLEE_ACK_BUSY) {//接通忙

            }
        }
    };

    /**
     * 所有控制指令的监听接口
     */
    private Observer<AVChatControlEvent> mControlNotification = new Observer<AVChatControlEvent>() {

        @Override
        public void onEvent(AVChatControlEvent avChatControlEvent) {

        }
    };

    /**
     * 监听多端登陆时其他端处理
     */
    private Observer<AVChatOnlineAckEvent> mOnlineAckNotification = new Observer<AVChatOnlineAckEvent>() {

        @Override
        public void onEvent(AVChatOnlineAckEvent avChatOnlineAckEvent) {

        }
    };
}