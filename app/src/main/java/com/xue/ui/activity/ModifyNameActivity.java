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
import com.elianshang.tools.TextTool;
import com.xue.BaseApplication;
import com.xue.R;
import com.xue.asyns.HttpAsyncTask;
import com.xue.bean.UserDetailInfo;
import com.xue.http.HttpApi;
import com.xue.http.impl.DataHull;

public class ModifyNameActivity extends BaseActivity implements View.OnClickListener, TextWatcher {

    public static void launch(Context context) {
        Intent intent = new Intent(context, ModifyNameActivity.class);
        context.startActivity(intent);
    }

    private ImageView mBackImageView;

    private TextView mTitleTextView;

    private TextView mRightTextView;

    private EditText mRealNameEditText;

    private String mRealName;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_name);

        initActionBar();
        findView();

        init();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mRealNameEditText.addTextChangedListener(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mRealNameEditText.removeTextChangedListener(this);
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

            mTitleTextView.setText("修改姓名");
            mRightTextView.setText("保存");
        }
    }

    private void findView() {
        mRealNameEditText = findViewById(R.id.realName);
        EditTextTool.setEmojiFilter(mRealNameEditText);
    }

    private void init() {
        UserDetailInfo userDetailInfo = BaseApplication.get().getUser().getUserDetailInfo();
        if (userDetailInfo != null) {
            mRealName = userDetailInfo.getRealName();
            mRealNameEditText.setText(mRealName);
            mRealNameEditText.setSelection(mRealName.length());
        }
    }

    @Override
    public void onClick(View v) {
        if (mBackImageView == v) {
            finish();
        } else if (mRightTextView == v) {
            String realName = mRealNameEditText.getText().toString();
            new UploadTask(this, realName).start();
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
        String fText = TextTool.getFullWidthText(text, 5, true);
        if (!TextUtils.equals(text, fText)) {
            mRealNameEditText.setText(fText);
            mRealNameEditText.setSelection(fText.length());
            return;
        }

        if (TextUtils.isEmpty(text) || TextUtils.equals(text, mRealName)) {
            mRightTextView.setVisibility(View.GONE);
        } else {
            mRightTextView.setVisibility(View.VISIBLE);
        }
    }

    private class UploadTask extends HttpAsyncTask<UserDetailInfo> {

        private String realName;

        public UploadTask(Context context, String realName) {
            super(context, true, true);
            this.realName = realName;
        }

        @Override
        public DataHull<UserDetailInfo> doInBackground() {
            return HttpApi.updateUserDetailInfo(realName, null, null, null, null, null);
        }

        @Override
        public void onPostExecute(int updateId, UserDetailInfo result) {
            BaseApplication.get().setUserDetailInfo(result);
            ModifyNameActivity.this.finish();
        }
    }
}
