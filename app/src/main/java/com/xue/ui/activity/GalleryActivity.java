package com.xue.ui.activity;

import android.content.Context;
import android.content.Intent;
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
import com.xue.R;
import com.xue.adapter.GalleryGridAdapter;
import com.xue.support.view.GridItemDecoration;
import com.xue.tools.GlideImageLoader;
import com.yancy.gallerypick.config.GalleryConfig;
import com.yancy.gallerypick.config.GalleryPick;
import com.yancy.gallerypick.inter.IHandlerCallBack;

import java.util.ArrayList;
import java.util.List;

public class GalleryActivity extends BaseActivity implements View.OnClickListener, GalleryGridAdapter.AddCallBack {


    public static void launch(Context context) {
        Intent intent = new Intent(context, GalleryActivity.class);
        context.startActivity(intent);
    }

    private ImageView mBackImageView;

    private TextView mTitleTextView;

    private TextView mRightTextView;

    private RecyclerView mRecyclerView;

    private GalleryGridAdapter mAdapter;

    private ArrayList<String> mImageList = new ArrayList();

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
            mAdapter = new GalleryGridAdapter(this);

            mRecyclerView.setAdapter(mAdapter);
            mAdapter.setDataList(mImageList);
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
        IHandlerCallBack iHandlerCallBack = new IHandlerCallBack() {
            @Override
            public void onStart() {
                Log.e("xue", "去选择了");
            }

            @Override
            public void onSuccess(List<String> photoList) {
                Log.e("xue", "成功了" + photoList);
                mImageList.clear();
                mImageList.addAll(photoList);
                Log.e("xue", "mImageList == " + mImageList);
                mAdapter.notifyDataSetChanged();
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
                .multiSelect(true, 9)
                .pathList((List<String>) mImageList.clone())
                .isShowCamera(true)
                .filePath("/Gallery/Pictures")
                .build();

        GalleryPick.getInstance().setGalleryConfig(galleryConfig).open(this);
    }

    @Override
    public void onClickAdd() {
        goPickPhoto();
    }
}