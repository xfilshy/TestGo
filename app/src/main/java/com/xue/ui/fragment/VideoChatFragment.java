package com.xue.ui.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.netease.nimlib.sdk.avchat.AVChatManager;
import com.netease.nimlib.sdk.avchat.constant.AVChatVideoScalingType;
import com.netease.nimlib.sdk.avchat.model.AVChatSurfaceViewRenderer;
import com.xue.R;
import com.xue.ui.activity.AVChatController;

/**
 * Created by xfilshy on 2018/1/18.
 */
@SuppressLint("ValidFragment")
public class VideoChatFragment extends BaseFragment implements View.OnClickListener {

    private FrameLayout largeFrameLayout;

    private FrameLayout smallFrameLayout;

    private AVChatSurfaceViewRenderer largeRender;

    private AVChatSurfaceViewRenderer smallRender;

    private AVChatController mAVChatController;

    private String mAction;

    public VideoChatFragment(AVChatController avChatController, String action) {
        this.mAVChatController = avChatController;
        this.mAction = action;
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
        initLargeSurface();
        init();
    }

    private void findView() {
        largeFrameLayout = getView().findViewById(R.id.large);
        smallFrameLayout = getView().findViewById(R.id.small);

        smallRender = new AVChatSurfaceViewRenderer(getActivity());
    }

    private void initLargeSurface() {
        if (largeRender == null) {
            largeRender = new AVChatSurfaceViewRenderer(getActivity());
            largeFrameLayout.addView(largeRender);
        }

        largeRender.setZOrderMediaOverlay(false);
    }

    private void initSmallSurface() {
        if (smallRender == null) {
            smallRender = new AVChatSurfaceViewRenderer(getActivity());
            smallFrameLayout.addView(smallRender);
        }

        smallRender.setZOrderMediaOverlay(true);
        smallFrameLayout.setVisibility(View.GONE);
    }

    private void init() {
        if (TextUtils.equals("call", mAction)) {
            doCall();
        } else if (TextUtils.equals("accept", mAction)) {
            doAccept();
        }
    }

    public void doCall() {
        largeFrameLayout.setVisibility(View.VISIBLE);
        mAVChatController.call();
        AVChatManager.getInstance().setupLocalVideoRender(largeRender, false, AVChatVideoScalingType.SCALE_ASPECT_BALANCED);
    }

    public void doAccept() {
        mAVChatController.accept();
    }

    @Override
    public void onClick(View view) {

    }
}
