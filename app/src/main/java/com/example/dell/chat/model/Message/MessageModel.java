package com.example.dell.chat.model.Message;

import com.example.dell.chat.bean.Message;
import com.example.dell.chat.model.Callback;

import java.util.List;

/**
 * Created by courageface on 2018/4/26.
 */

public interface MessageModel {

    //初始化最近联系人
    List<Message> InitContact();

    //删除联系人
    void DelContact(final int contact_id, final Callback<Void> callback);

    //点击联系人
    void ClickContact(final int contact_id, final Callback<Void> callback);

    //新建联系人
    void CreateContact(final int contact_id, final Callback<Void> callback);

    //id查联系人
    List<Message> CheckMessage(final int contact_id);
}
