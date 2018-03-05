package com.xue.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.xue.R;
import com.xue.bean.RechargeList;

public class PayActivity extends SwipeBackBaseActivity implements View.OnClickListener {

    public static void launch(Context context, RechargeList.Recharge recharge) {
        Intent intent = new Intent(context, PayActivity.class);
        intent.putExtra("recharge", recharge);
        context.startActivity(intent);
    }

    private TextView mPriceTextView;

    private TextView mDiamondTextView;

    private CheckBox mWeChatCheckBox;

    private CheckBox mZhiFubaoCheckBox;

    private TextView mConfirmTextView;

    private RechargeList.Recharge mRecharge;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay);

        readExtra();
        findView();
        fillData();
    }

    @Override
    protected boolean hasActionBar() {
        return true;
    }

    @Override
    protected String actionBarTitle() {
        return "支付订单";
    }

    @Override
    protected String actionBarRight() {
        return null;
    }

    private void findView() {
        mPriceTextView = findViewById(R.id.price);
        mDiamondTextView = findViewById(R.id.diamond);
        mWeChatCheckBox = findViewById(R.id.wechat);
        mZhiFubaoCheckBox = findViewById(R.id.zhifubao);
        mConfirmTextView = findViewById(R.id.confirm);

        mWeChatCheckBox.setOnClickListener(this);
        mZhiFubaoCheckBox.setOnClickListener(this);
        mConfirmTextView.setOnClickListener(this);
    }

    private void readExtra() {
        mRecharge = (RechargeList.Recharge) getIntent().getSerializableExtra("recharge");
    }

    private void fillData() {
        mPriceTextView.setText("￥" + mRecharge.getPrice());
        mDiamondTextView.setText("购买" + mRecharge.getDiamond() + "个钻石");
        mConfirmTextView.setText("确认支付  ￥" + mRecharge.getPrice());
    }

    @Override
    public void onClick(View v) {
        if (mWeChatCheckBox == v) {
            mWeChatCheckBox.setChecked(true);
            mZhiFubaoCheckBox.setChecked(false);
            mConfirmTextView.setEnabled(true);
        } else if (mZhiFubaoCheckBox == v) {
            mWeChatCheckBox.setChecked(false);
            mZhiFubaoCheckBox.setChecked(true);
            mConfirmTextView.setEnabled(true);
        } else if (mConfirmTextView == v) {

        }
    }
}
