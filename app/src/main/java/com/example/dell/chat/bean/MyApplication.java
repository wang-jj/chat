package com.example.dell.chat.bean;

import android.app.Application;


import com.example.dell.chat.tools.Dao;

import org.litepal.LitePal;

/**
 * Created by wang on 2018/4/12.
 */

public class MyApplication extends Application {

    public static Dao dao;

    public static User user;

    public static User getUser() {
        return user;
    }

    public static void setUser(User usera) {
        user = usera;
    }

    public static Dao getDao() {
        return dao;
    }

    public static int timeout=5;

    @Override
    public void onCreate(){
        super.onCreate();
        LitePal.initialize(this);
        this.user=null;
        dao=new Dao(this);
    }

    public static int getTimeout() {
        return timeout;
    }
}
