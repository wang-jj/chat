package com.example.dell.chat.presenter;

import android.content.Intent;

import com.example.dell.chat.base.BaseActivity;
import com.example.dell.chat.base.BasePresenter;
import com.example.dell.chat.model.Callback;
import com.example.dell.chat.model.Login.LoginModel;
import com.example.dell.chat.model.Login.LoginModelImpl;
import com.example.dell.chat.model.Main.MainModel;
import com.example.dell.chat.model.Main.MainModelImpl;
import com.example.dell.chat.view.LoginActivity;

/**
 * Created by wang on 2018/4/25.
 */

public class MainPresenter<T extends BaseActivity> extends BasePresenter<T> {

    private MainModel mainModel=new MainModelImpl();

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
}
