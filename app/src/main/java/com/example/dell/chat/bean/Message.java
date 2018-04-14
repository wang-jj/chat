package com.example.dell.chat.bean;

import org.litepal.crud.DataSupport;

import java.util.Date;

/**
 * 常用联系人class 对应数据表 Message
 * Created by courageface on 2018/4/3.
 */

public class Message extends DataSupport {
    private int userid;  //本用户的ID
    private int id;    //联系人ID
    private String profileID;   //联系人头像
    private String nickname;   //联系人用户名
    private String latest_content;   //最后一条信息
    private Date latest_time;   //最后一条信息的时间
    private int unread;   //是否有未读消息 0为已读，1为未读

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

    public String getProfileID() {
        return profileID;
    }

    public void setProfileID(String profileID) {
        this.profileID = profileID;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getLatest_content() {
        return latest_content;
    }

    public void setLatest_content(String latest_content) {
        this.latest_content = latest_content;
    }

    public Date getLatest_time() {
        return latest_time;
    }

    public void setLatest_time(Date latest_time) {
        this.latest_time = latest_time;
    }

    public int getUnread() {
        return unread;
    }

    public void setUnread(int unread) {
        this.unread = unread;
    }
}