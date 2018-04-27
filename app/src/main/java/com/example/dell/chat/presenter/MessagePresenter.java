package com.example.dell.chat.presenter;

import com.example.dell.chat.base.BaseActivity;
import com.example.dell.chat.base.BasePresenter;
import com.example.dell.chat.bean.Message;
import com.example.dell.chat.model.Callback;
import com.example.dell.chat.model.Message.MessageModel;
import com.example.dell.chat.model.Message.MessageModelImpl;

import java.lang.ref.WeakReference;
import java.util.List;

/**
 * Created by courageface on 2018/4/27.
 */

public class MessagePresenter {

//    protected WeakReference<T> mViewRef;
//
//    //关联
//    public void attachView(T view) {
//        mViewRef = new WeakReference<T>(view);
//    }
//
//    //解除关联
//    public void detachView() {
//        if (mViewRef != null) {
//            mViewRef.clear();
//        }
//    }
//    //得到view
//    protected T getView() {
//        return mViewRef.get();
//    }
    private MessageModel messageModel = new MessageModelImpl();

    public MessagePresenter(){
        super();
    }

    //展示最近联系人
    public void dispContact(){
        messageModel.InitContact(new Callback<List<Message>>() {
            @Override
            public void execute(List<Message> datas) {

            }
        });
    }

    //删除此联系人
    public void delContact(int contact_id){
        messageModel.DelContact(contact_id, new Callback<Void>() {
            @Override
            public void execute(Void datas) {

            }
        });
    }

    //点击联系人
    public void clickContact(int contact_id){
        messageModel.ClickContact(contact_id, new Callback<Void>() {
            @Override
            public void execute(Void datas) {

            }
        });
    }
}
