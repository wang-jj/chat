package com.example.dell.chat.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;
import org.litepal.annotation.Column;
import org.litepal.crud.DataSupport;

import java.util.Date;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by dell on 2018/4/10.
 */

//用于查看用户个人资料
@Entity
public class User {
    @Id(autoincrement = true)
    private Long id;
    private String email;
    private int user_id;//用户id
    private String user_name;//用户名
    private String image_path;//头像地址
    private String school;//学校
    private int gender;//性别，1代表女性，2代表男性
    private String user_motto;//个性签名
    private String picture_path1;//朋友圈第一张图
    private String picture_path2;//朋友圈第二张图
    private String picture_path3;//朋友圈第三张图
    private String password;//密码
    private String birthday;//生日
    public String getBirthday() {
        return this.birthday;
    }
    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }
    public String getPassword() {
        return this.password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public String getPicture_path3() {
        return this.picture_path3;
    }
    public void setPicture_path3(String picture_path3) {
        this.picture_path3 = picture_path3;
    }
    public String getPicture_path2() {
        return this.picture_path2;
    }
    public void setPicture_path2(String picture_path2) {
        this.picture_path2 = picture_path2;
    }
    public String getPicture_path1() {
        return this.picture_path1;
    }
    public void setPicture_path1(String picture_path1) {
        this.picture_path1 = picture_path1;
    }
    public String getUser_motto() {
        return this.user_motto;
    }
    public void setUser_motto(String user_motto) {
        this.user_motto = user_motto;
    }
    public int getGender() {
        return this.gender;
    }
    public void setGender(int gender) {
        this.gender = gender;
    }
    public String getSchool() {
        return this.school;
    }
    public void setSchool(String school) {
        this.school = school;
    }
    public String getImage_path() {
        return this.image_path;
    }
    public void setImage_path(String image_path) {
        this.image_path = image_path;
    }
    public String getUser_name() {
        return this.user_name;
    }
    public void setUser_name(String user_name) {
        this.user_name = user_name;
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
    public String getEmail() {
        return this.email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    @Generated(hash = 1558431951)
    public User(Long id, String email, int user_id, String user_name,
            String image_path, String school, int gender, String user_motto,
            String picture_path1, String picture_path2, String picture_path3,
            String password, String birthday) {
        this.id = id;
        this.email = email;
        this.user_id = user_id;
        this.user_name = user_name;
        this.image_path = image_path;
        this.school = school;
        this.gender = gender;
        this.user_motto = user_motto;
        this.picture_path1 = picture_path1;
        this.picture_path2 = picture_path2;
        this.picture_path3 = picture_path3;
        this.password = password;
        this.birthday = birthday;
    }
    @Generated(hash = 586692638)
    public User() {
    }
}
