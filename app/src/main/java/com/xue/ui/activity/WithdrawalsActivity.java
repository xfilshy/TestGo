package com.xue.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.xue.R;

public class WithdrawalsActivity extends SwipeBackBaseActivity {

    public static void launch(Context context) {
        Intent intent = new Intent(context, WithdrawalsActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_withdrawals);

        findView();
    }

    @Override
    protected boolean hasActionBar() {
        return true;
    }

    @Override
    protected String actionBarTitle() {
        return "提现";
    }

    @Override
    protected String actionBarRight() {
        return "提现规则";
    }

    private void findView() {
        setActionRightVisibility(View.VISIBLE);
    }

    public void bindZhifubao(View view) {
        BindZhifubaoActivity.launch(this);
    }
}
