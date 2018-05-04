package com.example.dell.chat.model.Message;

import android.util.Log;
import android.widget.Toast;

import com.example.dell.chat.bean.Chat;
import com.example.dell.chat.bean.Message;
import com.example.dell.chat.bean.MyApplication;
import com.example.dell.chat.db.ChatDao;
import com.example.dell.chat.db.MessageDao;
import com.example.dell.chat.model.Callback;
import com.example.dell.chat.model.Execute;
import com.example.dell.chat.tools.ThreadTask;

import java.util.List;

/**
 * Created by courageface on 2018/4/27.
 */

public class MessageModelImpl implements MessageModel {

    //初始化最近联系人
    @Override
    public void InitContact(final Callback<List<Message>> callback){
        ThreadTask t = new ThreadTask<Void,Void,List<Message>>(callback, new Execute<List<Message>>() {
            @Override
            public List<Message> doExec() { //获取联系人列表List<Message>
                MessageDao messageDao = MyApplication.getDao().getMessageDao();
                List<Message> messages = messageDao.queryBuilder().orderDesc(MessageDao.Properties.Latest_time).list();
                if(messages == null){
                    return null;
                }
                return messages;
            }
        });
        t.execute();
    }

    //删除联系人
    @Override
    public void DelContact(final int contact_id, final Callback<Void> callback){
        ThreadTask t = new ThreadTask<Void,Void,Void>(callback, new Execute<Void>() {
            @Override
            public Void doExec() {//删除M、C表中对应记录
                int user_id = MyApplication.getUser().getUser_id();
                MessageDao messageDao = MyApplication.getDao().getMessageDao();
                List<Message> messages  = messageDao.queryBuilder().
                        where(MessageDao.Properties.User_id.eq(user_id),
                               MessageDao.Properties.Contact_id.eq(contact_id)).build().list();
                for (Message msg : messages){
                    messageDao.delete(msg);
                }

                ChatDao chatDao = MyApplication.getDao().getChatDao();
                List<Chat> chats = chatDao.queryBuilder().
                        where(ChatDao.Properties.User_id.eq(user_id),
                                ChatDao.Properties.Contact_id.eq(contact_id)).build().list();
                for (Chat chat : chats){
                    chatDao.delete(chat);
                }
                return null;
            }
        });
        t.execute();
    }

    //点击联系人
    @Override
    public void ClickContact(final int contact_id, final Callback<Void> callback){
        ThreadTask t = new ThreadTask<Void,Void,Void>(callback, new Execute<Void>() {
            @Override
            public Void doExec() {//更新M表中联系人信息为已读
                int user_id = MyApplication.getUser().getUser_id();
                MessageDao messageDao = MyApplication.getDao().getMessageDao();
                Message msg = messageDao.queryBuilder().
                        where(MessageDao.Properties.User_id.eq(user_id),
                                MessageDao.Properties.Contact_id.eq(contact_id)).build().unique();
                if(msg == null){
                    Log.d("message","contact does not exist");
                }
                else{
                    msg.setUnread(0);
                    messageDao.update(msg);
                }
                return null;
            }
        });
        t.execute();
    }
}
