package com.example.dell.chat.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.dell.chat.R;

//个人资料activity
public class DataActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        Button button=(Button)findViewById(R.id.cancel_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //退出登录 事件
            }
        });

        //初始化头像
        ImageView imageView_profile=(ImageView)findViewById(R.id.data_profile);
        imageView_profile.setImageResource(R.drawable.profile);
        //初始化昵称
        TextView textView_nickname=(TextView)findViewById(R.id.data_nickname);
        textView_nickname.setText("麦梓逗比旗");
        //初始化性别
        TextView textView_gender=(TextView)findViewById(R.id.data_gender);
        textView_gender.setText("男");
        //初始化生日
        TextView textView_birth=(TextView)findViewById(R.id.data_birth);
        textView_birth.setText("1996-06-18");
        //初始化简介
        TextView textView_pre=(TextView)findViewById(R.id.data_pre);
        textView_pre.setText("我是麦梓逗比旗...");
        //初始化账号
        TextView textView_account=(TextView)findViewById(R.id.data_account);
        textView_account.setText("23456789");
        //跳转生成名片二维码

    }

}
