package com.example.dell.chat.presenter;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.example.dell.chat.bean.Location;
import com.example.dell.chat.bean.MyApplication;
import com.example.dell.chat.bean.PersonalState;
import com.example.dell.chat.db.PersonalStateDao;
import com.example.dell.chat.model.Callback;
import com.example.dell.chat.model.Home.HomeModel;
import com.example.dell.chat.model.Home.HomeModelIlpl;
import com.example.dell.chat.model.Main.MainModel;
import com.example.dell.chat.model.Main.MainModelImpl;
import com.example.dell.chat.view.HomeFragment;
import com.example.dell.chat.view.LocalFragment;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import android.support.v4.app.Fragment;
import android.widget.Toast;

/**
 * Created by wang on 2018/4/26.
 */

//获取朋友圈
public class HomePresenter {
    private HomeModel homeModel=new HomeModelIlpl();
    private HomeFragment view;


    public HomePresenter(HomeFragment view){
        this.view=view;
    }

    public void UpdateMoment(){
        final HomeFragment.StateAdapter adapter=view.getAdapter();
        final RecyclerView stateRecyclerView=view.getStateRecyclerView();
        final SwipeRefreshLayout swipeRefreshLayout=view.getSwipeRefreshLayout();
        final List<PersonalState> old=adapter.getmStateList();
        homeModel.UpdateMoment(MyApplication.getLatitude(), MyApplication.getLongitude(),old, new Callback<List<PersonalState>>() {
            @Override
            public void execute(List<PersonalState> datas) {
                view.UpdateMoment(datas);
            }
        });
    }

    public void LoadMoment(){
        homeModel.LoadMoment(new Callback<List<PersonalState>>() {
            @Override
            public void execute(List<PersonalState> datas) {
                view.LoadMoment(datas);
            }
        });
    }

    public void SendLike(int id, PersonalState personalState){
        homeModel.SendLike(id,personalState);
    }

}
