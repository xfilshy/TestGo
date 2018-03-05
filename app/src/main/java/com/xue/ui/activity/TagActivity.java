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
import com.xue.bean.UserTagInfo;
import com.xue.http.HttpApi;
import com.xue.http.impl.DataHull;

public class TagActivity extends SwipeBackBaseActivity implements View.OnClickListener, TextWatcher {

    public static void launch(Context context, UserTagInfo.Tag tag) {
        Intent intent = new Intent(context, TagActivity.class);
        intent.putExtra("tag", tag);
        context.startActivity(intent);
    }

    private EditText mTagEditText;

    private TextView mDeleteTextView;

    private UserTagInfo.Tag mTag;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tag);

        readExtra();
        findView();

        init();
    }

    @Override
    protected boolean hasActionBar() {
        return true;
    }

    @Override
    protected String actionBarTitle() {
        return "标签";
    }

    @Override
    protected String actionBarRight() {
        return "保存";
    }

    @Override
    public void rightAction(View view) {
        super.rightAction(view);
        String tagName = mTagEditText.getText().toString();

        if (mTag != null) {
            new UploadTask(this, mTag.getTagId(), tagName).start();
        } else {
            new CreateTask(this, tagName).start();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mTagEditText.addTextChangedListener(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mTagEditText.removeTextChangedListener(this);
    }

    private void readExtra() {
        mTag = (UserTagInfo.Tag) getIntent().getSerializableExtra("tag");
    }

    private void findView() {
        mTagEditText = findViewById(R.id.tag);
        mDeleteTextView = findViewById(R.id.delete);
        EditTextTool.setEmojiFilter(mTagEditText);

        mDeleteTextView.setOnClickListener(this);
    }

    private void init() {
        if (mTag != null) {
            mTagEditText.setText(mTag.getTagName());
            mTagEditText.setSelection(mTag.getTagName().length());

            mDeleteTextView.setVisibility(View.VISIBLE);
        } else {
            mDeleteTextView.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        if (mDeleteTextView == v) {
            new DeleteTask(this, mTag.getTagId()).start();
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
        if (mTag == null) {
            if (TextUtils.isEmpty(text)) {
                setActionRightVisibility(View.GONE);
            } else {
                setActionRightVisibility(View.VISIBLE);
            }
        } else {
            if (TextUtils.isEmpty(text) || TextUtils.equals(text, mTag.getTagName())) {
                setActionRightVisibility(View.GONE);
            } else {
                setActionRightVisibility(View.VISIBLE);
            }
        }
    }

    private class CreateTask extends HttpAsyncTask<UserTagInfo> {


        private String tagName;

        public CreateTask(Context context, String tagName) {
            super(context, true, true);
            this.tagName = tagName;
        }

        @Override
        public DataHull<UserTagInfo> doInBackground() {
            return HttpApi.createUserTag(tagName);
        }

        @Override
        public void onPostExecute(int updateId, UserTagInfo result) {
            BaseApplication.get().setUserTagInfo(result);
            TagActivity.this.finish();
        }
    }

    private class UploadTask extends HttpAsyncTask<UserTagInfo> {

        private String tagId;

        private String tagName;

        public UploadTask(Context context, String tagId, String tagName) {
            super(context, true, true);
            this.tagId = tagId;
            this.tagName = tagName;
        }

        @Override
        public DataHull<UserTagInfo> doInBackground() {
            return HttpApi.updateUserTag(tagId, tagName);
        }

        @Override
        public void onPostExecute(int updateId, UserTagInfo result) {
            BaseApplication.get().setUserTagInfo(result);
            TagActivity.this.finish();
        }
    }

    private class DeleteTask extends HttpAsyncTask<UserTagInfo> {

        private String tagId;

        public DeleteTask(Context context, String tagId) {
            super(context, true, true);
            this.tagId = tagId;
        }

        @Override
        public DataHull<UserTagInfo> doInBackground() {
            return HttpApi.deleteUserTag(tagId);
        }

        @Override
        public void onPostExecute(int updateId, UserTagInfo result) {
            BaseApplication.get().setUserTagInfo(result);
            TagActivity.this.finish();
        }
    }
}
