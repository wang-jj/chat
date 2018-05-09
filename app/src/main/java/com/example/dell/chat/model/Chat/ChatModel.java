package com.example.dell.chat.model.Chat;

import com.example.dell.chat.bean.Chat;
import com.example.dell.chat.model.Callback;
import com.hyphenate.chat.EMMessage;

import java.util.List;

/**
 * Created by courageface on 2018/4/27.
 */

public interface ChatModel {
    //初始化聊天
    List<Chat> InitChat(final int contact_id);

    //发送消息
    void SendMessage(final int contact_id, final String content, final int type, final Callback<Void> callback);

    //接受消息
    void ReceiveMessage(final List<EMMessage> messages, final Callback<Void> callback);

    //发送评论通知
    void SendComment(final String user_name,final int comment_id,int holder_id);
}
