package com.example.dell.chat.presenter;

import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.dell.chat.R;
import com.example.dell.chat.TestActivity;
import com.example.dell.chat.base.BaseActivity;
import com.example.dell.chat.base.BasePresenter;
import com.example.dell.chat.bean.MyApplication;
import com.example.dell.chat.bean.PersonalState;
import com.example.dell.chat.model.Callback;
import com.example.dell.chat.model.Moment.MomentModel;
import com.example.dell.chat.model.Moment.MomentModelImpl;

import java.util.List;

/**
 * Created by wang on 2018/4/14.
 */

public class MomentPresenter<T extends BaseActivity> extends BasePresenter<T> {

    private MomentModel momentModel=new MomentModelImpl();

    public MomentPresenter(){
        super();
    }

    public void LoadMoment(){
        MyApplication myApplication=(MyApplication) (getView().getApplication());
        int user_id=myApplication.getUser().getUser_id();
        momentModel.LoadMoment(user_id, 0, -1, new Callback<List<PersonalState>>() {
            @Override
            public void execute(List<PersonalState> datas) {
                /*
                Log.d("TestActivity",String .valueOf(datas.get(0).getUser_id()));
                ((TestActivity)getView()).setImage();
                */
            }
        });
    }
}
