package com.xue.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.xue.BaseApplication;
import com.xue.R;
import com.xue.asyns.HttpAsyncTask;
import com.xue.bean.UserEducationInfo;
import com.xue.http.HttpApi;
import com.xue.http.impl.DataHull;
import com.xue.support.view.DatePicker;

public class EducationActivity extends BaseActivity implements View.OnClickListener, TextWatcher {

    public static void launch(Context context, UserEducationInfo.Education education) {
        Intent intent = new Intent(context, EducationActivity.class);
        intent.putExtra("education", education);
        context.startActivity(intent);
    }

    private ImageView mBackImageView;

    private TextView mTitleTextView;

    private TextView mRightTextView;

    private EditText mSchoolEditText;

    private EditText mMajorEditText;

    private EditText mAcademicEditText;

    private EditText mBeginAtEditText;

    private EditText mEndAtEditText;

    private EditText mDescribeEditText;

    private TextView mDeleteTextView;

    private UserEducationInfo.Education mEducation;

    private int mSchoolFlag = 0;

    private int mMajorFlag = 0;

    private int mAcademicFlag = 0;

    private int mBeginAtFlag = 0;

    private int mEndAtFlag = 0;

    private int mDescribeFlag = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_education);

        readExtra();
        initActionBar();
        findView();

        init();
    }

    @Override
    protected void onResume() {
        super.onResume();
        bindWatcher();
    }

    @Override
    protected void onPause() {
        super.onPause();
        unBindWatcher();
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
        mSchoolEditText = findViewById(R.id.school);
        mMajorEditText = findViewById(R.id.major);
        mAcademicEditText = findViewById(R.id.academic);
        mBeginAtEditText = findViewById(R.id.beginAt);
        mEndAtEditText = findViewById(R.id.endAt);
        mDescribeEditText = findViewById(R.id.describe);
        mDeleteTextView = findViewById(R.id.delete);

        mBeginAtEditText.setOnClickListener(this);
        mEndAtEditText.setOnClickListener(this);
        mDeleteTextView.setOnClickListener(this);
    }

    private void bindWatcher() {
        mSchoolEditText.addTextChangedListener(this);
        mMajorEditText.addTextChangedListener(this);
        mAcademicEditText.addTextChangedListener(this);
        mBeginAtEditText.addTextChangedListener(this);
        mEndAtEditText.addTextChangedListener(this);
        mDescribeEditText.addTextChangedListener(this);
    }

    private void unBindWatcher() {
        mSchoolEditText.removeTextChangedListener(this);
        mMajorEditText.removeTextChangedListener(this);
        mAcademicEditText.removeTextChangedListener(this);
        mBeginAtEditText.removeTextChangedListener(this);
        mEndAtEditText.removeTextChangedListener(this);
        mDescribeEditText.removeTextChangedListener(this);
    }

    private void init() {
        if (mEducation != null) {
            mSchoolEditText.setText(mEducation.getSchoolName());
            mMajorEditText.setText(mEducation.getMajorName());
            mAcademicEditText.setText(mEducation.getAcademicName());
            mBeginAtEditText.setText(mEducation.getBeginAt());
            mEndAtEditText.setText(mEducation.getEndAt());
            mDescribeEditText.setText(mEducation.getDescribe());

            mSchoolFlag = 1;
            mMajorFlag = 1;
            mAcademicFlag = 1;
            mBeginAtFlag = 1;
            mEndAtFlag = 1;
            mDescribeFlag = 1;

            mDeleteTextView.setVisibility(View.VISIBLE);
        } else {
            mDeleteTextView.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        if (mRightTextView == v) {
            String school = mSchoolEditText.getText().toString();
            String major = mMajorEditText.getText().toString();
            String academic = mAcademicEditText.getText().toString();
            String describe = mDescribeEditText.getText().toString();
            String beginAt = mBeginAtEditText.getText().toString();
            String endAt = mEndAtEditText.getText().toString();
            if (mEducation == null) {
                new CreateTask(this, school, major, academic, describe, beginAt, endAt).start();
            } else {

            }
        } else if (mDeleteTextView == v) {

        } else if (mBeginAtEditText == v) {
            DatePicker datePicker = new DatePicker(this, 1970, 2018, false);
            datePicker.show();
            datePicker.setCallback(new DatePicker.SelectedCallback() {
                @Override
                public void onSelected(String year, String month) {
                    String date = year + "/" + month;
                    mBeginAtEditText.setText(date);
                }
            });
        } else if (mEndAtEditText == v) {
            DatePicker datePicker = new DatePicker(this, 1970, 2018, true);
            datePicker.show();
            datePicker.setCallback(new DatePicker.SelectedCallback() {
                @Override
                public void onSelected(String year, String month) {
                    String date;
                    if (!TextUtils.equals("至今", year)) {
                        date = year + "/" + month;
                    } else {
                        date = "至今";
                    }
                    mEndAtEditText.setText(date);
                }
            });
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
        String text = s.toString();
        if (mSchoolEditText.getText() == s) {
            if (TextUtils.isEmpty(text)) {
                mSchoolFlag = 0;
            } else {
                if (mEducation != null && TextUtils.equals(mEducation.getSchoolName(), text)) {
                    mSchoolFlag = 1;
                } else {
                    mSchoolFlag = 2;
                }
            }
        } else if (mMajorEditText.getText() == s) {
            if (TextUtils.isEmpty(text)) {
                mMajorFlag = 0;
            } else {
                if (mEducation != null && TextUtils.equals(mEducation.getMajorName(), text)) {
                    mMajorFlag = 1;
                } else {
                    mMajorFlag = 2;
                }
            }
        } else if (mAcademicEditText.getText() == s) {
            if (TextUtils.isEmpty(text)) {
                mAcademicFlag = 0;
            } else {
                if (mEducation != null && TextUtils.equals(mEducation.getAcademicName(), text)) {
                    mAcademicFlag = 1;
                } else {
                    mAcademicFlag = 2;
                }
            }
        } else if (mBeginAtEditText.getText() == s) {
            if (TextUtils.isEmpty(text)) {
                mBeginAtFlag = 0;
            } else {
                if (mEducation != null && TextUtils.equals(mEducation.getBeginAt(), text)) {
                    mBeginAtFlag = 1;
                } else {
                    mBeginAtFlag = 2;
                }
            }
        } else if (mEndAtEditText.getText() == s) {
            if (TextUtils.isEmpty(text)) {
                mEndAtFlag = 0;
            } else {
                if (mEducation != null && TextUtils.equals(mEducation.getBeginAt(), text)) {
                    mEndAtFlag = 1;
                } else {
                    mEndAtFlag = 2;
                }
            }
        } else if (mDescribeEditText.getText() == s) {
            if (TextUtils.isEmpty(text)) {
                mDescribeFlag = 0;
            } else {
                if (mEducation != null && TextUtils.equals(mEducation.getDescribe(), text)) {
                    mDescribeFlag = 1;
                } else {
                    mDescribeFlag = 2;
                }
            }
        }

        if (mEducation != null) {
            if ((mSchoolFlag != 0 && mMajorFlag != 0 && mAcademicFlag != 0 && mBeginAtFlag != 0 && mEndAtFlag != 0)
                    && (mSchoolFlag == 2 || mMajorFlag == 2 || mAcademicFlag == 2 || mDescribeFlag == 2 || mBeginAtFlag == 2 && mEndAtFlag == 2)) {
                mRightTextView.setVisibility(View.VISIBLE);
            } else {
                mRightTextView.setVisibility(View.GONE);
            }
        } else {
            if (mSchoolFlag == 2 && mMajorFlag == 2 && mAcademicFlag == 2 && mBeginAtFlag == 2 && mEndAtFlag == 2) {
                mRightTextView.setVisibility(View.VISIBLE);
            } else {
                mRightTextView.setVisibility(View.GONE);
            }
        }
    }

    private class CreateTask extends HttpAsyncTask<UserEducationInfo> {

        private String school;

        private String major;

        private String academic;

        private String descirbe;

        private String beginAt;

        private String endAt;

        public CreateTask(Context context, String school, String major, String academic, String descirbe, String beginAt, String endAt) {
            super(context);

            this.school = school;
            this.major = major;
            this.academic = academic;
            this.descirbe = descirbe;
            this.beginAt = beginAt;
            this.endAt = endAt;
        }

        @Override
        public DataHull<UserEducationInfo> doInBackground() {
            return HttpApi.createUserEducation(school, major, academic, descirbe, beginAt, endAt);
        }

        @Override
        public void onPostExecute(int updateId, UserEducationInfo result) {
            BaseApplication.get().getUser().setUserEducationInfo(result);
            finish();
        }
    }

}
