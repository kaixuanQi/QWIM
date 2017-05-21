package com.example.qwim.bean;

import cn.bmob.v3.BmobObject;

/**
 * Created by qikaixuan on 17-4-25.
 */

public class Friend extends BmobObject {

    private MyUser user;
    private MyUser friendUser;

    //拼音
    private transient String pinyin;

    public String getPinyin() {
        return pinyin;
    }

    public void setPinyin(String pinyin) {
        this.pinyin = pinyin;
    }

    public MyUser getUser() {
        return user;
    }

    public void setUser(MyUser user) {
        this.user = user;
    }

    public MyUser getFriendUser() {
        return friendUser;
    }

    public void setFriendUser(MyUser friendUser) {
        this.friendUser = friendUser;
    }
}
