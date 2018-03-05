package com.xue.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.xue.BaseApplication;
import com.xue.R;
import com.xue.bean.User;
import com.xue.bean.UserDetailInfo;
import com.xue.imagecache.ImageCacheMannager;

public class BaseInfoActivity extends SwipeBackBaseActivity{

    public static void launch(Context context) {
        Intent intent = new Intent(context, BaseInfoActivity.class);
        context.startActivity(intent);
    }

    private ImageView mPhotoImageView;

    private TextView mRealNameTextView;

    private TextView mGenderTextView;

    private TextView mUidTextView;

    private TextView mCellphoneTextView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base_info);

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
        return null;
    }

    @Override
    protected void onResume() {
        super.onResume();
        init();
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

}
