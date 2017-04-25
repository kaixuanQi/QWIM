package com.example.qwim.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.qwim.R;
import com.example.qwim.ui.AddFriendActivity;

/**
 * Created by qikaixuan on 17-4-17.
 */

public class ContactFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true); // 配置Actionbar可先的属性
        View view = inflater.inflate(R.layout.contact_fragment, container, false);
        SwipeRefreshLayout swipe = (SwipeRefreshLayout) view.findViewById(R.id.contact_swipe);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.contact_recycler);

        return view;
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
