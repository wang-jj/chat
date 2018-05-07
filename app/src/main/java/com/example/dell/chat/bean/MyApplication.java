package com.example.dell.chat.bean;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDexApplication;


import com.awen.photo.FrescoImageLoader;
import com.baidu.mapapi.SDKInitializer;
import com.example.dell.chat.tools.Dao;
import com.example.dell.chat.view.ChatActivity;
import com.example.dell.chat.view.MsgFragment;
import com.hyphenate.EMMessageListener;


/**
 * Created by wang on 2018/4/12.
 */

public class MyApplication extends MultiDexApplication {

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

    public static int timeout=10;

    private static Context context;

    private static double Latitude;        //纬度
    private static double Longitude;       //经度

    private static int UpdateLocationTime=10000;

    private static MsgFragment Frag;

    private static EMMessageListener Listener;

    private static int ChattingMode=0;

    private static ChatActivity ChatActivity=null;

    @Override
    public void onCreate(){
        super.onCreate();
        SDKInitializer.initialize(this);
        this.user=null;
        dao=new Dao(this);
        MyApplication.context = getApplicationContext();
        FrescoImageLoader.init(this);
        //下面是配置toolbar颜色和存储图片地址的
        //FrescoImageLoader.init(this,android.R.color.holo_blue_light);
        //FrescoImageLoader.init(this,android.R.color.holo_blue_light,"/storage/xxxx/xxx");

    }

    public static int getTimeout() {
        return timeout;
    }

    public static Context getContext() {
        return context;
    }

    public static double getLatitude() {
        return Latitude;
    }

    public static double getLongitude() {
        return Longitude;
    }

    public static void setLatitude(double latitude) {
        Latitude = latitude;
    }

    public static void setLongitude(double longitude) {
        Longitude = longitude;
    }

    public static int getUpdateLocationTime() {
        return UpdateLocationTime;
    }

    public static MsgFragment getFrag(){
        return Frag;
    }

    public static void setFrag(MsgFragment frag){
        Frag = frag;

    }

    public static void setListener(EMMessageListener listener){
        Listener = listener;
    }

    public static EMMessageListener getListener(){
        return Listener;
    }

    public static void setChattingMode(int chattingMode){
        ChattingMode = chattingMode;
    }

    public static int getChattingMode(){
        return ChattingMode;
    }

    public static void setChatActivity(ChatActivity chatActivity){
        ChatActivity=chatActivity;
    }

    public static ChatActivity getChatActivity(){
        return ChatActivity;
    }
}
