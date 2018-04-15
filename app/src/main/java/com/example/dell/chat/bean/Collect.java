package com.example.dell.chat.bean;

/**
 * Created by Staroul on 2018/4/6.
 */

public class Collect {              //收藏类
    private String nickname;        //昵称
    private String content;         //昵称
    private String collect_time;    //事件
    private int profileID;          //头像
    private int image1ID;           //图片一
    private int image2ID;           //图片二
    private int image3ID;           //图片三
    private int img_type;           //recyclerview类型 1代表一张图片 以此类推

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getProfileID() {
        return profileID;
    }

    public void setProfileID(int profileID) {
        this.profileID = profileID;
    }

    public int getImage1ID() {
        return image1ID;
    }

    public void setImage1ID(int image1ID) {
        this.image1ID = image1ID;
    }

    public int getImage2ID() {
        return image2ID;
    }

    public void setImage2ID(int image2ID) {
        this.image2ID = image2ID;
    }

    public int getImage3ID() {
        return image3ID;
    }

    public void setImage3ID(int image3ID) {
        this.image3ID = image3ID;
    }

    public int getImg_type() {
        return img_type;
    }

    public void setImg_type(int img_type) {
        this.img_type = img_type;
    }

    public String getCollect_time() {
        return collect_time;
    }

    public void setCollect_time(String collect_time) {
        this.collect_time = collect_time;
    }
}
