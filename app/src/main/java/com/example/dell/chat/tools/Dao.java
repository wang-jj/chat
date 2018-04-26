package com.example.dell.chat.tools;

import android.content.Context;

import com.example.dell.chat.bean.Message;
import com.example.dell.chat.db.ChatDao;
import com.example.dell.chat.db.DaoMaster;
import com.example.dell.chat.db.DaoSession;
import com.example.dell.chat.db.MessageDao;
import com.example.dell.chat.db.PersonalStateDao;
import com.example.dell.chat.db.UserDao;

/**
 * Created by wang on 2018/4/14.
 */

public class Dao {

    private UserDao userDao;
    private MessageDao messageDao;
    private ChatDao chatDao;

    private PersonalStateDao personalStateDao;

    public Dao(Context context){
        createDao(context);
    }

    public void createDao(Context context){
        DaoMaster.DevOpenHelper devOpenHelper = new DaoMaster.DevOpenHelper(context, "chat.db", null);
        DaoMaster daoMaster = new DaoMaster(devOpenHelper.getEncryptedWritableDb("123456"));
        //DaoMaster daoMaster = new DaoMaster(devOpenHelper.getWritableDb());
        DaoSession daoSession = daoMaster.newSession();
        userDao = daoSession.getUserDao();
        messageDao = daoSession.getMessageDao();
        chatDao = daoSession.getChatDao();
        personalStateDao=daoSession.getPersonalStateDao();
    }


    public UserDao getUserDao() {
        return userDao;
    }

    public MessageDao getMessageDao() {
        return messageDao;
    }

    public ChatDao getChatDao() {
        return chatDao;
    }

    public PersonalStateDao getPersonalStateDao() {
        return personalStateDao;
    }
}
