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
import com.example.dell.chat.view.MsgFragment;
import com.hyphenate.chat.EMMessage;

import java.util.List;

import static java.sql.DriverManager.println;

/**
 * Created by courageface on 2018/4/27.
 */

public class ChatPresenter {

     private ChatModel chatModel = new ChatModelImpl();
     public ChatActivity view;
     public MsgFragment frag;

    public ChatPresenter(ChatActivity view,MsgFragment frag){
         super();
         this.view = view;
         this.frag=frag;
     }

    //展示聊天内容
     public void showChat(int contact_id){
         List<Chat> datas = chatModel.InitChat(contact_id);

//                 Chat c = new Chat();
//                 c.setContent("hahaha");
//                 c.setType(1);
//                 datas.add(c);
                 view.getAdapter().setAdapter(datas);


     }

     //发送消息
     public void send(int contact_id, String content,int type){

         chatModel.SendMessage(contact_id, content, type, new Callback<Void>() {
             @Override
             public void execute(Void datas) {
                 frag.getPresenter().dispContact();
                 frag.getAdapter().notifyDataSetChanged();
                 view.getAdapter().notifyItemChanged(view.getAdapter().getItemCount()-1);

             }
         });


     }


}
