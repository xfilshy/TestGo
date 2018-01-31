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

public class PriceActivity extends BaseActivity implements View.OnClickListener {


    public static void launch(Context context) {
        Intent intent = new Intent(context, PriceActivity.class);
        context.startActivity(intent);
    }

    private ImageView mBackImageView;

    private TextView mTitleTextView;

    private TextView mRightTextView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_price);

        initActionBar();
        findView();
    }

    private void initActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM); //Enable自定义的View
            actionBar.setCustomView(R.layout.actionbar_simple);//设置自定义的布局：actionbar_custom
            mBackImageView = actionBar.getCustomView().findViewById(R.id.back);
            mTitleTextView = actionBar.getCustomView().findViewById(R.id.title);
            mRightTextView = actionBar.getCustomView().findViewById(R.id.right);
            mBackImageView.setOnClickListener(this);
            mRightTextView.setOnClickListener(this);
            mRightTextView.setVisibility(View.VISIBLE);

            mTitleTextView.setText("通话定价");
            mRightTextView.setText("保存");
        }
    }

    private void findView() {

    }

    @Override
    public void onClick(View v) {
        if (mBackImageView == v) {
            finish();
        }
    }
}
