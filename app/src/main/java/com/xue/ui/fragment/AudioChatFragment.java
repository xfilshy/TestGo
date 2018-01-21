package com.xue.ui.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xue.R;
import com.xue.ui.activity.AVChatController;

/**
 * Created by xfilshy on 2018/1/18.
 */
@SuppressLint("ValidFragment")
public class AudioChatFragment extends BaseFragment {

    private AVChatController mAVChatController;

    private String mAction;

    public AudioChatFragment(AVChatController avChatController) {
        this.mAVChatController = avChatController;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_audio_chat, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
//        init();
    }

    private void init() {
        if (TextUtils.equals("call", mAction)) {
            mAVChatController.preview();
            mAVChatController.call();
        } else if (TextUtils.equals("accept", mAction)) {
            mAVChatController.preview();
            mAVChatController.callIn();
        }
    }
}