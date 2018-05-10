package com.example.dell.chat.model.Moment;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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


import java.io.ByteArrayOutputStream;
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

    private String PublishUrl="http://119.23.255.222/android/uppersonstate.php";
    private String SendImageUrl="http://119.23.255.222/android/uploadpic.php";

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

    //发朋友圈
    @Override
    public void Publish(final PersonalState personalState, Callback callback) {
        ThreadTask threadTask=new ThreadTask<Void,Void,String>(callback, new Execute<String>() {
            @Override
            public String doExec() {
                Log.e("Publish","jpg");
                /*
                String a="no";
                MediaType MEDIA_TYPE_PNG = MediaType.parse("image/png");
                MediaType MEDIA_TYPE_JPG = MediaType.parse("image/jpeg");
                MediaType MEDIA_TYPE_GIF = MediaType.parse("image/gif");
                MultipartBody.Builder multipartBodyBuilder = new MultipartBody.Builder().setType(MultipartBody.FORM);
                multipartBodyBuilder.addFormDataPart("personalState",new Gson().toJson(personalState));
                if(personalState.getImg_type()>0){//一张照片
                    String s=personalState.getImage1ID();
                    File file=new File(s);
                    if(s.substring(s.length()-3).equals("gif")) {
                        Log.e("Publish","gif");
                        multipartBodyBuilder.addFormDataPart("image", file.getName(), RequestBody.create(MEDIA_TYPE_GIF, file));
                    }else if(s.substring(s.length()-3).equals("jpg")||s.substring(s.length()-4).equals("jpeg")){
                        Log.e("Publish","jpg");
                        multipartBodyBuilder.addFormDataPart("image", file.getName(), RequestBody.create(MEDIA_TYPE_JPG, file));
                    }else {
                        Log.e("Publish","png");
                        multipartBodyBuilder.addFormDataPart("image", file.getName(), RequestBody.create(MEDIA_TYPE_PNG, file));
                    }
                    Log.e("Publish",s);
                    Log.e("Publish",String.valueOf(file.length()));
                    Log.e("Publish",file.getName());
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
                    OkHttpClient client=new OkHttpClient.Builder().connectTimeout(MyApplication.getTimeout(), TimeUnit.SECONDS).writeTimeout(MyApplication.getTimeout(),TimeUnit.SECONDS).readTimeout(MyApplication.getTimeout(),TimeUnit.SECONDS).build();
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
                */
                String encodeString1=translate(personalState.getImage1ID());
                String encodeString2=translate(personalState.getImage2ID());
                String encodeString3=translate(personalState.getImage3ID());
                String personalState_id=SendMassage(personalState);//发personalstate类
                //return personalState_id;
                Log.e("Publish", personalState_id );
                Log.e("Publish", String.valueOf(personalState.getImg_type()) );
                String restul1=null;
                if(personalState.getImg_type()>0){
                    String image_type=null;
                    String s=personalState.getImage1ID();
                    if(s.substring(s.length()-3).equals("gif")) {
                        image_type="gif";
                    }else if(s.substring(s.length()-3).equals("png")){
                        image_type="png";
                    }else {
                        image_type="jpg";
                    }
                    restul1=SendImage(personalState_id,image_type,1,encodeString1);
                }
                if(personalState.getImg_type()>1){
                    String image_type=null;
                    String s=personalState.getImage2ID();
                    if(s.substring(s.length()-3).equals("gif")) {
                        image_type="gif";
                    }else if(s.substring(s.length()-3).equals("png")){
                        image_type="png";
                    }else {
                        image_type="jpg";
                    }
                    String restul2=SendImage(personalState_id,image_type,2,encodeString2);
                }
                String restul3=null;
                if(personalState.getImg_type()>2){
                    String image_type=null;
                    String s=personalState.getImage3ID();
                    if(s.substring(s.length()-3).equals("gif")) {
                        image_type="gif";
                    }else if(s.substring(s.length()-3).equals("png")){
                        image_type="png";
                    }else {
                        image_type="jpg";
                    }
                    restul3=SendImage(personalState_id,image_type,3,encodeString3);
                }
                return "yes";
                /*
                try {
                    OkHttpClient client=new OkHttpClient.Builder().connectTimeout(MyApplication.getTimeout(), TimeUnit.SECONDS).build();
                    //RequestBody requestBody=new FormBody.Builder().add("user",new Gson().toJson(personalState).toString()).add("image",encodedString).build();
                    RequestBody requestBody=null;
                    if(personalState.getImg_type()==0){
                        requestBody=new FormBody.Builder().add("personalState",new Gson().toJson(personalState).toString()).build();
                    }else if(personalState.getImg_type()==1){
                        requestBody=new FormBody.Builder().add("image1",encodeString1).add("personalState",new Gson().toJson(personalState).toString()).build();
                    }else if(personalState.getImg_type()==2){
                        requestBody.
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
                //return "yes";
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

    public String SendImage(String personalstate_id,String image_tpye,int image_num,String encode){
        String a=null;
        try {
            OkHttpClient client=new OkHttpClient.Builder().connectTimeout(MyApplication.getTimeout(), TimeUnit.SECONDS).writeTimeout(MyApplication.getTimeout(), TimeUnit.SECONDS).readTimeout(MyApplication.getTimeout(), TimeUnit.SECONDS).build();
            RequestBody requestBody=new FormBody.Builder().add("personalState_id",personalstate_id).add("image_type",image_tpye).add("image_number",String.valueOf(image_num)).add("image",encode).build();
            Request request=new Request.Builder().url(SendImageUrl).post(requestBody).build();
            Response response=client.newCall(request).execute();
            a=response.body().string();
        }catch (Exception e){
            if(e instanceof SocketTimeoutException ||e instanceof ConnectException){//超时
                a="no";
            }else {
                e.printStackTrace();
            }
        }
        return a;
    }

    public String SendMassage(PersonalState personalState){
        String a=null;
        try {
            Log.e("Test", new Gson().toJson(personalState) );
            OkHttpClient client=new OkHttpClient.Builder().connectTimeout(MyApplication.getTimeout(), TimeUnit.SECONDS).writeTimeout(MyApplication.getTimeout(), TimeUnit.SECONDS).readTimeout(MyApplication.getTimeout(), TimeUnit.SECONDS).build();
            RequestBody requestBody=new FormBody.Builder().add("personalState",new Gson().toJson(personalState).toString()).build();
            Request request=new Request.Builder().url(PublishUrl).post(requestBody).build();
            Response response=client.newCall(request).execute();
            a=response.body().string();
        }catch (Exception e){
            if(e instanceof SocketTimeoutException ||e instanceof ConnectException){//超时
                a="no";
            }else {
                e.printStackTrace();
            }
        }
        return a;
    }

}
