package com.xue.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
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
import com.xue.bean.UserConfigInfo;
import com.xue.bean.UserExpertInfo;
import com.xue.http.HttpApi;
import com.xue.http.impl.DataHull;
import com.xw.repo.BubbleSeekBar;

public class FeeSettingActivity extends BaseActivity implements View.OnClickListener, BubbleSeekBar.OnProgressChangedListener, TextWatcher {


    public static void launch(Context context) {
        Intent intent = new Intent(context, FeeSettingActivity.class);
        context.startActivity(intent);
    }

    private ImageView mBackImageView;

    private TextView mTitleTextView;

    private TextView mRightTextView;

    private EditText mSignatureEditText;

    private TextView mFeeTextView;

    private TextView mConfirmTextView;

    private BubbleSeekBar mBubbleSeekBar;

    private UserConfigInfo mUserConfigInfo;

    private UserExpertInfo mUserExpertInfo;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fee_setting);

        initActionBar();
        findView();
        init();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mSignatureEditText.addTextChangedListener(this);
        mBubbleSeekBar.setOnProgressChangedListener(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mSignatureEditText.removeTextChangedListener(this);
        mBubbleSeekBar.setOnProgressChangedListener(null);
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

            mTitleTextView.setText("通话定价");
            mRightTextView.setText("保存");
        }
    }

    private void findView() {
        mSignatureEditText = findViewById(R.id.signature);
        mFeeTextView = findViewById(R.id.fee);
        mConfirmTextView = findViewById(R.id.confirm);
        mBubbleSeekBar = findViewById(R.id.seekBar);

        mConfirmTextView.setOnClickListener(this);
    }

    private void init() {
        mUserExpertInfo = BaseApplication.get().getUser().getUserExpertInfo();
        mUserConfigInfo = BaseApplication.get().getUser().getUserConfigInfo();
        initSeekBar();

        if (mUserConfigInfo != null) {
            mFeeTextView.setText(mUserConfigInfo.getFeeDefault() + "钻石/分钟");
            mBubbleSeekBar.setProgress(mUserConfigInfo.getFeeDefault());
        }

        if (mUserExpertInfo != null) {
            mFeeTextView.setText(mUserExpertInfo.getServiceFee() + "钻石/分钟");
            mBubbleSeekBar.setProgress(mUserExpertInfo.getServiceFee());
            mSignatureEditText.setText(mUserExpertInfo.getSignature());
        }
    }

    private void initSeekBar() {
        mBubbleSeekBar.getConfigBuilder()
                .min(mUserConfigInfo.getFeeMin())
                .max(mUserConfigInfo.getFeeMax())
                .sectionCount(mUserConfigInfo.getFeeMax() - mUserConfigInfo.getFeeMin())
                .trackColor(ContextCompat.getColor(this, R.color.grey_dark))
                .secondTrackColor(ContextCompat.getColor(this, R.color.colorPrimary))
                .thumbColor(ContextCompat.getColor(this, R.color.colorAccent))
                .showSectionText()
                .sectionTextColor(ContextCompat.getColor(this, R.color.colorPrimary))
                .sectionTextSize(18)
                .touchToSeek()
                .bubbleColor(ContextCompat.getColor(this, R.color.colorAccent))
                .bubbleTextSize(18)
                .autoAdjustSectionMark()
                .alwaysShowBubble()
                .sectionTextPosition(BubbleSeekBar.TextPosition.SIDES)
                .build();

    }

    private void checkChange() {
        String text = mSignatureEditText.getText().toString();
        int progress = mBubbleSeekBar.getProgress();
        if (mUserExpertInfo != null) {
            if (!TextUtils.equals(mUserExpertInfo.getSignature(), text)) {
                mRightTextView.setVisibility(View.VISIBLE);
                return;
            }

            if (mUserExpertInfo.getServiceFee() != progress) {
                mRightTextView.setVisibility(View.VISIBLE);
                return;
            }

            mRightTextView.setVisibility(View.GONE);
        } else {
            if (!TextUtils.isEmpty(text)) {
                mRightTextView.setVisibility(View.VISIBLE);
                return;
            }

            if (mUserConfigInfo.getFeeDefault() != progress) {
                mRightTextView.setVisibility(View.VISIBLE);
                return;
            }

            mRightTextView.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        if (mRightTextView == v) {
            String signature = mSignatureEditText.getText().toString();
            int serviceFee = mBubbleSeekBar.getProgress();
            new UpdateTask(this, signature, serviceFee).start();
        }
    }

    @Override
    public void onProgressChanged(BubbleSeekBar bubbleSeekBar, int progress, float progressFloat) {
        mFeeTextView.setText(progress + "钻石/分钟");
        checkChange();
    }

    @Override
    public void getProgressOnActionUp(BubbleSeekBar bubbleSeekBar, int progress, float progressFloat) {

    }

    @Override
    public void getProgressOnFinally(BubbleSeekBar bubbleSeekBar, int progress, float progressFloat) {

    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        checkChange();
    }

    private class UpdateTask extends HttpAsyncTask<UserExpertInfo> {

        private String signature;

        private int serviceFee;

        public UpdateTask(Context context, String signature, int serviceFee) {
            super(context,true , true);
            this.signature = signature;
            this.serviceFee = serviceFee;
        }

        @Override
        public DataHull<UserExpertInfo> doInBackground() {
            return HttpApi.updateUserExpertInfo(signature, String.valueOf(serviceFee), null, null);
        }

        @Override
        public void onPostExecute(int updateId, UserExpertInfo result) {
            BaseApplication.get().setUserExpertInfo(result);
            finish();
        }
    }
}
