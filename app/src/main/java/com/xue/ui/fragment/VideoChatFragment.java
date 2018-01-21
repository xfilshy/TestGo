package com.xue.ui.fragment;

import android.annotation.SuppressLint;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.netease.nimlib.sdk.avchat.AVChatManager;
import com.netease.nimlib.sdk.avchat.constant.AVChatVideoScalingType;
import com.netease.nimlib.sdk.avchat.model.AVChatSurfaceViewRenderer;
import com.xue.R;
import com.xue.ui.activity.AVChatController;
import com.xue.ui.activity.AVChatControllerVideoModeCallback;

/**
 * Created by xfilshy on 2018/1/18.
 */
@SuppressLint("ValidFragment")
public class VideoChatFragment extends BaseFragment implements AVChatControllerVideoModeCallback {

    private FrameLayout largeFrameLayout;

    private FrameLayout smallFrameLayout;

    private AVChatSurfaceViewRenderer largeRender;

    private AVChatSurfaceViewRenderer smallRender;

    private AVChatController mAVChatController;

    public VideoChatFragment(AVChatController avChatController) {
        this.mAVChatController = avChatController;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_video_chat, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        findView();
        init();

        switchMode(mAVChatController.getVideoMode());
        mAVChatController.setVideoModeCallback(this);
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        AVChatManager.getInstance().setupLocalVideoRender(null, false, AVChatVideoScalingType.SCALE_ASPECT_BALANCED);
        AVChatManager.getInstance().setupRemoteVideoRender(null, null, false, AVChatVideoScalingType.SCALE_ASPECT_BALANCED);
        mAVChatController.setVideoModeCallback(null);
    }

    private void findView() {
        largeFrameLayout = getView().findViewById(R.id.large);
        smallFrameLayout = getView().findViewById(R.id.small);
    }

    private void init() {
        initLargeSurface();
        initSmallSurface();
    }

    private void initLargeSurface() {
        largeRender = new AVChatSurfaceViewRenderer(getActivity());
        largeRender.setZOrderMediaOverlay(false);
        largeFrameLayout.addView(largeRender);
    }

    private void initSmallSurface() {
        smallRender = new AVChatSurfaceViewRenderer(getActivity());
        smallRender.setZOrderMediaOverlay(true);
        smallRender.getHolder().setFormat(PixelFormat.TRANSPARENT);
        smallFrameLayout.addView(smallRender);
    }

    public void switchMode(int videoMode) {
        if (videoMode == 0) {
            locationMode();
        } else if (videoMode == 1) {
            acceptedMode();
        } else if (videoMode == 2) {
            acceptedMode4me();
        }
    }

    private void locationMode() {
        AVChatManager.getInstance().setupLocalVideoRender(null, false, AVChatVideoScalingType.SCALE_ASPECT_BALANCED);
        AVChatManager.getInstance().setupRemoteVideoRender(null, null, false, AVChatVideoScalingType.SCALE_ASPECT_BALANCED);

        largeFrameLayout.setVisibility(View.VISIBLE);
        smallFrameLayout.setVisibility(View.GONE);
        AVChatManager.getInstance().setupLocalVideoRender(largeRender, false, AVChatVideoScalingType.SCALE_ASPECT_BALANCED);
    }

    private void acceptedMode() {
        AVChatManager.getInstance().setupLocalVideoRender(null, false, AVChatVideoScalingType.SCALE_ASPECT_BALANCED);
        AVChatManager.getInstance().setupRemoteVideoRender(null, null, false, AVChatVideoScalingType.SCALE_ASPECT_BALANCED);

        largeFrameLayout.setVisibility(View.VISIBLE);
        smallFrameLayout.setVisibility(View.VISIBLE);
        AVChatManager.getInstance().setupLocalVideoRender(smallRender, false, AVChatVideoScalingType.SCALE_ASPECT_BALANCED);
        AVChatManager.getInstance().setupRemoteVideoRender(mAVChatController.getAVChatData().getAccount(), largeRender, false, AVChatVideoScalingType.SCALE_ASPECT_BALANCED);
    }

    private void acceptedMode4me() {
        AVChatManager.getInstance().setupLocalVideoRender(null, false, AVChatVideoScalingType.SCALE_ASPECT_BALANCED);
        AVChatManager.getInstance().setupRemoteVideoRender(null, null, false, AVChatVideoScalingType.SCALE_ASPECT_BALANCED);

        largeFrameLayout.setVisibility(View.VISIBLE);
        smallFrameLayout.setVisibility(View.VISIBLE);
        AVChatManager.getInstance().setupLocalVideoRender(largeRender, false, AVChatVideoScalingType.SCALE_ASPECT_BALANCED);
        AVChatManager.getInstance().setupRemoteVideoRender(mAVChatController.getAVChatData().getAccount(), smallRender, false, AVChatVideoScalingType.SCALE_ASPECT_BALANCED);
    }

    @Override
    public void change(int videoMode) {
        switchMode(videoMode);
    }
}
