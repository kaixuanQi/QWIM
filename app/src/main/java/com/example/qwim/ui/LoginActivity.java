package com.example.qwim.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.qwim.QWIMApplication;
import com.example.qwim.R;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.LogInListener;

/**
 * Created by qikaixuan on 17-4-12.
 */

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText mAccount;
    private EditText mPassword;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAccount = (EditText) findViewById(R.id.et_username);
        mPassword = (EditText) findViewById(R.id.et_password);
        Button login = (Button) findViewById(R.id.btn_login);
        TextView toRegister = (TextView) findViewById(R.id.tv_register);

        login.setOnClickListener(this);
        toRegister.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //跳转到注册界面
            case R.id.tv_register:
                Intent intent = new Intent();
                intent.setClass(this,RegisterActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.btn_login:
                String account = mAccount.getText().toString();
                String password = mPassword.getText().toString();
                BmobUser.loginByAccount(this, account, password, new LogInListener<BmobUser>() {
                    @Override
                    public void done(BmobUser bmobUser, BmobException e) {
                        if (bmobUser != null) {
                            Toast.makeText(QWIMApplication.getContext(), "登录成功,请稍候！", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(LoginActivity.this,MainActivity.class));
                            finish();
                        } else {
                            Toast.makeText(QWIMApplication.getContext(), e.getMessage()+"(" + e.getErrorCode() + ")", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        }

    }
}
