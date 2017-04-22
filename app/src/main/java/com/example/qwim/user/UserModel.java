package com.example.qwim.user;

import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.LogInListener;
import cn.bmob.v3.listener.SaveListener;

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

}
