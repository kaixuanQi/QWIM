package com.example.qwim.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.qwim.QWIMApplication;
import com.example.qwim.R;

import cn.bmob.v3.BmobUser;

/**
 * Created by qikaixuan on 17-4-17.
 */

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        Handler handler =new Handler(Looper.getMainLooper());
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                BmobUser bmobUser = BmobUser.getCurrentUser(QWIMApplication.getContext());
                if(bmobUser != null){
                    startActivity(new Intent(SplashActivity.this,MainActivity.class));
                    Log.e("splash","Main");
                    finish();
                }else{
                    //缓存用户对象为空时， 可打开用户注册界面…
                    startActivity(new Intent(SplashActivity.this,LoginActivity.class));
                    Log.e("splash","Login");
                    finish();
                }
            }
        },1000);
    }


}
