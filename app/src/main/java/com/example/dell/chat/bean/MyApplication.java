package com.example.dell.chat.bean;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.multidex.MultiDexApplication;
import android.util.Log;


import com.awen.photo.FrescoImageLoader;
import com.baidu.mapapi.SDKInitializer;
import com.example.dell.chat.tools.Dao;
import com.example.dell.chat.view.ChatActivity;
import com.example.dell.chat.view.MainActivity;
import com.example.dell.chat.view.MsgFragment;
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMOptions;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;


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

    private static String StorePath;

    public static String getStorePath() {
        return StorePath;
    }

    public static void setStorePath(String storePath) {
        StorePath = storePath;
    }

    public static void setDao(Dao dao) {
        MyApplication.dao = dao;
    }

    private static double Latitude;        //纬度
    private static double Longitude;       //经度

    private static int UpdateLocationTime=10000;

    private static MsgFragment Frag;

    private static EMMessageListener Listener;

    private static int ChattingMode=0;

    private static ChatActivity ChatActivity=null;

    private static MainActivity mainActivity;

    private static Boolean isInternet = false;

    public static Boolean getIsInternet() {
        return isInternet;
    }

    public static void setIsInternet(Boolean isInternet) {
        MyApplication.isInternet = isInternet;
    }

    public static MainActivity getMainActivity() {
        return mainActivity;
    }

    public static void setMainActivity(MainActivity mainActivity) {
        MyApplication.mainActivity = mainActivity;
    }

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
        //初始化环信sdk
        //
        EMOptions options = new EMOptions();
        // 默认添加好友时，是不需要验证的，改成需要验证
        options.setAcceptInvitationAlways(false);
        //自动登录 除非手动登出
        options.setAutoLogin(false);
        // 是否自动将消息附件上传到环信服务器，默认为True是使用环信服务器上传下载，如果设为 false，需要开发者自己处理附件消息的上传和下载
        options.setAutoTransferMessageAttachments(true);
        // 是否自动下载附件类消息的缩略图等，默认为 true 这里和上边这个参数相关联
        options.setAutoDownloadThumbnail(true);

        //应对第三方服务
        //
        Context appContext = MyApplication.getContext();
        int pid = android.os.Process.myPid();
        String processAppName = getAppName(pid);
        // 如果APP启用了远程的service，此application:onCreate会被调用2次
        // 为了防止环信SDK被初始化2次，加此判断会保证SDK被初始化1次
        // 默认的APP会在以包名为默认的process name下运行，如果查到的process name不是APP的process name就立即返回

        if (processAppName == null ||!processAppName.equalsIgnoreCase(appContext.getPackageName())) {
            Log.e("TestActivity", "enter the service process!");

            // 则此application::onCreate 是被service 调用的，直接返回
            return;
        }

        //初始化
        EMClient.getInstance().init(getApplicationContext(), options);
        //在做打包混淆时，关闭debug模式，避免消耗不必要的资源
        EMClient.getInstance().setDebugMode(true);
        Log.d("test", "i get there");
    }

    //获取当前应用名用于判断是否为环信在调用
    private String getAppName(int pID) {
        String processName = null;
        ActivityManager am = (ActivityManager) this.getSystemService(ACTIVITY_SERVICE);
        List l = am.getRunningAppProcesses();
        Iterator i = l.iterator();
        PackageManager pm = this.getPackageManager();
        while (i.hasNext()) {
            ActivityManager.RunningAppProcessInfo info = (ActivityManager.RunningAppProcessInfo) (i.next());
            try {
                if (info.pid == pID) {
                    processName = info.processName;
                    return processName;
                }
            } catch (Exception e) {
                // Log.d("Process", "Error>> :"+ e.toString());
            }
        }
        return processName;
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
