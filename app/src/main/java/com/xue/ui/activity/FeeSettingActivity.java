package com.xue.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.elianshang.tools.EditTextTool;
import com.elianshang.tools.TextTool;
import com.xue.BaseApplication;
import com.xue.R;
import com.xue.asyns.HttpAsyncTask;
import com.xue.bean.UserConfigInfo;
import com.xue.bean.UserExpertInfo;
import com.xue.http.HttpApi;
import com.xue.http.impl.DataHull;
import com.xw.repo.BubbleSeekBar;

public class FeeSettingActivity extends SwipeBackBaseActivity implements BubbleSeekBar.OnProgressChangedListener, TextWatcher {


    public static void launch(Context context) {
        Intent intent = new Intent(context, FeeSettingActivity.class);
        context.startActivity(intent);
    }

    private EditText mSignatureEditText;

    private TextView mFeeTextView;

    private BubbleSeekBar mBubbleSeekBar;

    private UserConfigInfo mUserConfigInfo;

    private UserExpertInfo mUserExpertInfo;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fee_setting);

        findView();
        init();
    }

    @Override
    protected boolean hasActionBar() {
        return true;
    }

    @Override
    protected String actionBarTitle() {
        return "通话定价";
    }

    @Override
    protected String actionBarRight() {
        return "保存";
    }

    @Override
    public void rightAction(View view) {
        super.rightAction(view);
        String signature = mSignatureEditText.getText().toString();
        int serviceFee = mBubbleSeekBar.getProgress();
        new UpdateTask(this, signature, serviceFee).start();
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

    private void findView() {
        mSignatureEditText = findViewById(R.id.signature);
        mFeeTextView = findViewById(R.id.fee);
        mBubbleSeekBar = findViewById(R.id.seekBar);

        EditTextTool.setEmojiFilter(mSignatureEditText);
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
                setActionRightVisibility(View.VISIBLE);
                return;
            }

            if (mUserExpertInfo.getServiceFee() != progress) {
                setActionRightVisibility(View.VISIBLE);
                return;
            }

            setActionRightVisibility(View.GONE);
        } else {
            if (!TextUtils.isEmpty(text)) {
                setActionRightVisibility(View.VISIBLE);
                return;
            }

            if (mUserConfigInfo.getFeeDefault() != progress) {
                setActionRightVisibility(View.VISIBLE);
                return;
            }

            setActionRightVisibility(View.GONE);
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
        String text = s.toString();
        String fText = TextTool.getFullWidthText(text, 10, true);
        if (!TextUtils.equals(text, fText)) {
            mSignatureEditText.setText(fText);
            mSignatureEditText.setSelection(fText.length());
            return;
        }
        checkChange();
    }

    private class UpdateTask extends HttpAsyncTask<UserExpertInfo> {

        private String signature;

        private int serviceFee;

        public UpdateTask(Context context, String signature, int serviceFee) {
            super(context, true, true);
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
