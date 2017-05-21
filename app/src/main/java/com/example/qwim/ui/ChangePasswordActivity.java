package com.example.qwim.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.qwim.R;
import com.example.qwim.base.BaseActivity;
import com.example.qwim.bean.MyUser;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by qikaixuan on 17-4-22.
 */

public class ChangePasswordActivity extends BaseActivity{
    private EditText mNowPass;
    private EditText mNewPass;
    private EditText mNewPassWord;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        mNowPass =  (EditText) findViewById(R.id.et_now_pass);
        mNewPass = (EditText) findViewById(R.id.et_new_pass);
        mNewPassWord = (EditText) findViewById(R.id.et_new_password);
        Button changePassWord = (Button) findViewById(R.id.btn_change_password);
        changePassWord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 1.获取输入框的值
                String now = mNowPass.getText().toString().trim();
                String news = mNewPass.getText().toString().trim();
                String newPassword = mNewPassWord.getText().toString();
                // 2.判断是否为空
                if(!TextUtils.isEmpty(now) & !TextUtils.isEmpty(news) & !TextUtils.isEmpty(newPassword)){
                    // 3.判断两次新密码是否一致
                    if(news.equals(newPassword)) {
                        // 4.重置密码
                        MyUser.updateCurrentUserPassword(ChangePasswordActivity.this,now, news, new UpdateListener() {
                            @Override
                            public void onSuccess() {
                                Toast.makeText(ChangePasswordActivity.this,"重置密码成功",
                                        Toast.LENGTH_SHORT).show();
                                finish();
                            }

                            @Override
                            public void onFailure(int i, String s) {
                                Toast.makeText(ChangePasswordActivity.this,"重置密码失败",
                                        Toast.LENGTH_SHORT).show();
                            }
                        });
                    }else {
                        Toast.makeText(ChangePasswordActivity.this,"两次密码不一致",
                                Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(ChangePasswordActivity.this,"输入框不能为空",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
