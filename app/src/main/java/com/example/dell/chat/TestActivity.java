package com.example.dell.chat;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.example.dell.chat.base.BaseActivity;
import com.example.dell.chat.bean.MyApplication;
import com.example.dell.chat.bean.User;
import com.example.dell.chat.presenter.LoginPresenter;
import org.litepal.tablemanager.Connector;

public class TestActivity extends BaseActivity<TestActivity,LoginPresenter<TestActivity>> {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Connector.getDatabase();
        User u=new User();
        u.setGender(1);
        u.setUser_id(1);
        u.setUser_name("wang");
        u.save();
        presenter.connect();
        Log.d("TestActivity","SUCCESS");
        MyApplication myApplication=(MyApplication)getApplication();
        setUserName(myApplication.getUser().getUser_name());
    }

    public void setUserName(String u) {
        TextView textView=(TextView)findViewById(R.id.textview1);
        textView.setText(u);
    }

    @Override
    protected LoginPresenter createPresenter() {
        return new LoginPresenter<TestActivity>();
    }
}
