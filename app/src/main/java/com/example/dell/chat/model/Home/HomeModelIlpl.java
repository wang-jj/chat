package com.example.dell.chat.model.Home;

import com.example.dell.chat.bean.MyApplication;
import com.example.dell.chat.bean.PersonalState;
import com.example.dell.chat.db.PersonalStateDao;
import com.example.dell.chat.model.Callback;
import com.example.dell.chat.model.Execute;
import com.example.dell.chat.tools.Dao;
import com.example.dell.chat.tools.ThreadTask;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by wang on 2018/4/26.
 */

public class HomeModelIlpl implements HomeModel {

    private String updateMomentUrl="";
    @Override
    public void UpdateMoment(final double latitude,final double longitude,final List<PersonalState> list,final Callback<List<PersonalState>> callback){
        ThreadTask<Void,Void,List<PersonalState>> threadTask=new ThreadTask<Void,Void,List<PersonalState>>(callback, new Execute<List<PersonalState>>() {
            @Override
            public List<PersonalState> doExec() {
                List<PersonalState> personalStates=null;
                try{//构建请求
                    OkHttpClient client=new OkHttpClient.Builder().connectTimeout(MyApplication.getTimeout(), TimeUnit.SECONDS).build();
                    RequestBody requestBody=new FormBody.Builder().add("latitude",String.valueOf(latitude)).add("longitude",String.valueOf(longitude)).build();
                    Request request=new Request.Builder().url(updateMomentUrl).post(requestBody).build();
                    Response response=client.newCall(request).execute();
                    String result=response.body().string();
                    personalStates =new Gson().fromJson(result, new TypeToken<List<PersonalState>>() {}.getType());
                    for(PersonalState i:personalStates){
                        i.setUpdate_time(new Date(System.currentTimeMillis()));
                        for (PersonalState j:list){
                            if(i.getPersonalstate_id()==j.getPersonalstate_id()){
                                personalStates.remove(i);
                                break;
                            }
                        }
                    }
                    //排序
                    Collections.sort(personalStates, new Comparator<PersonalState>() {
                        @Override
                        public int compare(PersonalState personalState1, PersonalState personalState2) {
                            return personalState2.getUpdate_time().compareTo(personalState1.getState_time());
                        }
                    });
                    //跟新数据库
                    PersonalStateDao personalStateDao=MyApplication.getDao().getPersonalStateDao();
                    personalStateDao.updateInTx(personalStates);
                }catch (Exception e){
                    if(e instanceof SocketTimeoutException ||e instanceof ConnectException){//超时

                    }else {
                        e.printStackTrace();
                    }
                }
                return personalStates;
            }
        });
        threadTask.execute();
    }
}
