package com.xue.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.xue.BaseApplication;
import com.xue.R;
import com.xue.asyns.HttpAsyncTask;
import com.xue.bean.UserBase;
import com.xue.http.HttpApi;
import com.xue.http.impl.DataHull;

/**
 * Created by xfilshy on 2018/1/17.
 */

public class PhoneLoginActivity extends AppCompatActivity implements View.OnClickListener {

    public static void launch(BaseActivity activity) {
        if (activity == null) {
            return;
        }

        Intent intent = new Intent(activity, PhoneLoginActivity.class);
        activity.startActivityForResult(intent, 1);
    }

    private EditText mCellphoneEditText;

    private EditText mVerifyCodeEditText;

    private Button mVerifyButton;

    private Button mLoginButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_login);

        findView();
    }

    private void findView() {
        mCellphoneEditText = findViewById(R.id.cellphone);
        mVerifyCodeEditText = findViewById(R.id.verifyCode);
        mVerifyButton = findViewById(R.id.verify);
        mLoginButton = findViewById(R.id.login);

        mLoginButton.setOnClickListener(this);
        mVerifyButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (mVerifyButton == view) {

        } else if (mLoginButton == view) {
            doLogin();
        }
    }

    private void doLogin() {
        String cellphone = mCellphoneEditText.getText().toString();
        String verifyCode = mVerifyCodeEditText.getText().toString();

        new LoginTask(this, cellphone, verifyCode).start();
    }


    private class LoginTask extends HttpAsyncTask<UserBase> {

        private String cellphone;

        private String verifyCode;

        public LoginTask(Context context, String cellphone, String verifyCode) {
            super(context);
            this.cellphone = cellphone;
            this.verifyCode = verifyCode;
        }

        @Override
        public DataHull<UserBase> doInBackground() {
            return HttpApi.phoneLogin(cellphone, verifyCode);
        }

        @Override
        public void onPostExecute(int updateId, UserBase result) {
            handleUser(result);
        }

        private void handleUser(UserBase userBase) {
            if (userBase != null) {
                Toast.makeText(PhoneLoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
                BaseApplication.get().setUser(userBase);
                setResult(RESULT_OK);
                finish();
            }
        }
    }
}
