package com.example.qwim.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.qwim.R;
import com.example.qwim.user.MyUser;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by qikaixuan on 17-4-20.
 */

public class AddFriendAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<MyUser> mMyUsers = new ArrayList<MyUser>();

    private OnRecyclerViewListener onRecyclerViewListener;

    public void setDatas(List<MyUser> list) {
        mMyUsers.clear();
        if (null != list) {
            mMyUsers.addAll(list);
        }
    }

    public void setOnRecyclerViewListener(OnRecyclerViewListener onRecyclerViewListener) {
        this.onRecyclerViewListener = onRecyclerViewListener;
    }
    /**获取用户
     * @param position
     * @return
     */
    public MyUser getItem(int position){
        return mMyUsers.get(position);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //找到item视图
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_add_lists, parent, false);
        return new AddFriendViewHolder(parent.getContext(), view, onRecyclerViewListener);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((BaseViewHolder)holder).bindData(mMyUsers.get(position));
    }

    @Override
    public int getItemViewType(int position) {
        return 1;
    }

    @Override
    public int getItemCount() {
        return mMyUsers.size();
    }
}
