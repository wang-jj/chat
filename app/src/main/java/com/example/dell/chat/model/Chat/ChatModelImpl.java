package com.example.dell.chat.model.Chat;

import com.example.dell.chat.bean.Chat;
import com.example.dell.chat.bean.Message;
import com.example.dell.chat.bean.MyApplication;
import com.example.dell.chat.db.ChatDao;
import com.example.dell.chat.db.MessageDao;
import com.example.dell.chat.model.Callback;
import com.example.dell.chat.model.Execute;
import com.example.dell.chat.tools.ThreadTask;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMMessageBody;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by courageface on 2018/4/27.
 */

public class ChatModelImpl implements ChatModel{

    //初始化聊天
    @Override
    public List<Chat> InitChat(final int contact_id){

          //获取聊天记录列表List<Chat>
             ChatDao chatDao = MyApplication.getDao().getChatDao();
             List<Chat> chats = chatDao.queryBuilder().where(ChatDao.Properties.User_id.eq(MyApplication.getUser().getUser_id()),
                     ChatDao.Properties.Contact_id.eq(contact_id)).build().list();
             return chats;

    }

    //发送消息
    @Override
    public void SendMessage(final int contact_id, final String content, final int msgtype,final Callback<Void> callback){
        ThreadTask t = new ThreadTask(callback, new Execute() {
            @Override
            public Void doExec() {
                //插入Chat表，更新Message表
//            String sender ;
//            String receiver ;
                long time ;
//            EMMessageBody body ;
                //EMMessage.Type type ;

                //环信发送信息 文本为0 图片为2
                if (msgtype == 0) {
                    //创建一条文本消息，content为消息文字内容，toChatUsername为对方用户或者群聊的id，后文皆是如此
                    EMMessage msg = EMMessage.createTxtSendMessage(content, String.valueOf(contact_id));
                    //发送消息
                    EMClient.getInstance().chatManager().sendMessage(msg);
//                sender = msg.getFrom();
//                receiver = msg.getTo();
                    time = System.currentTimeMillis();;
//                body = msg.getBody();
                }
                else if(msgtype == 2) {
                    //imagePath为图片本地路径，false为不发送原图（默认超过100k的图片会压缩后发给对方），需要发送原图传true
                    EMMessage msg = EMMessage.createImageSendMessage(content, true ,String.valueOf(contact_id));
                    //发送消息
                    EMClient.getInstance().chatManager().sendMessage(msg);
//                sender = msg.getFrom();
//                receiver = msg.getTo();
                    time = System.currentTimeMillis();
//                body = msg.getBody();
                }
                else{
                    return null;
                }

                //本地数据库Chat记录发送信息
                ChatDao chatDao = MyApplication.getDao().getChatDao();
                Chat chat = new Chat();
                chat.setUser_id(MyApplication.getUser().getUser_id());
                chat.setContact_id(contact_id);
                chat.setTime(time);
                chat.setContent(content);
                chat.setType(msgtype);
                chatDao.insert(chat);

                //更新Message最新一条信息内容与时间
                MessageDao messageDao = MyApplication.getDao().getMessageDao();
                int user_id = MyApplication.getUser().getUser_id();
                String latest_content = "";
                if(msgtype == 0){
                    latest_content = content;
                }
                else if(msgtype == 2){
                    latest_content = "[图片消息]";
                }
                Message message = messageDao.queryBuilder().where(MessageDao.Properties.User_id.eq(user_id),
                        MessageDao.Properties.Contact_id.eq(contact_id)).build().unique();
                while(message==null){}
                message.setLatest_time(time);
                message.setLatest_content(latest_content);
                messageDao.update(message);

                return null;

            }
        });
        t.execute();

    }

    //接受消息
    @Override
    public void ReceiveMessage(final List<EMMessage> messages, final Callback<Void> callback){
        ThreadTask t = new ThreadTask<Void,Void,Void>(callback, new Execute<Void>(){
            @Override
            public Void doExec(){ //插入Chat表，插入与更新Message表
                String sender ;
                String receiver ;
                long time ;
                EMMessageBody body ;
                EMMessage.Type type ;
                List<Chat> chats = new ArrayList<Chat>();

                //环信接受信息
                for(EMMessage msg : messages){
                    sender = msg.getFrom();
                    receiver = msg.getTo();
                    time = msg.getMsgTime();
                    body = msg.getBody();
                    type = msg.getType();

                    int msg_type = 1;
                    if(String.valueOf(type) == "TXT"){
                        msg_type = 1;
                    }
                    else if(String.valueOf(type) == "IMAGE"){
                        msg_type = 3;
                    }

                    String latest_content = "";
                    if(msg_type==1){
                        latest_content = body.toString();
                    }
                    else if(msg_type==3){
                        latest_content = "[图片消息]";
                    }

                    Chat chat = new Chat();
                    chat.setUser_id(Integer.valueOf(receiver));
                    chat.setContact_id(Integer.valueOf(sender));
                    chat.setTime(time);
                    chat.setContent(body.toString());
                    chat.setType(msg_type);
                    chats.add(chat);

                    //更新Message最新一条信息内容与时间与已读状态
                    MessageDao messageDao = MyApplication.getDao().getMessageDao();
                    Message themsg = messageDao.queryBuilder().where(MessageDao.Properties.User_id.eq(Integer.valueOf(receiver)),
                            MessageDao.Properties.Contact_id.eq(Integer.valueOf(sender))).build().unique();
                    if(themsg == null){ //在Message中创建
                        Message newmsg =  new Message();
                        newmsg.setUser_id(Integer.valueOf(receiver));
                        newmsg.setContact_id(Integer.valueOf(sender));
                        newmsg.setImage_path(""); //向服务器获取头像
                        newmsg.setContact_name("对方用户名");//向服务器获取用户名

                        newmsg.setLatest_content(latest_content);
                        newmsg.setLatest_time(time);
                        newmsg.setUnread(1);
                        messageDao.insert(newmsg);
                    }
                    else { //更新Message
                        themsg.setLatest_content(latest_content);
                        themsg.setLatest_time(time);
                        themsg.setUnread(1);
                        messageDao.update(themsg);
                    }
                }

                //本地数据库Chat记录接受信息
                ChatDao chatDao = MyApplication.getDao().getChatDao();
                chatDao.insertInTx(chats);

                return null;
            }
        });
        t.execute();
    }
}
