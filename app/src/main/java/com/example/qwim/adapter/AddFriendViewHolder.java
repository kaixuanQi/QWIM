package com.example.qwim.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.qwim.R;
import com.example.qwim.bean.MyUser;

/**
 * Created by qikaixuan on 17-4-21.
 */

public class AddFriendViewHolder extends BaseViewHolder {

    private final ImageView mAvatar;
    private final TextView mUserName;

    public AddFriendViewHolder(Context context, View itemView, OnRecyclerViewListener listener) {
        super(context, itemView, listener);
        mAvatar = (ImageView) itemView.findViewById(R.id.avatar);
        mUserName = (TextView) itemView.findViewById(R.id.find_username);
    }

    @Override
    public void bindData(Object o) {
        final MyUser user =(MyUser)o;
        //Glide 加载图片
        Glide.with(context).load(user.getAvatar())
                .placeholder(R.drawable.default_avatar).into(mAvatar);
        mUserName.setText(user.getUsername());
    }
}
