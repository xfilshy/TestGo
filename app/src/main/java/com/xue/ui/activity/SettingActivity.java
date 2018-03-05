package com.xue.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.xue.R;

public class SettingActivity extends SwipeBackBaseActivity {

    public static void launch(Context context) {
        Intent intent = new Intent(context, SettingActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_setting);
    }

    @Override
    protected boolean hasActionBar() {
        return true;
    }

    @Override
    protected String actionBarTitle() {
        return "设置";
    }

    @Override
    protected String actionBarRight() {
        return null;
    }

    public void goBaseInfo(View view) {
        BaseInfoActivity.launch(this);
    }

    public void goAgreement(View view) {
        AgreementActivity.launch(this);
    }
}
