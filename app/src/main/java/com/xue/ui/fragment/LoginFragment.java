package com.xue.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.elianshang.tools.WeakReferenceHandler;
import com.netease.nimlib.sdk.NIMSDK;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.auth.LoginInfo;
import com.xue.BaseApplication;
import com.xue.R;
import com.xue.bean.UserBase;
import com.xue.ui.activity.MainActivity;

public class LoginFragment extends BaseFragment implements RequestCallback<LoginInfo> {

    private WeakReferenceHandler<LoginFragment> mHandler = new WeakReferenceHandler<LoginFragment>(this) {
        @Override
        public void HandleMessage(LoginFragment loginFragment, Message msg) {
            super.HandleMessage(loginFragment, msg);
            if (msg.what == 1) {
                loginFragment.getActivity().setResult(AppCompatActivity.RESULT_OK);
                MainActivity.launch(loginFragment.getActivity());
                loginFragment.getActivity().finish();
            }
        }
    };

    private UserBase mUserBase;

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
            UserBase userBase = (UserBase) data.getSerializableExtra("userBase");
            this.mUserBase = userBase;
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
        LoginInfo info = new LoginInfo(mUserBase.getId(), mUserBase.getToken());
        NIMSDK.getAuthService().login(info).setCallback(this);
    }

    @Override
    public void onSuccess(LoginInfo loginInfo) {
        Log.e("xue", "授权成功");
        BaseApplication.get().setUser(mUserBase);
        mHandler.sendEmptyMessage(1);
    }

    @Override
    public void onFailed(int i) {
        Log.e("xue", "授权失败");
    }

    @Override
    public void onException(Throwable throwable) {
        Log.e("xue", "授权异常");
    }
}