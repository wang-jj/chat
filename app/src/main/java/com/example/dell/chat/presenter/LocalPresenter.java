package com.example.dell.chat.presenter;

import android.util.Log;

import com.example.dell.chat.bean.Location;
import com.example.dell.chat.bean.MyApplication;
import com.example.dell.chat.model.Callback;
import com.example.dell.chat.model.Home.HomeModel;
import com.example.dell.chat.model.Home.HomeModelIlpl;
import com.example.dell.chat.view.HomeFragment;
import com.example.dell.chat.view.LocalFragment;
import com.google.gson.Gson;

import java.util.List;

/**
 * Created by wang on 2018/5/2.
 */

public class LocalPresenter {
    private HomeModel homeModel=new HomeModelIlpl();
    private LocalFragment view;
    private String url="http://119.23.255.222/android";

    public LocalPresenter(LocalFragment view){
        this.view=view;
    }

    public void UpdatePerson(){
        homeModel.UpdatePerson(MyApplication.getLatitude(), MyApplication.getLongitude(), new Callback<List<Location>>() {
            @Override
            public void execute(List<Location> datas) {
                for(Location location:datas){
                    if(location.getImage1ID()!=null){
                        location.setImage1ID(url+location.getImage1ID().substring(1));
                    }
                    if(location.getImage2ID()!=null){
                        location.setImage2ID(url+location.getImage2ID().substring(1));
                    }
                    if(location.getImage3ID()!=null){
                        location.setImage3ID(url+location.getImage3ID().substring(1));
                    }
                }
                Log.e("personal", new Gson().toJson(datas));
                view.UpdatePerson(datas);
            }
        });
    }

    public void LoadPerson(){
        homeModel.UpdatePerson(MyApplication.getLatitude(), MyApplication.getLongitude(), new Callback<List<Location>>() {
            @Override
            public void execute(List<Location> datas) {
                for(Location location:datas){
                    if(location.getImage1ID()!=null){
                        location.setImg_type(0);
                        location.setImage1ID(url+location.getImage1ID().substring(1));
                    }
                    if(location.getImage2ID()!=null){
                        location.setImg_type(1);
                        location.setImage2ID(url+location.getImage2ID().substring(1));
                    }
                    if(location.getImage3ID()!=null){
                        location.setImg_type(2);
                        location.setImage3ID(url+location.getImage3ID().substring(1));
                    }
                }
                Log.e("personal", new Gson().toJson(datas));
                view.CreatePerson(datas);
            }
        });
    }
}
