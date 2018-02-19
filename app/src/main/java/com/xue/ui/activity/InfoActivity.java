package com.xue.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.elianshang.tools.ToastTool;
import com.elianshang.tools.UITool;
import com.xue.BaseApplication;
import com.xue.R;
import com.xue.asyns.HttpAsyncTask;
import com.xue.bean.User;
import com.xue.bean.UserDetailInfo;
import com.xue.bean.UserTagInfo;
import com.xue.http.HttpApi;
import com.xue.http.impl.DataHull;
import com.xue.imagecache.ImageCacheMannager;
import com.xue.oss.OssManager;
import com.xue.oss.SimpleOssManagerCallback;
import com.xue.support.view.TagGroup;
import com.xue.tools.GlideImageLoader;
import com.xue.tools.SimplePickHandlerCallBack;
import com.yancy.gallerypick.config.GalleryConfig;
import com.yancy.gallerypick.config.GalleryPick;

import java.util.List;

public class InfoActivity extends BaseActivity implements View.OnClickListener, TagGroup.OnTagClickListener {

    public static void launch(Context context) {
        Intent intent = new Intent(context, InfoActivity.class);
        context.startActivity(intent);
    }

    private long mUserTimeStamp;

    private ImageView mBackImageView;

    private TextView mTitleTextView;

    private TextView mRightTextView;

    private RelativeLayout mCoverRelativeLayout;

    private ImageView mCoverImageView;

    private ImageView mPhotoImageView;

    private TextView mHomeTownTextView;

    private TagGroup mTagGroup;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        initActionBar();
        findView();
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

    private void initActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM); //Enable自定义的View
            actionBar.setCustomView(R.layout.actionbar_simple);//设置自定义的布局：actionbar_custom
            mBackImageView = actionBar.getCustomView().findViewById(R.id.back);
            mTitleTextView = actionBar.getCustomView().findViewById(R.id.title);
            mRightTextView = actionBar.getCustomView().findViewById(R.id.right);

            mTitleTextView.setText("个人资料");
            mRightTextView.setText("保存");

            mBackImageView.setOnClickListener(this);
            mRightTextView.setOnClickListener(this);
        }
    }

    private void findView() {
        mCoverRelativeLayout = findViewById(R.id.coverLayout);
        mCoverImageView = findViewById(R.id.cover);
        mPhotoImageView = findViewById(R.id.photo);

        mHomeTownTextView = findViewById(R.id.homeTown);

        mTagGroup = findViewById(R.id.tagGroup);

        UITool.zoomViewByWidth(320, 320, mCoverImageView);

        mCoverRelativeLayout.setOnClickListener(this);
        mTagGroup.setOnTagClickListener(this);
    }

    private void init() {
        User user = BaseApplication.get().getUser();
        if (mUserTimeStamp != user.getTimeStamp()) {
            UserDetailInfo userDetailInfo = user.getUserDetailInfo();
            if (userDetailInfo != null) {
                ImageCacheMannager.loadImage(this, userDetailInfo.getCover(), mCoverImageView, false);
                ImageCacheMannager.loadImage(this, userDetailInfo.getProfile(), mPhotoImageView, true);

                mHomeTownTextView.setText(userDetailInfo.getHomeTownName());
            }

            UserTagInfo userTagInfo = user.getUserTagInfo();
            if (userTagInfo != null && userTagInfo.size() > 0) {
                mTagGroup.setTags(userTagInfo.toStringArray());
            }
        }

        mUserTimeStamp = user.getTimeStamp();
    }

    public void goDescribe(View view) {
        DescribeActivity.launch(this);
    }

    public void goGallery(View view) {
        MyGalleryActivity.launch(this);
    }

    public void goWork(View view) {
        WorkListActivity.launch(this);
    }

    public void goEducation(View view) {
        EducationListActivity.launch(this);
    }

    public void getHomeTown(View view) {
        CityListActivity.launch(this, CityListActivity.HomeTown);
    }

    public void getLocation(View view) {
        CityListActivity.launch(this, CityListActivity.Location);
    }

    public void getTag(View view) {
        TagActivity.launch(this, null);
    }

    @Override
    public void onClick(View v) {
        if (mBackImageView == v) {
            finish();
        } else if (mRightTextView == v) {

        } else if (mCoverRelativeLayout == v) {
            pickPhoto();
        }
    }

    public void pickPhoto() {
        SimplePickHandlerCallBack iHandlerCallBack = new SimplePickHandlerCallBack() {

            @Override
            public void onSuccess(List<String> photoList) {
                if (photoList != null && photoList.size() > 0) {
                    String photoPath = photoList.get(0);
                    ImageCacheMannager.loadImage(InfoActivity.this, photoPath, mCoverImageView, false);

                    new InfoActivity.UploadTask(InfoActivity.this, photoPath);
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
    public void onTagClick(String tag) {
        UserTagInfo userTagInfo = BaseApplication.get().getUser().getUserTagInfo();
        TagActivity.launch(this, userTagInfo.findTagByName(tag));
    }

    private static class UploadTask extends HttpAsyncTask<UserDetailInfo> {

        private String resultPath;

        public UploadTask(Context context, String photoPath) {
            super(context);
            OssManager.get().upload(photoPath, callback , true);
        }

        @Override
        public DataHull<UserDetailInfo> doInBackground() {
            return HttpApi.updateUserDetailInfo(null, null, null, null, resultPath, null);
        }

        @Override
        public void onPostExecute(int updateId, UserDetailInfo result) {
            ToastTool.show(context, "封面上传成功");
            BaseApplication.get().setUserDetailInfo(result);
        }

        @Override
        public void dataNull(int updateId, String errMsg) {
            super.dataNull(updateId, errMsg);
            ToastTool.show(context, "封面上传失败");
        }

        @Override
        public void netNull() {
            super.netNull();
            ToastTool.show(context, "封面上传失败");
        }

        @Override
        public void netErr(int updateId, String errMsg) {
            super.netErr(updateId, errMsg);
            ToastTool.show(context, "封面上传失败");
        }

        private OssManager.Callback callback = new SimpleOssManagerCallback() {

            @Override
            public void onSuccess(String file, String resultName) {
                UploadTask.this.resultPath = resultName;
                UploadTask.this.start();
            }


            @Override
            public void onInitFailure() {
                ToastTool.show(context, "封面上传失败");
            }

            @Override
            public void onFailure(String file, int code) {
                ToastTool.show(context, "封面上传失败");
            }
        };
    }
}
