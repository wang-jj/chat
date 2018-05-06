package com.example.dell.chat.presenter;

import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.example.dell.chat.base.BaseActivity;
import com.example.dell.chat.base.BasePresenter;
import com.example.dell.chat.bean.Location;
import com.example.dell.chat.bean.MyApplication;
import com.example.dell.chat.model.Callback;
import com.example.dell.chat.model.Home.HomeModel;
import com.example.dell.chat.model.Home.HomeModelIlpl;
import com.example.dell.chat.model.Login.LoginModel;
import com.example.dell.chat.model.Login.LoginModelImpl;
import com.example.dell.chat.model.Main.MainModel;
import com.example.dell.chat.model.Main.MainModelImpl;
import com.example.dell.chat.view.LoginActivity;

import java.util.List;

/**
 * Created by wang on 2018/4/25.
 */

public class MainPresenter<T extends BaseActivity> extends BasePresenter<T> {

    private MainModel mainModel=new MainModelImpl();
    //private HomeModel homeModel=new HomeModelIlpl();

    public MainPresenter(){
        super();
    }

    //退出登陆
    public void SignOut(){
        mainModel.SignOut(new Callback<Void>() {
            @Override
            public void execute(Void datas) {
                Intent intent=new Intent(getView(),LoginActivity.class);
                getView().startActivity(intent);
            }
        });
    }

    public void SendLocation(double Latitude,double Longitude){
        mainModel.sendLocation(Latitude, Longitude, new Callback<String>() {
            @Override
            public void execute(String datas) {
                if(datas.equals("no")){//无网络
                    Toast.makeText(MyApplication.getContext(),"网络错误，请检测你的网络设置",Toast.LENGTH_LONG).show();
                }else{
                    Log.e("Main", datas );
                }
            }
        });
    }
}
