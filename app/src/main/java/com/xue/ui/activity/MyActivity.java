package com.xue.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.elianshang.tools.ToastTool;
import com.xue.BaseApplication;
import com.xue.R;
import com.xue.asyns.HttpAsyncTask;
import com.xue.bean.User;
import com.xue.bean.UserInfoDetail;
import com.xue.http.HttpApi;
import com.xue.http.impl.DataHull;
import com.xue.imagecache.ImageCacheMannager;
import com.xue.oss.OssManager;
import com.xue.tools.GlideImageLoader;
import com.xue.tools.SimplePickHandlerCallBack;
import com.yancy.gallerypick.config.GalleryConfig;
import com.yancy.gallerypick.config.GalleryPick;

import java.util.List;

public class MyActivity extends BaseActivity implements View.OnClickListener {

    public static void launch(Context context) {
        Intent intent = new Intent(context, MyActivity.class);
        context.startActivity(intent);
    }

    private User mUser;

    private ImageView mPhotoImageView;

    private TextView mRealNameTextView;

    private TextView mUidTextView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);
        mUser = BaseApplication.get().getUser();

        findView();
        init();

        new UserInfoTask(this).start();
    }

    private void findView() {
        mPhotoImageView = findViewById(R.id.photo);
        mRealNameTextView = findViewById(R.id.realName);
        mUidTextView = findViewById(R.id.uid);
    }

    private void init() {
        ImageCacheMannager.loadImage(this, mUser.getUserInfoDetail().getProfile(), mPhotoImageView, true);
        mUidTextView.setText(mUser.getUserBase().getUid());

        UserInfoDetail userInfoDetail = mUser.getUserInfoDetail();
        if (userInfoDetail != null) {
            mRealNameTextView.setText(userInfoDetail.getRealName());
        }
    }

    public void goPrice(View view) {
        PriceActivity.launch(this);
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
        SimplePickHandlerCallBack iHandlerCallBack = new SimplePickHandlerCallBack() {

            @Override
            public void onSuccess(List<String> photoList) {
                if (photoList != null && photoList.size() > 0) {
                    String photoPath = photoList.get(0);
                    ImageCacheMannager.loadImage(MyActivity.this, photoPath, mPhotoImageView, true);

                    new UploadTask(MyActivity.this, photoPath);
                }
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

    private class UserInfoTask extends HttpAsyncTask<User> {

        public UserInfoTask(Context context) {
            super(context);
        }

        @Override
        public DataHull<User> doInBackground() {
            return HttpApi.userInfo();
        }

        @Override
        public void onPostExecute(int updateId, User result) {
            BaseApplication.get().setUser(result, false);
            mUser = BaseApplication.get().getUser();
            init();
        }
    }

    private static class UploadTask extends HttpAsyncTask<UserInfoDetail> implements OssManager.Callback {

        private String resultPath;

        public UploadTask(Context context, String cover) {
            super(context);

            OssManager.get().setCallback(this);
            OssManager.get().upload(cover);
        }

        @Override
        public DataHull<UserInfoDetail> doInBackground() {
            return HttpApi.updateUserInfoDetail(null, null, null, resultPath, null, null);
        }

        @Override
        public void onPostExecute(int updateId, UserInfoDetail result) {
            ToastTool.show(context, "头像上传成功");
            BaseApplication.get().setUserInfoDetail(result);
        }

        @Override
        public void dataNull(int updateId, String errMsg) {
            super.dataNull(updateId, errMsg);
            ToastTool.show(context, "头像上传失败");
        }

        @Override
        public void netNull() {
            super.netNull();
            ToastTool.show(context, "头像上传失败");
        }

        @Override
        public void netErr(int updateId, String errMsg) {
            super.netErr(updateId, errMsg);
            ToastTool.show(context, "头像上传失败");
        }

        @Override
        public void onInit() {

        }

        @Override
        public void onInitFailure() {

        }

        @Override
        public void onStarted() {

        }

        @Override
        public void onProgress(String file, float progress) {

        }

        @Override
        public void onSuccess(String file, String resultName) {
            this.resultPath = resultName;
            start();
        }

        @Override
        public void onFailure(String file, int code) {
            ToastTool.show(context, "头像上传失败");
        }

        @Override
        public void onFinish() {

        }
    }
}
