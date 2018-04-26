package com.example.dell.chat.model.Moment;

import android.util.Base64;
import android.util.Log;

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
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by wang on 2018/4/13.
 */

public class MomentModelImpl implements MomentModel {

    private String PublishUrl="http://119.23.255.222/android/uploaddiscover.php";

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
        ThreadTask threadTask=new ThreadTask<Void,Void,String>(callback, new Execute<String>() {
            @Override
            public String doExec() {
                String a="no";
                MediaType MEDIA_TYPE_PNG = MediaType.parse("image/png");
                MediaType MEDIA_TYPE_JPG = MediaType.parse("image/jpeg");
                MediaType MEDIA_TYPE_GIF = MediaType.parse("image/gif");
                MultipartBody.Builder multipartBodyBuilder = new MultipartBody.Builder();
                multipartBodyBuilder.setType(MultipartBody.FORM);
                multipartBodyBuilder.addFormDataPart("personalState",new Gson().toJson(personalState));
                if(personalState.getImg_type()>0){//一张照片
                    String s=personalState.getImage1ID();
                    File file=new File(s);
                    if(s.substring(s.length()-3).equals("gif")) {
                        multipartBodyBuilder.addFormDataPart("image", file.getName(), RequestBody.create(MEDIA_TYPE_GIF, file));
                    }else if(s.substring(s.length()-3).equals("jpg")||s.substring(s.length()-4).equals("jpeg")){
                        multipartBodyBuilder.addFormDataPart("image", file.getName(), RequestBody.create(MEDIA_TYPE_JPG, file));
                    }else {
                        multipartBodyBuilder.addFormDataPart("image", file.getName(), RequestBody.create(MEDIA_TYPE_PNG, file));
                    }
                }
                if(personalState.getImg_type()>1){//两张照片
                    String s=personalState.getImage2ID();
                    File file=new File(s);
                    if(s.substring(s.length()-3).equals("gif")) {
                        multipartBodyBuilder.addFormDataPart("image", file.getName(), RequestBody.create(MEDIA_TYPE_GIF, file));
                    }else if(s.substring(s.length()-3).equals("jpg")||s.substring(s.length()-4).equals("jpeg")){
                        multipartBodyBuilder.addFormDataPart("image", file.getName(), RequestBody.create(MEDIA_TYPE_JPG, file));
                    }else {
                        multipartBodyBuilder.addFormDataPart("image", file.getName(), RequestBody.create(MEDIA_TYPE_PNG, file));
                    }
                }
                if(personalState.getImg_type()>2){//三张照片
                    String s=personalState.getImage3ID();
                    File file=new File(s);
                    if(s.substring(s.length()-3).equals("gif")) {
                        multipartBodyBuilder.addFormDataPart("image", file.getName(), RequestBody.create(MEDIA_TYPE_GIF, file));
                    }else if(s.substring(s.length()-3).equals("jpg")||s.substring(s.length()-4).equals("jpeg")){
                        multipartBodyBuilder.addFormDataPart("image", file.getName(), RequestBody.create(MEDIA_TYPE_JPG, file));
                    }else {
                        multipartBodyBuilder.addFormDataPart("image", file.getName(), RequestBody.create(MEDIA_TYPE_PNG, file));
                    }
                }
                try{//构建请求
                    OkHttpClient client=new OkHttpClient.Builder().connectTimeout(MyApplication.getTimeout(), TimeUnit.SECONDS).build();
                    MultipartBody multipartBody=multipartBodyBuilder.build();
                    Request request=new Request.Builder().url(PublishUrl).post(multipartBody).build();
                    Response response=client.newCall(request).execute();
                    a=response.body().string();
                }catch (Exception e){
                    if(e instanceof SocketTimeoutException ||e instanceof ConnectException){//超时
                        a="no";
                    }else {
                        e.printStackTrace();
                    }
                }
                /*
                String encodeString1=translate(personalState.getImage1ID());
                String encodeString2=translate(personalState.getImage2ID());
                String encodeString3=translate(personalState.getImage3ID());
                if(personalState.getImg_type()>0){
                    String s=personalState.getImage1ID();
                    if(s.substring(s.length()-3).equals("gif")) {
                        personalState.setImage1ID("gif");
                    }else {
                        personalState.setImage1ID("jpg");
                    }
                }
                if(personalState.getImg_type()>1){
                    String s=personalState.getImage2ID();
                    if(s.substring(s.length()-3).equals("gif")) {
                        personalState.setImage2ID("gif");
                    }else {
                        personalState.setImage2ID("jpg");
                    }
                }
                if(personalState.getImg_type()>2){
                    String s=personalState.getImage3ID();
                    if(s.substring(s.length()-3).equals("gif")) {
                        personalState.setImage3ID("gif");
                    }else {
                        personalState.setImage3ID("jpg");
                    }
                }
                try {
                    OkHttpClient client=new OkHttpClient.Builder().connectTimeout(MyApplication.getTimeout(), TimeUnit.SECONDS).build();
                    //RequestBody requestBody=new FormBody.Builder().add("user",new Gson().toJson(personalState).toString()).add("image",encodedString).build();
                    RequestBody requestBody=null;
                    if(personalState.getImg_type()==0){
                        requestBody=new FormBody.Builder().add("personalState",new Gson().toJson(personalState).toString()).build();
                    }else if(personalState.getImg_type()==1){
                        requestBody=new FormBody.Builder().add("image1",encodeString1).add("personalState",new Gson().toJson(personalState).toString()).build();
                    }else if(personalState.getImg_type()==2){
                        requestBody=new FormBody.Builder().add("image1",encodeString1).add("image2",encodeString2).add("personalState",new Gson().toJson(personalState).toString()).build();
                    }else {
                        requestBody=new FormBody.Builder().add("image1",encodeString1).add("image2",encodeString2).add("image3",encodeString3).add("personalState",new Gson().toJson(personalState).toString()).build();
                    }
                    Log.e("Test", new Gson().toJson(personalState).toString() );
                    Request request=new Request.Builder().url(PublishUrl).post(requestBody).build();
                    Response response=client.newCall(request).execute();
                    a=response.body().string();
                    return a;
                }catch (Exception e){
                    if(e instanceof SocketTimeoutException ||e instanceof ConnectException){//超时
                        a="no";
                    }else {
                        e.printStackTrace();
                    }
                }
                */
                return a;
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
