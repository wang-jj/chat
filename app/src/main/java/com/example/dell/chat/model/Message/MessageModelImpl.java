package com.example.dell.chat.model.Message;

import android.util.Log;
import android.widget.Toast;

import com.example.dell.chat.bean.Chat;
import com.example.dell.chat.bean.Contact;
import com.example.dell.chat.bean.Message;
import com.example.dell.chat.bean.MyApplication;
import com.example.dell.chat.db.ChatDao;
import com.example.dell.chat.db.MessageDao;
import com.example.dell.chat.model.Callback;
import com.example.dell.chat.model.Execute;
import com.example.dell.chat.tools.ThreadTask;
import com.google.gson.Gson;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by courageface on 2018/4/27.
 */

public class MessageModelImpl implements MessageModel {

    //初始化最近联系人
    @Override
    public List<Message> InitContact(){
//        ThreadTask t = new ThreadTask<Void,Void,List<Message>>(callback, new Execute<List<Message>>() {
//            @Override
//            public List<Message> doExec() { //获取联系人列表List<Message>
//                MessageDao messageDao = MyApplication.getDao().getMessageDao();
//                List<Message> messages = messageDao.queryBuilder().where(MessageDao.Properties.User_id.eq(MyApplication.getUser().getUser_id())).
//                        orderDesc(MessageDao.Properties.Latest_time).list();//
//                if(messages == null){
//                    return null;
//                }
//                return messages;
//            }
//        });
//        t.execute();
        MessageDao messageDao = MyApplication.getDao().getMessageDao();
                List<Message> messages = messageDao.queryBuilder().where(MessageDao.Properties.User_id.eq(MyApplication.getUser().getUser_id())).
                        orderDesc(MessageDao.Properties.Latest_time).list();
                return  messages;
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

    //新建联系人 若已存在则不新建
    @Override
    public void CreateContact(final int contact_id, final Callback<Void> callback){
        ThreadTask t =new ThreadTask<Void,Void,Void>(callback, new Execute() {
            @Override
            public Void doExec() {
                String GetHeadImageAndNickname = "http://119.23.255.222/android/useridinfo.php";
                MessageDao messageDao = MyApplication.getDao().getMessageDao();
                List<Message> list = messageDao.queryBuilder().where(MessageDao.Properties.User_id.eq(MyApplication.getUser().getUser_id()),
                        MessageDao.Properties.Contact_id.eq(contact_id)).build().list();
                if(list.size()==0){
                    Message m = new Message();
                    String result=null;
                    try {
                        OkHttpClient client = new OkHttpClient.Builder().connectTimeout(MyApplication.getTimeout(), TimeUnit.SECONDS).build();
                        String URL = GetHeadImageAndNickname + "?user_id=" + contact_id; //获取对方头像和昵称
                        Request request = new Request.Builder().url(URL).build();
                        Response response = client.newCall(request).execute();
                        result=response.body().string();
                        Contact contact=new Gson().fromJson(result,Contact.class);
                        m.setImage_path(contact.getProfile());//向服务器获取头像
                        m.setContact_name(contact.getNickname());//向服务器获取用户名
                    }
                    catch(Exception e){
                        if(e instanceof SocketTimeoutException ||e instanceof ConnectException){//超时
                            result="out_of_time";
                        }else {
                            e.printStackTrace();
                        }
                    }

                    m.setUser_id(MyApplication.getUser().getUser_id());
                    m.setContact_id(contact_id);
                    m.setLatest_content("我们可以开始聊天啦！");
                    m.setLatest_time( System.currentTimeMillis());
                    m.setUnread(0);

                    messageDao.insert(m);
                }
                return null;
            }
        });
        t.execute();
    }

    @Override
    public List<Message> CheckMessage(final int contact_id){
        MessageDao messageDao = MyApplication.getDao().getMessageDao();
        List<Message> list = messageDao.queryBuilder().where(MessageDao.Properties.Contact_id.eq(contact_id)).build().list();
        return list;
    }
}
