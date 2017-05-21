package com.example.qwim.adapter.ReceiveMessageHolder;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;


import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.example.qwim.R;
import com.example.qwim.adapter.OnRecyclerViewListener;
import com.example.qwim.adapter.base.BaseViewHolder;

import java.text.SimpleDateFormat;


import cn.bmob.newim.bean.BmobIMImageMessage;
import cn.bmob.newim.bean.BmobIMMessage;
import cn.bmob.newim.bean.BmobIMUserInfo;

/**
 * 接收到的文本类型
 */
public class ReceiveImageHolder extends BaseViewHolder {

  private final ImageView mAvatar;
  private final TextView mTime;
  private final ImageView mPicture;
  private final ProgressBar mProgressLoad;

  public ReceiveImageHolder(Context context, View itemView, OnRecyclerViewListener onRecyclerViewListener) {
    super(context, itemView,onRecyclerViewListener);
    mAvatar = (ImageView) itemView.findViewById(R.id.iv_avatar);
    mTime = (TextView) itemView.findViewById(R.id.tv_time);
    mPicture = (ImageView) itemView.findViewById(R.id.iv_picture);
    mProgressLoad = (ProgressBar) itemView.findViewById(R.id.progress_load);
  }

  @Override
  public void bindData(Object o) {
    BmobIMMessage msg = (BmobIMMessage)o;
    //用户信息的获取必须在buildFromDB之前，否则会报错'Entity is detached from DAO context'
    final BmobIMUserInfo info = msg.getBmobIMUserInfo();
    Glide.with(getContext()).load(info != null ? info.getAvatar() : null).into(mAvatar);
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm");
    String time = dateFormat.format(msg.getCreateTime());
    mTime.setText(time);
    //可使用buildFromDB方法转化为指定类型的消息
    final BmobIMImageMessage message = BmobIMImageMessage.buildFromDB(false,msg);
    //显示图片
    Glide.with(getContext()).load(message.getRemoteUrl())
            .into(new GlideDrawableImageViewTarget(mPicture){
              @Override
              public void onLoadStarted(Drawable placeholder) {
                super.onLoadStarted(placeholder);
                mProgressLoad.setVisibility(View.VISIBLE);
              }

              @Override
              public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> animation) {
                super.onResourceReady(resource, animation);
                mProgressLoad.setVisibility(View.INVISIBLE);
              }

              @Override
              public void onLoadFailed(Exception e, Drawable errorDrawable) {
                super.onLoadFailed(e, errorDrawable);
                mProgressLoad.setVisibility(View.INVISIBLE);
              }
            });

  }

  public void showTime(boolean isShow) {
    mTime.setVisibility(isShow ? View.VISIBLE : View.GONE);
  }
}