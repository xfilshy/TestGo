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
import com.xue.bean.RechargeList;
import com.xue.bean.WalletDecorator;
import com.xue.http.HttpApi;
import com.xue.http.impl.DataHull;

import java.util.ArrayList;

public class RechargeActivity extends BaseActivity implements View.OnClickListener {

    public static void launch(Context context) {
        Intent intent = new Intent(context, RechargeActivity.class);
        context.startActivity(intent);
    }

    private ImageView mBackImageView;

    private TextView mHistoryTextView;

    private TextView mConfirmButton;

    private WalletDecorator mWalletDecorator;

    private TextView mDiamondTextView;

    private ArrayList<ItemViewHolder> viewHolders = new ArrayList(6);

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_recharge);
        initActionBar();
        findView();

        new GetWalletTask(this).start();
    }

    private void initActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM); //Enable自定义的View
            actionBar.setCustomView(R.layout.actionbar_recharge);//设置自定义的布局：actionbar_custom
            mBackImageView = actionBar.getCustomView().findViewById(R.id.back);
            mHistoryTextView = actionBar.getCustomView().findViewById(R.id.history);
            mBackImageView.setOnClickListener(this);
            mHistoryTextView.setOnClickListener(this);
        }
    }

    private void findView() {
        mDiamondTextView = findViewById(R.id.diamond);
        mConfirmButton = findViewById(R.id.confirm);

        viewHolders.add(new ItemViewHolder(findViewById(R.id.item1)));
        viewHolders.add(new ItemViewHolder(findViewById(R.id.item2)));
        viewHolders.add(new ItemViewHolder(findViewById(R.id.item3)));
        viewHolders.add(new ItemViewHolder(findViewById(R.id.item4)));
        viewHolders.add(new ItemViewHolder(findViewById(R.id.item5)));
        viewHolders.add(new ItemViewHolder(findViewById(R.id.item6)));

        mConfirmButton.setEnabled(true);
        mConfirmButton.setOnClickListener(this);
    }

    private void fillData() {
        mDiamondTextView.setText(FloatStringTool.twoDecimalPlaces(mWalletDecorator.getWalletInfo().getDiamond()));

        for (int i = 0; i < mWalletDecorator.getRechargeList().size() && i < 6; i++) {
            ItemViewHolder itemViewHolder = viewHolders.get(i);
            itemViewHolder.fill(mWalletDecorator.getRechargeList().get(i));
        }
    }

    @Override
    public void onClick(View v) {
        if (mBackImageView == v) {
            finish();
        } else if (mHistoryTextView == v) {
            RechargeHistoryActivity.launch(this);
        } else if (mConfirmButton == v) {
            PayActivity.launch(this);
        }
    }

    private static class ItemViewHolder {

        private View itemView;

        private TextView diamondTextView;

        private TextView priceTextView;

        ItemViewHolder(View itemView) {
            this.itemView = itemView;
            diamondTextView = itemView.findViewById(R.id.diamond);
            priceTextView = itemView.findViewById(R.id.price);
        }

        void fill(RechargeList.Recharge recharge) {
            itemView.setVisibility(View.VISIBLE);
            diamondTextView.setText(recharge.getDiamond());
            priceTextView.setText("售价" + recharge.getPrice() + "元");
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
