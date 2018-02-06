package com.xue.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.elianshang.tools.UITool;
import com.previewlibrary.GPreviewBuilder;
import com.xue.R;
import com.xue.adapter.AdapterOnItemClickCallback;
import com.xue.adapter.GalleryGridAdapter;
import com.xue.bean.Gallery;
import com.xue.bean.PreviewPicture;
import com.xue.oss.OssManager;
import com.xue.support.view.GridItemDecoration;
import com.xue.tools.GlideImageLoader;
import com.xue.tools.SimplePickHandlerCallBack;
import com.yancy.gallerypick.config.GalleryConfig;
import com.yancy.gallerypick.config.GalleryPick;

import java.util.ArrayList;
import java.util.List;

public class GalleryActivity extends BaseActivity implements View.OnClickListener, AdapterOnItemClickCallback<Gallery.Picture>, OssManager.Callback {


    public static void launch(Context context) {
        Intent intent = new Intent(context, GalleryActivity.class);
        context.startActivity(intent);
    }

    private ImageView mBackImageView;

    private TextView mTitleTextView;

    private TextView mRightTextView;

    private RecyclerView mRecyclerView;

    private GalleryGridAdapter mAdapter;

    private Gallery mGallery = new Gallery();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);

        initActionBar();
        findView();
    }

    private void initActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM); //Enable自定义的View
            actionBar.setCustomView(R.layout.actionbar_simple);//设置自定义的布局：actionbar_custom
            mBackImageView = actionBar.getCustomView().findViewById(R.id.back);
            mTitleTextView = actionBar.getCustomView().findViewById(R.id.title);
            mRightTextView = actionBar.getCustomView().findViewById(R.id.right);
            mBackImageView.setOnClickListener(this);
            mRightTextView.setOnClickListener(this);
            mRightTextView.setVisibility(View.VISIBLE);

            mTitleTextView.setText("相册");
            mRightTextView.setText("保存");
        }
    }

    private void findView() {
        mRecyclerView = findViewById(R.id.recyclerView);

        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 4));
        mRecyclerView.addItemDecoration(new GridItemDecoration(UITool.dipToPx(this, 3)));

        if (mAdapter == null) {
            mAdapter = new GalleryGridAdapter();

            mRecyclerView.setAdapter(mAdapter);
            mAdapter.setDataList(mGallery);
            mAdapter.setCallback(this);
        }

        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        if (mBackImageView == v) {
            finish();
        }
    }

    public void goPickPhoto() {
        SimplePickHandlerCallBack iHandlerCallBack = new SimplePickHandlerCallBack() {

            @Override
            public void onSuccess(List<String> photoList) {
                Log.e("xue", "成功了" + photoList);
                Gallery gallery = new Gallery();
                for (String path : photoList) {
                    Gallery.Picture picture = new Gallery.Picture();
                    picture.setUrl(path);
                    gallery.add(picture);
                }
                mGallery.removeAll(gallery);
                mGallery.addAll(gallery);
                mAdapter.notifyDataSetChanged();

                OssManager.get().upload(photoList, GalleryActivity.this);
            }
        };
        GalleryConfig galleryConfig = new GalleryConfig.Builder()
                .imageLoader(new GlideImageLoader())
                .iHandlerCallBack(iHandlerCallBack)
                .provider("com.xue.fileprovider")
                .multiSelect(true, 9)
//                .pathList((List<String>) mImageList.clone())
                .isShowCamera(true)
                .filePath("/Gallery/Pictures")
                .build();

        GalleryPick.getInstance().setGalleryConfig(galleryConfig).open(this);
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
        Log.e("xue", "批量上传  成功  " + file);
    }

    @Override
    public void onFailure(String file, int code) {
        Log.e("xue", "批量上传  失败  " + file);
    }

    @Override
    public void onFinish() {
        Log.e("xue", "批量上传  完成");
    }

    @Override
    public void onItemClick(Gallery.Picture picture, View view) {
        if (picture == null) {
            goPickPhoto();
        } else {
            int i = mGallery.indexOf(picture);
            GPreviewBuilder.from(this)
                    .setData(getPreviewPictures(view))
                    .setCurrentIndex(i)
                    .setDrag(true)
                    .setType(GPreviewBuilder.IndicatorType.Number)
                    .start();
        }
    }

    private ArrayList<PreviewPicture> getPreviewPictures(View view) {
        ArrayList<PreviewPicture> mThumbViewInfoList = new ArrayList<>();
        for (int i = 0; i < mGallery.size(); i++) {
            PreviewPicture previewPicture = new PreviewPicture(mGallery.get(i).getUrl());
            Rect bounds = new Rect();
            view.getGlobalVisibleRect(bounds);
            previewPicture.setBounds(bounds);

            mThumbViewInfoList.add(previewPicture);
        }
        return mThumbViewInfoList;
    }

}
