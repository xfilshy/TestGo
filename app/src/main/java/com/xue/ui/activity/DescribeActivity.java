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

import com.elianshang.tools.EditTextTool;
import com.xue.BaseApplication;
import com.xue.R;
import com.xue.asyns.HttpAsyncTask;
import com.xue.bean.UserDetailInfo;
import com.xue.http.HttpApi;
import com.xue.http.impl.DataHull;

public class DescribeActivity extends BaseActivity implements View.OnClickListener, TextWatcher {

    public static void launch(Context context) {
        Intent intent = new Intent(context, DescribeActivity.class);
        context.startActivity(intent);
    }

    private ImageView mBackImageView;

    private TextView mTitleTextView;

    private TextView mRightTextView;

    private String mIntro;

    private EditText mIntroEditText;

    private TextView mNumberTextView;

    private int maxNumber = 500;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_describe);

        initActionBar();
        findView();

        init();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mIntroEditText.addTextChangedListener(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mIntroEditText.removeTextChangedListener(this);
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

            mTitleTextView.setText("独白");
            mRightTextView.setText("保存");
        }
    }

    private void findView() {
        mIntroEditText = findViewById(R.id.intro);
        mNumberTextView = findViewById(R.id.number);

        EditTextTool.setEmojiFilter(mIntroEditText);
    }

    private void init() {
        UserDetailInfo userDetailInfo = BaseApplication.get().getUser().getUserDetailInfo();
        if (userDetailInfo != null) {
            mIntro = userDetailInfo.getIntro();
            mIntroEditText.setText(mIntro);
            mIntroEditText.setSelection(mIntro.length());
        }
    }

    @Override
    public void onClick(View v) {
        if (mBackImageView == v) {
            finish();
        } else if (mRightTextView == v) {
            String intro = mIntroEditText.getText().toString();
            new UploadTask(this, intro).start();
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

        if (TextUtils.equals(text, mIntro)) {
            mRightTextView.setVisibility(View.GONE);
        } else {
            mRightTextView.setVisibility(View.VISIBLE);
        }

        int num = maxNumber - text.length();
        mNumberTextView.setText(String.valueOf(num));
        if (num < 0) {
            mNumberTextView.setTextColor(getResources().getColor(R.color.red));
        } else {
            mNumberTextView.setTextColor(getResources().getColor(R.color.grey_dark));
        }
    }


    private class UploadTask extends HttpAsyncTask<UserDetailInfo> {

        private String intro;

        public UploadTask(Context context, String intro) {
            super(context);
            this.intro = intro;
        }

        @Override
        public DataHull<UserDetailInfo> doInBackground() {
            return HttpApi.updateUserDetailInfo(null, null, null, null, null, intro);
        }

        @Override
        public void onPostExecute(int updateId, UserDetailInfo result) {
            BaseApplication.get().setUserDetailInfo(result);
            DescribeActivity.this.finish();
        }
    }
}
