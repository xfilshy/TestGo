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
import com.xue.bean.RechargeList;
import com.xue.bean.WalletDecorator;
import com.xue.http.HttpApi;
import com.xue.http.impl.DataHull;

import java.util.ArrayList;

public class RechargeActivity extends SwipeBackBaseActivity implements View.OnClickListener {

    public static void launch(Context context) {
        Intent intent = new Intent(context, RechargeActivity.class);
        context.startActivity(intent);
    }

    private TextView mConfirmButton;

    private WalletDecorator mWalletDecorator;

    private TextView mDiamondTextView;

    private ArrayList<ItemViewHolder> viewHolders = new ArrayList(6);

    private ItemViewHolder mSelectedViewHolder;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_recharge);
        findView();

        new GetWalletTask(this).start();
    }

    @Override
    protected boolean hasActionBar() {
        return true;
    }

    @Override
    protected String actionBarTitle() {
        return "购买钻石";
    }

    @Override
    protected String actionBarRight() {
        return "购买记录";
    }

    @Override
    public void rightAction(View view) {
        super.rightAction(view);
        RechargeHistoryActivity.launch(this);
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

        mConfirmButton.setOnClickListener(this);
        setActionRightVisibility(View.VISIBLE);
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
        if (mConfirmButton == v) {
            PayActivity.launch(this, mSelectedViewHolder.recharge);
        }
    }

    private class ItemViewHolder {

        private View itemView;

        private TextView diamondTextView;

        private TextView priceTextView;

        private RechargeList.Recharge recharge;

        ItemViewHolder(View itemView) {
            this.itemView = itemView;
            diamondTextView = itemView.findViewById(R.id.diamond);
            priceTextView = itemView.findViewById(R.id.price);
        }

        void fill(RechargeList.Recharge recharge) {
            this.recharge = recharge;

            itemView.setVisibility(View.VISIBLE);
            diamondTextView.setText(recharge.getDiamond());
            priceTextView.setText("售价" + recharge.getPrice() + "元");

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
