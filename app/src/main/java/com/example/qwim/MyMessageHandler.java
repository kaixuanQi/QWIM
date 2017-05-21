package com.example.qwim;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.Toast;

import com.example.qwim.bean.AddFriendMessage;
import com.example.qwim.bean.AgreeAddFriendMessage;
import com.example.qwim.bean.MyUser;
import com.example.qwim.bean.UpdateCacheListener;
import com.example.qwim.bean.UserModel;
import com.example.qwim.db.NewFriend;
import com.example.qwim.db.NewFriendManager;
import com.example.qwim.event.RefreshEvent;
import com.example.qwim.ui.MainActivity;

import org.greenrobot.eventbus.EventBus;

import java.util.logging.Logger;

import cn.bmob.newim.bean.BmobIMMessage;
import cn.bmob.newim.bean.BmobIMMessageType;
import cn.bmob.newim.bean.BmobIMUserInfo;
import cn.bmob.newim.event.MessageEvent;
import cn.bmob.newim.event.OfflineMessageEvent;
import cn.bmob.newim.listener.BmobIMMessageHandler;
import cn.bmob.newim.notification.BmobNotificationManager;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

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
        excuteMessage(messageEvent);
    }

    @Override
    public void onOfflineReceive(final OfflineMessageEvent offlineMessageEvent) {
        super.onOfflineReceive(offlineMessageEvent);

    }

    private void excuteMessage(final MessageEvent messageEvent) {
//检测用户信息是否需要更新
        UserModel.getInstance().updateUserInfo(messageEvent, new UpdateCacheListener() {
            @Override
            public void done(BmobException e) {
                BmobIMMessage msg = messageEvent.getMessage();
                if (BmobIMMessageType.getMessageTypeValue(msg.getMsgType()) == 0) {//用户自定义的消息类型，其类型值均为0
                    processCustomMessage(msg, messageEvent.getFromUserInfo());
                } else {//SDK内部内部支持的消息类型
                    if (BmobNotificationManager.getInstance(context).isShowNotification()) {//如果需要显示通知栏，SDK提供以下两种显示方式：
                        Intent pendingIntent = new Intent(context, MainActivity.class);
                        pendingIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        //1、多个用户的多条消息合并成一条通知：有XX个联系人发来了XX条消息
                        BmobNotificationManager.getInstance(context).showNotification(messageEvent, pendingIntent);
                        //2、自定义通知消息：始终只有一条通知，新消息覆盖旧消息
//                        BmobIMUserInfo info =messageEvent.getFromUserInfo();
//                        //这里可以是应用图标，也可以将聊天头像转成bitmap
//                        Bitmap largetIcon = BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher);
//                        BmobNotificationManager.getInstance(context).showNotification(largetIcon,
//                                info.getName(),msg.getContent(),"您有一条新消息",pendingIntent);
                    } else {//直接发送消息事件

                        EventBus.getDefault().post(messageEvent);
                    }
                }
            }
        });
    }
    /**
     * 处理自定义消息类型
     *
     * @param msg
     */
    private void processCustomMessage(BmobIMMessage msg, BmobIMUserInfo info) {
        //自行处理自定义消息类型
        String type = msg.getMsgType();
        //发送页面刷新的广播
        EventBus.getDefault().post(new RefreshEvent());
        //处理消息
        if (type.equals("add")) {//接收到的添加好友的请求
            NewFriend friend = AddFriendMessage.convert(msg);
            //本地好友请求表做下校验，本地没有的才允许显示通知栏--有可能离线消息会有些重复
            long id = NewFriendManager.getInstance(context).insertOrUpdateNewFriend(friend);
            if (id > 0) {
                showAddNotify(friend);
            }
        } else if (type.equals("agree")) {//接收到的对方同意添加自己为好友,此时需要做的事情：1、添加对方为好友，2、显示通知
            AgreeAddFriendMessage agree = AgreeAddFriendMessage.convert(msg);
            addFriend(agree.getFromId());//添加消息的发送方为好友
            //这里应该也需要做下校验--来检测下是否已经同意过该好友请求，我这里省略了
            showAgreeNotify(info, agree);
        } else {
            Toast.makeText(context, "接收到的自定义消息：" + msg.getMsgType() + "," + msg.getContent() + "," + msg.getExtra(), Toast.LENGTH_SHORT).show();
        }
    }
    /**
     * 显示对方添加自己为好友的通知
     *
     * @param friend
     */
    private void showAddNotify(NewFriend friend) {
        Intent pendingIntent = new Intent(context, MainActivity.class);
        pendingIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        //这里可以是应用图标，也可以将聊天头像转成bitmap
        Bitmap largetIcon = BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher);
        BmobNotificationManager.getInstance(context).showNotification(largetIcon,
                friend.getName(), friend.getMsg(), friend.getName() + "请求添加你为朋友", pendingIntent);
    }

    /**
     * 显示对方同意添加自己为好友的通知
     *
     * @param info
     * @param agree
     */
    private void showAgreeNotify(BmobIMUserInfo info, AgreeAddFriendMessage agree) {
        Intent pendingIntent = new Intent(context, MainActivity.class);
        pendingIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        Bitmap largetIcon = BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher);
        BmobNotificationManager.getInstance(context).showNotification(largetIcon, info.getName(), agree.getMsg(), agree.getMsg(), pendingIntent);
    }

    /**
     * 添加对方为自己的好友
     *
     * @param uid
     */
    private void addFriend(String uid) {
        MyUser user = new MyUser();
        user.setObjectId(uid);
        UserModel.getInstance()
                .agreeAddFriend(user, new SaveListener() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onFailure(int i, String s) {

                    }
                });
    }
}
