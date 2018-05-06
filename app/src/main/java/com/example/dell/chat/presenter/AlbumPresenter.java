package com.example.dell.chat.presenter;

import android.util.Log;

import com.example.dell.chat.base.BaseActivity;
import com.example.dell.chat.base.BasePresenter;
import com.example.dell.chat.bean.Album;
import com.example.dell.chat.bean.PersonalState;
import com.example.dell.chat.model.Album.AlbumModel;
import com.example.dell.chat.model.Album.AlbumModelImpl;
import com.example.dell.chat.model.Callback;
import com.example.dell.chat.view.AlbumActivity;
import com.google.gson.Gson;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by wang on 2018/5/6.
 */

public class AlbumPresenter<T extends BaseActivity> extends BasePresenter<T>  {
    private AlbumModel albumModel=new AlbumModelImpl();

    public AlbumPresenter(){
        super();
    }

    public void LoadAlbum(int User_id){
        albumModel.LoadPersonalState(User_id, new Callback<List<PersonalState>>() {
            @Override
            public void execute(List<PersonalState> datas) {
                Collections.sort(datas, new Comparator<PersonalState>() {
                    @Override
                    public int compare(PersonalState personalState1, PersonalState personalState2) {
                        return personalState2.getState_time().compareTo(personalState1.getState_time());
                    }
                });
                ((AlbumActivity)getView()).LoadPersonalState(datas);
            }
        });
    }
}
