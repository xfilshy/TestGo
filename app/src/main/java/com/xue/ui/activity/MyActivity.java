package com.xue.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;

import com.xue.R;
import com.xue.imagecache.ImageCacheMannager;

public class MyActivity extends BaseActivity implements View.OnClickListener {

    public static void launch(Context context) {
        Intent intent = new Intent(context, MyActivity.class);
        context.startActivity(intent);
    }

    private ImageView mPhotoImageView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_my);

        findView();
        init();
    }

    private void findView() {
        mPhotoImageView = findViewById(R.id.photo);
    }

    private void init() {
        ImageCacheMannager.loadImage(this, R.drawable.photo_test, mPhotoImageView, true);
    }


    public void closeActivity(View view) {
        finish();
    }

    public void goInfo(View view) {
        InfoActivity.launch(this);
    }

    public void goRecharge(View view) {
        RechargeActivity.launch(this);
    }

    public void goPayments(View view) {
        PaymentsActivity.launsh(this);
    }

    public void goSetting(View view) {
        SettingActivity.launch(this);
    }

    @Override
    public void onClick(View v) {

    }
}
