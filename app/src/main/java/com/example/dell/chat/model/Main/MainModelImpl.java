package com.example.dell.chat.model.Main;

import com.example.dell.chat.bean.MyApplication;
import com.example.dell.chat.bean.User;
import com.example.dell.chat.db.UserDao;
import com.example.dell.chat.model.Callback;
import com.example.dell.chat.model.Execute;
import com.example.dell.chat.tools.ThreadTask;

/**
 * Created by wang on 2018/4/25.
 */

public class MainModelImpl implements MainModel {
    @Override
    public void SignOut(final Callback<Void> callback) {
        ThreadTask t=new ThreadTask<Void,Void,Void>(callback, new Execute() {
            @Override
            public Void doExec() {
                UserDao userDao= MyApplication.getDao().getUserDao();
                User u=MyApplication.getUser();
                u.setPassword(null);
                userDao.update(u);
                //请求网络
                return null;
            }
        });
        t.execute();
    }
}
