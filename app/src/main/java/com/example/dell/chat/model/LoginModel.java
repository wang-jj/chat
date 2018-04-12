package com.example.dell.chat.model;

import com.example.dell.chat.bean.User;


/**
 * Created by wang on 2018/4/12.
 */

public interface LoginModel {

    //查找最近一次登陆的账户，返回整个user类
    User FindLastUser();

    //更新用户信息，如头像、个性签名、朋友圈照片等
    void UpdateUser(User u);

    //插入新用户
    void CreateUser(User u);
}
