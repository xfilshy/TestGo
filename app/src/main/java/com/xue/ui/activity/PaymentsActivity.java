package com.xue.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.xue.R;

public class PaymentsActivity extends BaseActivity implements View.OnClickListener {

    public static void launsh(Context context) {
        Intent intent = new Intent(context, PaymentsActivity.class);
        context.startActivity(intent);
    }

    private ImageView mBackImageView;

    private TextView mHistoryTextView;

    private TextView mRechargeTextView;

    private TextView mWithdrawalsTextView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payments);

        initActionBar();
        findView();
    }

    private void initActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM); //Enable自定义的View
            actionBar.setCustomView(R.layout.actionbar_payments);//设置自定义的布局：actionbar_custom
            mBackImageView = actionBar.getCustomView().findViewById(R.id.back);
            mHistoryTextView = actionBar.getCustomView().findViewById(R.id.history);
            mBackImageView.setOnClickListener(this);
            mHistoryTextView.setOnClickListener(this);
        }
    }

    private void findView() {
        mRechargeTextView = findViewById(R.id.recharge);
        mWithdrawalsTextView = findViewById(R.id.withdrawals);

        mRechargeTextView.setOnClickListener(this);
        mWithdrawalsTextView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (mBackImageView == v) {
            finish();
        } else if (mHistoryTextView == v) {
            PaymentsHistoryActivity.launch(this);
        } else if (mRechargeTextView == v) {
            RechargeNiubiActivity.launch(this);
        } else if (mWithdrawalsTextView == v) {
            WithdrawalsActivity.launch(this);
        }
    }
}
