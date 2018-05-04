package com.example.dell.chat.presenter;

import android.util.Log;
import android.widget.Toast;

import com.example.dell.chat.base.BaseActivity;
import com.example.dell.chat.base.BasePresenter;
import com.example.dell.chat.bean.Chat;
import com.example.dell.chat.bean.MyApplication;
import com.example.dell.chat.model.Callback;
import com.example.dell.chat.model.Chat.ChatModel;
import com.example.dell.chat.model.Chat.ChatModelImpl;
import com.example.dell.chat.view.ChatActivity;
import com.hyphenate.chat.EMMessage;

import java.util.List;

import static java.sql.DriverManager.println;

/**
 * Created by courageface on 2018/4/27.
 */

public class ChatPresenter {
//<ChatActivity extends BaseActivity> extends BasePresenter<ChatActivity>
     private ChatModel chatModel = new ChatModelImpl();
     public ChatActivity view;



    public ChatPresenter(ChatActivity view){
         super();
         this.view = view;
     }

    //展示聊天内容
     public void showChat(){
         chatModel.InitChat(new Callback<List<Chat>>() {
             @Override
             public void execute(List<Chat> datas) {
                 Chat c = new Chat();
                 c.setContent("hahaha");
                 c.setType(1);
                 datas.add(c);
                 view.adapter.setAdapter(datas);
                //view.adapter = view.new ChatAdapter(datas);
             }
         });
     }

     //发送消息
     public void send(int contact_id, String content,int type){
         chatModel.SendMessage(contact_id, content, type, new Callback<Void>() {
             @Override
             public void execute(Void datas) {

             }
         });
     }

     //接受消息
     public void receive(List<EMMessage> messages){
         chatModel.ReceiveMessage(messages, new Callback<Void>() {
             @Override
             public void execute(Void datas) {

             }
         });
     }
}
