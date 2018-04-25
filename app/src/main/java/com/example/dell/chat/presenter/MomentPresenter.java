package com.example.dell.chat.presenter;

import android.Manifest;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.bumptech.glide.Glide;
import com.example.dell.chat.R;
import com.example.dell.chat.TestActivity;
import com.example.dell.chat.base.BaseActivity;
import com.example.dell.chat.base.BasePresenter;
import com.example.dell.chat.bean.MyApplication;
import com.example.dell.chat.bean.PersonalState;
import com.example.dell.chat.model.Callback;
import com.example.dell.chat.model.Moment.MomentModel;
import com.example.dell.chat.model.Moment.MomentModelImpl;
import com.example.dell.chat.view.PublishActivity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by wang on 2018/4/14.
 */

public class MomentPresenter<T extends BaseActivity> extends BasePresenter<T> {

    private MomentModel momentModel=new MomentModelImpl();

    public MomentPresenter(){
        super();
    }

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
        //获取时间
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yy-MM-dd HH:mm:ss");
        Date date = new Date(System.currentTimeMillis());
        personalState.setState_time(date);
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
            }
        });
    }

    public void setLocation(){
        LocationClient locationClient=new LocationClient(MyApplication.getContext());
        locationClient.registerLocationListener(new BDAbstractLocationListener() {//设置回调
            @Override
            public void onReceiveLocation(BDLocation bdLocation) {
                String addr = bdLocation.getAddrStr();//获取地址
                if(addr==null){
                    return;
                }
                ((TextView)getView().findViewById(R.id.mylocation)).setText(addr);
            }
        });
        LocationClientOption option = new LocationClientOption();
        option.setIsNeedAddress(true);//地址
        locationClient.setLocOption(option);
        /*
        if(ContextCompat.checkSelfPermission(getView(), Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(getView(),new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);
        }else {
            locationClient.start();
        }
        */
        locationClient.start();
    }

}
