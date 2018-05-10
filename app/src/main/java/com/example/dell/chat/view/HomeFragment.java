package com.example.dell.chat.view;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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

import com.awen.photo.photopick.controller.PhotoPagerConfig;
import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.dell.chat.R;
import com.example.dell.chat.bean.MyApplication;
import com.example.dell.chat.bean.PersonalState;
import com.example.dell.chat.presenter.HomePresenter;
import com.example.dell.chat.tools.Dao;
import com.example.dell.chat.tools.Notify;
import com.example.dell.chat.tools.translate;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.security.auth.login.LoginException;

import static android.app.Activity.RESULT_OK;
import static java.lang.Thread.sleep;

/**
 * Created by Staroul on 2018/3/30.
 */

//动态的fragment
public class HomeFragment extends Fragment {

    private View view;
    private StateAdapter adapter;
    private RecyclerView stateRecyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private LinearLayout progressLayout;
    private HomePresenter homePresenter;
    private LayoutAnimationController controller;
    private RequestOptions requestOptions=new RequestOptions().centerCrop();
    private int pos=-1;
    private List<PersonalState>AllPersonalState=new ArrayList<>();
    private int state=0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        Log.e("HOME", "oncreate" );
        view=inflater.inflate(R.layout.home_fragment,container,false);
        homePresenter=new HomePresenter(this);
        progressLayout=(LinearLayout)view.findViewById(R.id.state_progress);
        progressLayout.setVisibility(View.VISIBLE);

        //动画
        //final LayoutAnimationController controller = AnimationUtils.loadLayoutAnimation(getActivity(), R.anim.layout_animation_fall_down);
        //final RecyclerView stateRecyclerView=(RecyclerView)view.findViewById(R.id.state_recycler_view);
        //stateRecyclerView.setLayoutAnimation(controller);
        //stateRecyclerView.setAdapter(adapter);

        controller = AnimationUtils.loadLayoutAnimation(getActivity(), R.anim.layout_animation_fall_down);


        stateRecyclerView=(RecyclerView)view.findViewById(R.id.state_recycler_view);
        stateRecyclerView.setNestedScrollingEnabled(false);
        LinearLayoutManager layoutManager=new LinearLayoutManager(getActivity());

        stateRecyclerView.setLayoutManager(layoutManager);

        //设置适配器以及list用以显示数据
        adapter=new StateAdapter(new ArrayList<PersonalState>());    //通过getState()初始化adapter

        final Handler handler = new Handler();
        /*
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {//设置延时看动画效果  模拟实际运行时使用的时间  正式使用时去掉handler
                stateRecyclerView.setLayoutAnimation(controller);   //recyclerview动画
                stateRecyclerView.setAdapter(adapter);              //recyclerview初始化
                progressLayout.setVisibility(View.GONE);
            }
        }, 1000);
        */


