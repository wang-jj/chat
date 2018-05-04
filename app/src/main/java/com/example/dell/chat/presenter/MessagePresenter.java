package com.example.dell.chat.presenter;

import android.util.Log;

import com.example.dell.chat.bean.Message;
import com.example.dell.chat.model.Callback;
import com.example.dell.chat.model.Message.MessageModel;
import com.example.dell.chat.model.Message.MessageModelImpl;
import com.example.dell.chat.view.MsgFragment;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by courageface on 2018/4/27.
 */

public class MessagePresenter {

    private MsgFragment view;
    private MessageModel messageModel = new MessageModelImpl();

    public MessagePresenter(MsgFragment view){
        super();
        this.view = view;
    }

    //展示最近联系人
    public void dispContact(){
        messageModel.InitContact(new Callback<List<Message>>() {
            @Override
            public void execute(List<Message> datas) {
                //测试
//                Message m = new Message();
//                m.setLatest_content("msg");
//                m.setContact_id(456);
//                datas.add(m);
                view.getAdapter().setAdapter(datas);
                return;
            }
        });
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
}
