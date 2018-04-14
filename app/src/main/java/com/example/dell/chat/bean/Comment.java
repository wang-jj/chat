package com.example.dell.chat.bean;

/**
 * Created by Staroul on 2018/4/3.
 */

public class Comment {              //评论类
    private int momentID;           //动态id
    private int profileID;          //头像
    private String nickname;        //昵称
    private String comment_content; //评论内容
    private String comment_time;    //评论时间

    public int getMomentID() {
        return momentID;
    }

    public void setMomentID(int momentID) {
        this.momentID = momentID;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public int getProfileID() {
        return profileID;
    }

    public void setProfileID(int profileID) {
        this.profileID = profileID;
    }

    public String getComment_content() {
        return comment_content;
    }

    public void setComment_content(String comment_content) {
        this.comment_content = comment_content;
    }

    public String getComment_time() {
        return comment_time;
    }

    public void setComment_time(String comment_time) {
        this.comment_time = comment_time;
    }
}
