package com.example.qwim.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.qwim.R;
import com.example.qwim.base.BaseActivity;
import com.example.qwim.bean.AddFriendMessage;
import com.example.qwim.bean.MyUser;

import java.util.HashMap;
import java.util.Map;

import cn.bmob.newim.BmobIM;
import cn.bmob.newim.bean.BmobIMConversation;
import cn.bmob.newim.bean.BmobIMMessage;
import cn.bmob.newim.bean.BmobIMUserInfo;
import cn.bmob.newim.core.BmobIMClient;
import cn.bmob.newim.listener.MessageSendListener;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;

/**
 * Created by qikaixuan on 17-4-21.
 */

public class UserInfoActivity extends BaseActivity implements View.OnClickListener{

    private ImageView mUserAvatar;
    private TextView mUserName;
    private TextView mName;
    private TextView mUserSignature;
    private Button mAddFriendButton;
    private Button mChatButton;
    private BmobIMUserInfo mUserInfo;

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
        mUserInfo = new BmobIMUserInfo(user.getObjectId(),user.getUsername(),user.getAvatar());
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
        mAddFriendButton.setOnClickListener(this);
        mChatButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add_friend_btn:
                sendAddFriendMessage();
                break;
            case R.id.chat_btn:
                //启动一个会话，设置isTransient设置为false,则会在本地数据库的会话列表中先创建（如果没有）与该用户的会话信息，且将用户信息存储到本地的用户表中
                BmobIMConversation c = BmobIM.getInstance()
                        .startPrivateConversation(mUserInfo,false,null);
                Bundle bundle = new Bundle();
                bundle.putSerializable("c", c);
                startActivity(ChatActivity.class, bundle, false);
                break;
        }
    }

    /**
     * 发送添加好友的请求
     */
    private void sendAddFriendMessage(){
        //启动一个会话，如果isTransient设置为true,则不会创建在本地会话表中创建记录，
        //设置isTransient设置为false,则会在本地数据库的会话列表中先创建（如果没有）与该用户的会话信息，且将用户信息存储到本地的用户表中
        BmobIMConversation c = BmobIM.getInstance().startPrivateConversation(mUserInfo, true,null);
        //这个obtain方法才是真正创建一个管理消息发送的会话
        BmobIMConversation conversation = BmobIMConversation.obtain(BmobIMClient.getInstance(), c);
        AddFriendMessage msg =new AddFriendMessage();
        MyUser currentUser = BmobUser.getCurrentUser(this,MyUser.class);
        msg.setContent("很高兴认识你，可以加个好友吗?");//给对方的一个留言信息
        Map<String,Object> map =new HashMap<>();
        map.put("name", currentUser.getUsername());//发送者姓名，这里只是举个例子，其实可以不需要传发送者的信息过去
        map.put("avatar",currentUser.getAvatar());//发送者的头像
        map.put("uid",currentUser.getObjectId());//发送者的uid
        msg.setExtraMap(map);
        conversation.sendMessage(msg, new MessageSendListener() {
            @Override
            public void done(BmobIMMessage msg, BmobException e) {
                if (e == null) {//发送成功
                    Toast.makeText(UserInfoActivity.this,"好友请求发送成功，等待验证",
                            Toast.LENGTH_SHORT).show();
                } else {//发送失败
                    Toast.makeText(UserInfoActivity.this,"发送失败:" + e.getMessage(),
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
