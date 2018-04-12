package com.example.dell.chat.model;

import com.example.dell.chat.bean.User;

import org.litepal.crud.DataSupport;

import java.util.List;

/**
 * Created by wang on 2018/4/12.
 */

public class LoginModelImpl implements LoginModel {

    @Override
    public User FindLastUser(){
        User u=DataSupport.findFirst(User.class);
        return u;
    }

    @Override
    public void UpdateUser(User u){
        u.save();
    }

    @Override
    public void CreateUser(User u){
        DataSupport.deleteAll(User.class);
        u.save();
    }
}
