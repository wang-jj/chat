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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.awen.photo.photopick.controller.PhotoPagerConfig;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.dell.chat.R;
import com.example.dell.chat.base.BaseActivity;
import com.example.dell.chat.bean.Comment;
import com.example.dell.chat.bean.MyApplication;
import com.example.dell.chat.bean.PersonalState;
import com.example.dell.chat.bean.User;
import com.example.dell.chat.db.PersonalStateDao;
import com.example.dell.chat.model.Chat.ChatModelImpl;
import com.example.dell.chat.presenter.CommentPresenter;
import com.example.dell.chat.presenter.LoginPresenter;
import com.example.dell.chat.tools.CircleImageView;
import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

//动态详情的评论activity
public class CommentActivity extends BaseActivity<CommentActivity,CommentPresenter<CommentActivity>> implements View.OnClickListener {

    private EditText editText;
    private PersonalState personalState;
    private RequestOptions requestOptions=new RequestOptions().centerCrop();
    private CommentAdapter adapter;
    private RecyclerView commentRecyclerView;
    private LinearLayout progressLayout;
    private int user_id;
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        personalState=(PersonalState)getIntent().getSerializableExtra("personalstate");
        user_id=personalState.getUser_id();
        Load();
        presenter.DownComment(personalState.getPersonalstate_id());
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });


        progressLayout=(LinearLayout)findViewById(R.id.comment_progress);
        progressLayout.setVisibility(View.VISIBLE);

        commentRecyclerView=(RecyclerView)findViewById(R.id.comment_recycler_view);
        commentRecyclerView.setNestedScrollingEnabled(false);
        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        commentRecyclerView.setLayoutManager(layoutManager);
        adapter=new CommentAdapter(new ArrayList<Comment>());  //初始化recyclerview

        commentRecyclerView.setAdapter(adapter);
        //progressLayout.setVisibility(View.GONE);
        //final Handler handler = new Handler();
        /*
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {//设置延时看动画效果  模拟实际运行时使用的时间  正式使用时去掉handler
                commentRecyclerView.setAdapter(adapter);
                progressLayout.setVisibility(View.GONE);
            }
        }, 1000);
        */

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
                    String set_content;
                    if(editText.getHint()!=null){
                        set_content=editText.getHint().toString()+editText.getText().toString();
                    }else {
                        set_content=editText.getText().toString();
                    }
                    editText.setText("");
                    editText.setHint("");
                    presenter.UpComment(personalState.getPersonalstate_id(),MyApplication.getUser().getUser_id(),user_id,set_content);
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                    /*
                    final Comment comment=new Comment();
                    comment.setNickname("谢欣逗比言");
                    comment.setComment_content(set_content);
                    comment.setComment_time(new Date(System.currentTimeMillis()));
                    //comment.setProfileID(R.drawable.profile);
                    adapter.CommentAdd(0,comment);
                    adapter.notifyItemInserted(0);
                    commentRecyclerView.scrollToPosition(0);
                    */
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

        /*
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
                    comment.setComment_time(new Date(System.currentTimeMillis()));
                    //comment.setProfileID(R.drawable.profile);
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
        */

        //点赞函数 点赞时切换图片 实现点赞效果
        LinearLayout likeLinearLayout=(LinearLayout)findViewById(R.id.comment_like_linear);
        likeLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(personalState.getPictureID()==0){
                    ImageView like=(ImageView)findViewById(R.id.comment_like_picture);
                    TextView num=(TextView)findViewById(R.id.comment_state_like) ;
                    like.setImageResource(R.drawable.like_checked);
                    personalState.setLike(personalState.getLike()+1);
                    personalState.setPictureID(1);
                    num.setText(String.valueOf(personalState.getLike()));
                    personalState.setPictureID(1);
                    presenter.SendLike(personalState);
                    new ChatModelImpl().SendComment(MyApplication.getUser().getUser_name()+" 点赞了你",personalState.getPersonalstate_id(),personalState.getUser_id());
                }
            }
        });
        findViewById(R.id.comment_state_image1).setOnClickListener(this);
        findViewById(R.id.comment_state_image2).setOnClickListener(this);
        findViewById(R.id.comment_state_image3).setOnClickListener(this);
    }

    /*
    private List<Comment> getComment(){     //初始化recyclerview的函数
        List<Comment> commentList=new ArrayList<>();
        for(int i=1;i<=10;i++){
            Comment comment=new Comment();
            comment.setNickname("谢欣逗比言"+i);
            comment.setComment_content("你是智障吗，废物！");
            comment.setComment_time(new Date(System.currentTimeMillis()));
            comment.setProfileID(R.drawable.profile);
            commentList.add(comment);   //添加入stateList中
        }
        return commentList;
    }
    */

    @Override
    public void onBackPressed() {   //重载back函数 当edit显示为回复某人时 先设置为空 若edit以为空 则直接返回到上一个activity
        if (editText.getHint()==null||editText.getHint().toString().isEmpty()) {
            Intent intent=new Intent();
            intent.putExtra("return",personalState);
            setResult(RESULT_OK,intent);
            finish();
            //super.onBackPressed();
        } else {
            user_id=personalState.getHolder_id();
            editText.setHint("");
        }
        //super.onBackPressed();
    }

    //recyclerview的adapter定义
    class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder>{
        private List<Comment> mCommentList;

        /*
        @Override
        public void onBindViewHolder(ViewHolder holder, int position, List<Object> payloads) {
            super.onBindViewHolder(holder, position, payloads);
            Comment comment=mCommentList.get(position);
            Glide.with(CommentActivity.this).load(comment.getProfileID()).thumbnail(0.1f).into(holder.commentProfile);
        }
        */

        public List<Comment> getmCommentList() {
            return mCommentList;
        }

        public void setmCommentList(List<Comment> mCommentList) {
            this.mCommentList = mCommentList;
        }

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
                    user_id=comment.getUser_id();
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
            Glide.with(CommentActivity.this).load(comment.getProfileID()).thumbnail(0.1f).into(holder.commentProfile);
            holder.commentNickName.setText(comment.getNickname());
            holder.commentContent.setText(comment.getComment_content());
            holder.commentTime.setText(simpleDateFormat.format(comment.getComment_time()));
        }

        @Override
        public int getItemCount(){
            return mCommentList.size();
        }
    }

    @Override
    protected CommentPresenter createPresenter() {
        return new CommentPresenter();
    }

    protected void Load(){
        CircleImageView circleImageView=(CircleImageView)findViewById(R.id.comment_state_profile);
        Glide.with(CommentActivity.this).load(personalState.getProfileID()).apply(requestOptions).into(circleImageView);
        TextView nick_name=(TextView)findViewById(R.id.comment_state_nickname);
        nick_name.setText(personalState.getNickname());
        TextView school=(TextView)findViewById(R.id.comment_state_school);
        school.setText(personalState.getSchool());
        TextView content=(TextView)findViewById(R.id.comment_state_content);
        content.setText(personalState.getContent());
        if(personalState.getImage1ID()!=null){
            ImageView imageView1=(ImageView)findViewById(R.id.comment_state_image1);
            Glide.with(CommentActivity.this).load(personalState.getImage1ID()).apply(requestOptions).into(imageView1);
        }
        if(personalState.getImage2ID()!=null){
            ImageView imageView2=(ImageView)findViewById(R.id.comment_state_image2);
            Glide.with(CommentActivity.this).load(personalState.getImage2ID()).apply(requestOptions).into(imageView2);
        }
        if(personalState.getImage3ID()!=null){
            ImageView imageView3=(ImageView)findViewById(R.id.comment_state_image3);
            Glide.with(CommentActivity.this).load(personalState.getImage3ID()).apply(requestOptions).into(imageView3);
        }
        TextView location=(TextView)findViewById(R.id.comment_state_location);
        location.setText(personalState.getLocation());
        TextView like=(TextView)findViewById(R.id.comment_state_like);
        like.setText(String.valueOf(personalState.getLike()));
        if(personalState.getPictureID()!=0){
            ImageView like_picture=(ImageView)findViewById(R.id.comment_like_picture);
            like_picture.setImageResource(R.drawable.like_checked);
        }
        TextView comment_num=(TextView)findViewById(R.id.comment_state_comment);
        comment_num.setText(String.valueOf(personalState.getComment()));
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.comment_state_image1:
                if(personalState.getImage1ID()!=null){
                    ArrayList<String>urls=new ArrayList<>();
                    urls.add(personalState.getImage1ID());
                    if(personalState.getImage2ID()!=null){
                        urls.add(personalState.getImage2ID());
                    }
                    if(personalState.getImage3ID()!=null){
                        urls.add(personalState.getImage3ID());
                    }
                    seePicture(urls,0);
                }
                break;
            case R.id.comment_state_image2 :
                if(personalState.getImage2ID()!=null){
                    ArrayList<String>urls=new ArrayList<>();
                    urls.add(personalState.getImage1ID());
                    urls.add(personalState.getImage2ID());
                    if(personalState.getImage3ID()!=null){
                        urls.add(personalState.getImage3ID());
                    }
                    seePicture(urls,1);
                }
                break;
            case R.id.comment_state_image3:
                if(personalState.getImage3ID()!=null){
                    ArrayList<String>urls=new ArrayList<>();
                    urls.add(personalState.getImage1ID());
                    urls.add(personalState.getImage2ID());
                    urls.add(personalState.getImage3ID());
                    seePicture(urls,2);
                }
                break;
            default:
                Log.e("comment", "error" );
                break;
        }
    }

    public void seePicture(ArrayList<String> url,int position){
        new PhotoPagerConfig.Builder(CommentActivity.this).setBigImageUrls(url).setSavaImage(true).setSaveImageLocalPath(MyApplication.getStorePath()).setPosition(position).setOpenDownAnimate(true).build();
    }

    public void UpdateRecycleView(Comment comment){
        adapter.CommentAdd(0,comment);
        adapter.notifyItemInserted(0);
        commentRecyclerView.scrollToPosition(0);
        personalState.setComment(personalState.getComment()+1);
        TextView comment_num=(TextView)findViewById(R.id.comment_state_comment);
        comment_num.setText(String.valueOf(personalState.getComment()));
        PersonalStateDao personalStateDao=MyApplication.getDao().getPersonalStateDao();
        if(personalState.getId()!=null){
            personalStateDao.update(personalState);
        }
        Log.e("comment", new Gson().toJson(personalState));
    }
    public void LoadComment(List<Comment>comments){
        if(comments==null){
            comments=new ArrayList<Comment>();
        }
        adapter.setmCommentList(comments);
        adapter.notifyItemInserted(0);
        commentRecyclerView.scrollToPosition(0);
        progressLayout.setVisibility(View.GONE);
    }
}
