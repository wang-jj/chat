package com.example.dell.chat.model.Home;

import com.example.dell.chat.bean.PersonalState;
import com.example.dell.chat.model.Callback;

import java.util.List;

/**
 * Created by wang on 2018/4/26.
 */

public interface HomeModel {
    //更新朋友圈
    void UpdateMoment(double latitude,double longitude,List<PersonalState> list,final Callback<List<PersonalState>> callback);
}
