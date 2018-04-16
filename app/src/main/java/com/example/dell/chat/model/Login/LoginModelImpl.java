package com.example.dell.chat.model.Login;


import com.example.dell.chat.bean.MyApplication;
import com.example.dell.chat.bean.User;
import com.example.dell.chat.db.UserDao;
import com.example.dell.chat.model.Callback;
import com.example.dell.chat.model.Execute;
import com.example.dell.chat.tools.ThreadTask;

import org.litepal.crud.DataSupport;

import java.util.List;


/**
 * Created by wang on 2018/4/12.
 */

public class LoginModelImpl implements LoginModel {

    @Override
    public void FindLastUser(final Callback<User> callback){
        UserDao userDao= MyApplication.getDao().getUserDao();
        List<User> users=userDao.loadAll();
        //网络请求
        MyApplication.setUser(users.get(0));
        callback.execute(users.get(users.size()-1));
    }

    @Override
    public void UpdateUser(final User u){
        ThreadTask t=new ThreadTask<Void,Void,Void>(null, new Execute<Void>() {
            @Override
            public Void doExec() {
                UserDao userDao=MyApplication.getDao().getUserDao();
                userDao.update(u);
                MyApplication.setUser(u);
                return null;
            }
        });
        t.execute();
    }

    @Override
    public void CreateUser(final User u){
        UpdateUser(u);
    }
}
