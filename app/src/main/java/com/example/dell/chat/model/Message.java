package com.example.dell.chat.model;

import java.util.UUID;

/**
 * Created by courageface on 2018/4/3.
 */

public class Message {
    private UUID mId;
    private String mName;
    private int mImage; //聊天对象用户头像
    private String mMsg; //最后一条聊天记录
    private int mLasttime;//最后记录对应时间
    //private


    public Message(){
        mId = UUID.randomUUID();
    }

    public Message(UUID uuid){
        mId = uuid;
    }

    public UUID getId() {
        return mId;
    }

    public void setId(UUID id) {
        mId = id;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public int getImage() {
        return mImage;
    }

    public void setImage(int image) {
        mImage = image;
    }

    public String getMsg() {
        return mMsg;
    }

    public void setMsg(String msg) {
        mMsg = msg;
    }

    public int getLasttime() {
        return mLasttime;
    }

    public void setLasttime(int lasttime) {
        mLasttime = lasttime;
    }
}
