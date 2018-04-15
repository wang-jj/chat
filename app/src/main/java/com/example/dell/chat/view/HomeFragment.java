package com.example.dell.chat.view;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dell.chat.R;
import com.example.dell.chat.bean.PersonalState;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Thread.sleep;

/**
 * Created by Staroul on 2018/3/30.
 */

//动态的fragment
public class HomeFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        final View view=inflater.inflate(R.layout.home_fragment,container,false);

        final LinearLayout progressLayout=(LinearLayout)view.findViewById(R.id.state_progress);
        progressLayout.setVisibility(View.VISIBLE);

        final LayoutAnimationController controller = AnimationUtils.loadLayoutAnimation(getActivity(), R.anim.layout_animation_fall_down);

        final RecyclerView stateRecyclerView=(RecyclerView)view.findViewById(R.id.state_recycler_view);
        stateRecyclerView.setNestedScrollingEnabled(false);
        LinearLayoutManager layoutManager=new LinearLayoutManager(getActivity());

        stateRecyclerView.setLayoutManager(layoutManager);

        //设置适配器以及list用以显示数据
        final StateAdapter adapter=new StateAdapter(getState());    //通过getState()初始化adapter

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {//设置延时看动画效果  模拟实际运行时使用的时间  正式使用时去掉handler
                stateRecyclerView.setLayoutAnimation(controller);   //recyclerview动画
                stateRecyclerView.setAdapter(adapter);              //recyclerview初始化
                progressLayout.setVisibility(View.GONE);
            }
        }, 1000);

        //底部刷新函数 底部刷新时逻辑写在此函数
        final NestedScrollView nestedScrollView=(NestedScrollView)view.findViewById(R.id.home_scroll_view);
        nestedScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, final int scrollX, final int scrollY, int oldScrollX, int oldScrollY) {
                if (scrollY == (v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight())) {
                    //底部刷新
                    if(progressLayout.getVisibility()==View.GONE){
                        progressLayout.setVisibility(View.VISIBLE);
                    }
                    final PersonalState personalState=new PersonalState();
                    personalState.setNickname("麦梓逗比旗");
                    personalState.setSchool("华南理工大学");
                    personalState.setContent("超级喜欢这种类型的小猫！好想带只回家的！真想知道怎么养这种小猫！");
                    personalState.setLocation("广州·华南理工大学");
                    personalState.setLike(233);
                    personalState.setComment(666);
                    personalState.setProfileID(R.drawable.profile);
                    personalState.setImage1ID(R.drawable.sample1);
                    personalState.setImage2ID(R.drawable.sample2);
                    personalState.setImg_type(1);
                    personalState.setPictureID(R.drawable.like);
                    //personalState.setState_time("09:22");
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            //Toast.makeText(getActivity(),String.valueOf(adapter.getItemCount()),Toast.LENGTH_SHORT).show();
                            adapter.StateAdd(adapter.getItemCount(),personalState);
                            //Toast.makeText(getActivity(),String.valueOf(adapter.getItemCount()),Toast.LENGTH_SHORT).show();
                            adapter.notifyItemInserted(adapter.getItemCount());
                            progressLayout.setVisibility(View.GONE);
                        }
                    }, 1000);
                }
            }
        });

        //顶部刷新函数 顶部刷新的逻辑写在此函数
        final SwipeRefreshLayout swipeRefreshLayout=(SwipeRefreshLayout)view.findViewById(R.id.swipe_state_recycler);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {   //顶部刷新
                PersonalState personalState=new PersonalState();
                personalState.setNickname("麦梓逗比旗");
                personalState.setSchool("华南理工大学");
                personalState.setContent("超级喜欢这种类型的小猫！好想带只回家的！真想知道怎么养这种小猫！");
                personalState.setLocation("广州·华南理工大学");
                personalState.setLike(233);
                personalState.setComment(666);
                personalState.setProfileID(R.drawable.profile);
                personalState.setImage1ID(R.drawable.sample1);
                personalState.setImg_type(0);
                personalState.setPictureID(R.drawable.like);
                //personalState.setState_time("09:22");
                adapter.StateAdd(0,personalState);
                adapter.notifyItemInserted(0);
                stateRecyclerView.scrollToPosition(0);
                swipeRefreshLayout.setRefreshing(false);
            }
        });
        swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorWhite));
        swipeRefreshLayout.setProgressBackgroundColorSchemeColor(getResources().getColor(R.color.colorPrimary));

        return view;
    }

    //初始化测试数据
    private List<PersonalState> getState(){
        List<PersonalState> stateList=new ArrayList<>();
        for(int i=1;i<=5;i++){
            PersonalState personalState=new PersonalState();
            //记得set上用户ID 用以跳转
            personalState.setNickname("麦梓逗比旗"+i);
            personalState.setSchool("华南理工大学");
            personalState.setContent("超级喜欢这种类型的小猫！好想带"+i+"只回家的！真想知道怎么养这种小猫！");
            personalState.setLocation("广州·华南理工大学");
            personalState.setLike(233);
            personalState.setComment(666);
            personalState.setProfileID(R.drawable.profile);
            if((i-1)%3==0){
                personalState.setImage1ID(R.drawable.sample1);
                personalState.setImg_type(0);
            }else if((i-1)%3==1){
                personalState.setImage1ID(R.drawable.sample1);
                personalState.setImage2ID(R.drawable.sample2);
                personalState.setImg_type(1);
            }else{
                personalState.setImage1ID(R.drawable.sample1);
                personalState.setImage2ID(R.drawable.sample2);
                personalState.setImage3ID(R.drawable.sample3);
                personalState.setImg_type(2);
            }
            personalState.setPictureID(R.drawable.like);
            //personalState.setState_time("09:22");
            stateList.add(personalState);   //添加入stateList中
        }
        return stateList;
    }

    //动态内容RecyclerView Adapter定义
    class StateAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
        private List<PersonalState> mStateList;
        private static final int TYPE_ONE_IMAGE=0;
        private static final int TYPE_TWO_IMAGE=1;
        private static final int TYPE_THREE_IMAGE=2;

        public StateAdapter(List<PersonalState> stateList){
            mStateList=stateList;
        }

        public void StateAdd(int position,PersonalState personalState){
            mStateList.add(position,personalState);
            return;
        }

        public void StateRemove(int position){
            mStateList.remove(position);
            return;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){

            final View view;
            final RecyclerView.ViewHolder holder;

            if(viewType==TYPE_ONE_IMAGE){   //一张图片时的布局
                view=LayoutInflater.from(parent.getContext()).inflate(R.layout.state_item_one,parent,false);
                holder=new OneViewHolder(view);
                ((OneViewHolder)holder).stateNickName.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v){
                        int position=((OneViewHolder)holder).getAdapterPosition();
                        PersonalState personalState=mStateList.get(position);
                        //跳转个人资料activity
                        Intent intent=new Intent(v.getContext(),AlbumActivity.class);
                        startActivity(intent);
                    }
                });
                ((OneViewHolder)holder).stateLikeLinear.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int position=((OneViewHolder)holder).getAdapterPosition();
                        PersonalState personalState=mStateList.get(position);
                        //点赞
                        ((OneViewHolder)holder).stateLikePicture.setImageResource(R.drawable.like_checked);
                    }
                });
                ((OneViewHolder)holder).stateCommentLinear.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //跳转动态详情activity
                        Intent intent=new Intent(v.getContext(),CommentActivity.class);
                        startActivity(intent);
                    }
                });
                ((OneViewHolder)holder).stateLinear.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent=new Intent(v.getContext(),CommentActivity.class);
                        startActivity(intent);
                    }
                });
            }else if(viewType==TYPE_TWO_IMAGE){ //两张图片时的布局
                view=LayoutInflater.from(parent.getContext()).inflate(R.layout.state_item_two,parent,false);
                holder=new TwoViewHolder(view);
                ((TwoViewHolder)holder).stateNickName.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v){
                        int position=((TwoViewHolder)holder).getAdapterPosition();
                        PersonalState personalState=mStateList.get(position);
                        //跳转个人资料activity
                        Intent intent=new Intent(v.getContext(),AlbumActivity.class);
                        startActivity(intent);
                    }
                });
                ((TwoViewHolder)holder).stateLikeLinear.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int position=((TwoViewHolder)holder).getAdapterPosition();
                        PersonalState personalState=mStateList.get(position);
                        //点赞
                        ((TwoViewHolder)holder).stateLikePicture.setImageResource(R.drawable.like_checked);
                    }
                });
                ((TwoViewHolder)holder).stateCommentLinear.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //跳转动态详情activity
                        Intent intent=new Intent(v.getContext(),CommentActivity.class);
                        startActivity(intent);
                    }
                });
                ((TwoViewHolder)holder).stateLinear.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent=new Intent(v.getContext(),CommentActivity.class);
                        startActivity(intent);
                    }
                });
            }else { //三张图片时的布局
                view=LayoutInflater.from(parent.getContext()).inflate(R.layout.state_item,parent,false);
                holder=new ViewHolder(view);
                ((ViewHolder)holder).stateNickName.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v){
                        int position=((ViewHolder)holder).getAdapterPosition();
                        PersonalState personalState=mStateList.get(position);
                        //跳转个人资料activity
                        Intent intent=new Intent(v.getContext(),AlbumActivity.class);
                        startActivity(intent);
                    }
                });
                ((ViewHolder)holder).stateLikeLinear.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int position=((ViewHolder)holder).getAdapterPosition();
                        PersonalState personalState=mStateList.get(position);
                        ((ViewHolder)holder).stateLikePicture.setImageResource(R.drawable.like_checked);
                    }
                });
                ((ViewHolder)holder).stateCommentLinear.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent=new Intent(v.getContext(),CommentActivity.class);
                        startActivity(intent);
                    }
                });
                ((ViewHolder)holder).stateLinear.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent=new Intent(v.getContext(),CommentActivity.class);
                        startActivity(intent);
                    }
                });
            }
            return holder;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder,int position){
            if(holder instanceof OneViewHolder){
                PersonalState personalState=mStateList.get(position);
                ((OneViewHolder)holder).stateNickName.setText(personalState.getNickname());
                ((OneViewHolder)holder).stateSchool.setText(personalState.getSchool());
                ((OneViewHolder)holder).stateContent.setText(personalState.getContent());
                ((OneViewHolder)holder).stateLocation.setText(personalState.getLocation());
                //((OneViewHolder)holder).stateTime.setText(personalState.getState_time());
                ((OneViewHolder)holder).stateLike.setText(String.valueOf(personalState.getLike()));      //用以将int类型转换成String类型使用setText显示
                ((OneViewHolder)holder).stateComment.setText(String.valueOf(personalState.getComment()));
                //stateProfile 就是一个ImageView
                //Glide.with(getActivity()).load(personalState.getProfileID()).into(((OneViewHolder)holder).stateProfile);
                ((OneViewHolder)holder).stateProfile.setImageResource(personalState.getProfileID());
                ((OneViewHolder)holder).stateImage1.setImageResource(personalState.getImage1ID());
                ((OneViewHolder)holder).stateLikePicture.setImageResource(personalState.getPictureID());
            }else if(holder instanceof TwoViewHolder){
                PersonalState personalState=mStateList.get(position);
                ((TwoViewHolder)holder).stateNickName.setText(personalState.getNickname());
                ((TwoViewHolder)holder).stateSchool.setText(personalState.getSchool());
                ((TwoViewHolder)holder).stateContent.setText(personalState.getContent());
                ((TwoViewHolder)holder).stateLocation.setText(personalState.getLocation());
                //((TwoViewHolder)holder).stateTime.setText(personalState.getState_time());
                ((TwoViewHolder)holder).stateLike.setText(String.valueOf(personalState.getLike()));      //用以将int类型转换成String类型使用setText显示
                ((TwoViewHolder)holder).stateComment.setText(String.valueOf(personalState.getComment()));
                ((TwoViewHolder)holder).stateProfile.setImageResource(personalState.getProfileID());
                ((TwoViewHolder)holder).stateImage1.setImageResource(personalState.getImage1ID());
                ((TwoViewHolder)holder).stateImage2.setImageResource(personalState.getImage2ID());
                ((TwoViewHolder)holder).stateLikePicture.setImageResource(personalState.getPictureID());
            }else{
                PersonalState personalState=mStateList.get(position);
                ((ViewHolder)holder).stateNickName.setText(personalState.getNickname());
                ((ViewHolder)holder).stateSchool.setText(personalState.getSchool());
                ((ViewHolder)holder).stateContent.setText(personalState.getContent());
                ((ViewHolder)holder).stateLocation.setText(personalState.getLocation());
                //((ViewHolder)holder).stateTime.setText(personalState.getState_time());
                ((ViewHolder)holder).stateLike.setText(String.valueOf(personalState.getLike()));      //用以将int类型转换成String类型使用setText显示
                ((ViewHolder)holder).stateComment.setText(String.valueOf(personalState.getComment()));
                ((ViewHolder)holder).stateProfile.setImageResource(personalState.getProfileID());
                ((ViewHolder)holder).stateImage1.setImageResource(personalState.getImage1ID());
                ((ViewHolder)holder).stateImage2.setImageResource(personalState.getImage2ID());
                ((ViewHolder)holder).stateImage3.setImageResource(personalState.getImage3ID());
                ((ViewHolder)holder).stateLikePicture.setImageResource(personalState.getPictureID());
            }
        }

        @Override
        public int getItemViewType(int position){
            PersonalState personalState=mStateList.get(position);
            if(personalState.getImg_type()==TYPE_ONE_IMAGE){
                return TYPE_ONE_IMAGE;
            }else if(personalState.getImg_type()==TYPE_TWO_IMAGE){
                return TYPE_TWO_IMAGE;
            }else{
                return TYPE_THREE_IMAGE;
            }
        }

        @Override
        public int getItemCount(){
            return mStateList.size();
        }

        class OneViewHolder extends RecyclerView.ViewHolder{
            TextView stateNickName;
            TextView stateSchool;
            TextView stateContent;
            TextView stateLocation;
            TextView stateTime;
            TextView stateLike;     //点赞数 类型为int
            TextView stateComment;  //评论数 类型为int
            ImageView stateProfile; //头像 类型为int 之后按照后端需求修改 暂时引用drawable中的已有图片资源，即int
            ImageView stateImage1;
            ImageView stateLikePicture; //设置点赞的图片
            LinearLayout stateLikeLinear;
            LinearLayout stateCommentLinear;
            LinearLayout stateLinear;

            public OneViewHolder(View view){
                super(view);
                stateNickName=(TextView)view.findViewById(R.id.state_nickname);
                stateSchool=(TextView)view.findViewById(R.id.state_school);
                stateContent=(TextView)view.findViewById(R.id.state_content);
                stateLocation=(TextView)view.findViewById(R.id.state_location);
                stateTime=(TextView)view.findViewById(R.id.state_time);
                stateLike=(TextView)view.findViewById(R.id.state_like);
                stateComment=(TextView)view.findViewById(R.id.state_comment);
                stateProfile=(ImageView)view.findViewById(R.id.state_profile);
                stateImage1=(ImageView)view.findViewById(R.id.state_image1);
                stateLikePicture=(ImageView)view.findViewById(R.id.like_picture);
                stateLikeLinear=(LinearLayout)view.findViewById(R.id.like_linear);
                stateCommentLinear=(LinearLayout)view.findViewById(R.id.comment_linear);
                stateLinear=(LinearLayout)view.findViewById(R.id.state_linear);
            }
        }

        class TwoViewHolder extends RecyclerView.ViewHolder{
            TextView stateNickName;
            TextView stateSchool;
            TextView stateContent;
            TextView stateLocation;
            TextView stateTime;
            TextView stateLike;     //点赞数 类型为int
            TextView stateComment;  //评论数 类型为int
            ImageView stateProfile; //头像 类型为int 之后按照后端需求修改 暂时引用drawable中的已有图片资源，即int
            ImageView stateImage1;
            ImageView stateImage2;
            ImageView stateLikePicture; //设置点赞的图片
            LinearLayout stateLikeLinear;
            LinearLayout stateCommentLinear;
            LinearLayout stateLinear;

            public TwoViewHolder(View view){
                super(view);
                stateNickName=(TextView)view.findViewById(R.id.state_nickname);
                stateSchool=(TextView)view.findViewById(R.id.state_school);
                stateContent=(TextView)view.findViewById(R.id.state_content);
                stateLocation=(TextView)view.findViewById(R.id.state_location);
                stateTime=(TextView)view.findViewById(R.id.state_time);
                stateLike=(TextView)view.findViewById(R.id.state_like);
                stateComment=(TextView)view.findViewById(R.id.state_comment);
                stateProfile=(ImageView)view.findViewById(R.id.state_profile);
                stateImage1=(ImageView)view.findViewById(R.id.state_image1);
                stateImage2=(ImageView)view.findViewById(R.id.state_image2);
                stateLikePicture=(ImageView)view.findViewById(R.id.like_picture);
                stateLikeLinear=(LinearLayout)view.findViewById(R.id.like_linear);
                stateCommentLinear=(LinearLayout)view.findViewById(R.id.comment_linear);
                stateLinear=(LinearLayout)view.findViewById(R.id.state_linear);
            }
        }

        class ViewHolder extends RecyclerView.ViewHolder{
            TextView stateNickName;
            TextView stateSchool;
            TextView stateContent;
            TextView stateLocation;
            TextView stateTime;
            TextView stateLike;     //点赞数 类型为int
            TextView stateComment;  //评论数 类型为int
            ImageView stateProfile; //头像 类型为int 之后按照后端需求修改 暂时引用drawable中的已有图片资源，即int
            ImageView stateImage1;
            ImageView stateImage2;
            ImageView stateImage3;
            ImageView stateLikePicture; //设置点赞的图片
            LinearLayout stateLikeLinear;
            LinearLayout stateCommentLinear;
            LinearLayout stateLinear;

            public ViewHolder(View view){
                super(view);
                stateNickName=(TextView)view.findViewById(R.id.state_nickname);
                stateSchool=(TextView)view.findViewById(R.id.state_school);
                stateContent=(TextView)view.findViewById(R.id.state_content);
                stateLocation=(TextView)view.findViewById(R.id.state_location);
                stateTime=(TextView)view.findViewById(R.id.state_time);
                stateLike=(TextView)view.findViewById(R.id.state_like);
                stateComment=(TextView)view.findViewById(R.id.state_comment);
                stateProfile=(ImageView)view.findViewById(R.id.state_profile);
                stateImage1=(ImageView)view.findViewById(R.id.state_image1);
                stateImage2=(ImageView)view.findViewById(R.id.state_image2);
                stateImage3=(ImageView)view.findViewById(R.id.state_image3);
                stateLikePicture=(ImageView)view.findViewById(R.id.like_picture);
                stateLikeLinear=(LinearLayout)view.findViewById(R.id.like_linear);
                stateCommentLinear=(LinearLayout)view.findViewById(R.id.comment_linear);
                stateLinear=(LinearLayout)view.findViewById(R.id.state_linear);
            }
        }
    }
}