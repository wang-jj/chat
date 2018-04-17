package com.example.dell.chat.presenter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;

import com.example.dell.chat.TestActivity;
import com.example.dell.chat.base.BaseActivity;
import com.example.dell.chat.base.BasePresenter;
import com.example.dell.chat.bean.MyApplication;
import com.example.dell.chat.bean.User;
import com.example.dell.chat.model.Callback;
import com.example.dell.chat.model.Login.LoginModel;
import com.example.dell.chat.model.Login.LoginModelImpl;
import com.example.dell.chat.view.LoadActivity;
import com.example.dell.chat.view.LoginActivity;
import com.example.dell.chat.view.MainActivity;

import java.sql.SQLRecoverableException;

/**
 * Created by wang on 2018/4/13.
 */

public class LoginPresenter<T extends BaseActivity> extends BasePresenter<T>  {

    private LoginModel loginModel=new LoginModelImpl();

    public LoginPresenter(){
        super();
    }

    public void creatAlert(CharSequence charSequence){
        AlertDialog.Builder dialog=new AlertDialog.Builder(getView());
        dialog.setMessage(charSequence);
        dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        dialog.show();
    }

    //通过记住密码方式登陆
    public void connect() {
        loginModel.FindLastUser(new Callback<User>() {
            @Override
            public void execute(User datas) {
                if(datas!=null){//不为空，判断返回的user的userid
                    if(datas.getUser_id()>0){//登陆成功
                        MyApplication.setUser(datas);
                        Intent intent=new Intent(getView(),MainActivity.class);
                        getView().startActivity(intent);
                    }else if(datas.getUser_id()==-1){//密码错误
                        creatAlert("密码错误，请重写登陆");
                    }else if(datas.getUser_id()==-2){//账号不存在
                        creatAlert("账号不存在，请重写登陆");
                    }else if(datas.getUser_id()==-3){//网络错误
                        creatAlert("网络连接不可用");
                    }
                }else {
                    //转到登陆界面
                    Intent intent=new Intent(getView(),LoginActivity.class);
                    getView().startActivity(intent);
                }
                Log.e("LoginActivity", String.valueOf(MyApplication.getUser()==null)  );
            }
        });
    }

    //通过邮箱登陆
    public void LoginByEmail(String email,String password){
        loginModel.LoginByEmail(email, password, new Callback<User>() {
            @Override
            public void execute(User datas) {
                if(datas.getUser_id()>0){

                }
            }
        });
    }
}
