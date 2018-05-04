package com.example.dell.chat.presenter;

import android.Manifest;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.example.dell.chat.R;
import com.example.dell.chat.base.BaseActivity;
import com.example.dell.chat.base.BasePresenter;
import com.example.dell.chat.bean.MyApplication;
import com.example.dell.chat.bean.PersonalState;
import com.example.dell.chat.model.Callback;
import com.example.dell.chat.model.Moment.MomentModel;
import com.example.dell.chat.model.Moment.MomentModelImpl;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

/**
 * Created by wang on 2018/4/14.
 */

public class MomentPresenter<T extends BaseActivity> extends BasePresenter<T> {

    private MomentModel momentModel=new MomentModelImpl();

    public MomentPresenter(){
        super();
    }

    private double latitude ;    //获取纬度信息
    private double longitude ;    //获取经度信息
    private String addr ;            //获取地址

    public void LoadMoment(){
        MyApplication myApplication=(MyApplication) (getView().getApplication());
        int user_id=myApplication.getUser().getUser_id();
        momentModel.LoadMoment(user_id, 0, -1, new Callback<List<PersonalState>>() {
            @Override
            public void execute(List<PersonalState> datas) {
                /*
                Log.d("TestActivity",String .valueOf(datas.get(0).getUser_id()));
                ((TestActivity)getView()).setImage();
                */
            }
        });
    }

    public void Publish(final PersonalState personalState){
        //判断地理位置有没有开启
        if(ContextCompat.checkSelfPermission(getView(), Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(getView(),new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);
        }
        //判断能否联网

        else{
            personalState.setLocation(addr);
            personalState.setLatitude(latitude);
            personalState.setLongitude(longitude);
            Log.e("Test", MyApplication.getUser().getUser_name() );
            momentModel.Publish(personalState, new com.example.dell.chat.model.Callback<String>() {//联网发动态
                @Override
                public void execute(String datas) {
                    if(datas.equals("no")){//连接超时
                        Toast.makeText(MyApplication.getContext(),"网络错误，请检测你的网络设置",Toast.LENGTH_LONG).show();
                        Log.e("Publish", datas );
                    }else if(datas!=null){
                        Log.e("Publish", datas );
                        getView().finish();
                    }
                }
            });
            //获取时间
            //SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yy-MM-dd HH:mm:ss");
            //Date date = new Date(System.currentTimeMillis());
            //personalState.setState_time(date);
            /*
            LocationClient locationClient=new LocationClient(MyApplication.getContext());
            locationClient.registerLocationListener(new BDAbstractLocationListener() {//设置回调
                @Override
                public void onReceiveLocation(BDLocation bdLocation) {
                    double latitude = bdLocation.getLatitude();    //获取纬度信息
                    double longitude = bdLocation.getLongitude();    //获取经度信息
                    String addr = bdLocation.getAddrStr();            //获取地址
                    personalState.setLocation(addr);
                    personalState.setLatitude(latitude);
                    personalState.setLongitude(longitude);
                    Log.e("Test", MyApplication.getUser().getUser_name() );
                    momentModel.Publish(personalState, new Callback<String>() {//联网发动态
                        @Override
                        public void execute(String datas) {
                            if(datas.equals("no")){//连接超时
                                //Toast.makeText(getView(),"网络错误，请检测你的网络设置",Toast.LENGTH_LONG).show();
                                Log.e("Publish", datas );
                            }else if(datas!=null){
                                Log.e("Publish", datas );
                                getView().finish();
                            }
                        }
                    });
                }
            });
            LocationClientOption option = new LocationClientOption();
            option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
            option.setIsNeedAddress(true);//地址
            option.setCoorType("bd09ll");
            option.setOpenGps(true);
            locationClient.setLocOption(option);
            locationClient.start();
            */

        }
    }

    public void setLocation(){
        LocationClient locationClient=new LocationClient(MyApplication.getContext());
        locationClient.registerLocationListener(new BDAbstractLocationListener() {//设置回调
            @Override
            public void onReceiveLocation(BDLocation bdLocation) {
                addr = bdLocation.getAddrStr();//获取地址
                latitude = bdLocation.getLatitude();    //获取纬度信息
                longitude = bdLocation.getLongitude();    //获取经度信息
                if(addr==null){
                    return;
                }
                ((TextView)getView().findViewById(R.id.mylocation)).setText(addr);
                Log.e("locations", String.valueOf(latitude) );
            }
        });
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        option.setIsNeedAddress(true);//地址
        option.setCoorType("wgs84");
        option.setOpenGps(true);
        locationClient.setLocOption(option);
        locationClient.start();
        /*
        if(ContextCompat.checkSelfPermission(getView(), Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(getView(),new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);
        }else {
            locationClient.start();
        }
        */
    }

}
