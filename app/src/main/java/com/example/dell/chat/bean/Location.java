package com.example.dell.chat.bean;

import java.io.Serializable;

/**
 * Created by Staroul on 2018/4/6.
 */

public class Location implements Serializable{             //附近的人 类
    private String nickname;        //昵称
    private String school;          //学校
    private String introduction;    //简介
    private String profileID;          //头像
    private String image1ID;           //图片一
    private String image2ID;           //图片一
    private String image3ID;           //图片一
    private int img_type;           //recyclerview类型 1代表一张图片 以此类推
    private int user_id;            //用户id
    private double Latitude;        //纬度
    private double Longitude;       //经度
    private int gender;

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }


    public void setLatitude(double latitude) {
        Latitude = latitude;
    }

    public void setLongitude(double longitude) {
        Longitude = longitude;
    }

    public double getLatitude() {
        return Latitude;
    }

    public double getLongitude() {
        return Longitude;
    }

    public String getNickname() {
        return nickname;
    }

    public String getSchool() {
        return school;
    }

    public String getIntroduction() {
        return introduction;
    }

    public String getProfileID() {
        return profileID;
    }

    public String getImage1ID() {
        return image1ID;
    }

    public String getImage2ID() {
        return image2ID;
    }

    public String getImage3ID() {
        return image3ID;
    }

    public int getImg_type() {
        return img_type;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public void setProfileID(String profileID) {
        this.profileID = profileID;
    }

    public void setImage1ID(String image1ID) {
        this.image1ID = image1ID;
    }

    public void setImage2ID(String image2ID) {
        this.image2ID = image2ID;
    }

    public void setImage3ID(String image3ID) {
        this.image3ID = image3ID;
    }

    public void setImg_type(int img_type) {
        this.img_type = img_type;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }
}
