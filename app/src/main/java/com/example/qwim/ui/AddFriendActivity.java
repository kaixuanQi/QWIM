package com.example.qwim.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.qwim.R;
import com.example.qwim.adapter.AddFriendAdapter;
import com.example.qwim.adapter.OnRecyclerViewListener;
import com.example.qwim.base.BaseActivity;
import com.example.qwim.user.MyUser;
import com.example.qwim.user.UserModel;

import java.util.List;

import cn.bmob.v3.listener.FindListener;

/**
 * Created by qikaixuan on 17-4-19.
 */

public class AddFriendActivity extends BaseActivity implements View.OnClickListener {

    private SwipeRefreshLayout mSwipeRefreshLayout;
    private EditText mFindUser;
    private AddFriendAdapter mAddFriendAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        mFindUser = (EditText) findViewById(R.id.et_find_name);
        Button findUserButton = (Button) findViewById(R.id.btn_search);
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.add_sw_refresh);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.friend_lists);
        findUserButton.setOnClickListener(this);
        //RecyclerView 适配
        mAddFriendAdapter = new AddFriendAdapter();
        recyclerView.setAdapter(mAddFriendAdapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        mAddFriendAdapter.setOnRecyclerViewListener(new OnRecyclerViewListener() {
            @Override
            public void onItemClick(int position) {
                Bundle bundle = new Bundle();
                MyUser user = mAddFriendAdapter.getItem(position);
                bundle.putSerializable("u", user);
                startActivity(UserInfoActivity.class,bundle,false);
            }

            @Override
            public boolean onItemLongClick(int position) {
                return true;
            }
        });
        mSwipeRefreshLayout.setEnabled(true);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                query();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_search:
                //开始刷新
                mSwipeRefreshLayout.setRefreshing(true);
                query();
                break;
        }
    }

    private void query() {
        String findName = mFindUser.getText().toString();
        if (TextUtils.isEmpty(findName)) {
            Toast.makeText(this,"请输入用户名",Toast.LENGTH_SHORT).show();
            //刷新停止
            mSwipeRefreshLayout.setRefreshing(false);
            return;
        }
        UserModel.getInstance().queryUsers(findName, 20, new FindListener<MyUser>() {
            @Override
            public void onSuccess(List<MyUser> list) {
                mSwipeRefreshLayout.setRefreshing(false);
                mAddFriendAdapter.setDatas(list);
                mAddFriendAdapter.notifyDataSetChanged();

            }

            @Override
            public void onError(int i, String s) {
                mSwipeRefreshLayout.setRefreshing(false);
                mAddFriendAdapter.setDatas(null);
                mAddFriendAdapter.notifyDataSetChanged();
                Toast.makeText(getApplicationContext(),s+"("+i+")",Toast.LENGTH_SHORT).show();
            }
        });
    }
}
