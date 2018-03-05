package com.xue.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.elianshang.tools.FloatStringTool;
import com.xue.R;
import com.xue.asyns.HttpAsyncTask;
import com.xue.bean.WalletDecorator;
import com.xue.http.HttpApi;
import com.xue.http.impl.DataHull;

public class PaymentsActivity extends SwipeBackBaseActivity implements View.OnClickListener {

    public static void launch(Context context) {
        Intent intent = new Intent(context, PaymentsActivity.class);
        context.startActivity(intent);
    }

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

        findView();
        new GetWalletTask(this).start();
    }

    @Override
    protected boolean hasActionBar() {
        return true;
    }

    @Override
    protected String actionBarTitle() {
        return "我的收益";
    }

    @Override
    protected String actionBarRight() {
        return "收支记录";
    }

    @Override
    public void rightAction(View view) {
        super.rightAction(view);
        PaymentsHistoryActivity.launch(this);
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
        setActionRightVisibility(View.VISIBLE);
    }

    private void fillData() {
        mNCoinTextView.setText(FloatStringTool.twoDecimalPlaces(mWalletDecorator.getWalletInfo().getNCoin()));
        mMoneyTextView.setText("相当于人民币：" + FloatStringTool.twoDecimalPlaces(mWalletDecorator.getWalletInfo().getMoney()) + "元");
        mEncashTotalTextView.setText(FloatStringTool.twoDecimalPlaces(mWalletDecorator.getWalletInfo().getEncashmentTotal()));
        mIncomeTotalTextView.setText(FloatStringTool.twoDecimalPlaces(mWalletDecorator.getWalletInfo().getIncomeNCoinTotal()));
    }

    @Override
    public void onClick(View v) {
        if (mRechargeTextView == v) {
            RechargeNCoinActivity.launch(this);
        } else if (mWithdrawalsTextView == v) {
            WithdrawalsActivity.launch(this);
        }
    }

    private class GetWalletTask extends HttpAsyncTask<WalletDecorator> {

        public GetWalletTask(Context context) {
            super(context, true, true);
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
