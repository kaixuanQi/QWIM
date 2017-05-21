package com.example.qwim.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.example.qwim.R;
import com.example.qwim.base.BaseActivity;

import java.util.List;

import cn.bmob.newim.event.MessageEvent;
import cn.bmob.newim.listener.MessageListHandler;

/**
 * Created by qi on 2017/5/19.
 */

public class ChatActivity extends BaseActivity implements MessageListHandler{
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
    }

    @Override
    public void onMessageReceive(List<MessageEvent> list) {

    }
}
