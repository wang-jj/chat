package com.example.dell.chat.presenter;

import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.example.dell.chat.base.BaseActivity;
import com.example.dell.chat.base.BasePresenter;
import com.example.dell.chat.bean.Chat;
import com.example.dell.chat.bean.Contact;
import com.example.dell.chat.bean.MyApplication;
import com.example.dell.chat.model.Callback;
import com.example.dell.chat.model.Chat.ChatModel;
import com.example.dell.chat.model.Chat.ChatModelImpl;
import com.example.dell.chat.model.Execute;
import com.example.dell.chat.tools.Dao;
import com.example.dell.chat.tools.ThreadTask;
import com.example.dell.chat.view.AlbumActivity;
import com.example.dell.chat.view.ChatActivity;
import com.example.dell.chat.view.MsgFragment;
import com.google.gson.Gson;
import com.hyphenate.chat.EMMessage;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static java.sql.DriverManager.println;

/**
 * Created by courageface on 2018/4/27.
 */

public class ChatPresenter {

     private ChatModel chatModel = new ChatModelImpl();
     public ChatActivity view;
     public MsgFragment frag;
     public String GetUserInfo = "http://119.23.255.222/android/useridinfo.php";

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

     //获取用户个人信息
    public void getInfo(final int contact_id){
        info(contact_id, new Callback<Contact>() {
            @Override
            public void execute(Contact contact) {
                Intent intent=Dao.SetIntent(new Intent(MyApplication.getContext(),AlbumActivity.class), contact_id, contact.getProfile(), contact.getIntroduction(), contact.getNickname(), contact.getSchool());
Log.e("info","callback called");
                view.startActivity(intent);
            }
        });
    }


    public void info(final int contact_id, final Callback<Contact> callback){
        ThreadTask t = new ThreadTask<Void,Void,Void>(callback, new Execute<Contact>() {
            @Override
            public Contact doExec() {
                Log.e("usr info", String.valueOf(contact_id));
                String result = null;
                Contact contact=null;
                try {
                    OkHttpClient client = new OkHttpClient.Builder().connectTimeout(MyApplication.getTimeout(), TimeUnit.SECONDS).build();

                    String URL = GetUserInfo + "?user_id=" + contact_id; //获取对方头像和昵称
                    Log.e("usr info", URL);
                    Request request = new Request.Builder().url(URL).build();
                    Response response = client.newCall(request).execute();
                    result = response.body().string();
                    contact = new Gson().fromJson(result, Contact.class);
                    //Log.e("usr info", String.valueOf(contact_id));
                    //跳转个人资料activity
                    //Dao.SetIntent(view.mIntent, contact_id, contact.getProfile(), contact.getIntroduction(), contact.getNickname(), contact.getSchool());
                    //Log.e("intent",contact.getProfile()+contact.getIntroduction()+contact.getNickname()+contact.getSchool());
                } catch (Exception e) {
                    if (e instanceof SocketTimeoutException || e instanceof ConnectException) {//超时
                        result = "out_of_time";
                    } else {
                        Log.e("usr info", "error");
                        e.printStackTrace();
                    }
                }
                return contact;
            }
        });
        t.execute();
    }
}
