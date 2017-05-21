package com.example.qwim.adapter;

import android.content.Context;
import android.view.View;

import com.example.qwim.R;
import com.example.qwim.adapter.base.BaseRecyclerAdapter;
import com.example.qwim.adapter.base.BaseRecyclerHolder;
import com.example.qwim.bean.Friend;
import com.example.qwim.bean.MyUser;
import com.example.qwim.db.NewFriendManager;

import java.util.Collection;

/**
 * Created by qikaixuan on 17-4-25.
 */

public class ContactAdapter extends BaseRecyclerAdapter<Friend> {
    public static final int TYPE_NEW_FRIEND = 0;
    public static final int TYPE_ITEM = 1;

    public ContactAdapter(Context context, IMultipleItem items, Collection datas) {
        super(context, items, datas);
    }

    @Override
    public void bindView(BaseRecyclerHolder holder, Friend friend, int position) {
        if(holder.layoutId== R.layout.item_add_lists){
            MyUser user =friend.getFriendUser();
            //好友头像
            holder.setImageView(user == null ? null : user.getAvatar(), R.drawable.default_avatar, R.id.avatar);
            //好友名称
            holder.setText(R.id.find_username,user==null?"未知":user.getUsername());
        }else if(holder.layoutId==R.layout.header_new_friend){
            if(NewFriendManager.getInstance(context).hasNewFriendInvitation()){
                holder.setVisible(R.id.iv_msg_tips,View.VISIBLE);
            }else{
                holder.setVisible(R.id.iv_msg_tips, View.GONE);
            }
        }
    }
}
