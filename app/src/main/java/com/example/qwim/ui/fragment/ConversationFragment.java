package com.example.qwim.ui.fragment;

import android.content.Intent;
import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.qwim.R;
import com.example.qwim.ui.AddFriendActivity;

import cn.bmob.newim.event.OfflineMessageEvent;

/**
 * Created by qikaixuan on 17-4-17.
 */

public class ConversationFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true); // 配置Actionbar可先的属性

        return inflater.inflate(R.layout.conversation_fragment, container, false);
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

    public void onEventMainThread(OfflineMessageEvent event){
        //重新刷新列表

    }
}
