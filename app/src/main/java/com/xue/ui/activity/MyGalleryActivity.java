package com.xue.ui.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.elianshang.tools.ToastTool;
import com.elianshang.tools.UITool;
import com.previewlibrary.GPreviewBuilder;
import com.xue.R;
import com.xue.adapter.AdapterOnItemClickCallback;
import com.xue.adapter.AdapterOnItemLongClickCallback;
import com.xue.adapter.MyGalleryGridAdapter;
import com.xue.asyns.HttpAsyncTask;
import com.xue.bean.MomentInfoList;
import com.xue.bean.PreviewPicture;
import com.xue.http.HttpApi;
import com.xue.http.impl.DataHull;
import com.xue.oss.OssManager;
import com.xue.oss.SimpleOssManagerCallback;
import com.xue.support.view.GridItemDecoration;
import com.xue.tools.GlideImageLoader;
import com.xue.tools.SimplePickHandlerCallBack;
import com.yancy.gallerypick.config.GalleryConfig;
import com.yancy.gallerypick.config.GalleryPick;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MyGalleryActivity extends BaseActivity implements View.OnClickListener, AdapterOnItemClickCallback<MomentInfoList.MomentRes>, AdapterOnItemLongClickCallback<MomentInfoList.MomentRes> {


    public static void launch(Context context) {
        Intent intent = new Intent(context, MyGalleryActivity.class);
        context.startActivity(intent);
    }

    private ImageView mBackImageView;

    private TextView mTitleTextView;

    private TextView mRightTextView;

    private RecyclerView mRecyclerView;

    private MyGalleryGridAdapter mAdapter;

    private String mMomentId;

    private int mOriginalSize;

    private boolean mDeleteFlag;

    private ArrayList<MomentInfoList.MomentRes> mMomentResList = new ArrayList();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_gallery);

        initActionBar();
        findView();

        fillData();
        new GetTask(this).start();
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

            mTitleTextView.setText("相册");
            mRightTextView.setText("保存");
        }
    }

    private void findView() {
        mRecyclerView = findViewById(R.id.recyclerView);

        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 4));
        mRecyclerView.addItemDecoration(new GridItemDecoration(UITool.dipToPx(this, 3)));

    }

    private void fillData() {
        checkResChange();

        if (mAdapter == null) {
            mAdapter = new MyGalleryGridAdapter();

            mRecyclerView.setAdapter(mAdapter);
            mAdapter.setCallback(this);
            mAdapter.setLongClickCallback(this);
        }

        mAdapter.setDataList(mMomentResList);
        mAdapter.notifyDataSetChanged();
    }

    private void checkResChange() {
        if (mOriginalSize != mMomentResList.size() || mDeleteFlag) {
            mRightTextView.setVisibility(View.VISIBLE);
        } else {
            mRightTextView.setVisibility(View.GONE);
        }

    }

    @Override
    public void onClick(View v) {
        if (mRightTextView == v) {
            new SaveTask(this, mMomentId, null, mMomentResList);
        }
    }

    @Override
    public void onItemClick(MomentInfoList.MomentRes momentRes, View view) {
        if (momentRes == null) {
            goPickPhoto();
        } else {
            int i = mMomentResList.indexOf(momentRes);
            GPreviewBuilder.from(this)
                    .setData(getPreviewPictures(view))
                    .setCurrentIndex(i)
                    .setDrag(true)
                    .setType(GPreviewBuilder.IndicatorType.Number)
                    .start();
        }
    }

    @Override
    public void onItemLongClick(final MomentInfoList.MomentRes momentRes, View view) {
        AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setTitle("提示")
                .setMessage("确认删除图片")
                .setNegativeButton("取消", null)
                .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mDeleteFlag = true;
                        mMomentResList.remove(momentRes);
                        fillData();
                    }
                }).create();
        alertDialog.show();
    }

    private ArrayList<PreviewPicture> getPreviewPictures(View view) {
        ArrayList<PreviewPicture> mThumbViewInfoList = new ArrayList();
        for (int i = 0; i < mMomentResList.size(); i++) {
            PreviewPicture previewPicture = new PreviewPicture(mMomentResList.get(i).getUrl());
            Rect bounds = new Rect();
            view.getGlobalVisibleRect(bounds);
            previewPicture.setBounds(bounds);

            mThumbViewInfoList.add(previewPicture);
        }
        return mThumbViewInfoList;
    }

    public void goPickPhoto() {
        SimplePickHandlerCallBack iHandlerCallBack = new SimplePickHandlerCallBack() {

            @Override
            public void onSuccess(List<String> photoList) {
                Log.e("xue", "成功了" + photoList);
                addToRes(photoList);

                fillData();
            }
        };
        GalleryConfig galleryConfig = new GalleryConfig.Builder()
                .imageLoader(new GlideImageLoader())
                .iHandlerCallBack(iHandlerCallBack)
                .provider("com.xue.fileprovider")
                .multiSelect(true, 9 - mMomentResList.size())
                .pathList(resToStringArray())
                .isShowCamera(true)
                .filePath("/Gallery/Pictures")
                .build();

        GalleryPick.getInstance().setGalleryConfig(galleryConfig).open(this);
    }

    /**
     * 将本地资源加入
     */
    private void addToRes(List<String> photoList) {
        ArrayList<MomentInfoList.MomentRes> tmp = new ArrayList();

        for (MomentInfoList.MomentRes res : mMomentResList) {
            if (!TextUtils.isEmpty(res.getResId())) {
                tmp.add(res);
            }
        }

        for (String s : photoList) {
            MomentInfoList.MomentRes res = new MomentInfoList.MomentRes();
            res.setUrl(s);

            tmp.add(res);
        }

        mMomentResList.clear();
        mMomentResList.addAll(tmp);
        checkResChange();
    }

    private ArrayList<String> resToStringArray() {
        ArrayList<String> stringList = new ArrayList();

        for (MomentInfoList.MomentRes res : mMomentResList) {
            if (TextUtils.isEmpty(res.getResId())) {
                stringList.add(res.getUrl());
            }
        }

        return stringList;
    }


    private class GetTask extends HttpAsyncTask<MomentInfoList> {

        public GetTask(Context context) {
            super(context , true , true);
        }

        @Override
        public DataHull<MomentInfoList> doInBackground() {
            return HttpApi.getMomentInfoList(null, "1", "1", null);
        }

        @Override
        public void onPostExecute(int updateId, MomentInfoList result) {
            if (result.size() > 0) {
                mMomentId = result.get(0).getId();
                mMomentResList.clear();
                mMomentResList.addAll(result.get(0).getResList());
                mOriginalSize = result.get(0).getResList().size();
                fillData();
            }
        }
    }

    private class SaveTask extends HttpAsyncTask<MomentInfoList.MomentInfo> {

        private String momentId;

        private String text;

        private ArrayList<MomentInfoList.MomentRes> resList;

        private ArrayList<String> pics;

        public SaveTask(Context context, String momentId, String text, ArrayList<MomentInfoList.MomentRes> resList) {
            super(context , true , true);
            this.momentId = momentId;
            this.text = text;
            this.resList = resList;
            init();

        }

        private void init() {
            ArrayList<String> list = new ArrayList();
            if (resList != null && resList.size() > 0) {
                for (MomentInfoList.MomentRes res : resList) {
                    if (TextUtils.isEmpty(res.getResId())) {
                        list.add(res.getUrl());
                    }
                }
            }

            if (list.isEmpty()) {
                start();
            } else {
                OssManager.get().upload(list, callback , false);
            }
        }

        @Override
        public DataHull<MomentInfoList.MomentInfo> doInBackground() {
            if (TextUtils.isEmpty(mMomentId)) {
                return HttpApi.createMomentInfo(text, toResListString());
            } else {
                return HttpApi.updateMomentInfo(momentId, text, toResListString());
            }
        }

        private String toResListString() {
            String resListString = null;
            JSONArray jsonArray = new JSONArray();
            try {
                if (resList != null) {
                    for (MomentInfoList.MomentRes res : resList) {
                        if (!TextUtils.isEmpty(res.getResId())) {
                            JSONObject object = new JSONObject();
                            object.put("type", res.getType());
                            object.put("res_id", res.getResId());

                            jsonArray.put(object);
                        }
                    }
                }

                if (pics != null) {
                    for (String p : pics) {
                        JSONObject object = new JSONObject();
                        object.put("type", "1");
                        object.put("res_id", p);

                        jsonArray.put(object);
                    }
                }

                resListString = jsonArray.toString();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return resListString;
        }

        @Override
        public void onPostExecute(int updateId, MomentInfoList.MomentInfo result) {
            mMomentId = result.getId();
            mMomentResList.clear();
            mMomentResList.addAll(result.getResList());
            mOriginalSize = result.getResList().size();
            mDeleteFlag = false ;
            fillData();

            ToastTool.show(context, "上传成功");
        }

        private OssManager.Callback callback = new SimpleOssManagerCallback() {

            @Override
            public void onSuccess(String file, String resultName) {
                if (pics == null) {
                    pics = new ArrayList();
                }
                pics.add(resultName);
            }

            @Override
            public void onInitFailure() {
                ToastTool.show(context, "初始化上传照片失败");
            }

            @Override
            public void onFailure(String file, int code) {
                ToastTool.show(context, "照片上传失败");
            }

            @Override
            public void onFinish() {
                super.onFinish();
                start();
            }
        };
    }
}
