package com.example.qwim.ui.fragment;

import android.content.Intent;
import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.qwim.R;
import com.example.qwim.adapter.ConversationAdapter;
import com.example.qwim.adapter.IMultipleItem;
import com.example.qwim.adapter.OnRecyclerViewListener;
import com.example.qwim.bean.Conversation;
import com.example.qwim.bean.PrivateConversation;
import com.example.qwim.event.RefreshEvent;
import com.example.qwim.ui.AddFriendActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import cn.bmob.newim.BmobIM;
import cn.bmob.newim.bean.BmobIMConversation;
import cn.bmob.newim.event.MessageEvent;
import cn.bmob.newim.event.OfflineMessageEvent;

/**
 * Created by qikaixuan on 17-4-17.
 */

public class ConversationFragment extends Fragment {

    private ConversationAdapter mAdapter;
    private SwipeRefreshLayout mSwipe;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true); // 配置Actionbar可先的属性
        View view = inflater.inflate(R.layout.conversation_fragment, container, false);
        mSwipe = (SwipeRefreshLayout) view.findViewById(R.id.conversation_swipe);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.conversation_recycler);
        IMultipleItem<Conversation> multipleItem = new IMultipleItem<Conversation>() {
            @Override
            public int getItemLayoutId(int viewtype) {
                return R.layout.item_conversation;
            }

            @Override
            public int getItemViewType(int postion, Conversation conversation) {
                return 0;
            }

            @Override
            public int getItemCount(List<Conversation> list) {
                return list.size();
            }
        };
        mAdapter = new ConversationAdapter(getContext(), multipleItem, null);
        recyclerView.setAdapter(mAdapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        mSwipe.setEnabled(true);
        setListener();
        return view;
    }

    private void setListener() {
        mSwipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                query();
            }
        });
        mAdapter.setOnRecyclerViewListener(new OnRecyclerViewListener() {
            @Override
            public void onItemClick(int position) {
                mAdapter.getItem(position).onClick(getActivity());
            }

            @Override
            public boolean onItemLongClick(int position) {
                mAdapter.getItem(position).onLongClick(getActivity());
                mAdapter.remove(position);
                return true;
            }
        });
    }

    /**
      查询本地会话
     */
    public void query(){
        mAdapter.bindDatas(getConversations());
        mAdapter.notifyDataSetChanged();
        mSwipe.setRefreshing(false);
    }

    /**
     * 获取会话列表的数据：增加新朋友会话
     * @return
     */
    private List<Conversation> getConversations(){
        //添加会话
        List<Conversation> conversationList = new ArrayList<>();
        conversationList.clear();
        List<BmobIMConversation> list = BmobIM.getInstance().loadAllConversation();
        if(list!=null && list.size()>0){
            for (BmobIMConversation item:list){
                switch (item.getConversationType()){
                    case 1://私聊
                        conversationList.add(new PrivateConversation(item));
                        break;
                    default:
                        break;
                }
            }
        }
//        //添加新朋友会话-获取好友请求表中最新一条记录
//        List<NewFriend> friends = NewFriendManager.getInstance(getActivity()).getAllNewFriend();
//        if(friends!=null && friends.size()>0){
//            conversationList.add(new NewFriendConversation(friends.get(0)));
//        }
        //重新排序
        Collections.sort(conversationList);
        return conversationList;
    }
    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }
    /**注册自定义消息接收事件
     * @param event
     */
    @Subscribe
    public void onEventMainThread(RefreshEvent event){

        //因为新增`新朋友`这种会话类型
        mAdapter.bindDatas(getConversations());
        mAdapter.notifyDataSetChanged();
    }

    /**注册离线消息接收事件
     * @param event
     */
    public void onEventMainThread(OfflineMessageEvent event){
        //重新刷新列表
        mAdapter.bindDatas(getConversations());
        mAdapter.notifyDataSetChanged();
    }
    /**注册消息接收事件
     * @param event
     * 1、与用户相关的由开发者自己维护，SDK内部只存储用户信息
     * 2、开发者获取到信息后，可调用SDK内部提供的方法更新会话
     */
    @Subscribe
    public void onEventMainThread(MessageEvent event){
        //重新获取本地消息并刷新列表
        mAdapter.bindDatas(getConversations());
        mAdapter.notifyDataSetChanged();
    }

    //toolbar选项
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_add,menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_add:
                startActivity(new Intent(getContext(), AddFriendActivity.class));
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
