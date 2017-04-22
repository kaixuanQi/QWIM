package com.example.qwim;

import android.app.Application;
import android.content.Context;

import cn.bmob.newim.BmobIM;

/**
 * Created by qikaixuan on 17-4-12.
 */

public class QWIMApplication extends Application {
    private static Context context;
    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        //NewIM初始化
        BmobIM.init(this);
        //注册消息接收器
        BmobIM.registerDefaultMessageHandler(new MyMessageHandler(this));
    }

    public static Context getContext(){
        return context;
    }
}
