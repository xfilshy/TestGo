package com.xue.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xue.R;
import com.xue.bean.UserEducationInfo;

public class EducationActivity extends BaseActivity implements View.OnClickListener {

    public static void launch(Context context, UserEducationInfo.Education education) {
        Intent intent = new Intent(context, EducationActivity.class);
        intent.putExtra("education", education);
        context.startActivity(intent);
    }

    private ImageView mBackImageView;

    private TextView mTitleTextView;

    private TextView mRightTextView;

    private TextView mSchoolTextView;

    private TextView mSpecialityTextView;

    private TextView mDegreeTextView;

    private TextView mStartYearTextView;

    private TextView mStartMonthTextView;

    private LinearLayout mEndLinearLayout;

    private TextView mEndYearTextView;

    private TextView mEndMonthTextView;

    private CheckBox mSinceCheckBox;

    private TextView mDescribeTextView;

    private TextView mDeleteTextView;

    private UserEducationInfo.Education mEducation;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_education);

        readExtra();
        initActionBar();
        findView();

        init();
    }

    private void readExtra() {
        mEducation = (UserEducationInfo.Education) getIntent().getSerializableExtra("education");
    }

    private void initActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM); //Enable自定义的View
            actionBar.setCustomView(R.layout.actionbar_simple);//设置自定义的布局：actionbar_custom
            mBackImageView = actionBar.getCustomView().findViewById(R.id.back);
            mTitleTextView = actionBar.getCustomView().findViewById(R.id.title);
            mRightTextView = actionBar.getCustomView().findViewById(R.id.right);
            mRightTextView.setOnClickListener(this);

            mTitleTextView.setText("教育经历");
            mRightTextView.setText("保存");
        }
    }

    private void findView() {
        mSchoolTextView = findViewById(R.id.school);
        mSpecialityTextView = findViewById(R.id.speciality);
        mDegreeTextView = findViewById(R.id.degree);
        mStartYearTextView = findViewById(R.id.startYear);
        mStartMonthTextView = findViewById(R.id.startMonth);
        mEndLinearLayout = findViewById(R.id.endLayout);
        mEndYearTextView = findViewById(R.id.endYear);
        mEndMonthTextView = findViewById(R.id.endMonth);
        mSinceCheckBox = findViewById(R.id.since);
        mDescribeTextView = findViewById(R.id.describe);
        mDeleteTextView = findViewById(R.id.delete);

        mDeleteTextView.setOnClickListener(this);
    }

    private void init() {
        if (mEducation != null) {
            mSchoolTextView.setText(mEducation.getSchool());
            mSpecialityTextView.setText(mEducation.getSpeciality());
            mDegreeTextView.setText(mEducation.getDegree());
            mDescribeTextView.setText(mEducation.getDescribe());

            mDeleteTextView.setVisibility(View.VISIBLE);
        } else {
            mDeleteTextView.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        if (mRightTextView == v) {

        } else if (mDeleteTextView == v) {

        }
    }
}
