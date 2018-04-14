package com.example.dell.chat.tools;

import android.content.Context;

import com.example.dell.chat.db.DaoMaster;
import com.example.dell.chat.db.DaoSession;
import com.example.dell.chat.db.PersonalStateDao;
import com.example.dell.chat.db.UserDao;

/**
 * Created by wang on 2018/4/14.
 */

public class Dao {

    private UserDao userDao;

    private PersonalStateDao personalStateDao;

    public Dao(Context context){
        createDao(context);
    }

    public void createDao(Context context){
        DaoMaster.DevOpenHelper devOpenHelper = new DaoMaster.DevOpenHelper(context, "chat.db", null);
        DaoMaster daoMaster = new DaoMaster(devOpenHelper.getWritableDb());
        DaoSession daoSession = daoMaster.newSession();
        userDao = daoSession.getUserDao();
        personalStateDao=daoSession.getPersonalStateDao();
    }


    public UserDao getUserDao() {
        return userDao;
    }

    public PersonalStateDao getPersonalStateDao() {
        return personalStateDao;
    }
}
