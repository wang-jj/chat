package com.example.dell.chat.model.Login;


import android.util.Base64;
import android.util.Log;

import com.example.dell.chat.bean.MyApplication;
import com.example.dell.chat.bean.User;
import com.example.dell.chat.db.UserDao;
import com.example.dell.chat.model.Callback;
import com.example.dell.chat.model.Execute;
import com.example.dell.chat.tools.ThreadTask;
import com.google.gson.Gson;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
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
    String createUrl="http://119.23.255.222/android/register.php";

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
                }else if(users.get(0).getPassword()==null||users.get(0).getPassword()==""){//注销了，需要重新输入密码
                    return users.get(0);
                }else {//user表不为空，请求网络判断账号密码是否正确
                    User u=null;
                    try {
                        Log.e("LoadActivity", new Gson().toJson(users.get(0)).toString());
                        OkHttpClient client=new OkHttpClient.Builder().connectTimeout(MyApplication.getTimeout(), TimeUnit.SECONDS).build();
                        //MediaType JSON=MediaType.parse("application/json; charset=utf-8");
                        //RequestBody requestBody=RequestBody.create(JSON,new Gson().toJson(users.get(0)));
                        Request request=new Request.Builder().url(LoginUrl+"?user="+new Gson().toJson(users.get(0)).toString()).build();
                        //Request request=new Request.Builder().url(LoginUrl).post(requestBody).build();
                        Response response=client.newCall(request).execute();
                        String a=response.body().string();
                        u=new Gson().fromJson(a,User.class);
                        u.setPassword(users.get(0).getPassword());
                        u.setId(users.get(0).getId());
                        //登录环信
                        String user_id=String.valueOf(u.getUser_id());
                        String password=u.getPassword();
                        EMClient.getInstance().login(user_id,password,new EMCallBack() {//回调
                            @Override
                            public void onSuccess() {
                                EMClient.getInstance().groupManager().loadAllGroups();
                                EMClient.getInstance().chatManager().loadAllConversations();
                                Log.d("log in", "登录聊天服务器成功！");
                            }

                            @Override
                            public void onProgress(int progress, String status) {

                            }

                            @Override
                            public void onError(int code, String message) {
                                Log.d("log in", "登录聊天服务器失败！");
                            }
                        });
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
    public void CreateUser(final User user,final Callback<User> callback){
        ThreadTask t=new ThreadTask<Void,Void,User>(callback, new Execute<User>() {
            @Override
            public User doExec() {
                //网络请求
                User u=null;
                String encodedString =null;
                try {
                    File file = new File(user.getImage_path());
                    FileInputStream inputFile = null;
                    try {
                        inputFile = new FileInputStream(file);
                        byte[] buffer = new byte[(int) file.length()];
                        inputFile.read(buffer);
                        inputFile.close();
                        encodedString = Base64.encodeToString(buffer, Base64.NO_WRAP);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    OkHttpClient client=new OkHttpClient.Builder().connectTimeout(MyApplication.getTimeout(), TimeUnit.SECONDS).build();
                    RequestBody requestBody=new FormBody.Builder().add("user",new Gson().toJson(user).toString()).add("image",encodedString).build();
                    Request request=new Request.Builder().url(createUrl).post(requestBody).build();
                    Response response=client.newCall(request).execute();
                    String a=response.body().string();
                    //Log.e("LoadActivity", a);
                    u=new Gson().fromJson(a,User.class);
                    if(u.getUser_id()!=-1){//注册环信
                        String user_id=String.valueOf(user.getUser_id());
                        String password=user.getPassword();
                        try {
                            //注册失败会抛出HyphenateException
                            EMClient.getInstance().createAccount(user_id, password);//同步方法
                        } catch (HyphenateException e) {
                            Log.e("sign up", "sign up failed");
                        }
                    }
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
                    MyApplication.setUser(null);
                    //上传图片
                }
                return u;
            }
        });
        t.execute();
    }

    @Override
    public void LoginByEmail(final User user, Callback<User> callback) {
        ThreadTask t=new ThreadTask<Void,Void,User>(callback, new Execute<User>() {
            @Override
            public User doExec() {
                User u=null;
                try {
                    Log.e("erroractivity", new Gson().toJson(user).toString() );
                    OkHttpClient client=new OkHttpClient.Builder().connectTimeout(MyApplication.getTimeout(), TimeUnit.SECONDS).build();
                    //RequestBody requestBody=new FormBody.Builder().add("user",new Gson().toJson(user).toString()).build();
                    Request request=new Request.Builder().url(LoginUrl+"?user="+new Gson().toJson(user).toString()).build();
                    Response response=client.newCall(request).execute();
                    String a=response.body().string();
                    u=new Gson().fromJson(a,User.class);
                    u.setPassword(user.getPassword());
                    if(u.getUser_id()>0){//登录环信
                        String user_id=String.valueOf(u.getUser_id());
                        String password=u.getPassword();
                        EMClient.getInstance().login(user_id,password,new EMCallBack() {//回调
                            @Override
                            public void onSuccess() {
                                EMClient.getInstance().groupManager().loadAllGroups();
                                EMClient.getInstance().chatManager().loadAllConversations();
                                Log.d("log in", "登录聊天服务器成功！");
                            }

                            @Override
                            public void onProgress(int progress, String status) {

                            }

                            @Override
                            public void onError(int code, String message) {
                                Log.d("log in", "登录聊天服务器失败！");
                            }
                        });
                    }
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
