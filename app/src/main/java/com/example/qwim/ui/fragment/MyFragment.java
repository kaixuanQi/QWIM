package com.example.qwim.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.qwim.R;
import com.example.qwim.bean.MyUser;
import com.example.qwim.ui.MoreActivity;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.UpdateListener;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by qikaixuan on 17-4-17.
 */

public class MyFragment extends Fragment implements View.OnClickListener {


    private Boolean isEnabled = false;
    private View mView;
    private CircleImageView mAvatar;
    private TextView mUserName;
    private EditText mName;
    private EditText mAge;
    private EditText mDescribe;
    private RadioGroup mRadioGroup;
    private Button mChangeInfo;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true); // 配置Actionbar可先的属性
        mView = inflater.inflate(R.layout.my_fragment, container, false);
        initView();
        return mView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        MyUser user = BmobUser.getCurrentUser(getActivity(), MyUser.class);
        if (user != null) {
            Glide.with(getActivity()).load(user.getAvatar())
                    .placeholder(R.drawable.default_avatar).into(mAvatar);
            mUserName.setText(user.getUsername());
            if (user.getName()!=null) {
                mName.setText(user.getName());
            }
            mRadioGroup.check(user.isSex()?R.id.female_btn:R.id.male_btn);
            if (user.getAge()!=null) {
                mAge.setText(user.getAge());
            }
            if (user.getDesc()!=null) {
                mDescribe.setText(user.getDesc());
            }
            setEnabled(isEnabled);
        }
        mChangeInfo.setOnClickListener(this);
    }

    private void initView() {
        mAvatar = (CircleImageView) mView.findViewById(R.id.profile_image);
        mUserName = (TextView) mView.findViewById(R.id.tv_username);
        mName = (EditText) mView.findViewById(R.id.et_name);
        mAge = (EditText) mView.findViewById(R.id.et_age);
        mDescribe = (EditText) mView.findViewById(R.id.et_desc);
        mRadioGroup = (RadioGroup) mView.findViewById(R.id.radio_group);
        mChangeInfo = (Button) mView.findViewById(R.id.edit_btn);
    }

    /**
     * 控制焦点
     *
     * @param is
     */
    private void setEnabled(boolean is) {
        mName.setEnabled(is);
        for (int i = 0; i < mRadioGroup.getChildCount(); i++) {
            mRadioGroup.getChildAt(i).setEnabled(is);
        }
        mAge.setEnabled(is);
        mDescribe.setEnabled(is);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.edit_btn:
                if (isEnabled == false) {
                    mChangeInfo.setText("确认修改");
                    setEnabled(true);
                    isEnabled = true;
                } else {
                    // 1.拿到输入框的值
                    String name = mName.getText().toString();
                    String age = mAge.getText().toString();
                    //true代表男
                    Boolean sex = mRadioGroup.getCheckedRadioButtonId()==R.id.female_btn?true:false;
                    String desc = mDescribe.getText().toString();
                    // 2.判断是否为空
                    if (!TextUtils.isEmpty(name) & !TextUtils.isEmpty(age)) {
                        // 3.更新属性
                        MyUser user = new MyUser();
                        user.setName(name);
                        user.setAge(age);
                        user.setSex(sex);
                        if (!TextUtils.isEmpty(desc)) {
                            user.setDesc(desc);
                        } else {
                            user.setDesc(getString(R.string.text_nothing));
                        }
                        BmobUser bmobUser = BmobUser.getCurrentUser(getActivity());
                        user.update(getActivity(), bmobUser.getObjectId(), new UpdateListener() {
                            @Override
                            public void onSuccess() {
                                mChangeInfo.setText("编辑资料");
                                setEnabled(false);
                                Toast.makeText(getActivity(), "修改成功！", Toast.LENGTH_SHORT).show();
                                isEnabled = false;
                            }

                            @Override
                            public void onFailure(int i, String s) {
                                Toast.makeText(getActivity(), "修改失败！", Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else {
                        Toast.makeText(getActivity(),"输入框不能为空！",Toast.LENGTH_SHORT).show();
                    }
                }
                break;
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
                startActivity(new Intent(getActivity(), MoreActivity.class));
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
