package com.example.qwim;

import android.content.Context;

import cn.bmob.newim.event.MessageEvent;
import cn.bmob.newim.event.OfflineMessageEvent;
import cn.bmob.newim.listener.BmobIMMessageHandler;

/**
 * Created by qikaixuan on 17-4-12.
 */

public class MyMessageHandler extends BmobIMMessageHandler {

    private Context context;

    public MyMessageHandler(Context context) {
        this.context = context;
    }

    @Override
    public void onMessageReceive(final MessageEvent messageEvent) {
        super.onMessageReceive(messageEvent);

    }

    @Override
    public void onOfflineReceive(final OfflineMessageEvent offlineMessageEvent) {
        super.onOfflineReceive(offlineMessageEvent);

    }
}
