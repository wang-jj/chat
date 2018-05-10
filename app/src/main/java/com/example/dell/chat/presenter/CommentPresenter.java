package com.example.dell.chat.presenter;

import android.util.Log;
import android.widget.Toast;

import com.example.dell.chat.base.BaseActivity;
import com.example.dell.chat.base.BasePresenter;
import com.example.dell.chat.bean.Comment;
import com.example.dell.chat.bean.MyApplication;
import com.example.dell.chat.bean.PersonalState;
import com.example.dell.chat.model.Callback;
import com.example.dell.chat.model.Chat.ChatModelImpl;
import com.example.dell.chat.model.Comment.CommentModel;
import com.example.dell.chat.model.Comment.CommentModelImpl;
import com.example.dell.chat.model.Home.HomeModel;
import com.example.dell.chat.model.Home.HomeModelIlpl;
import com.example.dell.chat.view.CommentActivity;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.util.Date;
import java.util.List;

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

    public void UpComment(final int momentID, int user_id, final int holder_id, final String content){
        commentModel.UpComment(momentID, user_id, holder_id, content, new Callback<String>() {
            @Override
            public void execute(String datas) {
                if(datas.equals("out_of_time")){
                    Toast.makeText(MyApplication.getContext(),"网络连接失败",Toast.LENGTH_LONG).show();
                    Log.e("up", datas);
                }else {
                    Log.e("up", datas);
                    new ChatModelImpl().SendComment(MyApplication.getUser().getUser_name()+" 评论/回复了你",momentID,holder_id);
                    Comment comment=new Comment();
                    comment.setComment_content(content);
                    comment.setProfileID(MyApplication.getUser().getImage_path());
                    comment.setMomentID(momentID);
                    comment.setHolder_id(holder_id);
                    comment.setUser_id(MyApplication.getUser().getUser_id());
                    comment.setComment_time(new Date(System.currentTimeMillis()));
                    comment.setNickname(MyApplication.getUser().getUser_name());
                    ((CommentActivity)getView()).UpdateRecycleView(comment);
                }
            }
        });
    }

    public void DownComment(int momentID){
        commentModel.LoadComment(momentID, new Callback<String>() {
            @Override
            public void execute(String datas) {
                if(datas.equals("out_of_time")){
                    Toast.makeText(MyApplication.getContext(),"网络连接失败",Toast.LENGTH_LONG).show();
                    Log.e("up", datas);
                }else {
                    Gson gson=new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
                    List<Comment>comments =gson.fromJson(datas, new TypeToken<List<Comment>>() {}.getType());
                    Log.e("up", datas);
                    Log.e("up", new Gson().toJson(comments));
                    ((CommentActivity)getView()).LoadComment(comments);
                }
            }
        });
    }
}
