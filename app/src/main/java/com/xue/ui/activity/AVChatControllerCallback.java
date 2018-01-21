package com.xue.ui.activity;

/**
 * Created by xfilshy on 2018/1/18.
 */

public interface AVChatControllerCallback {

    void preview();

    void call();

    void callSuccess();

    void callReject();

    void callBusy();

    void callIn();

    void accept();

    void acceptSuccess();

    void firstFrame();

    void userJoined();

    void userLeave();

    void hangup();

    void hangupSuccess();

    void switchVideo2Audio();

    void switchVideo2AudioAgree();

    void switchVideo2AudioReject();

    void switchAudio2Video();

    void switchAudio2VideoAgree();

    void switchAudio2VideoReject();
}
