package com.example.dell.chat.presenter;

import android.content.Context;
import android.util.Log;

import com.example.dell.chat.TestActivity;
import com.example.dell.chat.base.BaseActivity;
import com.example.dell.chat.base.BasePresenter;
import com.example.dell.chat.bean.MyApplication;
import com.example.dell.chat.bean.User;
import com.example.dell.chat.model.Callback;
import com.example.dell.chat.model.Login.LoginModel;
import com.example.dell.chat.model.Login.LoginModelImpl;

/**
 * Created by wang on 2018/4/13.
 */

public class LoginPresenter<T extends BaseActivity> extends BasePresenter<T>  {

    private LoginModel loginModel=new LoginModelImpl();

    public LoginPresenter(){
        super();
    }

    public void connect() {
        loginModel.FindLastUser(new Callback<User>() {
            @Override
            public void execute(User datas) {
                if(datas!=null){
                    ((TestActivity)getView()).setUserName(datas.getUser_name());
                    //登陆成功
                }else {
                    //转到登陆界面
                }
            }
        });
    }
}
