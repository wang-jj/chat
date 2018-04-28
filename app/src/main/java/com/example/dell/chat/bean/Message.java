package com.example.dell.chat.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.litepal.crud.DataSupport;

import java.util.Date;
import org.greenrobot.greendao.annotation.Generated;

/**
 * 常用联系人class 对应数据表 Message
 * Created by courageface on 2018/4/3.
 */
@Entity
public class Message  {
    @Id(autoincrement = true)
    private Long id;
    private int user_id;  //本用户的ID
    private int contact_id;    //联系人ID
    private String image_path;   //联系人头像路径
    private String contact_name;   //联系人用户名
    private String latest_content;   //最后一条信息
    private long latest_time;   //最后一条信息的时间
    private int unread;   //是否有未读消息 0为已读，1为未读
    public int getUnread() {
        return this.unread;
    }
    public void setUnread(int unread) {
        this.unread = unread;
    }
    public long getLatest_time() {
        return this.latest_time;
    }
    public void setLatest_time(long latest_time) {
        this.latest_time = latest_time;
    }
    public String getLatest_content() {
        return this.latest_content;
    }
    public void setLatest_content(String latest_content) {
        this.latest_content = latest_content;
    }
    public String getContact_name() {
        return this.contact_name;
    }
    public void setContact_name(String contact_name) {
        this.contact_name = contact_name;
    }
    public String getImage_path() {
        return this.image_path;
    }
    public void setImage_path(String image_path) {
        this.image_path = image_path;
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
    @Generated(hash = 601982840)
    public Message(Long id, int user_id, int contact_id, String image_path,
            String contact_name, String latest_content, long latest_time, int unread) {
        this.id = id;
        this.user_id = user_id;
        this.contact_id = contact_id;
        this.image_path = image_path;
        this.contact_name = contact_name;
        this.latest_content = latest_content;
        this.latest_time = latest_time;
        this.unread = unread;
    }
    @Generated(hash = 637306882)
    public Message() {
    }


}