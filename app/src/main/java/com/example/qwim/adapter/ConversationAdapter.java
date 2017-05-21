package com.example.qwim.adapter;

import android.content.Context;
import android.view.View;

import com.example.qwim.R;
import com.example.qwim.TimeUtil;
import com.example.qwim.adapter.base.BaseRecyclerAdapter;
import com.example.qwim.adapter.base.BaseRecyclerHolder;
import com.example.qwim.bean.Conversation;

import java.util.Collection;

/**
 * Created by qikaixuan on 17-4-26.
 */

public class ConversationAdapter extends BaseRecyclerAdapter<Conversation> {

    public ConversationAdapter(Context context, IMultipleItem<Conversation> items, Collection<Conversation> datas) {
        super(context, items, datas);
    }

    @Override
    public void bindView(BaseRecyclerHolder holder, Conversation conversation, int position) {
        //头像
        String avatar= (String) conversation.getAvatar();
        holder.setImageView(avatar, R.drawable.default_avatar, R.id.conversation_avatar);

        //消息
        holder.setText(R.id.conversation_msg, conversation.getLastMessageContent());
        //标题
        holder.setText(R.id.conversation_title, conversation.getcName());
        //时间
        holder.setText(R.id.recent_time, TimeUtil.getChatTime(false,conversation.getLastMessageTime()));
        //查询指定未读消息数
        long unread = conversation.getUnReadCount();
        if(unread>0){
            holder.setVisible(R.id.unread_msg_count, View.VISIBLE);
            holder.setText(R.id.unread_msg_count, String.valueOf(unread));
        }else{
            holder.setVisible(R.id.unread_msg_count, View.GONE);
        }

    }
}
