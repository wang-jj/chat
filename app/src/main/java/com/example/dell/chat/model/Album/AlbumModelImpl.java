package com.example.dell.chat.model.Album;

import android.util.Log;

import com.example.dell.chat.bean.Location;
import com.example.dell.chat.bean.MyApplication;
import com.example.dell.chat.bean.PersonalState;
import com.example.dell.chat.model.Callback;
import com.example.dell.chat.model.Execute;
import com.example.dell.chat.tools.ThreadTask;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
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
 * Created by wang on 2018/5/6.
 */

public class AlbumModelImpl implements AlbumModel {

    private String URL="http://119.23.255.222/android/personaldis.php ";

    @Override
    public void LoadPersonalState(final int User_id, Callback<List<PersonalState>> callback) {
        ThreadTask<Void,Void,List<PersonalState>> threadTask=new ThreadTask<>(callback, new Execute<List<PersonalState>>() {
            @Override
            public List<PersonalState> doExec() {
                List<PersonalState>personalStates=null;
                try {
                    OkHttpClient client=new OkHttpClient.Builder().connectTimeout(MyApplication.getTimeout(), TimeUnit.SECONDS).build();
                    RequestBody requestBody=new FormBody.Builder().add("user_id",String.valueOf(User_id)).build();
                    Request request=new Request.Builder().url(URL).post(requestBody).build();
                    Response response=client.newCall(request).execute();
                    String result=response.body().string();
                    int i=result.indexOf("[");
                    result=result.substring(i);
                    //Log.e("album", result);
                    Gson gson=new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
                    personalStates=gson.fromJson(result,new TypeToken<List<PersonalState>>(){}.getType());
                }catch (Exception e) {
                    if (e instanceof SocketTimeoutException || e instanceof ConnectException) {//超时
                        Log.e("updateperson", "outoftime");
                    } else {
                        e.printStackTrace();
                    }
                }
                return personalStates;
            }
        });
        threadTask.execute();
    }
}
