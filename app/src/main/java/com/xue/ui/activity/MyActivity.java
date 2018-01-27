package com.xue.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.xue.R;
import com.xue.imagecache.ImageCacheMannager;

public class MyActivity extends BaseActivity implements View.OnClickListener {

    public static void launch(Context context) {
        Intent intent = new Intent(context, MyActivity.class);
        context.startActivity(intent);
    }

    private ImageView mPhotoImageView;

    private LinearLayout mRechargeLinerLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_my);

        findView();
        init();
    }

    private void findView() {
        mPhotoImageView = findViewById(R.id.photo);

        mRechargeLinerLayout = findViewById(R.id.recharge);

        mRechargeLinerLayout.setOnClickListener(this);
    }

    private void init() {
        ImageCacheMannager.loadImage(this, R.drawable.photo_test, mPhotoImageView, true);
    }


    public void closeActivity(View view) {
        finish();
    }

    @Override
    public void onClick(View v) {
        if (mRechargeLinerLayout == v) {
            RechargeActivity.launch(this);
        }
    }
}