        //底部刷新函数 底部刷新时逻辑写在此函数
        final NestedScrollView nestedScrollView=(NestedScrollView)view.findViewById(R.id.home_scroll_view);
        nestedScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, final int scrollX, final int scrollY, int oldScrollX, int oldScrollY) {
                if (scrollY == (v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight())) {
                    progressLayout.setVisibility(View.VISIBLE);
                    for(int i=0;i<20&&state<AllPersonalState.size();i++){
                        adapter.getmStateList().add(AllPersonalState.get(state));
                        state++;
                    }
                    //底部刷新
                    Handler handler=new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            adapter.notifyDataSetChanged();
                            progressLayout.setVisibility(View.GONE);
                        }
                    }, 1000);
                    //progressLayout.setVisibility(View.GONE);
                    /*
                    final PersonalState personalState=new PersonalState();
                    personalState.setNickname("麦梓逗比旗");
                    personalState.setSchool("华南理工大学");
                    personalState.setContent("超级喜欢这种类型的小猫！好想带只回家的！真想知道怎么养这种小猫！");
                    personalState.setLocation("广州·华南理工大学");
                    personalState.setLike(233);
                    personalState.setComment(666);
                    personalState.setProfileID("https://cn.bing.com/s/hpb/NorthMale_EN-US8782628354_1920x1080.jpg");
                    personalState.setImage1ID("https://cn.bing.com/s/hpb/NorthMale_EN-US8782628354_1920x1080.jpg");
                    personalState.setImage2ID("https://cn.bing.com/s/hpb/NorthMale_EN-US8782628354_1920x1080.jpg");
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
                    */
                }
            }
        });

        //顶部刷新函数 顶部刷新的逻辑写在此函数
        swipeRefreshLayout=(SwipeRefreshLayout)view.findViewById(R.id.swipe_state_recycler);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {   //顶部刷新
                /*
                PersonalState personalState=new PersonalState();
                personalState.setNickname("麦梓逗比旗");
                personalState.setSchool("华南理工大学");
                personalState.setContent("超级喜欢这种类型的小猫！好想带只回家的！真想知道怎么养这种小猫！");
                personalState.setLocation("广州·华南理工大学");
                personalState.setLike(233);
                personalState.setComment(666);
                personalState.setProfileID("https://cn.bing.com/s/hpb/NorthMale_EN-US8782628354_1920x1080.jpg");
                personalState.setImage1ID("https://cn.bing.com/s/hpb/NorthMale_EN-US8782628354_1920x1080.jpg");
                personalState.setImg_type(0);
                personalState.setPictureID(R.drawable.like);
                //personalState.setState_time("09:22");
                adapter.StateAdd(0,personalState);
                adapter.notifyItemInserted(0);
                stateRecyclerView.scrollToPosition(0);
                swipeRefreshLayout.setRefreshing(false);
                */
                homePresenter.UpdateMoment();
            }
        });
        swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorWhite));
        swipeRefreshLayout.setProgressBackgroundColorSchemeColor(getResources().getColor(R.color.colorPrimary));
        homePresenter.LoadMoment();
        return view;
    }



    //动态内容RecyclerView Adapter定义
    public class StateAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
        private List<PersonalState> mStateList;
        private static final int TYPE_ONE_IMAGE=0;
        private static final int TYPE_TWO_IMAGE=1;
        private static final int TYPE_THREE_IMAGE=2;

        public void setmStateList(List<PersonalState> mStateList) {
            this.mStateList = mStateList;
        }

        public List<PersonalState> getmStateList() {
            return mStateList;
        }

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
                        pos=position;
                        PersonalState personalState=mStateList.get(position);
                        //跳转个人资料activity
                        Intent intent=new Intent(v.getContext(),AlbumActivity.class);
                        Dao.SetIntent(intent,personalState.getUser_id(),personalState.getProfileID(),personalState.getIntorduction(),personalState.getNickname(),personalState.getSchool());
                        startActivity(intent);
                    }
                });
                ((OneViewHolder)holder).stateProfile.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ((OneViewHolder)holder).stateNickName.performClick();
                    }
                });
                ((OneViewHolder)holder).stateLikeLinear.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int position=((OneViewHolder)holder).getAdapterPosition();
                        PersonalState personalState=mStateList.get(position);
                        pos=position;
                        //点赞
                        if(personalState.getPictureID()==0){
                            ((OneViewHolder)holder).stateLikePicture.setImageResource(R.drawable.like_checked);
                            personalState.setPictureID(1);
                            personalState.setLike(personalState.getLike()+1);
                            ((OneViewHolder)holder).stateLike.setText(String.valueOf(personalState.getLike()));
                            homePresenter.SendLike(personalState.getPersonalstate_id(),personalState);
                        }
                    }
                });
                ((OneViewHolder)holder).stateCommentLinear.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //跳转动态详情activity
                        int position=((OneViewHolder)holder).getAdapterPosition();
                        pos=position;
                        PersonalState personalState=mStateList.get(position);
                        Intent intent=new Intent(v.getContext(),CommentActivity.class);
                        intent.putExtra("personalstate",personalState);
                        startActivityForResult(intent,1);
                    }
                });
                ((OneViewHolder)holder).stateLinear.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int position=((OneViewHolder)holder).getAdapterPosition();
                        pos=position;
                        PersonalState personalState=mStateList.get(position);
                        Intent intent=new Intent(v.getContext(),CommentActivity.class);
                        intent.putExtra("personalstate",personalState);
                        startActivityForResult(intent,1);
                    }
                });
                ((OneViewHolder)holder).stateImage1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int position=((OneViewHolder)holder).getAdapterPosition();
                        PersonalState personalState=mStateList.get(position);
                        if(personalState.getImage1ID()!=null){
                            ArrayList<String> urls=new ArrayList<>();
                            urls.add(personalState.getImage1ID());
                            seePicture(urls,0);
                        }
                    }
                });
            }else if(viewType==TYPE_TWO_IMAGE){ //两张图片时的布局
                view=LayoutInflater.from(parent.getContext()).inflate(R.layout.state_item_two,parent,false);
                holder=new TwoViewHolder(view);
                ((TwoViewHolder)holder).stateNickName.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v){
                        int position=((TwoViewHolder)holder).getAdapterPosition();
                        pos=position;
                        PersonalState personalState=mStateList.get(position);
                        //跳转个人资料activity
                        Intent intent=new Intent(v.getContext(),AlbumActivity.class);
                        Dao.SetIntent(intent,personalState.getUser_id(),personalState.getProfileID(),personalState.getIntorduction(),personalState.getNickname(),personalState.getSchool());
                        startActivity(intent);
                    }
                });
                ((TwoViewHolder)holder).stateProfile.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ((TwoViewHolder)holder).stateNickName.performClick();
                    }
                });
                ((TwoViewHolder)holder).stateLikeLinear.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int position=((TwoViewHolder)holder).getAdapterPosition();
                        pos=position;
                        PersonalState personalState=mStateList.get(position);
                        //点赞
                        if(personalState.getPictureID()==0){
                            ((TwoViewHolder)holder).stateLikePicture.setImageResource(R.drawable.like_checked);
                            personalState.setPictureID(1);
                            personalState.setLike(personalState.getLike()+1);
                            ((TwoViewHolder)holder).stateLike.setText(String.valueOf(personalState.getLike()));
                            homePresenter.SendLike(personalState.getPersonalstate_id(),personalState);
                        }
                    }
                });
                ((TwoViewHolder)holder).stateCommentLinear.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //跳转动态详情activity
                        int position=((TwoViewHolder)holder).getAdapterPosition();
                        pos=position;
                        PersonalState personalState=mStateList.get(position);
                        Intent intent=new Intent(v.getContext(),CommentActivity.class);
                        intent.putExtra("personalstate",personalState);
                        startActivityForResult(intent,1);
                    }
                });
                ((TwoViewHolder)holder).stateLinear.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int position=((TwoViewHolder)holder).getAdapterPosition();
                        pos=position;
                        PersonalState personalState=mStateList.get(position);
                        Intent intent=new Intent(v.getContext(),CommentActivity.class);
                        intent.putExtra("personalstate",personalState);
                        startActivityForResult(intent,1);
                    }
                });
                ((TwoViewHolder)holder).stateImage1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int position=((TwoViewHolder)holder).getAdapterPosition();
                        PersonalState personalState=mStateList.get(position);
                        ArrayList<String> urls=new ArrayList<>();
                        urls.add(personalState.getImage1ID());
                        urls.add(personalState.getImage2ID());
                        seePicture(urls,0);
                    }
                });
                ((TwoViewHolder)holder).stateImage2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int position=((TwoViewHolder)holder).getAdapterPosition();
                        PersonalState personalState=mStateList.get(position);
                        ArrayList<String> urls=new ArrayList<>();
                        urls.add(personalState.getImage1ID());
                        urls.add(personalState.getImage2ID());
                        seePicture(urls,1);
                    }
                });
            }else { //三张图片时的布局
                view=LayoutInflater.from(parent.getContext()).inflate(R.layout.state_item,parent,false);
                holder=new ViewHolder(view);
                ((ViewHolder)holder).stateNickName.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v){
                        int position=((ViewHolder)holder).getAdapterPosition();
                        pos=position;
                        PersonalState personalState=mStateList.get(position);
                        //跳转个人资料activity
                        Intent intent=new Intent(v.getContext(),AlbumActivity.class);
                        Dao.SetIntent(intent,personalState.getUser_id(),personalState.getProfileID(),personalState.getIntorduction(),personalState.getNickname(),personalState.getSchool());
                        startActivity(intent);
                    }
                });
                ((ViewHolder)holder).stateProfile.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ((ViewHolder)holder).stateNickName.performClick();
                    }
                });
                ((ViewHolder)holder).stateLikeLinear.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int position=((ViewHolder)holder).getAdapterPosition();
                        pos=position;
                        PersonalState personalState=mStateList.get(position);
                        if(personalState.getPictureID()==0){
                            ((ViewHolder)holder).stateLikePicture.setImageResource(R.drawable.like_checked);
                            personalState.setPictureID(1);
                            personalState.setLike(personalState.getLike()+1);
                            ((ViewHolder)holder).stateLike.setText(String.valueOf(personalState.getLike()));
                            homePresenter.SendLike(personalState.getPersonalstate_id(),personalState);
                        }
                    }
                });
                ((ViewHolder)holder).stateCommentLinear.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int position=((ViewHolder)holder).getAdapterPosition();
                        pos=position;
                        PersonalState personalState=mStateList.get(position);
                        Intent intent=new Intent(v.getContext(),CommentActivity.class);
                        intent.putExtra("personalstate",personalState);
                        startActivityForResult(intent,1);
                    }
                });
                ((ViewHolder)holder).stateLinear.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int position=((ViewHolder)holder).getAdapterPosition();
                        pos=position;
                        PersonalState personalState=mStateList.get(position);
                        Intent intent=new Intent(v.getContext(),CommentActivity.class);
                        intent.putExtra("personalstate",personalState);
                        startActivityForResult(intent,1);
                    }
                });
                ((ViewHolder)holder).stateImage1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int position=((ViewHolder)holder).getAdapterPosition();
                        PersonalState personalState=mStateList.get(position);
                        ArrayList<String> urls=new ArrayList<>();
                        urls.add(personalState.getImage1ID());
                        urls.add(personalState.getImage2ID());
                        urls.add(personalState.getImage3ID());
                        seePicture(urls,0);
                    }
                });
                ((ViewHolder)holder).stateImage2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int position=((ViewHolder)holder).getAdapterPosition();
                        PersonalState personalState=mStateList.get(position);
                        ArrayList<String> urls=new ArrayList<>();
                        urls.add(personalState.getImage1ID());
                        urls.add(personalState.getImage2ID());
                        urls.add(personalState.getImage3ID());
                        seePicture(urls,1);
                    }
                });
                ((ViewHolder)holder).stateImage3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int position=((ViewHolder)holder).getAdapterPosition();
                        PersonalState personalState=mStateList.get(position);
                        ArrayList<String> urls=new ArrayList<>();
                        urls.add(personalState.getImage1ID());
                        urls.add(personalState.getImage2ID());
                        urls.add(personalState.getImage3ID());
                        seePicture(urls,2);
                    }
                });
            }
            return holder;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder,int position){
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            if(holder instanceof OneViewHolder){
                PersonalState personalState=mStateList.get(position);
                ((OneViewHolder)holder).stateNickName.setText(personalState.getNickname());
                ((OneViewHolder)holder).stateSchool.setText(personalState.getSchool());
                /*
                String er= translate.unicode2String(personalState.getContent());
                String et= translate.string2Unicode(personalState.getContent());
                Log.e("789456", personalState.getContent() );
                Log.e("er", er );
                Log.e("et", et );
                */
                ((OneViewHolder)holder).stateContent.setText(personalState.getContent());
                ((OneViewHolder)holder).stateLocation.setText(personalState.getLocation());
                ((OneViewHolder)holder).stateTime.setText(simpleDateFormat.format(personalState.getState_time()));
                ((OneViewHolder)holder).stateLike.setText(String.valueOf(personalState.getLike()));      //用以将int类型转换成String类型使用setText显示
                ((OneViewHolder)holder).stateComment.setText(String.valueOf(personalState.getComment()));
                //stateProfile 就是一个ImageView
                Glide.with(getActivity()).load(personalState.getProfileID()).thumbnail(0.1f).into(((OneViewHolder)holder).stateProfile);
                Glide.with(getActivity()).load(personalState.getImage1ID()).apply(requestOptions).thumbnail(0.1f).into(((OneViewHolder)holder).stateImage1);
                /*
                if(personalState.getImage1ID()!=null){
                    Glide.with(getActivity()).load(personalState.getImage1ID()).apply(requestOptions).thumbnail(0.1f).into(((OneViewHolder)holder).stateImage1);
                }else {
                    ViewGroup.LayoutParams params=((OneViewHolder)holder).stateImage1.getLayoutParams();
                    params.height=10;
                    ((OneViewHolder)holder).stateImage1.setLayoutParams(params);
                }
                */
                //((OneViewHolder)holder).stateProfile.setImageResource(personalState.getProfileID());
                //((OneViewHolder)holder).stateImage1.setImageResource(personalState.getImage1ID());
                if(personalState.getPictureID()==0){
                    ((OneViewHolder)holder).stateLikePicture.setImageResource(R.drawable.like);
                }else {
                    ((OneViewHolder)holder).stateLikePicture.setImageResource(R.drawable.like_checked);
                }
            }else if(holder instanceof TwoViewHolder){
                PersonalState personalState=mStateList.get(position);
                ((TwoViewHolder)holder).stateNickName.setText(personalState.getNickname());
                ((TwoViewHolder)holder).stateSchool.setText(personalState.getSchool());
                ((TwoViewHolder)holder).stateContent.setText(personalState.getContent());
                ((TwoViewHolder)holder).stateLocation.setText(personalState.getLocation());
                ((TwoViewHolder)holder).stateTime.setText(simpleDateFormat.format(personalState.getState_time()));
                ((TwoViewHolder)holder).stateLike.setText(String.valueOf(personalState.getLike()));      //用以将int类型转换成String类型使用setText显示
                ((TwoViewHolder)holder).stateComment.setText(String.valueOf(personalState.getComment()));
                Glide.with(getActivity()).load(personalState.getProfileID()).thumbnail(0.1f).into(((TwoViewHolder)holder).stateProfile);
                Glide.with(getActivity()).load(personalState.getImage1ID()).apply(requestOptions).thumbnail(0.1f).into(((TwoViewHolder)holder).stateImage1);
                Glide.with(getActivity()).load(personalState.getImage2ID()).apply(requestOptions).thumbnail(0.1f).into(((TwoViewHolder)holder).stateImage2);
                /*
                ((TwoViewHolder)holder).stateProfile.setImageResource(personalState.getProfileID());
                ((TwoViewHolder)holder).stateImage1.setImageResource(personalState.getImage1ID());
                ((TwoViewHolder)holder).stateImage2.setImageResource(personalState.getImage2ID());
                */
                if(personalState.getPictureID()==0){
                    ((TwoViewHolder)holder).stateLikePicture.setImageResource(R.drawable.like);
                }else {
                    ((TwoViewHolder)holder).stateLikePicture.setImageResource(R.drawable.like_checked);
                }
            }else{
                PersonalState personalState=mStateList.get(position);
                ((ViewHolder)holder).stateNickName.setText(personalState.getNickname());
                ((ViewHolder)holder).stateSchool.setText(personalState.getSchool());
                ((ViewHolder)holder).stateContent.setText(personalState.getContent());
                ((ViewHolder)holder).stateLocation.setText(personalState.getLocation());
                //((ViewHolder)holder).stateTime.setText(personalState.getState_time());
                ((ViewHolder)holder).stateTime.setText(simpleDateFormat.format(personalState.getState_time()));
                ((ViewHolder)holder).stateLike.setText(String.valueOf(personalState.getLike()));      //用以将int类型转换成String类型使用setText显示
                ((ViewHolder)holder).stateComment.setText(String.valueOf(personalState.getComment()));
                Glide.with(getActivity()).load(personalState.getProfileID()).thumbnail(0.1f).into(((ViewHolder)holder).stateProfile);
                Glide.with(getActivity()).load(personalState.getImage1ID()).apply(requestOptions).thumbnail(0.1f).into(((ViewHolder)holder).stateImage1);
                Glide.with(getActivity()).load(personalState.getImage2ID()).apply(requestOptions).thumbnail(0.1f).into(((ViewHolder)holder).stateImage2);
                Glide.with(getActivity()).load(personalState.getImage3ID()).apply(requestOptions).thumbnail(0.1f).into(((ViewHolder)holder).stateImage3);
                /*
                ((ViewHolder)holder).stateProfile.setImageResource(personalState.getProfileID());
                ((ViewHolder)holder).stateImage1.setImageResource(personalState.getImage1ID());
                ((ViewHolder)holder).stateImage2.setImageResource(personalState.getImage2ID());
                ((ViewHolder)holder).stateImage3.setImageResource(personalState.getImage3ID());
                */
                if(personalState.getPictureID()==0){
                    ((ViewHolder)holder).stateLikePicture.setImageResource(R.drawable.like);
                }else {
                    ((ViewHolder)holder).stateLikePicture.setImageResource(R.drawable.like_checked);
                }
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e("HOME", "ondestory" );
    }


    //动画
    public void act() {
        final LayoutAnimationController controller = AnimationUtils.loadLayoutAnimation(getActivity(), R.anim.layout_animation_fall_down);
        final RecyclerView stateRecyclerView=(RecyclerView)view.findViewById(R.id.state_recycler_view);
        stateRecyclerView.setLayoutAnimation(controller);
    }

    public StateAdapter getAdapter() {
        return adapter;
    }

    public RecyclerView getStateRecyclerView() {
        return stateRecyclerView;
    }

    public SwipeRefreshLayout getSwipeRefreshLayout() {
        return swipeRefreshLayout;
    }

    public void LoadMoment(List<PersonalState>personalStates){
        //adapter.setmStateList(personalStates);
        AllPersonalState=personalStates;
        List<PersonalState>stateList=adapter.getmStateList();
        for(;state<20&&state<personalStates.size();state++){
            stateList.add(personalStates.get(state));
        }
        stateRecyclerView.setLayoutAnimation(controller);
        stateRecyclerView.setAdapter(adapter);

        progressLayout.setVisibility(View.GONE);
    }

    public void UpdateMoment(List<PersonalState> personalStates){
        for(int i=0;i<adapter.getmStateList().size()&&i<50;i++){
            adapter.notifyItemChanged(i);
        }
        if(personalStates.size()>0){
            adapter.getmStateList().addAll(0,personalStates);
            adapter.notifyDataSetChanged();
            AllPersonalState.addAll(0,personalStates);
        }
        stateRecyclerView.scrollToPosition(0);
        swipeRefreshLayout.setRefreshing(false);
    }

    public void seePicture(ArrayList<String> url,int position){
        new PhotoPagerConfig.Builder(getActivity()).setBigImageUrls(url).setSavaImage(true).setSaveImageLocalPath(MyApplication.getStorePath()).setPosition(position).setOpenDownAnimate(true).build();
    }

    /*
    @Override
    public void onResume() {
        super.onResume();
        Log.e("resume", "onResume: " );
        Log.e("resume", String.valueOf(pos) );
        if(pos>=0){
            PersonalState personalStates=adapter.getmStateList().get(pos);
            if(personalStates.getImg_type()==0){
                StateAdapter.OneViewHolder viewHolder= (StateAdapter.OneViewHolder)stateRecyclerView.findViewHolderForAdapterPosition(pos);
                viewHolder.stateComment.setText(String.valueOf(1));
                viewHolder.stateLike.setText(String.valueOf(1));
            }else if(personalStates.getImg_type()==1){
                StateAdapter.TwoViewHolder viewHolder= (StateAdapter.TwoViewHolder)stateRecyclerView.findViewHolderForAdapterPosition(pos);
                viewHolder.stateComment.setText(String.valueOf(1));
                viewHolder.stateLike.setText(String.valueOf(1));
            }else {
                StateAdapter.ViewHolder viewHolder= (StateAdapter.ViewHolder)stateRecyclerView.findViewHolderForAdapterPosition(pos);
                viewHolder.stateComment.setText(String.valueOf(1));
                viewHolder.stateLike.setText(String.valueOf(1));
            }
            //adapter.notifyItemChanged(pos);
        }
    }
    */

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case 1:
                if(resultCode==RESULT_OK){
                    PersonalState per=(PersonalState)data.getSerializableExtra("return");
                    if(pos>=0){
                        PersonalState personalState=adapter.getmStateList().get(pos);
                        personalState.setPictureID(per.getPictureID());
                        personalState.setComment(per.getComment());
                        personalState.setLike(per.getLike());
                        adapter.notifyItemChanged(pos);
                    }
                }
                break;
            default:
                break;
        }
    }

    public List<PersonalState> getAllPersonalState() {
        return AllPersonalState;
    }
}