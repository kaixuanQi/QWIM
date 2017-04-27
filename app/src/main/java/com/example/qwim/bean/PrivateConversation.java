package com.example.qwim.bean;

import android.content.Context;

import cn.bmob.newim.bean.BmobIMConversation;

/**
 * Created by qikaixuan on 17-4-27.
 */

public class PrivateConversation extends Conversation {
    public PrivateConversation(BmobIMConversation item) {
        super();
    }

    @Override
    public Object getAvatar() {
        return null;
    }

    @Override
    public long getLastMessageTime() {
        return 0;
    }

    @Override
    public String getLastMessageContent() {
        return null;
    }

    @Override
    public int getUnReadCount() {
        return 0;
    }

    @Override
    public void readAllMessages() {

    }

    @Override
    public void onClick(Context context) {

    }

    @Override
    public void onLongClick(Context context) {

    }
}
