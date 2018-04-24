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

                    /*
                    Log.e("LoadActivity", user.getImage_path());
                    MediaType MEDIA_TYPE_PNG = MediaType.parse("image/jpeg");
                    MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
                    File f=new File("/storage/emulated/0/Android/data/com.example.dell.chat/cache/luban_disk_cache/1524310777712611.JPEG");
                    Log.e("LoadActivity", user.getImage_path());
                    if (f!=null) {
                        builder.addFormDataPart("image", f.getName(), RequestBody.create(MEDIA_TYPE_PNG, f));
                    }
                    MultipartBody requestBody = builder.build();
                    */

                    /*
                    File file = new File(user.getImage_path());
                    FileInputStream inputFile = null;
                    String encodedString =null;
                    try {
                        inputFile = new FileInputStream(file);
                        byte[] buffer = new byte[(int) file.length()];
                        inputFile.read(buffer);
                        inputFile.close();
                        encodedString = Base64.encodeToString(buffer, Base64.NO_WRAP);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    RequestBody requestBody=new FormBody.Builder().add("image",encodedString).build();
                    //构建请求
                    Request req = new Request.Builder()
                            .url("http://119.23.255.222/android/try.php")//地址
                            .post(requestBody)//添加请求体
                            .build();
                    Response res=client.newCall(req).execute();
                    String b=res.body().string();
                    Log.e("LoadActivity", user.getImage_path());
                    //Log.e("LoadActivity", String.valueOf(b.length()));
                    //Log.e("LoadActivity", b);
                    */
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

    @Override
    public void SentImage(String path) {

    }
}
