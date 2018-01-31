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
import com.xue.imagecache.ImageCacheMannager;

public class InfoActivity extends BaseActivity implements View.OnClickListener {

    public static void launch(Context context) {
        Intent intent = new Intent(context, InfoActivity.class);
        context.startActivity(intent);
    }

    private ImageView mBackImageView;

    private TextView mSaveTextView;

    private ImageView mPhotoImageView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        initActionBar();
        findView();
    }

    private void initActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM); //Enable自定义的View
            actionBar.setCustomView(R.layout.actionbar_info);//设置自定义的布局：actionbar_custom
            mBackImageView = actionBar.getCustomView().findViewById(R.id.back);
            mSaveTextView = actionBar.getCustomView().findViewById(R.id.save);
            mBackImageView.setOnClickListener(this);
            mSaveTextView.setOnClickListener(this);
        }
    }

    private void findView() {
        mPhotoImageView = findViewById(R.id.photo);

        ImageCacheMannager.loadImage(this, R.drawable.photo_test, mPhotoImageView, true);
    }

    public void goDescribe(View view) {
        DescribeActivity.launch(this);
    }

    public void goGallery(View view) {
        GalleryActivity.launch(this);
    }

    public void goWork(View view) {
        WorkActivity.launch(this);
    }

    public void goEducation(View view) {
        EducationActivity.launch(this);
    }

    @Override
    public void onClick(View v) {
        if (mBackImageView == v) {
            finish();
        } else if (mSaveTextView == v) {

        }
    }
}
