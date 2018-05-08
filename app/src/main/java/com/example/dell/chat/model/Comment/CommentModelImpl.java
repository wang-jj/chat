package com.example.dell.chat.model.Comment;

import android.util.Log;

import com.example.dell.chat.bean.Contact;
import com.example.dell.chat.bean.Location;
import com.example.dell.chat.bean.MyApplication;
import com.example.dell.chat.model.Callback;
import com.example.dell.chat.model.Execute;
import com.example.dell.chat.tools.ThreadTask;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by wang on 2018/5/7.
 */

public class CommentModelImpl implements CommentModel {
    private String DownloadCommentUrl="http://119.23.255.222/android/downcomment.php";
    private String UpCommentUrl="http://119.23.255.222/android/upcomment.php";

    public void UpComment(final int momentID, final int user_id, final int holder_id, final String content, final Callback<String>callback){
        ThreadTask<Void,Void,String>threadTask=new ThreadTask<Void,Void,String>(callback, new Execute<String>() {
            @Override
            public String doExec() {
                String result=null;
                try{//构建请求
                    OkHttpClient client=new OkHttpClient.Builder().connectTimeout(MyApplication.getTimeout(), TimeUnit.SECONDS).build();
                    String URL=UpCommentUrl+"?momentid="+String.valueOf(momentID)+"&comment_content="+String.valueOf(content)+"&User_id="+String.valueOf(user_id)+"&Holder_id="+String.valueOf(holder_id);
                    Log.e("upmoment", URL );
                    Request request=new Request.Builder().url(URL).build();
                    Response response=client.newCall(request).execute();
                    result=response.body().string();
                    Log.e("upmoment", result );
                }catch (Exception e){
                    if(e instanceof SocketTimeoutException ||e instanceof ConnectException){//超时
                        result="out_of_time";
                    }else {
                        e.printStackTrace();
                    }
                }
                return result;
            }
        });
        threadTask.execute();
    }

    @Override
    public void LoadComment(final int momentid,final Callback<String>callback){
        ThreadTask<Void,Void,String>threadTask=new ThreadTask<Void,Void,String>(callback, new Execute<String>() {
            @Override
            public String doExec() {
                String result=null;
                try{//构建请求
                    OkHttpClient client=new OkHttpClient.Builder().connectTimeout(MyApplication.getTimeout(), TimeUnit.SECONDS).build();
                    String URL=DownloadCommentUrl+"?momentid="+String.valueOf(momentid);
                    Log.e("upmoment", URL );
                    Request request=new Request.Builder().url(URL).build();
                    Response response=client.newCall(request).execute();
                    result=response.body().string();
                }catch (Exception e){
                    if(e instanceof SocketTimeoutException ||e instanceof ConnectException){//超时
                        result="out_of_time";
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
