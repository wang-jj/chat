package com.example.dell.chat.model.Album;

import com.example.dell.chat.bean.PersonalState;
import com.example.dell.chat.model.Callback;

import java.util.List;

/**
 * Created by wang on 2018/5/6.
 */

public interface AlbumModel {
    //通过user_id查看朋友圈
    void LoadPersonalState(final int User_id, final Callback<List<PersonalState>> callback);
}
