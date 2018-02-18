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
import com.xue.bean.RechargeNCoinList;
import com.xue.bean.WalletDecorator;
import com.xue.http.HttpApi;
import com.xue.http.impl.DataHull;

import java.util.ArrayList;

public class RechargeNCoinActivity extends BaseActivity implements View.OnClickListener {

    public static void launch(Context context) {
        Intent intent = new Intent(context, RechargeNCoinActivity.class);
        context.startActivity(intent);
    }

    private ImageView mBackImageView;

    private TextView mTitleTextView;

    private TextView mConfirmButton;

    private WalletDecorator mWalletDecorator;

    private TextView mNCoinTextView;

    private ArrayList<ItemViewHolder> viewHolders = new ArrayList(6);

    private ItemViewHolder mSelectedViewHolder;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_recharge_ncoin);
        initActionBar();
        findView();

        new GetWalletTask(this).start();
    }

    private void initActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM); //Enable自定义的View
            actionBar.setCustomView(R.layout.actionbar_simple);//设置自定义的布局：actionbar_custom
            mBackImageView = actionBar.getCustomView().findViewById(R.id.back);
            mTitleTextView = actionBar.getCustomView().findViewById(R.id.title);

            mTitleTextView.setText("牛币购买钻石");
            mBackImageView.setOnClickListener(this);
        }
    }

    private void findView() {
        mNCoinTextView = findViewById(R.id.nCoin);
        mConfirmButton = findViewById(R.id.confirm);

        viewHolders.add(new ItemViewHolder(findViewById(R.id.item1)));
        viewHolders.add(new ItemViewHolder(findViewById(R.id.item2)));
        viewHolders.add(new ItemViewHolder(findViewById(R.id.item3)));
        viewHolders.add(new ItemViewHolder(findViewById(R.id.item4)));
        viewHolders.add(new ItemViewHolder(findViewById(R.id.item5)));
        viewHolders.add(new ItemViewHolder(findViewById(R.id.item6)));

        mConfirmButton.setOnClickListener(this);
    }


    private void fillData() {
        mNCoinTextView.setText(FloatStringTool.twoDecimalPlaces(mWalletDecorator.getWalletInfo().getNCoin()));

        for (int i = 0; i < mWalletDecorator.getRechargeNCoinList().size() && i < 6; i++) {
            ItemViewHolder itemViewHolder = viewHolders.get(i);
            itemViewHolder.fill(mWalletDecorator.getRechargeNCoinList().get(i));
        }
    }

    @Override
    public void onClick(View v) {
        if (mBackImageView == v) {
            finish();
        } else if (mConfirmButton == v) {

        }
    }


    private class ItemViewHolder {

        private View itemView;

        private TextView diamondTextView;

        private TextView nCoinTextView;

        private RechargeNCoinList.RechargeNCoin rechargeNCoin;

        ItemViewHolder(View itemView) {
            this.itemView = itemView;
            diamondTextView = itemView.findViewById(R.id.diamond);
            nCoinTextView = itemView.findViewById(R.id.nCoin);
        }

        void fill(RechargeNCoinList.RechargeNCoin rechargeNCoin) {
            this.rechargeNCoin = rechargeNCoin;

            itemView.setVisibility(View.VISIBLE);
            diamondTextView.setText(rechargeNCoin.getDiamond());
            nCoinTextView.setText("售价" + rechargeNCoin.getnCoin() + "牛币");

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mSelectedViewHolder != null) {
                        if (mSelectedViewHolder.itemView != v) {
                            mSelectedViewHolder.itemView.setSelected(false);
                        }
                    }

                    mSelectedViewHolder = ItemViewHolder.this;
                    mSelectedViewHolder.itemView.setSelected(true);
                    mConfirmButton.setEnabled(true);
                }
            });
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
