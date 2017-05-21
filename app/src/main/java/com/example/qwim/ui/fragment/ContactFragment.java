package com.example.qwim.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
import android.view.ViewTreeObserver;
import android.widget.Toast;

import com.example.qwim.R;
import com.example.qwim.adapter.ContactAdapter;
import com.example.qwim.adapter.IMultipleItem;
import com.example.qwim.adapter.OnRecyclerViewListener;
import com.example.qwim.bean.Friend;
import com.example.qwim.bean.MyUser;
import com.example.qwim.bean.UserModel;
import com.example.qwim.event.RefreshEvent;
import com.example.qwim.ui.AddFriendActivity;
import com.example.qwim.ui.ChatActivity;
import com.example.qwim.ui.NewFriendActivity;
import com.github.promeg.pinyinhelper.Pinyin;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.newim.BmobIM;
import cn.bmob.newim.bean.BmobIMConversation;
import cn.bmob.newim.bean.BmobIMUserInfo;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.DeleteListener;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by qikaixuan on 17-4-17.
 */

public class ContactFragment extends Fragment {

    private SwipeRefreshLayout mSwipe;
    private ContactAdapter mAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true); // 配置Actionbar可先的属性
        View view = inflater.inflate(R.layout.contact_fragment, container, false);
        mSwipe = (SwipeRefreshLayout) view.findViewById(R.id.contact_swipe);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.contact_recycler);
        IMultipleItem<Friend> mutlipleItem = new IMultipleItem<Friend>() {

            @Override
            public int getItemViewType(int postion, Friend friend) {
                if (postion == 0) {
                    return ContactAdapter.TYPE_NEW_FRIEND;
                } else {
                    return ContactAdapter.TYPE_ITEM;
                }
            }

            @Override
            public int getItemLayoutId(int viewtype) {
                if (viewtype == ContactAdapter.TYPE_NEW_FRIEND) {
                    return R.layout.header_new_friend;
                } else {
                    return R.layout.item_add_lists;
                }
            }

            @Override
            public int getItemCount(List<Friend> list) {
                return list.size() + 1;
            }
        };
        mAdapter = new ContactAdapter(getActivity(), mutlipleItem, null);
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
                if (position == 0) {//跳转到新朋友页面
                    startActivity(new Intent(getActivity(), NewFriendActivity.class), null);
                } else {
                    Friend friend = mAdapter.getItem(position);
                    MyUser user = friend.getFriendUser();
                    BmobIMUserInfo info = new BmobIMUserInfo(user.getObjectId(), user.getUsername(), user.getAvatar());
                    //启动一个会话，实际上就是在本地数据库的会话列表中先创建（如果没有）与该用户的会话信息，且将用户信息存储到本地的用户表中
                    BmobIMConversation c = BmobIM.getInstance().startPrivateConversation(info, null);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("c", c);
                    startActivity(new Intent(getActivity(), ChatActivity.class), bundle);
                }
            }

            @Override
            public boolean onItemLongClick(final int position) {

                if (position == 0) {
                    return true;
                }
                UserModel.getInstance().deleteFriend(mAdapter.getItem(position),
                        new DeleteListener() {
                            @Override
                            public void onSuccess() {
                                Toast.makeText(getActivity(),"好友删除成功",
                                        Toast.LENGTH_SHORT).show();
                                mAdapter.remove(position);
                            }

                            @Override
                            public void onFailure(int i, String s) {
                                Toast.makeText(getActivity(),"好友删除失败:"+i+s,
                                        Toast.LENGTH_SHORT).show();
                            }
                        });

                return true;
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        mSwipe.setRefreshing(true);
        query();
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

    /**
     * 注册自定义消息接收事件
     *
     * @param event
     */
    @Subscribe
    public void onEventMainThread(RefreshEvent event) {
        //重新刷新列表

        mAdapter.notifyDataSetChanged();
    }

    /**
     * 查询本地会话
     */
    public void query() {
        UserModel.getInstance().queryFriends(

                new FindListener<Friend>() {
                    @Override
                    public void onSuccess(List<Friend> list) {
                        List<Friend> friends = new ArrayList<Friend>();
                        friends.clear();
                        //添加首字母
                        for (int i = 0; i < list.size(); i++) {
                            Friend friend = list.get(i);
                            String username = friend.getFriendUser().getUsername();
                            String pinyin = Pinyin.toPinyin(username.charAt(0));
//                    Logger.i(pinyin);
                            friend.setPinyin(pinyin.substring(0, 1).toUpperCase());
                            friends.add(friend);
                        }
                        mAdapter.bindDatas(friends);
                        mAdapter.notifyDataSetChanged();
                        mSwipe.setRefreshing(false);
                    }

                    @Override
                    public void onError(int i, String s) {
                        mAdapter.bindDatas(null);
                        mAdapter.notifyDataSetChanged();
                        mSwipe.setRefreshing(false);
                    }
                }


        );
    }
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
