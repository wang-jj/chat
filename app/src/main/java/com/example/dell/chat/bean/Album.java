package com.example.dell.chat.bean;

import android.provider.ContactsContract;

import java.util.Date;

/**
 * Created by Staroul on 2018/4/6.
 */

public class Album {            //个人资料类
    private String content;     //动态内容
    private String location;    //动态定位
    private String image1ID;       //图片一
    private String image2ID;       //图片二
    private String image3ID;       //图片三
    private int img_type;       //recyclerview类型 1代表一张图片 以此类推
    private int album_id;
    private Date time;

    public int getAlbum_id() {
        return album_id;
    }

    public Date getTime() {
        return time;
    }

    public void setAlbum_id(int album_id) {
        this.album_id = album_id;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setLocation(String location) {
        this.location = location;
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

    public String getContent() {
        return content;
    }

    public String getLocation() {
        return location;
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
}
