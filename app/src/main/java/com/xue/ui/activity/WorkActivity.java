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
import com.xue.bean.IndustryList;
import com.xue.bean.UserWorkInfo;
import com.xue.http.HttpApi;
import com.xue.http.impl.DataHull;
import com.xue.support.view.DatePicker;

public class WorkActivity extends BaseActivity implements View.OnClickListener, TextWatcher {

    public static void launch(Context context, UserWorkInfo.Work work) {
        Intent intent = new Intent(context, WorkActivity.class);
        intent.putExtra("work", work);
        context.startActivity(intent);
    }

    private ImageView mBackImageView;

    private TextView mTitleTextView;

    private TextView mRightTextView;

    private EditText mCompanyEditText;

    private EditText mIndustryEditText;

    private EditText mPositionEditText;

    private EditText mDirectionEditText;

    private EditText mBeginAtEditText;

    private EditText mEndAtEditText;

    private EditText mDescribeEditText;

    private TextView mDeleteTextView;

    private UserWorkInfo.Work mWork;

    private int mCompanyFlag = 0;

    private int mIndustryFlag = 0;

    private int mPositionFlag = 0;

    private int mDirectionFlag = 0;

    private int mBeginAtFlag = 0;

    private int mEndAtFlag = 0;

    private int mDescribeFlag = 0;

