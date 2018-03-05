package com.xue.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

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

public class BusinessCardCertifyActivity extends SwipeBackBaseActivity {

    public static void launch(Context context) {
        Intent intent = new Intent(context, BusinessCardCertifyActivity.class);
        context.startActivity(intent);
    }

    private ImageView mPhotoImageView;

    private String mImagePath;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_card_certify);

        findView();
        init();
    }

    @Override
    protected boolean hasActionBar() {
        return true;
    }

    @Override
    protected String actionBarTitle() {
        return "名片认证（未认证）";
    }

    @Override
    protected String actionBarRight() {
        return "保存";
    }

    @Override
    public void rightAction(View view) {
        super.rightAction(view);
        new BusinessCardCertifyActivity.UploadTask(this, mImagePath);
    }

    private void findView() {
        mPhotoImageView = findViewById(R.id.photo);

        UITool.zoomViewByWidth(320, 240, mPhotoImageView);
    }


    private void init() {
        UserExpertInfo userExpertInfo = BaseApplication.get().getUser().getUserExpertInfo();
        if (userExpertInfo != null) {
            if (!TextUtils.isEmpty(userExpertInfo.getBusinessCardImg())) {
                ImageCacheMannager.loadImage(this, userExpertInfo.getBusinessCardImg(), mPhotoImageView, false);
            }

            if (TextUtils.equals("0", userExpertInfo.getBusinessCardAuth())) {
                setActionTitle("名片认证（未认证）");
            } else if (TextUtils.equals("1", userExpertInfo.getBusinessCardAuth())) {
                setActionTitle("名片认证（认证中）");
            } else if (TextUtils.equals("2", userExpertInfo.getBusinessCardAuth())) {
                setActionTitle("名片认证（认证通过）");
            } else if (TextUtils.equals("3", userExpertInfo.getBusinessCardAuth())) {
                setActionTitle("名片认证（认证失败）");
            }
        }
    }

    public void goPickBusinessCard(final View view) {
        SimplePickHandlerCallBack iHandlerCallBack = new SimplePickHandlerCallBack() {

            @Override
            public void onSuccess(List<String> photoList) {
                if (photoList != null && photoList.size() > 0) {
                    String photoPath = photoList.get(0);
                    ImageCacheMannager.loadImage(BusinessCardCertifyActivity.this, photoPath, mPhotoImageView, false);

                    mImagePath = photoPath;
                    setActionRightVisibility(View.VISIBLE);
                }
            }
        };
        GalleryConfig galleryConfig = new GalleryConfig.Builder()
                .imageLoader(new GlideImageLoader())
                .iHandlerCallBack(iHandlerCallBack)
                .provider("com.xue.fileprovider")
                .crop(true, 4, 3, 1080, 810)
                .isShowCamera(true)
                .filePath("/Gallery/Pictures")
                .build();

        GalleryPick.getInstance().setGalleryConfig(galleryConfig).open(this);
    }

    private class UploadTask extends HttpAsyncTask<UserExpertInfo> {

        private String resultPath;

        public UploadTask(Context context, String imagePath) {
            super(context, true, true);
            showLoadingDialog();
            OssManager.get().upload(imagePath, callback, true);
            this.resultPath = imagePath;
        }

        @Override
        public DataHull<UserExpertInfo> doInBackground() {
            return HttpApi.updateUserExpertInfo(null, null, null, resultPath);
        }

        @Override
        public void onPostExecute(int updateId, UserExpertInfo result) {
            ToastTool.show(context, "名片上传成功，等待审核");
            BaseApplication.get().setUserExpertInfo(result);
            finish();
        }

        @Override
        public void dataNull(int updateId, String errMsg) {
            super.dataNull(updateId, errMsg);
            ToastTool.show(context, "名片上传失败");
        }

        @Override
        public void netNull() {
            super.netNull();
            ToastTool.show(context, "名片上传失败");
        }

        @Override
        public void netErr(int updateId, String errMsg) {
            super.netErr(updateId, errMsg);
            ToastTool.show(context, "名片上传失败");
        }

        private OssManager.Callback callback = new SimpleOssManagerCallback() {

            @Override
            public void onSuccess(String file, String resultName) {
                UploadTask.this.resultPath = resultName;
                UploadTask.this.start();
            }

            @Override
            public void onInitFailure() {
                ToastTool.show(context, "名片上传失败");
            }

            @Override
            public void onFailure(String file, int code) {
                ToastTool.show(context, "名片上传失败");
            }
        };
    }
}
