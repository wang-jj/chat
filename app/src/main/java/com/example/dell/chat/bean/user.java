package com.example.dell.chat.bean;

/**
 * Created by dell on 2018/4/10.
 */

//用于查看用户个人资料
public class user {
    private int user_id;//用户id
    private String user_name;//用户名
    private String image_path;//头像地址
    private String school;//学校
    private int gender;//性别，1代表女性，2代表男性
    private String user_motto;//个性签名
    private String picture_path1;//朋友圈第一张图
    private String picture_path2;//朋友圈第二张图
    private String picture_path3;//朋友圈第三张图

    public int getUser_id() {
        return user_id;
    }

    public String getUser_name() {
        return user_name;
    }

    public String getImage_path() {
        return image_path;
    }

    public String getSchool() {
        return school;
    }

    public int getGender() {
        return gender;
    }

    public String getUser_motto() {
        return user_motto;
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

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public void setImage_path(String image_path) {
        this.image_path = image_path;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public void setUser_motto(String user_motto) {
        this.user_motto = user_motto;
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
