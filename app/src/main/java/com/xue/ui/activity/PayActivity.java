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
import com.xue.bean.RechargeList;

public class PayActivity extends BaseActivity implements View.OnClickListener {

    public static void launch(Context context, RechargeList.Recharge recharge) {
        Intent intent = new Intent(context, PayActivity.class);
        intent.putExtra("recharge", recharge);
        context.startActivity(intent);
    }

    private RechargeList.Recharge mRecharge;

    private ImageView mBackImageView;

    private TextView mTitleTextView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay);

        readExtra();
        findView();
        initActionBar();
    }

    private void initActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM); //Enable自定义的View
            actionBar.setCustomView(R.layout.actionbar_simple);//设置自定义的布局：actionbar_custom
            mBackImageView = actionBar.getCustomView().findViewById(R.id.back);
            mTitleTextView = actionBar.getCustomView().findViewById(R.id.title);
            mBackImageView.setOnClickListener(this);

            mTitleTextView.setText("支付订单");
        }
    }

    private void findView() {

    }

    private void readExtra() {
        mRecharge = (RechargeList.Recharge) getIntent().getSerializableExtra("recharge");
    }

    @Override
    public void onClick(View v) {
        if (mBackImageView == v) {
            finish();
        }
    }
}
