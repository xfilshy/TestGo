package com.xue.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.xue.R;
import com.xue.imagecache.ImageCacheMannager;
import com.xue.tools.GlideImageLoader;
import com.yancy.gallerypick.config.GalleryConfig;
import com.yancy.gallerypick.config.GalleryPick;
import com.yancy.gallerypick.inter.IHandlerCallBack;

import java.util.List;

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

    public void goCertify(View view) {
        CertifyActivity.launch(this);
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

    public void goPickPhoto(View view) {
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
                    ImageCacheMannager.loadImage(MyActivity.this, photoPath, mPhotoImageView, true);
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
                .crop(true, 1, 1, 1000, 1000)
                .isShowCamera(true)
                .filePath("/Gallery/Pictures")
                .build();

        GalleryPick.getInstance().setGalleryConfig(galleryConfig).open(this);
    }

    @Override
    public void onClick(View v) {

    }
}
