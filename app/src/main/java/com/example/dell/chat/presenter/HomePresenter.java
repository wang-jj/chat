package com.example.dell.chat.presenter;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.example.dell.chat.bean.MyApplication;
import com.example.dell.chat.bean.PersonalState;
import com.example.dell.chat.model.Callback;
import com.example.dell.chat.model.Home.HomeModel;
import com.example.dell.chat.model.Home.HomeModelIlpl;
import com.example.dell.chat.view.HomeFragment;

import java.util.Date;
import java.util.List;

/**
 * Created by wang on 2018/4/26.
 */

public class HomePresenter {
    private HomeModel homeModel=new HomeModelIlpl();
    private HomeFragment view;

    public HomePresenter(HomeFragment view){
        this.view=view;
    }

    public void UpdateMoment(){
        LocationClient locationClient=new LocationClient(MyApplication.getContext());
        locationClient.registerLocationListener(new BDAbstractLocationListener() {//设置回调
            @Override
            public void onReceiveLocation(BDLocation bdLocation) {
                String addr = bdLocation.getAddrStr();//获取地址
                double latitude = bdLocation.getLatitude();    //获取纬度信息
                double longitude = bdLocation.getLongitude();    //获取经度信息
                homeModel.UpdateMoment(latitude, longitude, new Callback<List<PersonalState>>() {
                    @Override
                    public void execute(List<PersonalState> datas) {
                        HomeFragment.StateAdapter adapter=view.getAdapter();
                        RecyclerView stateRecyclerView=view.getStateRecyclerView();
                        SwipeRefreshLayout swipeRefreshLayout=view.getSwipeRefreshLayout();
                        List<PersonalState> old=adapter.getmStateList();
                        for(PersonalState i:datas){
                            i.setUpdate_time(new Date(System.currentTimeMillis()));
                            for (PersonalState j:old){
                                if(i.getPersonalstate_id()==j.getPersonalstate_id()){
                                    datas.remove(i);
                                    break;
                                }
                            }
                        }
                    }
                });
                Log.e("MAIN", addr );
            }
        });
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        option.setIsNeedAddress(true);//地址
        option.setCoorType("bd09ll");
        option.setScanSpan(10000);
        option.setOpenGps(true);
        locationClient.setLocOption(option);
        //locationClient.start();
    }
}
