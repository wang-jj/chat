package com.example.dell.chat.model.Home;

import com.example.dell.chat.bean.Location;
import com.example.dell.chat.bean.PersonalState;
import com.example.dell.chat.model.Callback;

import java.util.List;

/**
 * Created by wang on 2018/4/26.
 */

public interface HomeModel {
    //更新朋友圈
    void UpdateMoment(double latitude,double longitude,List<PersonalState> list,final Callback<List<PersonalState>> callback);
    //更新附近的人
    void UpdatePerson(double latitude,double longitude,final Callback<List<Location>> callback);
    //从数据库加载朋友圈
    void LoadMoment(final Callback<List<PersonalState>> callback);
    //点赞
    void SendLike(final int Personalstate_id,final PersonalState personalState);
}
