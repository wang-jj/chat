package com.example.dell.chat;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.dell.chat.base.BaseActivity;
import com.example.dell.chat.bean.MyApplication;
import com.example.dell.chat.bean.PersonalState;
import com.example.dell.chat.bean.User;
import com.example.dell.chat.db.PersonalStateDao;
import com.example.dell.chat.db.UserDao;
import com.example.dell.chat.model.Callback;
import com.example.dell.chat.model.Login.LoginModel;
import com.example.dell.chat.model.Login.LoginModelImpl;
import com.example.dell.chat.model.Moment.MomentModel;
import com.example.dell.chat.model.Moment.MomentModelImpl;
import com.example.dell.chat.presenter.LoginPresenter;
import com.example.dell.chat.presenter.MomentPresenter;

import org.json.JSONObject;

import java.util.List;

public class TestActivity extends BaseActivity<TestActivity,LoginPresenter<TestActivity>> {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_test);    //删除testacticvity时记得删除布局文件
        User u=new User();
        u.setGender(1);
        u.setUser_id(1);
        u.setUser_name("wang");
        UserDao userDao=MyApplication.getDao().getUserDao();
        userDao.insert(u);
        PersonalState personalState=new PersonalState();
        personalState.setHolder_id(2);
        personalState.setContent("jinjie");
        PersonalStateDao personalStateDao=MyApplication.getDao().getPersonalStateDao();
        personalStateDao.insert(personalState);
        List<PersonalState>personalStates=personalStateDao.loadAll();
        Log.d("MainActivity",  String.valueOf(personalStates.get(0).getHolder_id()));
        u.setUser_name("lin");
        MomentModel momentModel=new MomentModelImpl();
        momentModel.LoadMoment(1, 1, 1, new Callback<List<PersonalState>>() {
            @Override
            public void execute(List<PersonalState> datas) {
                setUserName(datas.get(0).getContent());
            }
        });
    }

    public void setUserName(String u) {
        TextView textView=(TextView)findViewById(R.id.textview1);
        textView.setText(u);
    }

    public void setImage(){
        ImageView imageView=findViewById(R.id.image_view);
        Glide.with(this).load("http://i.imgur.com/DvpvklR.png").into(imageView);
    }

    @Override
    protected LoginPresenter createPresenter() {
        return new LoginPresenter();
    }
}
