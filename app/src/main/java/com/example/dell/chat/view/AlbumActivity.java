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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dell.chat.R;
import com.example.dell.chat.bean.Album;

import java.util.ArrayList;
import java.util.List;

//个人资料 相册Activity
public class AlbumActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album);
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
        imageView_profile.setImageResource(R.drawable.profile);
        //初始化昵称
        TextView textView_nickname=(TextView)findViewById(R.id.album_nickname);
        textView_nickname.setText("麦梓逗比旗");
        //初始化学校
        TextView textView_school=(TextView)findViewById(R.id.album_school);
        textView_school.setText("中山纪念小学");
        //初始化简介
        TextView textView_pre=(TextView)findViewById(R.id.album_pre);
        textView_pre.setText("我是在场所有人的爸爸啊，你们这群废物！");

        final LinearLayout progressLayout=(LinearLayout)findViewById(R.id.album_progress);
        progressLayout.setVisibility(View.VISIBLE);

        final RecyclerView albumRecyclerView=(RecyclerView)findViewById(R.id.album_recycler_view);
        albumRecyclerView.setNestedScrollingEnabled(false);
        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        albumRecyclerView.setLayoutManager(layoutManager);
        final AlbumAdapter adapter=new AlbumAdapter(getAlbum());    //通过getAlbum()初始化list

        final LayoutAnimationController controller = AnimationUtils.loadLayoutAnimation(this, R.anim.layout_animation_slide_bottom);

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {//设置延时看动画效果  模拟实际运行时使用的时间  正式使用时去掉handler
                albumRecyclerView.setLayoutAnimation(controller);   //recyclerview初始化动画
                albumRecyclerView.setAdapter(adapter);              //recyclerview初始化数据
                progressLayout.setVisibility(View.GONE);
            }
        }, 1000);

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

    }

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

    //recyclerview的adapter定义
    class AlbumAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
        private List<Album> mAlbumList;
        private static final int TYPE_ONE_IMAGE=0;
        private static final int TYPE_TWO_IMAGE=1;
        private static final int TYPE_THREE_IMAGE=2;

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
            }else if(viewType==TYPE_TWO_IMAGE){
                view=LayoutInflater.from(parent.getContext()).inflate(R.layout.album_item_two,parent,false);
                holder=new TwoViewHolder(view);
            }else{
                view=LayoutInflater.from(parent.getContext()).inflate(R.layout.album_item,parent,false);
                holder=new ViewHolder(view);
            }

            return holder;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position){
            if(holder instanceof OneViewHolder){
                Album album=mAlbumList.get(position);
                ((OneViewHolder)holder).albumContent.setText(album.getContent());
                ((OneViewHolder)holder).albumLocation.setText(album.getLocation());
                ((OneViewHolder)holder).albumImage1.setImageResource(album.getImage1ID());
            }else if(holder instanceof TwoViewHolder){
                Album album=mAlbumList.get(position);
                ((TwoViewHolder)holder).albumContent.setText(album.getContent());
                ((TwoViewHolder)holder).albumLocation.setText(album.getLocation());
                ((TwoViewHolder)holder).albumImage1.setImageResource(album.getImage1ID());
                ((TwoViewHolder)holder).albumImage2.setImageResource(album.getImage2ID());
            }else{
                Album album=mAlbumList.get(position);
                ((ViewHolder)holder).albumContent.setText(album.getContent());
                ((ViewHolder)holder).albumLocation.setText(album.getLocation());
                ((ViewHolder)holder).albumImage1.setImageResource(album.getImage1ID());
                ((ViewHolder)holder).albumImage2.setImageResource(album.getImage2ID());
                ((ViewHolder)holder).albumImage3.setImageResource(album.getImage3ID());
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

            public OneViewHolder(View view){
                super(view);
                albumContent=(TextView)view.findViewById(R.id.album_content);
                albumLocation=(TextView)view.findViewById(R.id.album_location);
                albumImage1=(ImageView)view.findViewById(R.id.album_image1);
            }
        }

        class TwoViewHolder extends RecyclerView.ViewHolder{
            TextView albumContent;
            TextView albumLocation;
            ImageView albumImage1;
            ImageView albumImage2;

            public TwoViewHolder(View view){
                super(view);
                albumContent=(TextView)view.findViewById(R.id.album_content);
                albumLocation=(TextView)view.findViewById(R.id.album_location);
                albumImage1=(ImageView)view.findViewById(R.id.album_image1);
                albumImage2=(ImageView)view.findViewById(R.id.album_image2);
            }
        }

        class ViewHolder extends RecyclerView.ViewHolder{
            TextView albumContent;
            TextView albumLocation;
            ImageView albumImage1;
            ImageView albumImage2;
            ImageView albumImage3;

            public ViewHolder(View view){
                super(view);
                albumContent=(TextView)view.findViewById(R.id.album_content);
                albumLocation=(TextView)view.findViewById(R.id.album_location);
                albumImage1=(ImageView)view.findViewById(R.id.album_image1);
                albumImage2=(ImageView)view.findViewById(R.id.album_image2);
                albumImage3=(ImageView)view.findViewById(R.id.album_image3);
            }
        }
    }
}
