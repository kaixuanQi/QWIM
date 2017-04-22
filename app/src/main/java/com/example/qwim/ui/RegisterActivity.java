package com.example.qwim.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.qwim.R;
import com.example.qwim.base.BaseActivity;
import com.example.qwim.event.FinishEvent;
import com.example.qwim.user.UserModel;

import org.greenrobot.eventbus.EventBus;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.LogInListener;

/**
 * Created by qikaixuan on 17-4-13.
 */

public class RegisterActivity extends BaseActivity {

    private TextView mUserName;
    private TextView mPassword;
    private TextView mPwAgain;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mUserName = (TextView) findViewById(R.id.et_username);
        mPassword = (TextView) findViewById(R.id.et_password);
        mPwAgain = (TextView) findViewById(R.id.et_password_again);
        Button register = (Button) findViewById(R.id.btn_register);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserModel.getInstance().register(mUserName.getText().toString(),
                        mPassword.getText().toString(),
                        mPwAgain.getText().toString(),
                        new LogInListener() {
                            @Override
                            public void done(Object o, BmobException e) {
                                if (e == null) {
                                    Toast.makeText(RegisterActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
                                    EventBus.getDefault().post(new FinishEvent());
                                    startActivity(LoginActivity.class, null, true);
                                } else {
                                    if (e.getErrorCode() == UserModel.CODE_NOT_EQUAL) {
                                        mPwAgain.setText("");
                                    }
                                    Toast.makeText(RegisterActivity.this,
                                            e.getMessage() + "(" + e.getErrorCode() + ")",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });
    }
}
