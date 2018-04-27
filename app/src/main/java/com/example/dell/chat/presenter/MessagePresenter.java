package com.example.dell.chat.presenter;

import com.example.dell.chat.base.BaseActivity;
import com.example.dell.chat.base.BasePresenter;
import com.example.dell.chat.bean.Message;
import com.example.dell.chat.model.Callback;
import com.example.dell.chat.model.Message.MessageModel;
import com.example.dell.chat.model.Message.MessageModelImpl;

import java.util.List;

/**
 * Created by courageface on 2018/4/27.
 */

public class MessagePresenter <T extends BaseActivity> extends BasePresenter<T> {

    private MessageModel messageModel = new MessageModelImpl();

    public MessagePresenter(){
        super();
    }

    public void dispContact(){
        messageModel.InitContact(new Callback<List<Message>>() {
            @Override
            public void execute(List<Message> datas) {

            }
        });
    }

    public void delContact(int contact_id){
        messageModel.DelContact(contact_id, new Callback<Void>() {
            @Override
            public void execute(Void datas) {

            }
        });
    }

    public void clickContact(int contact_id){
        messageModel.ClickContact(contact_id, new Callback<Void>() {
            @Override
            public void execute(Void datas) {

            }
        });
    }
}
