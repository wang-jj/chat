package com.example.dell.chat.bean;

import java.util.Date;

/**
 * Created by dell on 2018/4/10.
 */

//朋友圈的类，每一条朋友圈就是一个该类的对象
public class monent {
    private int moment_id;//朋友圈id
    private int user_id;//发这条朋友圈的用户的id
    private String user_name;//发这条朋友圈的用户的用户名
    private int holder_id;//当前登录账号的id
    private String context;//朋友圈的文字内容
    private String location;//地理位置
    private Date create_time;//发这条朋友圈的时间
    private Date update_time;//刷新这条朋友圈的时间
    private String image_path;//发这条朋友圈的用户的头像
    private int like_num;//点赞数
    private String picture_path1;//图片地址
    private String picture_path2;
    private String picture_path3;

    public int getMoment_id() {
        return moment_id;
    }

    public int getUser_id() {
        return user_id;
    }

    public String getUser_name() {
        return user_name;
    }

    public int getHolder_id() {
        return holder_id;
    }

    public String getContext() {
        return context;
    }

    public String getLocation() {
        return location;
    }

    public Date getCreate_time() {
        return create_time;
    }

    public Date getUpdate_time() {
        return update_time;
    }

    public String getImage_path() {
        return image_path;
    }

    public int getLike_num() {
        return like_num;
    }

    public String getPicture_path1() {
        return picture_path1;
    }

    public String getPicture_path2() {
        return picture_path2;
    }

    public String getPicture_path3() {
        return picture_path3;
    }

    public void setMoment_id(int moment_id) {
        this.moment_id = moment_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public void setHolder_id(int holder_id) {
        this.holder_id = holder_id;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setCreate_time(Date create_time) {
        this.create_time = create_time;
    }

    public void setUpdate_time(Date update_time) {
        this.update_time = update_time;
    }

    public void setImage_path(String image_path) {
        this.image_path = image_path;
    }

    public void setLike_num(int like_num) {
        this.like_num = like_num;
    }

    public void setPicture_path1(String picture_path1) {
        this.picture_path1 = picture_path1;
    }

    public void setPicture_path2(String picture_path2) {
        this.picture_path2 = picture_path2;
    }

    public void setPicture_path3(String picture_path3) {
        this.picture_path3 = picture_path3;
    }
}
