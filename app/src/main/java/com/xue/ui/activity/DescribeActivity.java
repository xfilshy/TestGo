package com.xue.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.elianshang.tools.EditTextTool;
import com.xue.BaseApplication;
import com.xue.R;
import com.xue.asyns.HttpAsyncTask;
import com.xue.bean.UserDetailInfo;
import com.xue.http.HttpApi;
import com.xue.http.impl.DataHull;

public class DescribeActivity extends SwipeBackBaseActivity implements TextWatcher {

    public static void launch(Context context) {
        Intent intent = new Intent(context, DescribeActivity.class);
        context.startActivity(intent);
    }

    private String mIntro;

    private EditText mIntroEditText;

    private TextView mNumberTextView;

    private int maxNumber = 500;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_describe);

        findView();

        init();
    }

    @Override
    protected boolean hasActionBar() {
        return true;
    }

    @Override
    protected String actionBarTitle() {
        return "独白";
    }

    @Override
    protected String actionBarRight() {
        return "保存";
    }

    @Override
    public void rightAction(View view) {
        super.rightAction(view);
        String intro = mIntroEditText.getText().toString();
        new UploadTask(this, intro).start();
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
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        String text = s.toString();

        if (TextUtils.equals(text, mIntro)) {
            setActionRightVisibility(View.GONE);
        } else {
            setActionRightVisibility(View.VISIBLE);
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
            super(context, true, true);
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
