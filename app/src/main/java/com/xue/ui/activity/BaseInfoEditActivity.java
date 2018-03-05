package com.xue.ui.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;

import com.elianshang.tools.ToastTool;
import com.xue.BaseApplication;
import com.xue.R;
import com.xue.asyns.HttpAsyncTask;
import com.xue.bean.UserDetailInfo;
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

public class BaseInfoEditActivity extends SwipeBackBaseActivity implements View.OnClickListener, TextWatcher {

    public static void launch(Activity activity) {
        Intent intent = new Intent(activity, BaseInfoEditActivity.class);
        activity.startActivityForResult(intent, 1);
    }

    private ImageView mPhotoImageView;

    private ImageView mPickImageView;

    private EditText mRealNameEditText;

    private CheckBox mMaleCheckBox;

    private CheckBox mFemaleCheckBox;

    private String mPhotoPath;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base_info_edit);
        BaseInfoEditActivity.this.setResult(RESULT_CANCELED);

        findView();
    }

    @Override
    protected boolean hasActionBar() {
        return true;
    }

    @Override
    protected String actionBarTitle() {
        return "基本资料";
    }

    @Override
    protected String actionBarRight() {
        return "保存";
    }

    @Override
    public void rightAction(View view) {
        super.rightAction(view);
        save();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mRealNameEditText.addTextChangedListener(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mRealNameEditText.removeTextChangedListener(this);
    }

    private void findView() {
        mPhotoImageView = findViewById(R.id.photo);
        mPickImageView = findViewById(R.id.pick);
        mRealNameEditText = findViewById(R.id.realName);
        mMaleCheckBox = findViewById(R.id.male);
        mFemaleCheckBox = findViewById(R.id.female);

        mPickImageView.setOnClickListener(this);
        mMaleCheckBox.setOnClickListener(this);
        mFemaleCheckBox.setOnClickListener(this);
    }

    private void checkChange() {
        String realName = mRealNameEditText.getText().toString();
        int gender = 0;
        if (mMaleCheckBox.isChecked()) {
            gender = 1;
        } else if (mFemaleCheckBox.isChecked()) {
            gender = 2;
        }

        if (!TextUtils.isEmpty(realName) && gender != 0 && !TextUtils.isEmpty(mPhotoPath)) {
            setActionRightVisibility(View.VISIBLE);
        } else {
            setActionRightVisibility(View.GONE);
        }
    }

    private void save() {
        String realName = mRealNameEditText.getText().toString();
        int gender = 0;
        if (mMaleCheckBox.isChecked()) {
            gender = 1;
        } else if (mFemaleCheckBox.isChecked()) {
            gender = 2;
        }

        new UpdateTask(this, realName, gender, mPhotoPath);
    }

    @Override
    public void onClick(View v) {
        if (mPickImageView == v) {
            SimplePickHandlerCallBack iHandlerCallBack = new SimplePickHandlerCallBack() {

                @Override
                public void onSuccess(List<String> photoList) {
                    if (photoList != null && photoList.size() > 0) {
                        String photoPath = photoList.get(0);
                        ImageCacheMannager.loadImage(BaseInfoEditActivity.this, photoPath, mPhotoImageView, true);

                        mPhotoPath = photoPath;
                        checkChange();
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
        } else if (mMaleCheckBox == v) {
            mMaleCheckBox.setChecked(true);
            mFemaleCheckBox.setChecked(false);
            checkChange();
        } else if (mFemaleCheckBox == v) {
            mMaleCheckBox.setChecked(false);
            mFemaleCheckBox.setChecked(true);
            checkChange();
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        checkChange();
    }

    private class UpdateTask extends HttpAsyncTask<UserDetailInfo> {

        private String realName;

        private int gender;

        private String resultPhotoPath;

        public UpdateTask(Context context, String realName, int gender, String photoPath) {
            super(context, true, true);
            this.realName = realName;
            this.gender = gender;

            OssManager.get().upload(photoPath, callback, true);
        }

        @Override
        public DataHull<UserDetailInfo> doInBackground() {
            return HttpApi.updateUserDetailInfo(realName, String.valueOf(gender), null, resultPhotoPath, null, null);
        }

        @Override
        public void onPostExecute(int updateId, UserDetailInfo result) {
            BaseApplication.get().setUserDetailInfo(result);
            BaseInfoEditActivity.this.setResult(RESULT_OK);
            finish();
        }

        @Override
        public void dataNull(int updateId, String errMsg) {
            super.dataNull(updateId, errMsg);
            ToastTool.show(BaseInfoEditActivity.this, "保存失败，请重试");
        }

        private OssManager.Callback callback = new SimpleOssManagerCallback() {
            @Override
            public void onSuccess(String file, String resultName) {
                ToastTool.show(BaseInfoEditActivity.this, "上传头像成功");
                resultPhotoPath = resultName;
                start();
            }

            @Override
            public void onInitFailure() {
                ToastTool.show(BaseInfoEditActivity.this, "初始化上传头像失败");
            }

            @Override
            public void onFailure(String file, int code) {
                ToastTool.show(BaseInfoEditActivity.this, "上传头像失败");
            }
        };
    }
}
