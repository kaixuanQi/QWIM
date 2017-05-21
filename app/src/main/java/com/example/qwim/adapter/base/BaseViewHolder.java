package com.example.qwim.adapter.base;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.qwim.adapter.OnRecyclerViewListener;

/**
 * Created by qikaixuan on 17-4-21.
 */

public abstract class BaseViewHolder<T> extends RecyclerView.ViewHolder implements View.OnClickListener,View.OnLongClickListener{

    public OnRecyclerViewListener onRecyclerViewListener;
    protected Context context;

    public BaseViewHolder(Context context,View view,OnRecyclerViewListener listener) {
        super(view);
        this.context=context;
        this.onRecyclerViewListener =listener;
        itemView.setOnClickListener(this);
        itemView.setOnLongClickListener(this);
    }

    public Context getContext() {
        return itemView.getContext();
    }

    //抽象方法，子类实现
    public abstract void bindData(T t);

    @Override
    public void onClick(View v) {
        if(onRecyclerViewListener!=null){
            onRecyclerViewListener.onItemClick(getAdapterPosition());
        }
    }

    @Override
    public boolean onLongClick(View v) {
        if(onRecyclerViewListener!=null){
            onRecyclerViewListener.onItemLongClick(getAdapterPosition());
        }
        return true;
    }
}
