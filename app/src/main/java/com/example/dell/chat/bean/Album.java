package com.example.dell.chat.bean;

/**
 * Created by Staroul on 2018/4/6.
 */

public class Album {            //个人资料类
    private String content;     //动态内容
    private String location;    //动态定位
    private int image1ID;       //图片一
    private int image2ID;       //图片二
    private int image3ID;       //图片三
    private int img_type;       //recyclerview类型 1代表一张图片 以此类推

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
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


    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getImg_type() {
        return img_type;
    }

    public void setImg_type(int img_type) {
        this.img_type = img_type;
    }
}
