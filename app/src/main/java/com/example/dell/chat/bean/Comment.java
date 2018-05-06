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
    private int User_id;            //评论用户的id
    private int Holder_id;          //被评论用户的id
    private String Holder_name;     //被评论的用户的用户名

    public int getHolder_id() {
        return Holder_id;
    }

    public String getHolder_name() {
        return Holder_name;
    }

    public void setHolder_id(int holder_id) {
        Holder_id = holder_id;
    }

    public void setHolder_name(String holder_name) {
        Holder_name = holder_name;
    }

    public int getUser_id() {
        return User_id;
    }

    public void setUser_id(int user_id) {
        User_id = user_id;
    }

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
