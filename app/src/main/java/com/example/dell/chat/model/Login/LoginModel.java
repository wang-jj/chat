package com.example.dell.chat.model.Login;

import com.example.dell.chat.bean.User;
import com.example.dell.chat.model.Callback;


/**
 * Created by wang on 2018/4/12.
 */

public interface LoginModel {

    //查找最近一次登陆的账户,有的话回调logincallback的onSuccess函数，否则onFail函数
    void FindLastUser(final Callback<User> callback);

    //更新用户信息，如头像、个性签名、朋友圈照片等
    void UpdateUser(final User u);

    //插入新用户
    void CreateUser(final User u);
}
