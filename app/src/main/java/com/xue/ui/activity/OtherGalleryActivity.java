package com.xue.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.elianshang.tools.UITool;
import com.previewlibrary.GPreviewBuilder;
import com.xue.R;
import com.xue.adapter.AdapterOnItemClickCallback;
import com.xue.adapter.OtherGalleryGridAdapter;
import com.xue.bean.PreviewPicture;
import com.xue.support.view.GridItemDecoration;

import java.util.ArrayList;

public class OtherGalleryActivity extends BaseActivity implements AdapterOnItemClickCallback<String> {


    public static void launch(Context context, ArrayList<String> pics) {
        Intent intent = new Intent(context, OtherGalleryActivity.class);
        intent.putExtra("pics", pics);
        context.startActivity(intent);
    }

    private TextView mTitleTextView;

    private RecyclerView mRecyclerView;

    private TextView mEmptyTextView;

    private OtherGalleryGridAdapter mAdapter;

    private ArrayList<String> mPictureList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_gallery);

        readExtra();
        initActionBar();
        findView();

        fillData();
    }

    private void readExtra() {
        mPictureList = getIntent().getStringArrayListExtra("pics");
    }

    private void initActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM); //Enable自定义的View
            actionBar.setCustomView(R.layout.actionbar_simple);//设置自定义的布局：actionbar_custom
            mTitleTextView = actionBar.getCustomView().findViewById(R.id.title);

            mTitleTextView.setText("他的相册");
        }
    }

    private void findView() {
        mEmptyTextView = findViewById(R.id.empty);
        mRecyclerView = findViewById(R.id.recyclerView);

        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 4));
        mRecyclerView.addItemDecoration(new GridItemDecoration(UITool.dipToPx(this, 3)));

    }

    private void fillData() {
        if (mPictureList == null || mPictureList.size() == 0) {
            mEmptyTextView.setVisibility(View.VISIBLE);
            mRecyclerView.setVisibility(View.GONE);
        } else {
            mEmptyTextView.setVisibility(View.GONE);
            mRecyclerView.setVisibility(View.VISIBLE);
        }

        if (mAdapter == null) {
            mAdapter = new OtherGalleryGridAdapter();

            mRecyclerView.setAdapter(mAdapter);
            mAdapter.setCallback(this);
        }

        mAdapter.setDataList(mPictureList);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(String url, View view) {
        int i = mPictureList.indexOf(url);
        GPreviewBuilder.from(this)
                .setData(getPreviewPictures(view))
                .setCurrentIndex(i)
                .setDrag(true)
                .setType(GPreviewBuilder.IndicatorType.Number)
                .start();
    }

    private ArrayList<PreviewPicture> getPreviewPictures(View view) {
        ArrayList<PreviewPicture> mThumbViewInfoList = new ArrayList();
        for (int i = 0; i < mPictureList.size(); i++) {
            PreviewPicture previewPicture = new PreviewPicture(mPictureList.get(i));
            Rect bounds = new Rect();
            view.getGlobalVisibleRect(bounds);
            previewPicture.setBounds(bounds);

            mThumbViewInfoList.add(previewPicture);
        }
        return mThumbViewInfoList;
    }
}
