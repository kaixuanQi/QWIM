package com.example.qwim.bean;

import android.text.TextUtils;
import android.util.Log;

import java.util.List;

import cn.bmob.newim.BmobIM;
import cn.bmob.newim.bean.BmobIMConversation;
import cn.bmob.newim.bean.BmobIMMessage;
import cn.bmob.newim.bean.BmobIMUserInfo;
import cn.bmob.newim.event.MessageEvent;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.DeleteListener;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.LogInListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

import static com.example.qwim.QWIMApplication.getContext;

/**
 * Created by qikaixuan on 17-4-19.
 */

public class UserModel {
    public static final int CODE_NULL = 1000;
    public static final int CODE_NOT_EQUAL = 1001;
    //饿汉式单例模式
    private static UserModel ourInstance = new UserModel();

    public static UserModel getInstance() {
        return ourInstance;
    }

    private UserModel() {}
    //注册
    public void register(String username,String password, String pwAgain, final LogInListener listener) {
        if(TextUtils.isEmpty(username)){
            listener.internalDone(new BmobException(CODE_NULL, "请填写用户名"));
            return;
        }
        if(TextUtils.isEmpty(password)){
            listener.internalDone(new BmobException(CODE_NULL, "请填写密码"));
            return;
        }
        if(TextUtils.isEmpty(pwAgain)){
            listener.internalDone(new BmobException(CODE_NULL, "请填写确认密码"));
            return;
        }
        if(!password.equals(pwAgain)){
            listener.internalDone(new BmobException(CODE_NOT_EQUAL, "两次输入的密码不一致，请重新输入"));
            return;
        }
        final MyUser user = new MyUser();
        user.setUsername(username);
        user.setPassword(password);
        user.signUp(getContext(), new SaveListener() {
            @Override
            public void onSuccess() {
                listener.done(null, null);
            }

            @Override
            public void onFailure(int i, String s) {
                listener.done(null, new BmobException(i, s));
            }
        });
    }

    public void queryUsers(String username,int limit,final FindListener<MyUser> listener){
        BmobQuery<MyUser> query = new BmobQuery<>();

        query.addWhereEqualTo("username", username);
        query.setLimit(limit);
        query.order("-createdAt");
        Log.d("AddFriend:","cuowu");
        query.findObjects(getContext(), new FindListener<MyUser>() {
            @Override
            public void onSuccess(List<MyUser> list) {
                if (list != null && list.size() > 0) {
                    listener.onSuccess(list);
                } else {
                    listener.onError(1000, "查无此人");
                }
            }
            @Override
            public void onError(int i, String s) {
                listener.onError(i, s);
            }
        });
    }
    /**
     * 查询用户信息
     *  @param objectId
     * @param listener
     */
    public void queryUserInfo(String objectId, final QueryUserListener listener) {
        BmobQuery<MyUser> query = new BmobQuery<>();
        query.addWhereEqualTo("objectId", objectId);
        query.findObjects(getContext(),
                new FindListener<MyUser>() {
                    @Override
                    public void onSuccess(List<MyUser> list) {
                        if (list != null && list.size() > 0) {
                            listener.done(list.get(0), null);
                        } else {
                            listener.done(null, new BmobException(000, "查无此人"));
                        }
                    }

                    @Override
                    public void onError(int i, String s) {
                        listener.done(null, new BmobException(000, "查无此人"));
                    }
                });


    }
    /**
     * 更新用户资料和会话资料
     *
     * @param event
     * @param listener
     */
    public void updateUserInfo(MessageEvent event, final UpdateCacheListener listener) {
        final BmobIMConversation conversation = event.getConversation();
        final BmobIMUserInfo info = event.getFromUserInfo();
        final BmobIMMessage msg = event.getMessage();
        String username = info.getName();
        String title = conversation.getConversationTitle();
        //sdk内部，将新会话的会话标题用objectId表示，因此需要比对用户名和会话标题--单聊，后续会根据会话类型进行判断
        if (!username.equals(title)) {
            UserModel.getInstance().queryUserInfo(info.getUserId(), new QueryUserListener() {
                @Override
                public void done(MyUser s, BmobException e) {
                    if (e == null) {
                        String name = s.getUsername();
                        String avatar = s.getAvatar();
                        conversation.setConversationIcon(avatar);
                        conversation.setConversationTitle(name);
                        info.setName(name);
                        info.setAvatar(avatar);
                        //更新用户资料
                        BmobIM.getInstance().updateUserInfo(info);
                        //更新会话资料-如果消息是暂态消息，则不更新会话资料
                        if (!msg.isTransient()) {
                            BmobIM.getInstance().updateConversation(conversation);
                        }
                    } else {

                    }
                    listener.done(null);
                }
            });
        } else {
            listener.done(null);
        }
    }
    /**
     * 同意添加好友：1、发送同意添加的请求，2、添加对方到自己的好友列表中
     */
    public void agreeAddFriend(MyUser friend, SaveListener listener) {
        Friend f = new Friend();
        MyUser user = BmobUser.getCurrentUser(getContext(),MyUser.class);
        f.setUser(user);
        f.setFriendUser(friend);
        f.save(getContext(),listener);
    }

    /**
     * 查询好友
     *
     * @param listener
     */
    public void queryFriends(final FindListener<Friend> listener) {
        BmobQuery<Friend> query = new BmobQuery<>();
        MyUser user = BmobUser.getCurrentUser(getContext(),MyUser.class);
        query.addWhereEqualTo("user", user);
        query.include("friendUser");
        query.order("-updatedAt");
        query.findObjects(getContext(),new FindListener<Friend>() {
            @Override
            public void onSuccess(List<Friend> list) {
                if (list != null && list.size() > 0) {
                    listener.onSuccess(list);
                } else {
                    listener.onError(0, "暂无联系人");
                }
            }

            @Override
            public void onError(int i, String s) {

            }

        });


    }

    /**
     * 删除好友
     *
     * @param f
     * @param listener
     */
    public void deleteFriend(Friend f, DeleteListener listener) {
        Friend friend = new Friend();
        friend.delete(getContext(), listener);
    }
}
