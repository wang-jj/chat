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

import com.bumptech.glide.Glide;
import com.example.dell.chat.R;
import com.example.dell.chat.bean.MyApplication;
import com.example.dell.chat.bean.User;

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

        User u= MyApplication.getUser();
        //初始化头像
        ImageView imageView_profile=(ImageView)findViewById(R.id.data_profile);
        Glide.with(DataActivity.this).load(u.getImage_path()).into(imageView_profile);
        //imageView_profile.setImageResource(R.drawable.profile);
        //初始化昵称
        TextView textView_nickname=(TextView)findViewById(R.id.data_nickname);
        textView_nickname.setText(u.getUser_name());
        //初始化性别
        TextView textView_gender=(TextView)findViewById(R.id.data_gender);
        if(u.getGender()==1){
            textView_gender.setText("女");
        }else if(u.getGender()==2){
            textView_gender.setText("男");
        }else {
            textView_gender.setText("无数据");
        }
        //初始化生日
        TextView textView_birth=(TextView)findViewById(R.id.data_birth);
        textView_birth.setText(u.getBirthday());
        //初始化简介
        TextView textView_pre=(TextView)findViewById(R.id.data_pre);
        textView_pre.setText(u.getUser_motto());
        //初始化账号
        TextView textView_account=(TextView)findViewById(R.id.data_account);
        textView_account.setText(String.valueOf(u.getUser_id()));
        //跳转生成名片二维码

    }

}
