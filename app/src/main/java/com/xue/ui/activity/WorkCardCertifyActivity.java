package com.xue.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.elianshang.tools.UITool;
import com.xue.R;
import com.xue.imagecache.ImageCacheMannager;
import com.xue.tools.GlideImageLoader;
import com.yancy.gallerypick.config.GalleryConfig;
import com.yancy.gallerypick.config.GalleryPick;
import com.yancy.gallerypick.inter.IHandlerCallBack;

import java.util.List;

public class WorkCardCertifyActivity extends BaseActivity implements View.OnClickListener {

    public static void launch(Context context) {
        Intent intent = new Intent(context, WorkCardCertifyActivity.class);
        context.startActivity(intent);
    }

    private ImageView mBackImageView;

    private TextView mTitleTextView;

    private ImageView mPhotoImageView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work_card_certify);

        initActionBar();
        findView();
    }

    private void findView() {
        mPhotoImageView = findViewById(R.id.photo);

        UITool.zoomViewByWidth(210, 280, mPhotoImageView);
    }

    private void initActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM); //Enable自定义的View
            actionBar.setCustomView(R.layout.actionbar_simple);//设置自定义的布局：actionbar_custom
            mBackImageView = actionBar.getCustomView().findViewById(R.id.back);
            mTitleTextView = actionBar.getCustomView().findViewById(R.id.title);
            mBackImageView.setOnClickListener(this);

            mTitleTextView.setText("工牌认证");
        }
    }

    public void goPickWorkCard(View view) {
        IHandlerCallBack iHandlerCallBack = new IHandlerCallBack() {
            @Override
            public void onStart() {
                Log.e("xue", "去选择了");
            }

            @Override
            public void onSuccess(List<String> photoList) {
                Log.e("xue", "成功了" + photoList);
                if (photoList != null && photoList.size() > 0) {
                    String photoPath = photoList.get(0);
                    ImageCacheMannager.loadImage(WorkCardCertifyActivity.this, photoPath, mPhotoImageView, false);
                }
            }

            @Override
            public void onCancel() {
                Log.e("xue", "取消了");
            }

            @Override
            public void onFinish() {
                Log.e("xue", "完成了");
            }

            @Override
            public void onError() {
                Log.e("xue", "错误了");
            }
        };
        GalleryConfig galleryConfig = new GalleryConfig.Builder()
                .imageLoader(new GlideImageLoader())
                .iHandlerCallBack(iHandlerCallBack)
                .provider("com.xue.fileprovider")
                .crop(true, 3, 4, 810, 1080)
                .isShowCamera(true)
                .filePath("/Gallery/Pictures")
                .build();

        GalleryPick.getInstance().setGalleryConfig(galleryConfig).open(this);
    }

    @Override
    public void onClick(View v) {
        if (mBackImageView == v) {
            finish();
        }
    }
}
