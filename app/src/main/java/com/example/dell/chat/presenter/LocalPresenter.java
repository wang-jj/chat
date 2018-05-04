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

    public LocalPresenter(LocalFragment view){
        this.view=view;
    }

    public void UpdatePerson(){
        homeModel.UpdatePerson(MyApplication.getLatitude(), MyApplication.getLongitude(), new Callback<List<Location>>() {
            @Override
            public void execute(List<Location> datas) {
                Log.e("personal", new Gson().toJson(datas));
                //view.UpdatePerson(datas);
            }
        });
    }
}
