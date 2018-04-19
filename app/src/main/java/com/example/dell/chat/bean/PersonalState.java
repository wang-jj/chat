package com.example.dell.chat.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;

import java.util.Date;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by Staroul on 2018/3/30.
 */

@Entity
public class PersonalState {        //动态类
    @Id(autoincrement = true)
    private Long id;
    private String nickname;        //昵称
    private String school;          //学校
    private String content;         //动态内容
    private String location;        //定位
    private Date state_time;        //发动态的时间
    private int like;               //点赞数
    private int comment;            //评论
    private String profileID;          //头像
    private String image1ID;           //图片一
    private String image2ID;           //图片一
    private String image3ID;           //图片一
    private int pictureID;          //点赞图片的样式 这个是实现点赞按钮的动画效果的
    private int img_type;           ////recyclerview类型 1代表一张图片 以此类推
    private int personalstate_id;   //动态id
    private int user_id;            //发这条朋友圈的用户的id
    @Index
    private int holder_id;          //当前登录账号的id
    @Index
    private Date update_time;       //刷新这条朋友圈的时间
    public Date getUpdate_time() {
        return this.update_time;
    }
    public void setUpdate_time(Date update_time) {
        this.update_time = update_time;
    }
    public int getHolder_id() {
        return this.holder_id;
    }
    public void setHolder_id(int holder_id) {
        this.holder_id = holder_id;
    }
    public int getUser_id() {
        return this.user_id;
    }
    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }
    public int getPersonalstate_id() {
        return this.personalstate_id;
    }
    public void setPersonalstate_id(int personalstate_id) {
        this.personalstate_id = personalstate_id;
    }
    public int getImg_type() {
        return this.img_type;
    }
    public void setImg_type(int img_type) {
        this.img_type = img_type;
    }
    public int getPictureID() {
        return this.pictureID;
    }
    public void setPictureID(int pictureID) {
        this.pictureID = pictureID;
    }
    public String getImage3ID() {
        return this.image3ID;
    }
    public void setImage3ID(String image3ID) {
        this.image3ID = image3ID;
    }
    public String getImage2ID() {
        return this.image2ID;
    }
    public void setImage2ID(String image2ID) {
        this.image2ID = image2ID;
    }
    public String getImage1ID() {
        return this.image1ID;
    }
    public void setImage1ID(String image1ID) {
        this.image1ID = image1ID;
    }
    public String getProfileID() {
        return this.profileID;
    }
    public void setProfileID(String profileID) {
        this.profileID = profileID;
    }
    public int getComment() {
        return this.comment;
    }
    public void setComment(int comment) {
        this.comment = comment;
    }
    public int getLike() {
        return this.like;
    }
    public void setLike(int like) {
        this.like = like;
    }
    public Date getState_time() {
        return this.state_time;
    }
    public void setState_time(Date state_time) {
        this.state_time = state_time;
    }
    public String getLocation() {
        return this.location;
    }
    public void setLocation(String location) {
        this.location = location;
    }
    public String getContent() {
        return this.content;
    }
    public void setContent(String content) {
        this.content = content;
    }
    public String getSchool() {
        return this.school;
    }
    public void setSchool(String school) {
        this.school = school;
    }
    public String getNickname() {
        return this.nickname;
    }
    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    @Generated(hash = 2035981307)
    public PersonalState(Long id, String nickname, String school, String content,
            String location, Date state_time, int like, int comment,
            String profileID, String image1ID, String image2ID, String image3ID,
            int pictureID, int img_type, int personalstate_id, int user_id,
            int holder_id, Date update_time) {
        this.id = id;
        this.nickname = nickname;
        this.school = school;
        this.content = content;
        this.location = location;
        this.state_time = state_time;
        this.like = like;
        this.comment = comment;
        this.profileID = profileID;
        this.image1ID = image1ID;
        this.image2ID = image2ID;
        this.image3ID = image3ID;
        this.pictureID = pictureID;
        this.img_type = img_type;
        this.personalstate_id = personalstate_id;
        this.user_id = user_id;
        this.holder_id = holder_id;
        this.update_time = update_time;
    }
    @Generated(hash = 981515077)
    public PersonalState() {
    }

}
