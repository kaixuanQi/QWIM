package com.example.qwim.adapter;

import android.content.Context;

import com.example.qwim.R;
import com.example.qwim.user.Friend;

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
//        if(holder.layoutId== R.layout.item_contact){
//            User user =friend.getFriendUser();
//            //好友头像
//            holder.setImageView(user == null ? null : user.getAvatar(), R.mipmap.head, R.id.iv_recent_avatar);
//            //好友名称
//            holder.setText(R.id.tv_recent_name,user==null?"未知":user.getUsername());
//        }else if(holder.layoutId==R.layout.header_new_friend){
//            if(NewFriendManager.getInstance(context).hasNewFriendInvitation()){
//                holder.setVisible(R.id.iv_msg_tips,View.VISIBLE);
//            }else{
//                holder.setVisible(R.id.iv_msg_tips, View.GONE);
//            }
//        }
    }
}
