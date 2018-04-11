package com.example.dell.chat.model;

import java.util.UUID;

/**
 * Created by courageface on 2018/4/8.
 */

public class Chat {

    private UUID mId;
    private int direction;
    private String mMsg;
    private int mImage; //聊天对象用户头像
    private int mTime;//记录时间
    private String mName;

    public Chat(){
        mId = UUID.randomUUID();
    }

    public Chat(UUID id){
        mId = id;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public UUID getId() {
        return mId;
    }

    public int getDirection() {
        return direction;
    }

    public void setDirection(int direction) {
        this.direction = direction;
    }

    public void setId(UUID id) {
        mId = id;
    }

    public String getMsg() {
        return mMsg;
    }

    public void setMsg(String msg) {
        mMsg = msg;
    }

    public int getImage() {
        return mImage;
    }

    public void setImage(int image) {
        mImage = image;
    }

    public int getTime() {
        return mTime;
    }

    public void setTime(int time) {
        mTime = time;
    }
}
