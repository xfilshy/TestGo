package com.xue.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.elianshang.tools.ToastTool;
import com.elianshang.tools.UITool;
import com.xue.BaseApplication;
import com.xue.R;
import com.xue.asyns.HttpAsyncTask;
import com.xue.bean.UserExpertInfo;
import com.xue.http.HttpApi;
import com.xue.http.impl.DataHull;
import com.xue.imagecache.ImageCacheMannager;
import com.xue.oss.OssManager;
import com.xue.oss.SimpleOssManagerCallback;
import com.xue.tools.GlideImageLoader;
import com.xue.tools.SimplePickHandlerCallBack;
import com.yancy.gallerypick.config.GalleryConfig;
import com.yancy.gallerypick.config.GalleryPick;

import java.util.List;

public class WorkCardCertifyActivity extends BaseActivity implements View.OnClickListener {

    public static void launch(Context context) {
        Intent intent = new Intent(context, WorkCardCertifyActivity.class);
        context.startActivity(intent);
    }

    private ImageView mBackImageView;

    private TextView mTitleTextView;

    private TextView mRightTextView;

    private ImageView mPhotoImageView;

    private String mImagePath;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work_card_certify);

        initActionBar();
        findView();
        init();
    }

    private void findView() {
        mPhotoImageView = findViewById(R.id.photo);
        UITool.zoomViewByWidth(210, 280, mPhotoImageView);
    }

    private void init() {
        UserExpertInfo userExpertInfo = BaseApplication.get().getUser().getUserExpertInfo();
        if (userExpertInfo != null) {
            if (!TextUtils.isEmpty(userExpertInfo.getWorkCardImg())) {
                ImageCacheMannager.loadImage(WorkCardCertifyActivity.this, userExpertInfo.getWorkCardImg(), mPhotoImageView, false);
            }

            if (TextUtils.equals("0", userExpertInfo.getWorkCardAuth())) {
                mTitleTextView.setText("工牌认证（未认证）");
            } else if (TextUtils.equals("1", userExpertInfo.getWorkCardAuth())) {
                mTitleTextView.setText("工牌认证（认证中）");
            } else if (TextUtils.equals("2", userExpertInfo.getWorkCardAuth())) {
                mTitleTextView.setText("工牌认证（认证通过）");
            } else if (TextUtils.equals("3", userExpertInfo.getWorkCardAuth())) {
                mTitleTextView.setText("工牌认证（认证失败）");
            }
        }
    }

    private void initActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM); //Enable自定义的View
            actionBar.setCustomView(R.layout.actionbar_simple);//设置自定义的布局：actionbar_custom
            mBackImageView = actionBar.getCustomView().findViewById(R.id.back);
            mTitleTextView = actionBar.getCustomView().findViewById(R.id.title);
            mRightTextView = actionBar.getCustomView().findViewById(R.id.right);
            mRightTextView.setOnClickListener(this);

            mTitleTextView.setText("工牌认证（未认证）");
            mRightTextView.setText("保存");
        }
    }

    public void goPickWorkCard(View view) {
        SimplePickHandlerCallBack iHandlerCallBack = new SimplePickHandlerCallBack() {

            @Override
            public void onSuccess(List<String> photoList) {
                Log.e("xue", "成功了" + photoList);
                if (photoList != null && photoList.size() > 0) {
                    String photoPath = photoList.get(0);
                    ImageCacheMannager.loadImage(WorkCardCertifyActivity.this, photoPath, mPhotoImageView, false);

                    mImagePath = photoPath;
                    mRightTextView.setVisibility(View.VISIBLE);
                }
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
        if (mRightTextView == v) {
            new UploadTask(this, mImagePath);
        }
    }


    private class UploadTask extends HttpAsyncTask<UserExpertInfo> {

        private String resultPath;

        public UploadTask(Context context, String imagePath) {
            super(context);
            OssManager.get().upload(imagePath, callback , true);
            this.resultPath = imagePath;
        }

        @Override
        public DataHull<UserExpertInfo> doInBackground() {
            return HttpApi.updateUserExpertInfo(null, null, resultPath, null);
        }

        @Override
        public void onPostExecute(int updateId, UserExpertInfo result) {
            ToastTool.show(context, "工牌上传成功，等待审核");
            BaseApplication.get().setUserExpertInfo(result);
            finish();
        }

        @Override
        public void dataNull(int updateId, String errMsg) {
            super.dataNull(updateId, errMsg);
            ToastTool.show(context, "工牌上传失败");
        }

        @Override
        public void netNull() {
            super.netNull();
            ToastTool.show(context, "工牌上传失败");
        }

        @Override
        public void netErr(int updateId, String errMsg) {
            super.netErr(updateId, errMsg);
            ToastTool.show(context, "工牌上传失败");
        }

        private OssManager.Callback callback = new SimpleOssManagerCallback() {

            @Override
            public void onSuccess(String file, String resultName) {
                UploadTask.this.resultPath = resultName;
                UploadTask.this.start();
            }

            @Override
            public void onInitFailure() {
                ToastTool.show(context, "工牌上传失败");
            }

            @Override
            public void onFailure(String file, int code) {
                ToastTool.show(context, "工牌上传失败");
            }
        };
    }
}
