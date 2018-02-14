package com.xue.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.elianshang.tools.ToastTool;
import com.xue.BaseApplication;
import com.xue.R;
import com.xue.asyns.HttpAsyncTask;
import com.xue.bean.User;
import com.xue.bean.UserConfigInfo;
import com.xue.bean.UserDetailInfo;
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

public class MyActivity extends BaseActivity implements View.OnClickListener {

    public static void launch(Context context) {
        Intent intent = new Intent(context, MyActivity.class);
        context.startActivity(intent);
    }

    private ImageView mPhotoImageView;

    private TextView mRealNameTextView;

    private ImageView mGenderImageView;

    private TextView mUidTextView;

    private TextView mSignatureTextView;

    private TextView mFeeTextView;

    private TextView mAuthTextView;

    private long mUserTimeStamp;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);
        findView();
        new UserInfoTask(this).start();

    }

    @Override
    protected void onResume() {
        super.onResume();
        init();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void findView() {
        mPhotoImageView = findViewById(R.id.photo);
        mRealNameTextView = findViewById(R.id.realName);
        mGenderImageView = findViewById(R.id.gender);
        mUidTextView = findViewById(R.id.uid);
        mSignatureTextView = findViewById(R.id.signature);
        mFeeTextView = findViewById(R.id.fee);
        mAuthTextView = findViewById(R.id.auth);
    }

    private void init() {
        User user = BaseApplication.get().getUser();
        if (mUserTimeStamp != user.getTimeStamp()) {
            mUidTextView.setText(user.getUserBase().getUid());

            UserDetailInfo userDetailInfo = user.getUserDetailInfo();
            if (userDetailInfo != null) {
                ImageCacheMannager.loadImage(this, user.getUserDetailInfo().getProfile(), mPhotoImageView, true);
                mRealNameTextView.setText(userDetailInfo.getRealName());
                if (TextUtils.equals("1", userDetailInfo.getGender())) {
                    ImageCacheMannager.loadImage(this, R.drawable.icon_male, mGenderImageView, false);
                } else if (TextUtils.equals("2", userDetailInfo.getGender())) {
                    ImageCacheMannager.loadImage(this, R.drawable.icon_female, mGenderImageView, false);
                }
            }

            UserConfigInfo userConfigInfo = user.getUserConfigInfo();
            if (userConfigInfo != null) {
                mFeeTextView.setVisibility(View.VISIBLE);
                mFeeTextView.setText(userConfigInfo.getFeeDefault() + "钻石/分钟");
            }

            UserExpertInfo userExpertInfo = user.getUserExpertInfo();
            if (userExpertInfo != null) {
                if (!TextUtils.isEmpty(userExpertInfo.getSignature())) {
                    mSignatureTextView.setVisibility(View.VISIBLE);
                    mSignatureTextView.setText(userExpertInfo.getSignature());
                } else {
                    mSignatureTextView.setVisibility(View.GONE);
                }

                if (userExpertInfo.getServiceFee() > 0) {
                    mFeeTextView.setVisibility(View.VISIBLE);
                    mFeeTextView.setText(userExpertInfo.getServiceFee() + "钻石/分钟");
                } else {
                    mFeeTextView.setVisibility(View.GONE);
                }

                if (TextUtils.equals("0", userExpertInfo.getStatus())) {
                    mAuthTextView.setText("未认证");
                } else if (TextUtils.equals("1", userExpertInfo.getStatus())) {
                    mAuthTextView.setText("认证中");
                } else if (TextUtils.equals("2", userExpertInfo.getStatus())) {
                    mAuthTextView.setText("认证通过");
                } else if (TextUtils.equals("3", userExpertInfo.getStatus())) {
                    mAuthTextView.setText("认证失败");
                }
            } else {
                mSignatureTextView.setVisibility(View.GONE);
            }
        }

        mUserTimeStamp = user.getTimeStamp();
    }

    public void goPrice(View view) {
        FeeSettingActivity.launch(this);
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
        PaymentsActivity.launch(this);
    }

    public void goSetting(View view) {
        SettingActivity.launch(this);
    }

    public void goPickPhoto(View view) {
        SimplePickHandlerCallBack iHandlerCallBack = new SimplePickHandlerCallBack() {

            @Override
            public void onSuccess(List<String> photoList) {
                if (photoList != null && photoList.size() > 0) {
                    final String photoPath = photoList.get(0);
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
            init();
        }
    }

    private static class UploadTask extends HttpAsyncTask<UserDetailInfo> {

        private String resultPath;

        public UploadTask(Context context, String cover) {
            super(context);

            OssManager.get().upload(cover, callback);
        }

        @Override
        public DataHull<UserDetailInfo> doInBackground() {
            return HttpApi.updateUserDetailInfo(null, null, null, resultPath, null, null);
        }

        @Override
        public void onPostExecute(int updateId, UserDetailInfo result) {
            ToastTool.show(context, "头像上传成功");
            BaseApplication.get().setUserDetailInfo(result);
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

        private OssManager.Callback callback = new SimpleOssManagerCallback() {

            @Override
            public void onSuccess(String file, String resultName) {
                UploadTask.this.resultPath = resultName;
                UploadTask.this.start();
            }

            @Override
            public void onInitFailure() {
                ToastTool.show(context, "初始化上传头像失败");
            }

            @Override
            public void onFailure(String file, int code) {
                ToastTool.show(context, "头像上传失败");
            }
        };

    }
}
