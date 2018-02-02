package com.xue.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.elianshang.tools.ToastTool;
import com.elianshang.tools.WeakReferenceHandler;
import com.netease.nimlib.sdk.NIMSDK;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.auth.LoginInfo;
import com.xue.BaseApplication;
import com.xue.R;
import com.xue.ui.activity.MainActivity;

public class LoginFragment extends BaseFragment implements RequestCallback<LoginInfo> {

    private WeakReferenceHandler<LoginFragment> mHandler = new WeakReferenceHandler<LoginFragment>(this) {
        @Override
        public void HandleMessage(LoginFragment loginFragment, Message msg) {
            super.HandleMessage(loginFragment, msg);
            if (msg.what == 1) {
                MainActivity.launch(loginFragment.getActivity());
                loginFragment.getActivity().finish();
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == AppCompatActivity.RESULT_OK) {
            doLogin();
        }
    }

    @Override
    public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {
        if (enter) {
            return AnimationUtils.loadAnimation(getActivity(), R.anim.slide_in);
        } else {
            return AnimationUtils.loadAnimation(getActivity(), R.anim.slide_out);
        }
    }

    public void doLogin() {
        LoginInfo info = new LoginInfo(BaseApplication.get().getUserId(), BaseApplication.get().getNeteaseToken());
        NIMSDK.getAuthService().login(info).setCallback(this);
    }

    @Override
    public void onSuccess(LoginInfo loginInfo) {
        ToastTool.show(getActivity(), "授权成功");
        mHandler.sendEmptyMessage(1);
    }

    @Override
    public void onFailed(int i) {
        ToastTool.show(getActivity(), "授权失败，请尝试重新登录");
        BaseApplication.get().setUser(null , true);
    }

    @Override
    public void onException(Throwable throwable) {
        ToastTool.show(getActivity(), "授权异常，请尝试重新登录");
        BaseApplication.get().setUser(null , true);
    }
}