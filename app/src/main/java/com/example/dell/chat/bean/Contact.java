package com.example.dell.chat.bean;

/**
 * Created by courageface on 2018/5/8.
 */

public class Contact {
    private String profile;
    private String nickname;
    private String introduction;
    private String school;

    public String getProfile() {
        return profile;
    }

    public String getNickname() {
        return nickname;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }
}

