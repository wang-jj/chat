package com.example.dell.chat.bean;

import android.app.Application;

import org.litepal.LitePal;

/**
 * Created by wang on 2018/4/12.
 */

public class MyApplication extends Application {
    public User user;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public void onCreate(){
        super.onCreate();
        LitePal.initialize(this);
        this.user=null;
    }
}
