package com.xue.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.xue.BaseApplication;
import com.xue.R;
import com.xue.bean.UserExpertInfo;

public class CertifyActivity extends SwipeBackBaseActivity {

    public static void launch(Context context) {
        Intent intent = new Intent(context, CertifyActivity.class);
        context.startActivity(intent);
    }

    private TextView mWorkCardAuthTextView;

    private TextView mBusinessCardAuthTextView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_certify);

        findView();
        init();
    }

    @Override
    protected boolean hasActionBar() {
        return true;
    }

    @Override
    protected String actionBarTitle() {
        return "申请认证";
    }

    @Override
    protected String actionBarRight() {
        return null;
    }

    private void findView() {
        mWorkCardAuthTextView = findViewById(R.id.workCardAuth);
        mBusinessCardAuthTextView = findViewById(R.id.businessCardAuth);
    }

    private void init() {
        UserExpertInfo userExpertInfo = BaseApplication.get().getUser().getUserExpertInfo();
        if (userExpertInfo != null) {
            if (TextUtils.equals("0", userExpertInfo.getBusinessCardAuth())) {
                mBusinessCardAuthTextView.setText("未认证");
            } else if (TextUtils.equals("1", userExpertInfo.getBusinessCardAuth())) {
                mBusinessCardAuthTextView.setText("认证中");
            } else if (TextUtils.equals("2", userExpertInfo.getBusinessCardAuth())) {
                mBusinessCardAuthTextView.setText("认证通过");
            } else if (TextUtils.equals("3", userExpertInfo.getBusinessCardAuth())) {
                mBusinessCardAuthTextView.setText("认证失败");
            }

            if (TextUtils.equals("0", userExpertInfo.getWorkCardAuth())) {
                mWorkCardAuthTextView.setText("未认证");
            } else if (TextUtils.equals("1", userExpertInfo.getWorkCardAuth())) {
                mWorkCardAuthTextView.setText("认证中");
            } else if (TextUtils.equals("2", userExpertInfo.getWorkCardAuth())) {
                mWorkCardAuthTextView.setText("认证通过");
            } else if (TextUtils.equals("3", userExpertInfo.getWorkCardAuth())) {
                mWorkCardAuthTextView.setText("认证失败");
            }
        }
    }

    public void goBusinessCardCertify(View view) {
        BusinessCardCertifyActivity.launch(this);
    }

    public void goWorkCardCertify(View view) {
        WorkCardCertifyActivity.launch(this);
    }
}
