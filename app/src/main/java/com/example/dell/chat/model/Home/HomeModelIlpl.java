package com.example.dell.chat.model.Home;

import android.util.Log;

import com.example.dell.chat.bean.Location;
import com.example.dell.chat.bean.MyApplication;
import com.example.dell.chat.bean.PersonalState;
import com.example.dell.chat.db.PersonalStateDao;
import com.example.dell.chat.model.Callback;
import com.example.dell.chat.model.Execute;
import com.example.dell.chat.tools.Dao;
import com.example.dell.chat.tools.ThreadTask;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
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

    private String updateMomentUrl="http://119.23.255.222/android/discoverynearby.php";
    private String UpdateNearbyURL="http://119.23.255.222/android/personnearby.php";
    private String url="http://119.23.255.222/android";
    private String sendLikeUrl="http://119.23.255.222/android/likedis.php";
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
                    Gson gson=new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
                    personalStates =gson.fromJson(result, new TypeToken<List<PersonalState>>() {}.getType());
                    //Log.e("moment1", new Gson().toJson(result) );
                    List<Integer> a=new ArrayList<>();
                    for(int q=personalStates.size()-1;q>=0;q--){
                        PersonalState i=personalStates.get(q);
                        i.setUpdate_time(new Date(System.currentTimeMillis()));
                        i.setHolder_id(MyApplication.getUser().getUser_id());
                        for (int w=0;w<list.size();w++){
                            PersonalState j=list.get(w);
                            if(i.getPersonalstate_id()==j.getPersonalstate_id()){
                                j.setProfileID(i.getProfileID());
                                j.setNickname(i.getNickname());
                                j.setSchool(i.getSchool());
                                j.setLike(i.getLike());
                                j.setComment(i.getComment());
                                a.add(new Integer(q));
                                break;
                            }
                        }
                    }
                    for(Integer integer:a){
                        personalStates.remove(integer.intValue());
                    }
                    //排序
                    if(personalStates.size()>1){
                        Collections.sort(personalStates, new Comparator<PersonalState>() {
                            @Override
                            public int compare(PersonalState personalState1, PersonalState personalState2) {
                                return personalState2.getState_time().compareTo(personalState1.getState_time());
                            }
                        });
                    }
                    for(PersonalState i:personalStates){
                        if(i.getImage1ID()!=null){
                            i.setImage1ID(url+i.getImage1ID().substring(1));
                            i.setImg_type(0);
                        }
                        if(i.getImage2ID()!=null){
                            i.setImage2ID(url+i.getImage2ID().substring(1));
                            i.setImg_type(1);
                        }
                        if(i.getImage3ID()!=null){
                            i.setImage3ID(url+i.getImage3ID().substring(1));
                            i.setImg_type(2);
                        }
                    }
                    PersonalStateDao personalStateDao=MyApplication.getDao().getPersonalStateDao();
                    if(personalStates.size()>0){
                        personalStateDao.insertInTx(personalStates);
                    }
                    //跟新数据库
                    personalStateDao.updateInTx(list);
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

    @Override
    public void UpdatePerson(final double latitude,final double longitude, Callback<List<Location>> callback) {
        ThreadTask<Void,Void,List<Location>>threadTask=new ThreadTask<>(callback, new Execute<List<Location>>() {
            @Override
            public List<Location> doExec() {
                List<Location> locations=null;
                try{//构建请求
                    OkHttpClient client=new OkHttpClient.Builder().connectTimeout(MyApplication.getTimeout(), TimeUnit.SECONDS).build();
                    RequestBody requestBody=new FormBody.Builder().add("latitude",String.valueOf(latitude)).add("longitude",String.valueOf(longitude)).build();
                    Request request=new Request.Builder().url(UpdateNearbyURL).post(requestBody).build();
                    Response response=client.newCall(request).execute();
                    String result=response.body().string();
                    locations=new Gson().fromJson(result,new TypeToken<List<Location>>(){}.getType());
                    //locations.remove(0);
                    //Log.e("location", result );
                }catch (Exception e){
                    if(e instanceof SocketTimeoutException ||e instanceof ConnectException){//超时
                        Log.e("updateperson", "outoftime" );
                    }else {
                        e.printStackTrace();
                    }
                }
                return locations;
            }
        });
        threadTask.execute();
    }

    @Override
    public void LoadMoment(Callback<List<PersonalState>> callback) {
        ThreadTask<Void,Void,List<PersonalState>> threadTask=new ThreadTask<Void,Void,List<PersonalState>>(callback, new Execute<List<PersonalState>>(){
            @Override
            public List<PersonalState> doExec() {
                List<PersonalState> personalStates=new ArrayList<>();
                PersonalStateDao personalStateDao=MyApplication.getDao().getPersonalStateDao();
                List<PersonalState> result=personalStateDao.queryBuilder().where(PersonalStateDao.Properties.Holder_id.eq(MyApplication.getUser().getUser_id())).orderDesc(PersonalStateDao.Properties.State_time).list();
                /*
                for(PersonalState i:result){
                    if(i.getImage1ID()!=null){
                        i.setImage1ID(url+i.getImage1ID().substring(1));
                    }
                    if(i.getImage2ID()!=null){
                        i.setImage2ID(url+i.getImage2ID().substring(1));
                    }
                    if(i.getImage3ID()!=null){
                        i.setImage3ID(url+i.getImage3ID().substring(1));
                    }
                }
                */
                personalStates.addAll(result);
                return personalStates;
            }
        });
        threadTask.execute();
    }

    @Override
    public void SendLike(final int Personalstate_id,final PersonalState personalState) {
        ThreadTask<Void,Void,Void>threadTask=new ThreadTask<>(null, new Execute<Void>() {
            @Override
            public Void doExec() {
                try{//构建请求
                    OkHttpClient client=new OkHttpClient.Builder().connectTimeout(MyApplication.getTimeout(), TimeUnit.SECONDS).build();
                    Request request=new Request.Builder().url(sendLikeUrl+"?user_id="+String.valueOf(MyApplication.getUser().getUser_id())+"&personalstate_id="+String.valueOf(Personalstate_id)).build();
                    Response response=client.newCall(request).execute();
                    String result=response.body().string();
                    //locations.remove(0);
                    Log.e("LIKE", result );
                    PersonalStateDao personalStateDao=MyApplication.getDao().getPersonalStateDao();
                    personalStateDao.update(personalState);
                }catch (Exception e){
                    if(e instanceof SocketTimeoutException ||e instanceof ConnectException){//超时
                        Log.e("updateperson", "outoftime" );
                    }else {
                        e.printStackTrace();
                    }
                }
                return null;
            }
        });
        threadTask.execute();
    }
}
