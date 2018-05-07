package com.example.dell.chat.presenter;

import com.example.dell.chat.base.BaseActivity;
import com.example.dell.chat.base.BasePresenter;
import com.example.dell.chat.bean.Location;
import com.example.dell.chat.bean.MyApplication;
import com.example.dell.chat.model.Callback;
import com.example.dell.chat.model.Home.HomeModel;
import com.example.dell.chat.model.Home.HomeModelIlpl;

import java.util.List;

/**
 * Created by wang on 2018/5/7.
 */

public class MapPresenter<T extends BaseActivity> extends BasePresenter<T> {
    private HomeModel homeModel=new HomeModelIlpl();

    //获取附近的人列表
    public void getLocation(Callback<List<Location>>callback){
        homeModel.UpdatePerson(MyApplication.getLatitude(),MyApplication.getLongitude(),callback);
    }
}
