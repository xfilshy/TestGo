package com.xue.ui.fragment;

import android.annotation.SuppressLint;

import com.xue.ui.activity.AVChatController;

/**
 * Created by xfilshy on 2018/1/18.
 */

@SuppressLint("ValidFragment")
public class AudioChatFragment extends BaseFragment {

    private AVChatController mAudioChatFragment;

    public AudioChatFragment(AVChatController avChatController, String action) {
        this.mAudioChatFragment = avChatController;
    }
}