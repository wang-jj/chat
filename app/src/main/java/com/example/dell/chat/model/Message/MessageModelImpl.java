package com.example.dell.chat.model.Message;

import com.example.dell.chat.bean.Message;
import com.example.dell.chat.model.Callback;
import com.example.dell.chat.model.Execute;
import com.example.dell.chat.tools.ThreadTask;

import java.util.List;

/**
 * Created by courageface on 2018/4/27.
 */

public class MessageModelImpl implements MessageModel {

    //初始化最近联系人
    @Override
    public void InitContact(final Callback<List<Message>> callback){
        ThreadTask t = new ThreadTask<Void,Void,List<Message>>(callback, new Execute<List<Message>>() {
            @Override
            public List<Message> doExec() {
                //获取联系人列表List<Message>
                return null;
            }
        });
        t.execute();
    }

    //删除联系人
    @Override
    public void DelContact(final int contact_id, final Callback<Void> callback){
        ThreadTask t = new ThreadTask<Void,Void,Void>(callback, new Execute<Void>() {
            @Override
            public Void doExec() {
                //删除M、C表中对应记录
                return null;
            }
        });
        t.execute();
    }

    //点击联系人
    @Override
    public void ClickContact(final int contact_id, final Callback<Void> callback){
        ThreadTask t = new ThreadTask<Void,Void,Void>(callback, new Execute<Void>() {
            @Override
            public Void doExec() {
                //更新M表中联系人信息为已读
                return null;
            }
        });
        t.execute();
    }
}
