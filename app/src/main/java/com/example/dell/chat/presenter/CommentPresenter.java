package com.example.dell.chat.presenter;

import com.example.dell.chat.base.BaseActivity;
import com.example.dell.chat.base.BasePresenter;
import com.example.dell.chat.bean.PersonalState;
import com.example.dell.chat.model.Comment.CommentModel;
import com.example.dell.chat.model.Comment.CommentModelImpl;
import com.example.dell.chat.model.Home.HomeModel;
import com.example.dell.chat.model.Home.HomeModelIlpl;

/**
 * Created by wang on 2018/5/6.
 */

public class CommentPresenter <T extends BaseActivity> extends BasePresenter<T> {
    private CommentModel commentModel=new CommentModelImpl();
    private HomeModel homeModel=new HomeModelIlpl();

    //点赞
    public void SendLike( PersonalState personalState){
        homeModel.SendLike(personalState.getPersonalstate_id(),personalState);
    }
}
