package com.example.dell.chat.view;

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
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.awen.photo.photopick.controller.PhotoPagerConfig;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.dell.chat.R;
import com.example.dell.chat.bean.Album;
import com.example.dell.chat.bean.MyApplication;
import com.example.dell.chat.bean.PersonalState;
import com.example.dell.chat.presenter.AlbumPresenter;
import com.example.dell.chat.base.BaseActivity;
import com.example.dell.chat.presenter.MessagePresenter;

import java.util.ArrayList;
import java.util.List;

//个人资料 相册Activity
public class AlbumActivity extends BaseActivity<AlbumActivity,AlbumPresenter<AlbumActivity>> {
    private int user_id ;
    private String nickname;
    private String profile;
    private AlbumAdapter adapter;
    private RecyclerView albumRecyclerView;
    private LinearLayoutManager layoutManager;
    private LayoutAnimationController controller;
    private LinearLayout progressLayout;
    private String url="http://119.23.255.222/android";
    private RequestOptions requestOptions=new RequestOptions().centerCrop();
    private List<PersonalState> pers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album);
        final Intent intent=getIntent();
        user_id=intent.getIntExtra("user_id",MyApplication.getUser().getUser_id());
        nickname=intent.getStringExtra("nickname");
        profile=intent.getStringExtra("profileID");
        Log.e("int", profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        //初始化顶部简介
        //初始化头像
        ImageView imageView_profile=(ImageView)findViewById(R.id.album_profile);
        Log.e("album","show head image");
        Glide.with(AlbumActivity.this).load(profile).into(imageView_profile);
        imageView_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<String>list=new ArrayList<>();
                list.add(intent.getStringExtra("profileID"));
                seePicture(list,0);
            }
        });
        //imageView_profile.setImageResource(R.drawable.profile);
        //初始化昵称
        TextView textView_nickname=(TextView)findViewById(R.id.album_nickname);
        textView_nickname.setText(intent.getStringExtra("nickname"));
        textView_nickname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //跳转到与该用户聊天
                MessagePresenter presenter = new MessagePresenter(MyApplication.getFrag());
                presenter.create(user_id);
                //传入头像 昵称 联系人id 传值到聊天界面
                Intent intent=new Intent(view.getContext(),ChatActivity.class);
                intent.putExtra("nickname",nickname);
                intent.putExtra("profile",profile);
                intent.putExtra("contact_id",user_id);
                startActivity(intent);
            }
        });
        //初始化学校
        TextView textView_school=(TextView)findViewById(R.id.album_school);
        textView_school.setText(intent.getStringExtra("school"));
        //初始化简介
        TextView textView_pre=(TextView)findViewById(R.id.album_pre);
        textView_pre.setText(intent.getStringExtra("introduction"));

        progressLayout=(LinearLayout)findViewById(R.id.album_progress);
        progressLayout.setVisibility(View.VISIBLE);

        albumRecyclerView=(RecyclerView)findViewById(R.id.album_recycler_view);
        albumRecyclerView.setNestedScrollingEnabled(false);
        layoutManager=new LinearLayoutManager(this);
        albumRecyclerView.setLayoutManager(layoutManager);
        adapter=new AlbumAdapter(new ArrayList<Album>());    //通过getAlbum()初始化list
        albumRecyclerView.setAdapter(adapter);

        controller = AnimationUtils.loadLayoutAnimation(this, R.anim.layout_animation_slide_bottom);


        presenter.LoadAlbum(user_id);
        /*
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {//设置延时看动画效果  模拟实际运行时使用的时间  正式使用时去掉handler
                albumRecyclerView.setLayoutAnimation(controller);   //recyclerview初始化动画
                albumRecyclerView.setAdapter(adapter);              //recyclerview初始化数据
                progressLayout.setVisibility(View.GONE);
            }
        }, 1000);
        */

        /*
        //底部刷新函数，将底部需要刷新的逻辑放入此函数
        NestedScrollView nestedScrollView=(NestedScrollView)findViewById(R.id.album_scroll_view);
        nestedScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (scrollY == (v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight())) {
                    //底部刷新
                    if(progressLayout.getVisibility()==View.GONE){
                        progressLayout.setVisibility(View.VISIBLE);
                    }
                    final Album album=new Album();
                    album.setContent("好想带只回家的！真想知道怎么养这种小猫！");
                    album.setLocation("广州·华南理工大学");
                    album.setImage1ID(R.drawable.sample1);
                    album.setImage2ID(R.drawable.sample2);
                    album.setImage3ID(R.drawable.sample3);
                    album.setImg_type(2);
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            adapter.AlbumAdd(adapter.getItemCount(),album);
                            adapter.notifyItemInserted(adapter.getItemCount());
                            progressLayout.setVisibility(View.GONE);
                        }
                    }, 1000);
                }
            }
        });
        */

    }

    /*
    private List<Album> getAlbum(){     //初始化recyclerview使用的list
        List<Album> albumList=new ArrayList<>();
        for(int i=1;i<=10;i++){
            Album album=new Album();
            album.setContent("超级喜欢这种类型的小猫！好想带"+i+"只回家的！真想知道怎么养这种小猫！");
            album.setLocation("广州·华南理工大学");
            if((i-1)%3==0){
                album.setImage1ID(R.drawable.sample1);
                album.setImg_type(0);
            }else if((i-1)%3==1){
                album.setImage1ID(R.drawable.sample1);
                album.setImage2ID(R.drawable.sample2);
                album.setImg_type(1);
            }else{
                album.setImage1ID(R.drawable.sample1);
                album.setImage2ID(R.drawable.sample2);
                album.setImage3ID(R.drawable.sample3);
                album.setImg_type(2);
            }
            albumList.add(album);
        }
        return albumList;
    }
    */

    //recyclerview的adapter定义
    class AlbumAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
        private List<Album> mAlbumList;
        private static final int TYPE_ONE_IMAGE=0;
        private static final int TYPE_TWO_IMAGE=1;
        private static final int TYPE_THREE_IMAGE=2;

        public List<Album> getmAlbumList() {
            return mAlbumList;
        }

        public void setmAlbumList(List<Album> mAlbumList) {
            this.mAlbumList = mAlbumList;
        }

        public AlbumAdapter(List<Album> albumList){
            mAlbumList=albumList;
        }

        public void AlbumAdd(int position,Album album){
            mAlbumList.add(position,album);
            return;
        }

        public void StateRemove(int position){
            mAlbumList.remove(position);
            return;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
            final View view;
            final RecyclerView.ViewHolder holder;

            if(viewType==TYPE_ONE_IMAGE){
                view=LayoutInflater.from(parent.getContext()).inflate(R.layout.album_item_one,parent,false);
                holder=new OneViewHolder(view);
                ((OneViewHolder)holder).linearLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int position=((OneViewHolder)holder).getAdapterPosition();
                        Intent intent=new Intent(AlbumActivity.this,CommentActivity.class);
                        intent.putExtra("personalstate",pers.get(position));
                        startActivity(intent);
                    }
                });
                ((OneViewHolder)holder).albumImage1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int position=((OneViewHolder)holder).getAdapterPosition();
                         Album album=mAlbumList.get(position);
                         if(album.getImage1ID()!=null){
                             ArrayList<String> urls=new ArrayList<>();
                             urls.add(album.getImage1ID());
                             seePicture(urls,0);
                         }
                    }
                });
            }else if(viewType==TYPE_TWO_IMAGE){
                view=LayoutInflater.from(parent.getContext()).inflate(R.layout.album_item_two,parent,false);
                holder=new TwoViewHolder(view);
                ((TwoViewHolder)holder).linearLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int position=((TwoViewHolder)holder).getAdapterPosition();
                        Intent intent=new Intent(AlbumActivity.this,CommentActivity.class);
                        intent.putExtra("personalstate",pers.get(position));
                        startActivity(intent);
                    }
                });
                ((TwoViewHolder)holder).albumImage1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int position=((TwoViewHolder)holder).getAdapterPosition();
                        final Album album=mAlbumList.get(position);
                        ArrayList<String> urls=new ArrayList<>();
                        urls.add(album.getImage1ID());
                        urls.add(album.getImage2ID());
                        seePicture(urls,0);
                    }
                });
                ((TwoViewHolder)holder).albumImage2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int position=((TwoViewHolder)holder).getAdapterPosition();
                        final Album album=mAlbumList.get(position);
                        ArrayList<String> urls=new ArrayList<>();
                        urls.add(album.getImage1ID());
                        urls.add(album.getImage2ID());
                        seePicture(urls,1);
                    }
                });
            }else{
                view=LayoutInflater.from(parent.getContext()).inflate(R.layout.album_item,parent,false);
                holder=new ViewHolder(view);
                ((ViewHolder)holder).linearLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int position=((ViewHolder)holder).getAdapterPosition();
                        Intent intent=new Intent(AlbumActivity.this,CommentActivity.class);
                        intent.putExtra("personalstate",pers.get(position));
                        startActivity(intent);
                    }
                });
                ((ViewHolder)holder).albumImage1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int position=((ViewHolder)holder).getAdapterPosition();
                        final Album album=mAlbumList.get(position);
                        ArrayList<String> urls=new ArrayList<>();
                        urls.add(album.getImage1ID());
                        urls.add(album.getImage2ID());
                        urls.add(album.getImage3ID());
                        seePicture(urls,0);
                    }
                });
                ((ViewHolder)holder).albumImage2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int position=((ViewHolder)holder).getAdapterPosition();
                        final Album album=mAlbumList.get(position);
                        ArrayList<String> urls=new ArrayList<>();
                        urls.add(album.getImage1ID());
                        urls.add(album.getImage2ID());
                        urls.add(album.getImage3ID());
                        seePicture(urls,1);
                    }
                });
                ((ViewHolder)holder).albumImage3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int position=((ViewHolder)holder).getAdapterPosition();
                        final Album album=mAlbumList.get(position);
                        ArrayList<String> urls=new ArrayList<>();
                        urls.add(album.getImage1ID());
                        urls.add(album.getImage2ID());
                        urls.add(album.getImage3ID());
                        seePicture(urls,2);
                    }
                });
            }

            return holder;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position){
            if(holder instanceof OneViewHolder){
                Album album=mAlbumList.get(position);
                ((OneViewHolder)holder).albumContent.setText(album.getContent());
                ((OneViewHolder)holder).albumLocation.setText(album.getLocation());
                if(album.getImage1ID()!=null){
                    Glide.with(AlbumActivity.this).load(album.getImage1ID()).apply(requestOptions).into(((OneViewHolder)holder).albumImage1);
                }
                //((OneViewHolder)holder).albumImage1.setImageResource(album.getImage1ID());
            }else if(holder instanceof TwoViewHolder){
                Album album=mAlbumList.get(position);
                ((TwoViewHolder)holder).albumContent.setText(album.getContent());
                ((TwoViewHolder)holder).albumLocation.setText(album.getLocation());
                if(album.getImage1ID()!=null){
                    Glide.with(AlbumActivity.this).load(album.getImage1ID()).apply(requestOptions).into(((TwoViewHolder)holder).albumImage1);
                }
                if(album.getImage2ID()!=null){
                    Glide.with(AlbumActivity.this).load(album.getImage2ID()).apply(requestOptions).apply(requestOptions).into(((TwoViewHolder)holder).albumImage2);
                }
                //((TwoViewHolder)holder).albumImage1.setImageResource(album.getImage1ID());
                //((TwoViewHolder)holder).albumImage2.setImageResource(album.getImage2ID());
            }else{
                Album album=mAlbumList.get(position);
                ((ViewHolder)holder).albumContent.setText(album.getContent());
                ((ViewHolder)holder).albumLocation.setText(album.getLocation());
                if(album.getImage1ID()!=null){
                    Glide.with(AlbumActivity.this).load(album.getImage1ID()).apply(requestOptions).into(((ViewHolder)holder).albumImage1);
                }
                if(album.getImage2ID()!=null){
                    Glide.with(AlbumActivity.this).load(album.getImage2ID()).apply(requestOptions).into(((ViewHolder)holder).albumImage2);
                }
                if(album.getImage3ID()!=null){
                    Glide.with(AlbumActivity.this).load(album.getImage3ID()).apply(requestOptions).into(((ViewHolder)holder).albumImage3);
                }
                //((ViewHolder)holder).albumImage1.setImageResource(album.getImage1ID());
                //((ViewHolder)holder).albumImage2.setImageResource(album.getImage2ID());
                //((ViewHolder)holder).albumImage3.setImageResource(album.getImage3ID());
            }
        }

        @Override
        public int getItemViewType(int position){
            Album album=mAlbumList.get(position);
            if(album.getImg_type()==TYPE_ONE_IMAGE){
                return TYPE_ONE_IMAGE;
            }else if(album.getImg_type()==TYPE_TWO_IMAGE){
                return TYPE_TWO_IMAGE;
            }else{
                return TYPE_THREE_IMAGE;
            }
        }

        @Override
        public int getItemCount(){
            return mAlbumList.size();
        }

        class OneViewHolder extends RecyclerView.ViewHolder{
            TextView albumContent;
            TextView albumLocation;
            ImageView albumImage1;
            LinearLayout linearLayout;

            public OneViewHolder(View view){
                super(view);
                albumContent=(TextView)view.findViewById(R.id.album_content);
                albumLocation=(TextView)view.findViewById(R.id.album_location);
                albumImage1=(ImageView)view.findViewById(R.id.album_image1);
                linearLayout=(LinearLayout)view.findViewById(R.id.alone);
            }
        }

        class TwoViewHolder extends RecyclerView.ViewHolder{
            TextView albumContent;
            TextView albumLocation;
            ImageView albumImage1;
            ImageView albumImage2;
            LinearLayout linearLayout;

            public TwoViewHolder(View view){
                super(view);
                albumContent=(TextView)view.findViewById(R.id.album_content);
                albumLocation=(TextView)view.findViewById(R.id.album_location);
                albumImage1=(ImageView)view.findViewById(R.id.album_image1);
                albumImage2=(ImageView)view.findViewById(R.id.album_image2);
                linearLayout=(LinearLayout)view.findViewById(R.id.altwo);
            }
        }

        class ViewHolder extends RecyclerView.ViewHolder{
            TextView albumContent;
            TextView albumLocation;
            ImageView albumImage1;
            ImageView albumImage2;
            ImageView albumImage3;
            LinearLayout linearLayout;

            public ViewHolder(View view){
                super(view);
                albumContent=(TextView)view.findViewById(R.id.album_content);
                albumLocation=(TextView)view.findViewById(R.id.album_location);
                albumImage1=(ImageView)view.findViewById(R.id.album_image1);
                albumImage2=(ImageView)view.findViewById(R.id.album_image2);
                albumImage3=(ImageView)view.findViewById(R.id.album_image3);
                linearLayout=(LinearLayout)view.findViewById(R.id.althree);
            }
        }
    }
    @Override
    protected AlbumPresenter createPresenter() {
        return new AlbumPresenter();
    }

    public void LoadPersonalState(List<PersonalState>personalStates){
        pers=personalStates;
        List<Album>albums=new ArrayList<>();
        for(PersonalState personalState:personalStates){
            Album album=new Album();
            album.setContent(personalState.getContent());
            album.setLocation(personalState.getLocation());
            album.setImg_type(personalState.getImg_type());
            if(personalState.getImage1ID()!=null){
                album.setImage1ID(url+personalState.getImage1ID().substring(1));
                personalState.setImage1ID(url+personalState.getImage1ID().substring(1));
                album.setImg_type(0);
            }
            if(personalState.getImage2ID()!=null){
                album.setImage2ID(url+personalState.getImage2ID().substring(1));
                personalState.setImage2ID(url+personalState.getImage2ID().substring(1));
                album.setImg_type(1);
            }
            if(personalState.getImage3ID()!=null){
                album.setImage3ID(url+personalState.getImage3ID().substring(1));
                personalState.setImage3ID(url+personalState.getImage3ID().substring(1));
                album.setImg_type(2);
            }
            album.setAlbum_id(personalState.getPersonalstate_id());
            album.setTime(personalState.getState_time());
            albums.add(album);
        }
        adapter.getmAlbumList().addAll(0,albums);
        adapter.notifyDataSetChanged();
        albumRecyclerView.setLayoutAnimation(controller);
        progressLayout.setVisibility(View.GONE);
    }
    public void seePicture(ArrayList<String> url,int position){
        new PhotoPagerConfig.Builder(AlbumActivity.this).setBigImageUrls(url).setSavaImage(true).setSaveImageLocalPath(MyApplication.getStorePath()).setPosition(position).setOpenDownAnimate(true).build();
    }
}
