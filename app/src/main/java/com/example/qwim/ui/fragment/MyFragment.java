package com.example.qwim.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.qwim.R;
import com.example.qwim.ui.MoreActivity;

/**
 * Created by qikaixuan on 17-4-17.
 */

public class MyFragment extends Fragment implements View.OnClickListener{


    public static final int REQUEST_CODE = 1;
    private View mView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true); // 配置Actionbar可先的属性
        mView = inflater.inflate(R.layout.my_fragment, container, false);
        return mView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        findView();
    }

    private void findView() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

        }
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_more, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_more:
                startActivityForResult(new Intent(getActivity(),
                        MoreActivity.class), REQUEST_CODE);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
