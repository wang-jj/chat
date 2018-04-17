package com.example.dell.chat.model.Login;


import android.util.Log;

import com.example.dell.chat.bean.MyApplication;
import com.example.dell.chat.bean.User;
import com.example.dell.chat.db.UserDao;
import com.example.dell.chat.model.Callback;
import com.example.dell.chat.model.Execute;
import com.example.dell.chat.tools.ThreadTask;
import com.google.gson.Gson;

import org.json.JSONObject;
import org.litepal.crud.DataSupport;

import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


/**
 * Created by wang on 2018/4/12.
 */

public class LoginModelImpl implements LoginModel {

    String LoginUrl="http://119.23.255.222/android/logininhis.php";
    String UpdateUrl;
    String createUrl="http://119.23.255.222/android/login.php\n";

    @Override
    public void FindLastUser(final Callback<User> callback){
        ThreadTask t=new ThreadTask<Void,Void,User>(callback, new Execute<User>() {
            @Override
            public User doExec() {
                //网络请求
                UserDao userDao= MyApplication.getDao().getUserDao();
                List<User> users=userDao.loadAll();
                if(users.size()==0){//user表空，返回空
                    return null;
                }else {//user表不为空，请求网络判断账号密码是否正确
                    User u=null;
                    try {
                        Log.e("LoadActivity", new Gson().toJson(users.get(0)).toString());
                        OkHttpClient client=new OkHttpClient.Builder().connectTimeout(MyApplication.getTimeout(), TimeUnit.SECONDS).build();
                        RequestBody requestBody=new FormBody.Builder().add("user",new Gson().toJson(users.get(0)).toString()).build();
                        Request request=new Request.Builder().url(LoginUrl+"?user="+new Gson().toJson(users.get(0)).toString()).build();
                        Response response=client.newCall(request).execute();
                        String a=response.body().string();
                        Log.e("LoadActivity", a);
                        //u=new Gson().fromJson(a,User.class);
                    }catch (IOException e){
                        if(e instanceof SocketTimeoutException||e instanceof ConnectException){
                            User exception=new User();
                            exception.setUser_id(-3);
                            return exception;
                        }else {
                            e.printStackTrace();
                        }
                    }
                    return u;
                }
            }
        });
        t.execute();
    }

    @Override
    public void UpdateUser(final User u){
        ThreadTask t=new ThreadTask<Void,Void,Void>(null, new Execute<Void>() {
            @Override
            public Void doExec() {
                UserDao userDao=MyApplication.getDao().getUserDao();
                userDao.update(u);
                MyApplication.setUser(u);
                return null;
            }
        });
        t.execute();
    }

    @Override
    public void CreateUser(final User u,final Callback<User> callback){
        ThreadTask t=new ThreadTask<Void,Void,User>(callback, new Execute<User>() {
            @Override
            public User doExec() {
                //网络请求
                User u=null;
                try {
                    //Log.e("LoadActivity", new Gson().toJson(MyApplication.getUser()).toString());
                    OkHttpClient client=new OkHttpClient.Builder().connectTimeout(MyApplication.getTimeout(), TimeUnit.SECONDS).build();
                    RequestBody requestBody=new FormBody.Builder().add("user",new Gson().toJson(MyApplication.getUser()).toString()).build();
                    Request request=new Request.Builder().url(LoginUrl+"?user="+new Gson().toJson(MyApplication.getUser()).toString()).build();
                    Response response=client.newCall(request).execute();
                    String a=response.body().string();
                    //Log.e("LoadActivity", a);
                    u=new Gson().fromJson(a,User.class);
                }catch (IOException e){
                    if(e instanceof SocketTimeoutException||e instanceof ConnectException){
                        User exception=new User();
                        exception.setUser_id(-3);
                        return exception;
                    }else {
                        e.printStackTrace();
                    }
                }
                if(u!=null&&u.getUser_id()>0){
                    String file=MyApplication.getUser().getImage_path();
                    MyApplication.setUser(null);
                    //上传图片
                }
                return u;
            }
        });
        t.execute();
    }

    @Override
    public void LoginByEmail(final String email, final String password, Callback<User> callback) {
        ThreadTask t=new ThreadTask<Void,Void,User>(callback, new Execute<User>() {
            @Override
            public User doExec() {
                User u=null;
                try {
                    OkHttpClient client=new OkHttpClient.Builder().connectTimeout(MyApplication.getTimeout(), TimeUnit.SECONDS).build();
                    RequestBody requestBody=new FormBody.Builder().add("email",email).add("password",password).build();
                    Request request=new Request.Builder().url(createUrl).post(requestBody).build();
                    Response response=client.newCall(request).execute();
                    String a=response.body().string();
                    u=new Gson().fromJson(a,User.class);
                }catch (IOException e){
                    if(e instanceof SocketTimeoutException||e instanceof ConnectException){
                        User exception=new User();
                        exception.setUser_id(-3);
                        return exception;
                    }else {
                        e.printStackTrace();
                    }
                }
                return u;
            }
        });
        t.execute();
    }
}
