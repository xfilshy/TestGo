package com.xue.ui.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.xue.BaseApplication;
import com.xue.R;
import com.xue.asyns.HttpAsyncTask;
import com.xue.bean.User;
import com.xue.http.HttpApi;
import com.xue.http.impl.DataHull;

/**
 * Created by xfilshy on 2018/1/17.
 */
public class PhoneLoginActivity extends AppCompatActivity implements View.OnClickListener {

    public static void launch(Activity activity) {
        if (activity == null) {
            return;
        }

        Intent intent = new Intent(activity, PhoneLoginActivity.class);
        activity.startActivityForResult(intent, 1);
    }

    private EditText mCellphoneEditText;

    private EditText mVerifyCodeEditText;

    private TextView mVerifyButton;

    private TextView mLoginButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_login);

        findView();
    }

    private void findView() {
        getSupportActionBar().setTitle("登录");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mCellphoneEditText = findViewById(R.id.cellphone);
        mVerifyCodeEditText = findViewById(R.id.verifyCode);
        mVerifyButton = findViewById(R.id.verify);
        mLoginButton = findViewById(R.id.login);

        mLoginButton.setOnClickListener(this);
        mVerifyButton.setOnClickListener(this);
        mCellphoneEditText.addTextChangedListener(cellphoneWatcher);
        mVerifyCodeEditText.addTextChangedListener(verifyWatcher);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            setResult(RESULT_OK);
            finish();
        } else if (requestCode == 1 && resultCode == RESULT_CANCELED) {
            BaseApplication.get().setUser(null, true);
            setResult(RESULT_CANCELED);
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mCellphoneEditText.addTextChangedListener(null);
        mVerifyCodeEditText.addTextChangedListener(null);
    }

    @Override
    public void onClick(View view) {
        if (mVerifyButton == view) {

        } else if (mLoginButton == view) {
            doLogin();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void doLogin() {
        String cellphone = mCellphoneEditText.getText().toString();
        String verifyCode = mVerifyCodeEditText.getText().toString();

        new LoginTask(this, cellphone, verifyCode).start();
    }

    private TextWatcher cellphoneWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            String value = s.toString();
            if (value.startsWith("1") && value.length() == 11) {
                mVerifyButton.setEnabled(true);
            } else {
                mVerifyButton.setEnabled(false);
            }
        }
    };

    private TextWatcher verifyWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            String value = s.toString();
            if (value.length() == 4) {
                mLoginButton.setEnabled(mVerifyButton.isEnabled() && true);
            } else {
                mLoginButton.setEnabled(false);
            }
        }
    };


    private class LoginTask extends HttpAsyncTask<User> {

        private String cellphone;

        private String verifyCode;

        public LoginTask(Context context, String cellphone, String verifyCode) {
            super(context);
            this.cellphone = cellphone;
            this.verifyCode = verifyCode;
        }

        @Override
        public DataHull<User> doInBackground() {
            return HttpApi.phoneLogin(cellphone, verifyCode);
        }

        @Override
        public void onPostExecute(int updateId, User result) {
            handleUser(result);
        }

        private void handleUser(User user) {
            BaseApplication.get().setUser(user, true);
            if (user != null) {
                if (user.getUserDetailInfo() != null) {
                    setResult(RESULT_OK);
                    finish();
                } else {
                    BaseInfoEditActivity.launch(PhoneLoginActivity.this);
                }
            }
        }
    }
}
