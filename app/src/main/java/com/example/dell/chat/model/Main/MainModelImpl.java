package com.example.dell.chat.model.Main;

import android.util.Log;

import com.example.dell.chat.bean.Location;
import com.example.dell.chat.bean.MyApplication;
import com.example.dell.chat.bean.User;
import com.example.dell.chat.db.UserDao;
import com.example.dell.chat.model.Callback;
import com.example.dell.chat.model.Execute;
import com.example.dell.chat.tools.ThreadTask;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hyphenate.chat.EMClient;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.FormBody;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by wang on 2018/4/25.
 */

public class MainModelImpl implements MainModel {

    private String LocationUrl="http://119.23.255.222/android/upaddress.php";
    private String UpdateNearbyURL="http://119.23.255.222/android/tryforme.php";

    @Override
    public void SignOut(final Callback<Void> callback) {
        ThreadTask t=new ThreadTask<Void,Void,Void>(callback, new Execute() {
            @Override
            public Void doExec() {
                UserDao userDao= MyApplication.getDao().getUserDao();
                User u=MyApplication.getUser();
                //退出环信
                String user_id=String.valueOf(u.getUser_id());
                String password=u.getPassword();
                EMClient.getInstance().logout(true);
                EMClient.getInstance().chatManager().removeMessageListener(MyApplication.getListener());
                Log.e("listener","it is removed");

                u.setPassword(null);
                userDao.update(u);
                //请求网络
                return null;
            }
        });
        t.execute();
    }

    @Override
    public void sendLocation(final double Latitude, final double Longitude,final Callback<String> callback) {
        ThreadTask threadTask=new ThreadTask<Void,Void,String>(callback, new Execute<String>() {
            @Override
            public String doExec() {
                String result="no";
                try{//构建请求
                    OkHttpClient client=new OkHttpClient.Builder().connectTimeout(MyApplication.getTimeout(), TimeUnit.SECONDS).build();
                    RequestBody requestBody=new FormBody.Builder().add("userid",String.valueOf(MyApplication.getUser().getUser_id())).add("latitude",String.valueOf(Latitude)).add("longitude",String.valueOf(Longitude)).build();
                    Request request=new Request.Builder().url(LocationUrl).post(requestBody).build();
                    Response response=client.newCall(request).execute();
                    result=response.body().string();
                }catch (Exception e){
                    if(e instanceof SocketTimeoutException ||e instanceof ConnectException){//超时
                        result="no";
                    }else {
                        e.printStackTrace();
                    }
                }
                return result;
            }
        });
        threadTask.execute();
    }

}
