package com.example.dell.chat.presenter;

import android.util.Log;
import android.view.ViewParent;

import com.example.dell.chat.R;
import com.example.dell.chat.bean.Message;
import com.example.dell.chat.bean.MyApplication;
import com.example.dell.chat.model.Callback;
import com.example.dell.chat.model.Chat.ChatModelImpl;
import com.example.dell.chat.model.Message.MessageModel;
import com.example.dell.chat.model.Message.MessageModelImpl;
import com.example.dell.chat.view.MsgFragment;
import com.hyphenate.chat.EMMessage;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by courageface on 2018/4/27.
 */

public class MessagePresenter {

    private MsgFragment view;
    private MessageModel messageModel = new MessageModelImpl();
    private ChatModelImpl chatModel = new ChatModelImpl();

    public MessagePresenter(MsgFragment view){
        super();
        this.view = view;
    }

    //展示最近联系人
    public void dispContact(){
        List<Message> datas = messageModel.InitContact();
        //view.getAdapter().setAdapter(new ArrayList<Message>());
        view.getAdapter().setAdapter(datas);
        return;
//            @Override
//            public void execute(List<Message> datas) {
//                //测试
////                Message m = new Message();
////                m.setLatest_content("msg");
////                m.setContact_id(456);
////                m.setContact_name("李遇见");
////                datas.add(m);
////                Message m1 = new Message();
////                m1.setContact_name("王锦杰");
////                m1.setLatest_content("666");
////                m1.setContact_id(456);
////                datas.add(m1);
//                view.getAdapter().setAdapter(datas);
//                return;
//            }
//        });
    }

    //删除此联系人
    public void delContact(int contact_id){
        messageModel.DelContact(contact_id, new Callback<Void>() {
            @Override
            public void execute(Void datas) {
                return;
            }
        });
    }

    //点击联系人
    public void clickContact(int contact_id){
        messageModel.ClickContact(contact_id, new Callback<Void>() {
            @Override
            public void execute(Void datas) {
                return;
            }
        });
    }

    //监听器接受消息
    public void receive(List<EMMessage> messages){
        chatModel.ReceiveMessage(messages, new Callback<Void>() {
            @Override
            public void execute(Void datas) {
                dispContact();
                view.getAdapter().notifyDataSetChanged();

                //可能还需要判断此时是否在聊天界面 与谁聊天
                if(MyApplication.getChatActivity()!=null){//&&MyApplication.getChattingMode()!=0){
                    MyApplication.getChatActivity().getAdapter().notifyDataSetChanged();
                }
                return;
            }
        });
    }

    //新建联系人
    public void create(int contact_id){
        messageModel.CreateContact(contact_id, new Callback<Void>() {
            @Override
            public void execute(Void datas) {
                dispContact();
                view.getAdapter().notifyItemChanged(0);
                return;
            }
        });
    }
}
