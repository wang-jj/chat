package com.example.dell.chat.model.Moment;

import android.util.Base64;

import com.example.dell.chat.bean.MyApplication;
import com.example.dell.chat.bean.PersonalState;
import com.example.dell.chat.bean.User;
import com.example.dell.chat.db.PersonalStateDao;
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
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by wang on 2018/4/13.
 */

public class MomentModelImpl implements MomentModel {

    private String PublishUrl="";

    @Override
    public void LoadMoment(final int holder_id, final int offset, final int limit,final Callback callback) {
        ThreadTask threadTask=new ThreadTask<Void,Void,List<PersonalState>>(callback, new Execute<List<PersonalState>>() {
            @Override
            public List<PersonalState> doExec() {
                PersonalStateDao personalStateDao=MyApplication.getDao().getPersonalStateDao();
                List<PersonalState> personalStates=personalStateDao.queryBuilder().orderDesc(PersonalStateDao.Properties.Holder_id).build().forCurrentThread().list();
                return personalStates;
            }
        });
        threadTask.execute();
    }

    @Override
    public void Publish(final PersonalState personalState, Callback callback) {
        ThreadTask threadTask=new ThreadTask<Void,Void,PersonalState>(callback, new Execute<PersonalState>() {
            @Override
            public PersonalState doExec() {
                String encodeString1=translate(personalState.getImage1ID());
                String encodeString2=translate(personalState.getImage2ID());
                String encodeString3=translate(personalState.getImage3ID());
                try {
                    OkHttpClient client=new OkHttpClient.Builder().connectTimeout(MyApplication.getTimeout(), TimeUnit.SECONDS).build();
                    //RequestBody requestBody=new FormBody.Builder().add("user",new Gson().toJson(personalState).toString()).add("image",encodedString).build();
                    RequestBody requestBody=null;
                    if(personalState.getImg_type()==0){
                        requestBody=new FormBody.Builder().add("userid",String.valueOf(MyApplication.getUser().getUser_id())).add("password",MyApplication.getUser().getPassword()).build();
                    }else if(personalState.getImg_type()==1){
                        requestBody=new FormBody.Builder().add("userid",String.valueOf(MyApplication.getUser().getUser_id())).add("password",MyApplication.getUser().getPassword()).add("image1",encodeString1).build();
                    }else if(personalState.getImg_type()==2){
                        requestBody=new FormBody.Builder().add("userid",String.valueOf(MyApplication.getUser().getUser_id())).add("password",MyApplication.getUser().getPassword()).add("image1",encodeString1).add("image2",encodeString2).build();
                    }else {
                        requestBody=new FormBody.Builder().add("userid",String.valueOf(MyApplication.getUser().getUser_id())).add("password",MyApplication.getUser().getPassword()).add("image1",encodeString1).add("image2",encodeString2).add("image3",encodeString3).build();
                    }
                    Request request=new Request.Builder().url(PublishUrl).post(requestBody).build();
                    Response response=client.newCall(request).execute();
                    String a=response.body().string();
                    PersonalState p=new Gson().fromJson(a,PersonalState.class);
                    return p;
                }catch (IOException e){
                    if(e instanceof SocketTimeoutException ||e instanceof ConnectException){//超时
                        PersonalState p=new PersonalState();
                        p.setPersonalstate_id(-1);
                        return p;
                    }else {
                        e.printStackTrace();
                    }
                }
                return null;
            }
        });
        threadTask.execute();
    }

    //图片转编码
    public String translate(String path){
        if(path==null){
            return null;
        }
        String encodedString =null;
        try {
            File file = new File(path);
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
        }catch (Exception e){
            e.printStackTrace();
        }
            return  encodedString;
    }
}
