package com.xue.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.xue.BaseApplication;
import com.xue.R;
import com.xue.bean.User;
import com.xue.bean.UserDetailInfo;
import com.xue.imagecache.ImageCacheMannager;

public class BaseInfoActivity extends BaseActivity implements View.OnClickListener {

    public static void launch(Context context) {
        Intent intent = new Intent(context, BaseInfoActivity.class);
        context.startActivity(intent);
    }

    private ImageView mBackImageView;

    private TextView mTitleTextView;

    private ImageView mPhotoImageView;

    private TextView mRealNameTextView;

    private TextView mGenderTextView;

    private TextView mUidTextView;

    private TextView mCellphoneTextView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base_info);

        initActionBar();
        findView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        init();
    }

    private void initActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM); //Enable自定义的View
            actionBar.setCustomView(R.layout.actionbar_simple);//设置自定义的布局：actionbar_custom
            mBackImageView = actionBar.getCustomView().findViewById(R.id.back);
            mTitleTextView = actionBar.getCustomView().findViewById(R.id.title);
            mBackImageView.setOnClickListener(this);

            mTitleTextView.setText("基本资料");
        }
    }

    private void findView() {
        mPhotoImageView = findViewById(R.id.photo);
        mRealNameTextView = findViewById(R.id.realName);
        mGenderTextView = findViewById(R.id.gender);
        mUidTextView = findViewById(R.id.uid);
        mCellphoneTextView = findViewById(R.id.cellphone);

    }

    private void init() {
        User user = BaseApplication.get().getUser();

        ImageCacheMannager.loadImage(this, R.drawable.photo_test, mPhotoImageView, true);
        mUidTextView.setText(user.getUserBase().getUid());
        mCellphoneTextView.setText(user.getUserBase().getCellphone());

        UserDetailInfo userDetailInfo = user.getUserDetailInfo();
        if (userDetailInfo != null) {
            mRealNameTextView.setText(userDetailInfo.getRealName());
            mGenderTextView.setText(userDetailInfo.getGenderName());
        }
    }

    public void goModifyName(View view) {
        ModifyNameActivity.launch(this);
    }

    @Override
    public void onClick(View v) {
        if (mBackImageView == v) {
            finish();
        }
    }
}
