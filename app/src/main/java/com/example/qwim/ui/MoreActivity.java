package com.example.qwim.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.qwim.R;
import com.example.qwim.base.BaseActivity;
import com.example.qwim.user.MyUser;

/**
 * Created by qikaixuan on 17-4-18.
 */

public class MoreActivity extends BaseActivity{

    private static final String[] strs = new String[]{"修改密码", "用户协议", "关于我们", "退出"};

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more);
        ListView listView = (ListView) findViewById(R.id.list_more);
        listView.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, strs));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switchActivity(position);
            }
        });
    }

    private void switchActivity(int position) {
        switch (position) {
            case 0: // 修改密码
                if (MyUser.getCurrentUser(this)!=null) {
                    startActivity(new Intent(this, ChangePasswordActivity.class));
                } else {
                    Toast.makeText(this, "您并没有登录",Toast.LENGTH_SHORT).show();
                }
                break;
            case 1: // 用户协议
                break;
            case 2: // 关于我们
                break;
            case 3: // 退出
                if (MyUser.getCurrentUser(this)!=null) {
                    onExit();
                } else {
                    Toast.makeText(this, "您并没有登录",Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
    }

    private void onExit() {
        new AlertDialog.Builder(this)
                .setMessage("确定退出吗？")
                .setCancelable(true)
                .setNegativeButton("取消",null)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        MyUser.logOut(MoreActivity.this);
                        startActivity(new Intent(MoreActivity.this,LoginActivity.class));
                        finish();

                    }
                }).show();
    }
}
