package com.example.qwim.ui;

import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.qwim.bean.MyUser;
import com.example.qwim.event.RefreshEvent;
import com.example.qwim.ui.fragment.ContactFragment;
import com.example.qwim.ui.fragment.ConversationFragment;
import com.example.qwim.ui.fragment.MyFragment;
import com.example.qwim.R;

import org.greenrobot.eventbus.EventBus;

import cn.bmob.newim.BmobIM;
import cn.bmob.newim.listener.ConnectListener;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;

public class MainActivity extends AppCompatActivity {

    private RadioGroup mRadioGroup;
    private Fragment mConversationFragment;
    private Fragment mContactFragment;
    private Fragment mMyFragment;
    private Fragment mCurrentFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MyUser user = BmobUser.getCurrentUser(this,MyUser.class);
        BmobIM.connect(user.getObjectId(), new ConnectListener() {
            @Override
            public void done(String uid, BmobException e) {
                if (e == null) {
                    //服务器连接成功就发送一个更新事件，同步更新会话及主页的小红点
                    EventBus.getDefault().post(new RefreshEvent());
                } else {

                }
            }
        });
        initTab();
        initFragment();
    }

    private void initFragment() {
        mConversationFragment = new ConversationFragment();
        mContactFragment = new ContactFragment();
        mMyFragment = new MyFragment();
        //设置初始按钮和碎片界面

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container,
                mConversationFragment).addToBackStack(null).commit();
        mCurrentFragment = mConversationFragment;
    }

    private void initTab() {
        mRadioGroup = (RadioGroup) findViewById(R.id.main_bottom);
        mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                switch (checkedId) {
                    case R.id.btn_conversation:
                        switchFragment(mConversationFragment);

                        break;
                    case R.id.btn_contact:
                        switchFragment(mContactFragment);

                        break;
                    case R.id.btn_setting:
                        switchFragment(mMyFragment);

                        break;
                    default:
                }
            }
        });
        mRadioGroup.check(R.id.btn_conversation);
    }

    private void switchFragment(Fragment fragment) {
        if (fragment != mCurrentFragment) {
            if (!fragment.isAdded()) {
                getSupportFragmentManager().beginTransaction().hide(mCurrentFragment)
                        .add(R.id.fragment_container, fragment).addToBackStack(null).commit();
            } else {
                getSupportFragmentManager().beginTransaction().hide(mCurrentFragment)
                        .show(fragment).addToBackStack(null).commit();
            }
            mCurrentFragment = fragment;
        }
    }

    // 记录用户首次点击返回键的时间
    private long firstTime = 0;
    /**
     * 监听keyUp 实现双击退出程序
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {
            long secondTime = System.currentTimeMillis();
            if (secondTime - firstTime > 2000) {
                Toast.makeText(this,"再按一次退出程序",Toast.LENGTH_SHORT).show();
                firstTime = secondTime;
                return true;
            } else {
                System.exit(0);
            }
        }
        return super.onKeyUp(keyCode, event);
    }
}
