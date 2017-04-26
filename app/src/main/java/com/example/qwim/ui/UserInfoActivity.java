package com.example.qwim.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.qwim.R;
import com.example.qwim.base.BaseActivity;
import com.example.qwim.bean.MyUser;

import cn.bmob.newim.bean.BmobIMUserInfo;
import cn.bmob.v3.BmobUser;

/**
 * Created by qikaixuan on 17-4-21.
 */

public class UserInfoActivity extends BaseActivity{

    private ImageView mUserAvatar;
    private TextView mUserName;
    private TextView mName;
    private TextView mUserSignature;
    private Button mAddFriendButton;
    private Button mChatButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userinfo);
        initView();
        //判断是否是本机用户
        MyUser user = (MyUser) getBundle().getSerializable("u");
        String currentUid = BmobUser.getCurrentUser(this,MyUser.class).getObjectId();
        if(user.getObjectId().equals(currentUid)){
            mAddFriendButton.setVisibility(View.GONE);
            mChatButton.setVisibility(View.GONE);
        }else{
            mAddFriendButton.setVisibility(View.VISIBLE);
            mChatButton.setVisibility(View.VISIBLE);
        }
        BmobIMUserInfo userInfo = new BmobIMUserInfo(user.getObjectId(),user.getUsername(),user.getAvatar());
        Glide.with(UserInfoActivity.this).load(user.getAvatar())
                .placeholder(R.drawable.default_avatar).into(mUserAvatar);
        //如果用户没有设置昵称，则使用用户账号名
        mName.setText(TextUtils.isEmpty(user.getName()) ? user.getUsername() : user.getName());
        mUserName.setText("账号："+user.getUsername());
        mUserSignature.setText(user.getDesc());
    }

    private void initView() {
        mUserAvatar = (ImageView) findViewById(R.id.info_avatar);
        mUserName = (TextView) findViewById(R.id.info_username);
        mName = (TextView) findViewById(R.id.info_name);
        mUserSignature = (TextView) findViewById(R.id.user_signature);
        mAddFriendButton = (Button) findViewById(R.id.add_friend_btn);
        mChatButton = (Button) findViewById(R.id.chat_btn);
    }
}