    private IndustryList.Industry mIndustry;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work);
        readExtra();
        initActionBar();
        findView();
        init();
    }

    @Override
    protected void onResume() {
        super.onResume();
        bindWatcher();

        if (mIndustry != null) {
            mIndustryEditText.setText(mIndustry.getName());
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        unBindWatcher();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            mIndustry = (IndustryList.Industry) data.getSerializableExtra("industry");
        }
    }

    private void readExtra() {
        mWork = (UserWorkInfo.Work) getIntent().getSerializableExtra("work");
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

            mTitleTextView.setText("工作经历");
            mRightTextView.setText("保存");
        }
    }

    private void findView() {
        mCompanyEditText = findViewById(R.id.company);
        mIndustryEditText = findViewById(R.id.industry);
        mPositionEditText = findViewById(R.id.position);
        mDirectionEditText = findViewById(R.id.direction);
        mBeginAtEditText = findViewById(R.id.beginAt);
        mEndAtEditText = findViewById(R.id.endAt);
        mDescribeEditText = findViewById(R.id.describe);
        mDeleteTextView = findViewById(R.id.delete);

        mIndustryEditText.setOnClickListener(this);
        mBeginAtEditText.setOnClickListener(this);
        mEndAtEditText.setOnClickListener(this);
        mDeleteTextView.setOnClickListener(this);
    }

    private void bindWatcher() {
        mCompanyEditText.addTextChangedListener(this);
        mIndustryEditText.addTextChangedListener(this);
        mPositionEditText.addTextChangedListener(this);
        mDirectionEditText.addTextChangedListener(this);
        mBeginAtEditText.addTextChangedListener(this);
        mEndAtEditText.addTextChangedListener(this);
        mDescribeEditText.addTextChangedListener(this);
    }

    private void unBindWatcher() {
        mCompanyEditText.removeTextChangedListener(this);
        mIndustryEditText.removeTextChangedListener(this);
        mPositionEditText.removeTextChangedListener(this);
        mDirectionEditText.removeTextChangedListener(this);
        mBeginAtEditText.removeTextChangedListener(this);
        mEndAtEditText.removeTextChangedListener(this);
        mDescribeEditText.removeTextChangedListener(this);
    }

    private void init() {
        if (mWork != null) {
            mCompanyEditText.setText(mWork.getCompanyName());
            mIndustryEditText.setText(mWork.getIndustryName());
            mPositionEditText.setText(mWork.getPositionName());
            mDirectionEditText.setText(mWork.getDirectionName());
            mBeginAtEditText.setText(mWork.getBeginAt());
            mEndAtEditText.setText(mWork.getEndAt());
            mDescribeEditText.setText(mWork.getDescribe());

            mCompanyFlag = 1;
            mIndustryFlag = 1;
            mPositionFlag = 1;
            mDirectionFlag = 1;
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
            String companyName = mCompanyEditText.getText().toString();
            String industryId;
            if (mIndustry != null) {
                industryId = mIndustry.getId();
            } else {
                industryId = mWork.getIndustryId();
            }
            String positionName = mPositionEditText.getText().toString();
            String directionName = mDirectionEditText.getText().toString();
            String describe = mDescribeEditText.getText().toString();
            String beginAt = mBeginAtEditText.getText().toString();
            String endAt = mEndAtEditText.getText().toString();

            if (mWork == null) {
                new CreateTask(this, companyName, industryId, positionName, directionName, describe, beginAt, endAt).start();
            } else {
                new UpdateTask(this, mWork.getId(), companyName, industryId, positionName, directionName, describe, beginAt, endAt).start();
            }
        } else if (mDeleteTextView == v) {
            if (mWork != null) {
                new DeleteTask(this, mWork.getId()).start();
            }
        } else if (mIndustryEditText == v) {
            IndustryListActivity.launch(this);
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
        if (mCompanyEditText.getText() == s) {
            if (TextUtils.isEmpty(text)) {
                mCompanyFlag = 0;
            } else {
                if (mWork != null && TextUtils.equals(mWork.getCompanyName(), text)) {
                    mCompanyFlag = 1;
                } else {
                    mCompanyFlag = 2;
                }
            }
        } else if (mIndustryEditText.getText() == s) {
            if (TextUtils.isEmpty(text)) {
                mIndustryFlag = 0;
            } else {
                if (mWork != null && TextUtils.equals(mWork.getIndustryName(), text)) {
                    mIndustryFlag = 1;
                } else {
                    mIndustryFlag = 2;
                }
            }
        } else if (mPositionEditText.getText() == s) {
            if (TextUtils.isEmpty(text)) {
                mPositionFlag = 0;
            } else {
                if (mWork != null && TextUtils.equals(mWork.getPositionName(), text)) {
                    mPositionFlag = 1;
                } else {
                    mPositionFlag = 2;
                }
            }
        } else if (mDirectionEditText.getText() == s) {
            if (TextUtils.isEmpty(text)) {
                mDirectionFlag = 0;
            } else {
                if (mWork != null && TextUtils.equals(mWork.getDirectionName(), text)) {
                    mDirectionFlag = 1;
                } else {
                    mDirectionFlag = 2;
                }
            }
        } else if (mBeginAtEditText.getText() == s) {
            if (TextUtils.isEmpty(text)) {
                mBeginAtFlag = 0;
            } else {
                if (mWork != null && TextUtils.equals(mWork.getBeginAt(), text)) {
                    mBeginAtFlag = 1;
                } else {
                    mBeginAtFlag = 2;
                }
            }
        } else if (mEndAtEditText.getText() == s) {
            if (TextUtils.isEmpty(text)) {
                mEndAtFlag = 0;
            } else {
                if (mWork != null && TextUtils.equals(mWork.getBeginAt(), text)) {
                    mEndAtFlag = 1;
                } else {
                    mEndAtFlag = 2;
                }
            }
        } else if (mDescribeEditText.getText() == s) {
            if (TextUtils.isEmpty(text)) {
                mDescribeFlag = 0;
            } else {
                if (mWork != null && TextUtils.equals(mWork.getDescribe(), text)) {
                    mDescribeFlag = 1;
                } else {
                    mDescribeFlag = 2;
                }
            }
        }

        if (mWork != null) {
            if ((mCompanyFlag != 0 && mIndustryFlag != 0 && mPositionFlag != 0 && mDirectionFlag != 0 && mBeginAtFlag != 0 && mEndAtFlag != 0)
                    && (mCompanyFlag == 2 || mIndustryFlag == 2 || mPositionFlag == 2 || mDirectionFlag == 2 || mDescribeFlag == 2 || mBeginAtFlag == 2 && mEndAtFlag == 2)) {
                mRightTextView.setVisibility(View.VISIBLE);
            } else {
                mRightTextView.setVisibility(View.GONE);
            }
        } else {
            if (mCompanyFlag == 2 && mIndustryFlag == 2 && mPositionFlag == 2 && mDirectionFlag == 2 && mBeginAtFlag == 2 && mEndAtFlag == 2) {
                mRightTextView.setVisibility(View.VISIBLE);
            } else {
                mRightTextView.setVisibility(View.GONE);
            }
        }
    }


    private class CreateTask extends HttpAsyncTask<UserWorkInfo> {

        private String companyName;

        private String industryId;

        private String positionName;

        private String directionName;

        private String describe;

        private String beginAt;

        private String endAt;

        public CreateTask(Context context, String companyName, String industryId, String positionName, String directionName, String describe, String beginAt, String endAt) {
            super(context);

            this.companyName = companyName;
            this.industryId = industryId;
            this.positionName = positionName;
            this.directionName = directionName;
            this.describe = describe;
            this.beginAt = beginAt;
            this.endAt = endAt;
        }

        @Override
        public DataHull<UserWorkInfo> doInBackground() {
            return HttpApi.createUserWork(companyName, industryId, positionName, directionName, describe, beginAt, endAt);
        }

        @Override
        public void onPostExecute(int updateId, UserWorkInfo result) {
            BaseApplication.get().getUser().setUserWorkInfo(result);
            finish();
        }
    }

    private class UpdateTask extends HttpAsyncTask<UserWorkInfo> {

        private String id;

        private String companyName;

        private String industryId;

        private String positionName;

        private String directionName;

        private String describe;

        private String beginAt;

        private String endAt;

        public UpdateTask(Context context, String id, String companyName, String industryId, String positionName, String directionName, String describe, String beginAt, String endAt) {
            super(context);

            this.id = id;
            this.companyName = companyName;
            this.industryId = industryId;
            this.positionName = positionName;
            this.directionName = directionName;
            this.describe = describe;
            this.beginAt = beginAt;
            this.endAt = endAt;
        }

        @Override
        public DataHull<UserWorkInfo> doInBackground() {
            return HttpApi.updateUserWork(id, companyName, industryId, positionName, directionName, describe, beginAt, endAt);
        }

        @Override
        public void onPostExecute(int updateId, UserWorkInfo result) {
            BaseApplication.get().getUser().setUserWorkInfo(result);
            finish();
        }
    }

    private class DeleteTask extends HttpAsyncTask<UserWorkInfo> {

        private String id;

        public DeleteTask(Context context, String id) {
            super(context);

            this.id = id;
        }

        @Override
        public DataHull<UserWorkInfo> doInBackground() {
            return HttpApi.deleteUserWork(id);
        }

        @Override
        public void onPostExecute(int updateId, UserWorkInfo result) {
            BaseApplication.get().getUser().setUserWorkInfo(result);
            finish();
        }
    }
}
