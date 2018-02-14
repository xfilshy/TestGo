package com.xue.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.elianshang.tools.FloatStringTool;
import com.xue.R;
import com.xue.asyns.HttpAsyncTask;
import com.xue.bean.WalletDecorator;
import com.xue.http.HttpApi;
import com.xue.http.impl.DataHull;

public class PaymentsActivity extends BaseActivity implements View.OnClickListener {

    public static void launch(Context context) {
        Intent intent = new Intent(context, PaymentsActivity.class);
        context.startActivity(intent);
    }

    private ImageView mBackImageView;

    private TextView mHistoryTextView;

    private TextView mRechargeTextView;

    private TextView mWithdrawalsTextView;

    private TextView mNCoinTextView;

    private TextView mMoneyTextView;

    private TextView mEncashTotalTextView;

    private TextView mIncomeTotalTextView;

    private WalletDecorator mWalletDecorator;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payments);

        initActionBar();
        findView();

        new GetWalletTask(this).start();
    }

    private void initActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM); //Enable自定义的View
            actionBar.setCustomView(R.layout.actionbar_payments);//设置自定义的布局：actionbar_custom
            mBackImageView = actionBar.getCustomView().findViewById(R.id.back);
            mHistoryTextView = actionBar.getCustomView().findViewById(R.id.history);
            mHistoryTextView.setOnClickListener(this);
        }
    }

    private void findView() {
        mRechargeTextView = findViewById(R.id.recharge);
        mWithdrawalsTextView = findViewById(R.id.withdrawals);
        mNCoinTextView = findViewById(R.id.nCoin);
        mMoneyTextView = findViewById(R.id.money);
        mEncashTotalTextView = findViewById(R.id.encashTotal);
        mIncomeTotalTextView = findViewById(R.id.incomeTotal);

        mRechargeTextView.setOnClickListener(this);
        mWithdrawalsTextView.setOnClickListener(this);
    }

    private void fillData() {
        mNCoinTextView.setText(FloatStringTool.twoDecimalPlaces(mWalletDecorator.getWalletInfo().getNCoin()));
        mMoneyTextView.setText("相当于人民币：" + FloatStringTool.twoDecimalPlaces(mWalletDecorator.getWalletInfo().getMoney()) + "元");
        mEncashTotalTextView.setText(FloatStringTool.twoDecimalPlaces(mWalletDecorator.getWalletInfo().getEncashmentTotal()));
        mIncomeTotalTextView.setText(FloatStringTool.twoDecimalPlaces(mWalletDecorator.getWalletInfo().getIncomeNCoinTotal()));
    }

    @Override
    public void onClick(View v) {
        if (mHistoryTextView == v) {
            PaymentsHistoryActivity.launch(this);
        } else if (mRechargeTextView == v) {
            RechargeNCoinActivity.launch(this);
        } else if (mWithdrawalsTextView == v) {
            WithdrawalsActivity.launch(this);
        }
    }

    private class GetWalletTask extends HttpAsyncTask<WalletDecorator> {

        public GetWalletTask(Context context) {
            super(context);
        }

        @Override
        public DataHull<WalletDecorator> doInBackground() {
            return HttpApi.getWalletInfo();
        }

        @Override
        public void onPostExecute(int updateId, WalletDecorator result) {
            mWalletDecorator = result;
            fillData();
        }
    }

}
