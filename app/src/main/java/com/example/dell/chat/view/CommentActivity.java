package com.example.dell.chat.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.dell.chat.R;
import com.example.dell.chat.bean.Comment;

import java.util.ArrayList;
import java.util.List;

//动态详情的评论activity
public class CommentActivity extends AppCompatActivity {

    private EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });


        final LinearLayout progressLayout=(LinearLayout)findViewById(R.id.comment_progress);
        progressLayout.setVisibility(View.VISIBLE);

        final RecyclerView commentRecyclerView=(RecyclerView)findViewById(R.id.comment_recycler_view);
        commentRecyclerView.setNestedScrollingEnabled(false);
        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        commentRecyclerView.setLayoutManager(layoutManager);
        final CommentAdapter adapter=new CommentAdapter(getComment());  //初始化recyclerview

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {//设置延时看动画效果  模拟实际运行时使用的时间  正式使用时去掉handler
                commentRecyclerView.setAdapter(adapter);
                progressLayout.setVisibility(View.GONE);
            }
        }, 1000);

        //评论输入点击函数 在点击时调用输入法
        editText=(EditText)findViewById(R.id.comment_edit);
        editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(ChatActivity.this,"edit点击",Toast.LENGTH_SHORT).show();
                editText.setFocusable(true);
                editText.setFocusableInTouchMode(true);
                editText.requestFocus();
                editText.requestFocusFromTouch();
                InputMethodManager inputManager =(InputMethodManager)editText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.showSoftInput(editText, 0);
            }
        });

        //发送按钮点击函数 在点击后 往recyclerview最顶部添加一项评论 以发送前edittext的hint(即recyclerview点击的那一项的nickname)
        //加上edittext中输入的内容作为评论内容
        ImageButton imageButton_send=(ImageButton)findViewById(R.id.comment_send);
        imageButton_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!editText.getText().toString().isEmpty()){
                    String set_content=editText.getHint().toString()+editText.getText().toString();
                    final Comment comment=new Comment();
                    comment.setNickname("谢欣逗比言");
                    comment.setComment_content(set_content);
                    comment.setComment_time("19:61");
                    comment.setProfileID(R.drawable.profile);
                    adapter.CommentAdd(0,comment);
                    adapter.notifyItemInserted(0);
                    commentRecyclerView.scrollToPosition(0);
                    editText.setText("");
                    editText.setHint("");
                }
            }
        });

        //设置 当点击评论按钮时 让edittext中的hint设置为空
        LinearLayout comment_linear=(LinearLayout)findViewById(R.id.comment_comment_linear);
        comment_linear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editText.setHint("");
            }
        });

        //底部刷新函数 即滑动到底部的刷新逻辑
        NestedScrollView nestedScrollView=(NestedScrollView)findViewById(R.id.comment_scroll_view);
        nestedScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (scrollY == (v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight())) {
                    //Toast.makeText(AlbumActivity.this,"刷新", Toast.LENGTH_SHORT).show();
                    if(progressLayout.getVisibility()==View.GONE){
                        progressLayout.setVisibility(View.VISIBLE);
                    }
                    final Comment comment=new Comment();
                    comment.setNickname("谢欣逗比言");
                    comment.setComment_content("你是智障吗，废物！");
                    comment.setComment_time("19:61");
                    comment.setProfileID(R.drawable.profile);
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            adapter.CommentAdd(adapter.getItemCount(),comment);
                            adapter.notifyItemInserted(adapter.getItemCount());
                            progressLayout.setVisibility(View.GONE);
                        }
                    }, 1000);
                }
            }
        });

        //点赞函数 点赞时切换图片 实现点赞效果
        LinearLayout likeLinearLayout=(LinearLayout)findViewById(R.id.comment_like_linear);
        likeLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImageView like=(ImageView)findViewById(R.id.comment_like_picture);
                like.setImageResource(R.drawable.like_checked);
            }
        });
    }

    private List<Comment> getComment(){     //初始化recyclerview的函数
        List<Comment> commentList=new ArrayList<>();
        for(int i=1;i<=10;i++){
            Comment comment=new Comment();
            comment.setNickname("谢欣逗比言"+i);
            comment.setComment_content("你是智障吗，废物！");
            comment.setComment_time("19:"+(i+43));
            comment.setProfileID(R.drawable.profile);
            commentList.add(comment);   //添加入stateList中
        }
        return commentList;
    }

    @Override
    public void onBackPressed() {   //重载back函数 当edit显示为回复某人时 先设置为空 若edit以为空 则直接返回到上一个activity
        if (editText.getHint().toString().isEmpty()) {
            super.onBackPressed();
        } else {
            editText.setHint("");
        }
    }

    //recyclerview的adapter定义
    class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder>{
        private List<Comment> mCommentList;

        class ViewHolder extends RecyclerView.ViewHolder{
            TextView commentNickName;
            TextView commentContent;
            TextView commentTime;
            ImageView commentProfile; //头像 类型为int 之后按照后端需求修改 暂时引用drawable中的已有图片资源，即int
            LinearLayout commentLinear;

            public ViewHolder(View view){
                super(view);
                commentNickName=(TextView)view.findViewById(R.id.comment_nickname);
                commentContent=(TextView)view.findViewById(R.id.comment_content);
                commentTime=(TextView)view.findViewById(R.id.comment_time);
                commentProfile=(ImageView)view.findViewById(R.id.comment_profile);
                commentLinear=(LinearLayout)view.findViewById(R.id.comment_linear);
            }
        }
        public CommentAdapter(List<Comment> commentList){
            mCommentList=commentList;
        }

        public void CommentAdd(int position,Comment comment){
            mCommentList.add(position,comment);
            return;
        }

        public void CommentRemove(int position){
            mCommentList.remove(position);
            return;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
            View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_item,parent,false);
            final ViewHolder holder=new ViewHolder(view);

            holder.commentLinear.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position=holder.getAdapterPosition();
                    Comment comment=mCommentList.get(position);
                    String set_comment_hint="回复 "+comment.getNickname()+": ";
                    editText.setHint(set_comment_hint);
                    editText.setFocusable(true);
                    editText.setFocusableInTouchMode(true);
                    editText.requestFocus();
                    editText.requestFocusFromTouch();
                    InputMethodManager inputManager =(InputMethodManager)editText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputManager.showSoftInput(editText, 0);
                }
            });

            return holder;
        }

        @Override
        public void onBindViewHolder(CommentAdapter.ViewHolder holder, int position){
            Comment comment=mCommentList.get(position);
            holder.commentNickName.setText(comment.getNickname());
            holder.commentContent.setText(comment.getComment_content());
            holder.commentTime.setText(comment.getComment_time());
            holder.commentProfile.setImageResource(comment.getProfileID());
        }

        @Override
        public int getItemCount(){
            return mCommentList.size();
        }
    }
}
