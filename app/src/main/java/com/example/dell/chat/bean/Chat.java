package com.example.dell.chat.bean;

import org.litepal.crud.DataSupport;

import java.util.Date;
import java.util.UUID;

/**
 * 聊天记录class 对应数据表 Chat
 * Created by courageface on 2018/4/8.
 */

public class Chat extends DataSupport{

    private int msgid;    //该条聊天记录的ID
    private int userid;    //本用户的ID
    private int id;    //联系人ID
    private String content; //聊天内容或文件路径
    private Date time;  //信息时间
    private int type;  //信息类型
    //0为发送文本，1为接收文本，2为发送图片，3为接收图片


    public int getMsgid() {
        return msgid;
    }

    public void setMsgid(int msgid) {
        this.msgid = msgid;
    }

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
