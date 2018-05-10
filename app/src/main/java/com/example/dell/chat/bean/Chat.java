package com.example.dell.chat.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Index;

/**
 * 聊天记录class 对应数据表 Chat
 * Created by courageface on 2018/4/8.
 */
@Entity
public class Chat implements Serializable {
    @Id(autoincrement = true)
    private Long id;
    //private int msg_id;    //该条聊天记录的ID 可能去除
    @Index
    private int user_id;    //本用户的ID
    private int contact_id;    //联系人ID
    private String content; //聊天内容或文件路径
    private long time;  //信息时间
    private int type;  //信息类型
    //0为发送文本，1为接收文本，2为发送图片，3为接收图片
    public int getType() {
        return this.type;
    }
    public void setType(int type) {
        this.type = type;
    }
    public long getTime() {
        return this.time;
    }
    public void setTime(long time) {
        this.time = time;
    }
    public String getContent() {
        return this.content;
    }
    public void setContent(String content) {
        this.content = content;
    }
    public int getContact_id() {
        return this.contact_id;
    }
    public void setContact_id(int contact_id) {
        this.contact_id = contact_id;
    }
    public int getUser_id() {
        return this.user_id;
    }
    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    @Generated(hash = 2039281702)
    public Chat(Long id, int user_id, int contact_id, String content, long time,
            int type) {
        this.id = id;
        this.user_id = user_id;
        this.contact_id = contact_id;
        this.content = content;
        this.time = time;
        this.type = type;
    }
    @Generated(hash = 519536279)
    public Chat() {
    }



}
