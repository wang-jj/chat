package com.example.dell.chat.view;

import android.arch.lifecycle.LifecycleOwner;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
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
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import com.awen.photo.photopick.controller.PhotoPagerConfig;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.dell.chat.R;
import com.example.dell.chat.bean.Location;
import com.example.dell.chat.bean.MyApplication;
import com.example.dell.chat.bean.Recommend;
import com.example.dell.chat.presenter.HomePresenter;
import com.example.dell.chat.presenter.LocalPresenter;
import com.example.dell.chat.tools.Dao;
import com.google.gson.Gson;
import com.luck.picture.lib.PictureSelector;


/**
 * Created by Staroul on 2018/3/30.
 */


public class LocalFragment extends Fragment {

    private View view;
    private LocalPresenter localPresenter=new LocalPresenter(this);
    private LocationAdapter adapter;
    private RecyclerView locationRecyclerView;
    private RecyclerView recommendRecyclerView;
    private LinearLayout progressLayout;
    private LayoutAnimationController controller;
    private RecommendAdapter recommend_adapter;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        view=inflater.inflate(R.layout.local_fragment,container,false);
        locationRecyclerView=(RecyclerView)view.findViewById(R.id.location_recycler_view);
        recommendRecyclerView=(RecyclerView)view.findViewById(R.id.recommend_recycler_view);
        progressLayout=(LinearLayout)view.findViewById(R.id.location_progress);
        progressLayout.setVisibility(View.VISIBLE);

        //动画 localFrag
        //final LayoutAnimationController controller = AnimationUtils.loadLayoutAnimation(getActivity(), R.anim.layout_animation_slide_bottom);
        //final RecyclerView locationRecyclerView=(RecyclerView)view.findViewById(R.id.location_recycler_view);
        //locationRecyclerView.setLayoutAnimation(controller);
        //locationRecyclerView.setAdapter(adapter);



        //定义推荐 recyclerview
        recommendRecyclerView.setNestedScrollingEnabled(false);
        LinearLayoutManager layoutManager_recommend=new LinearLayoutManager(getActivity());
        layoutManager_recommend.setOrientation(LinearLayoutManager.HORIZONTAL); //设置横向recyclerview
        recommendRecyclerView.setLayoutManager(layoutManager_recommend);
        //设置 推荐 适配器以及list用以显示数据
         recommend_adapter=new RecommendAdapter(getRecommend());


        //定义附近的人 recyclerview
        controller = AnimationUtils.loadLayoutAnimation(getActivity(), R.anim.layout_animation_slide_bottom);
        locationRecyclerView.setNestedScrollingEnabled(false);
        LinearLayoutManager layoutManager=new LinearLayoutManager(getActivity());
        locationRecyclerView.setLayoutManager(layoutManager);

        //设置 定位 适配器以及list用以显示数据
        adapter=new LocationAdapter(new ArrayList<Location>());

        //final Handler handler = new Handler();
        recommendRecyclerView.setAdapter(recommend_adapter);
        /*
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                locationRecyclerView.setLayoutAnimation(controller);
                recommendRecyclerView.setAdapter(recommend_adapter);
                locationRecyclerView.setAdapter(adapter);
                progressLayout.setVisibility(View.GONE);
            }
        }, 1000);
        */

        /*
        //底部刷新函数
        final NestedScrollView nestedScrollView=(NestedScrollView)view.findViewById(R.id.local_scroll_view);
        nestedScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, final int scrollX, final int scrollY, int oldScrollX, int oldScrollY) {
                if (scrollY == (v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight())) {
                    //底部刷新
                    if(progressLayout.getVisibility()==View.GONE){
                        progressLayout.setVisibility(View.VISIBLE);
                    }
                    final Location location=new Location();
                    location.setNickname("谢欣逗比言");
                    location.setSchool("华北理工小学");
                    location.setIntroduction("这个逗比太懒了，并没有自我介绍。");
                    location.setProfileID("https://cn.bing.com/s/hpb/NorthMale_EN-US8782628354_1920x1080.jpg");
                    location.setImage1ID("https://cn.bing.com/s/hpb/NorthMale_EN-US8782628354_1920x1080.jpg");
                    location.setImage2ID("https://cn.bing.com/s/hpb/NorthMale_EN-US8782628354_1920x1080.jpg");
                    location.setImage3ID("https://cn.bing.com/s/hpb/NorthMale_EN-US8782628354_1920x1080.jpg");
                    location.setImg_type(2);
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            adapter.LocationAdd(adapter.getItemCount(),location);
                            adapter.notifyItemInserted(adapter.getItemCount());
                            progressLayout.setVisibility(View.GONE);
                        }
                    }, 1000);
                }
            }
        });
        */

        //顶部刷新函数
        final SwipeRefreshLayout swipeRefreshLayout=(SwipeRefreshLayout)view.findViewById(R.id.swipe_location_recycler);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {   //顶部刷新
                /*
                Location location=new Location();
                location.setNickname("谢欣逗比言");
                location.setSchool("华北理工小学");
                location.setIntroduction("这个逗比太懒了，并没有自我介绍。");
                location.setProfileID("https://cn.bing.com/s/hpb/NorthMale_EN-US8782628354_1920x1080.jpg");
                location.setImage1ID("https://cn.bing.com/s/hpb/NorthMale_EN-US8782628354_1920x1080.jpg");
                location.setImage2ID("https://cn.bing.com/s/hpb/NorthMale_EN-US8782628354_1920x1080.jpg");
                location.setImage3ID("https://cn.bing.com/s/hpb/NorthMale_EN-US8782628354_1920x1080.jpg");
                location.setImg_type(2);
                adapter.LocationAdd(0,location);
                adapter.notifyItemInserted(0);
                locationRecyclerView.scrollToPosition(0);
                swipeRefreshLayout.setRefreshing(false);
                */
                localPresenter.UpdatePerson();
            }
        });
        swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorWhite));
        swipeRefreshLayout.setProgressBackgroundColorSchemeColor(getResources().getColor(R.color.colorPrimary));
        localPresenter.LoadPerson();
        localPresenter.Recommend();
        return view;
    }

    private List<Recommend> getRecommend(){                 //初始化推荐list
        List<Recommend> recommendList=new ArrayList<>();
        Recommend recommend_first=new Recommend();          //初始化推荐的第一项
        recommend_first.setNickname("我的推荐");
        recommend_first.setProfileID("http://119.23.255.222/android/image/profile.jpg");   //此处获取用户自己的头像
        recommendList.add(recommend_first);
        return recommendList;
    }

    private List<Location> getLocation(){                   //初始化定位list
        List<Location> locationList=new ArrayList<>();
        for(int i=1;i<=3;i++){
            Location location=new Location();
            location.setNickname("谢欣逗比言"+i);
            location.setSchool("华北理工小学");
            location.setProfileID("https://cn.bing.com/s/hpb/NorthMale_EN-US8782628354_1920x1080.jpg");
            location.setIntroduction("这个逗比太懒了，并没有自我介绍。");
            location.setUser_id(123+i);
            if((i-1)%3==0){
                location.setImage1ID("https://cn.bing.com/s/hpb/NorthMale_EN-US8782628354_1920x1080.jpg");
                location.setImg_type(0);
            }else if((i-1)%3==1){
                location.setImage1ID("https://cn.bing.com/s/hpb/NorthMale_EN-US8782628354_1920x1080.jpg");
                location.setImage2ID("https://cn.bing.com/s/hpb/NorthMale_EN-US8782628354_1920x1080.jpg");
                location.setImg_type(1);
            }else{
                location.setImage1ID("https://cn.bing.com/s/hpb/NorthMale_EN-US8782628354_1920x1080.jpg");
                location.setImage2ID("https://cn.bing.com/s/hpb/NorthMale_EN-US8782628354_1920x1080.jpg");
                location.setImage3ID("https://cn.bing.com/s/hpb/NorthMale_EN-US8782628354_1920x1080.jpg");
                location.setImg_type(2);
            }
            locationList.add(location);
        }
        return locationList;
    }

    class RecommendAdapter extends RecyclerView.Adapter<LocalFragment.RecommendAdapter.ViewHolder>{
        private List<Recommend> mRecommendList;

        public List<Recommend> getmRecommendList() {
            return mRecommendList;
        }

        public void setmRecommendList(List<Recommend> mRecommendList) {
            this.mRecommendList = mRecommendList;
        }

        class ViewHolder extends RecyclerView.ViewHolder{
            TextView recommendNickName;
            ImageView recommendProfile;

            public ViewHolder(View view){
                super(view);
                recommendNickName=(TextView)view.findViewById(R.id.recommend_nickname);
                recommendProfile=(ImageView)view.findViewById(R.id.recommend_profile);
            }
        }
        public RecommendAdapter(List<Recommend> recommendList){
            mRecommendList=recommendList;
        }

        public void RecommendAdd(int position,Recommend recommend){
            mRecommendList.add(position,recommend);
            return;
        }

        public void LocationRemove(int position){
            mRecommendList.remove(position);
            return;
        }

        @Override
        public LocalFragment.RecommendAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
            View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.recommend_item,parent,false);
            final LocalFragment.RecommendAdapter.ViewHolder holder=new LocalFragment.RecommendAdapter.ViewHolder(view);

            //推荐 头像点击事件 跳转到个人资料
            holder.recommendProfile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position=holder.getAdapterPosition();
                    if(position>=1){
                        Recommend location=mRecommendList.get(position);
                        Intent intent=new Intent(getActivity(),AlbumActivity.class);
                        Dao.SetIntent(intent,location.getUser_id(),location.getProfileID(),location.getIntroduction(),location.getNickname(),location.getSchool());
                        startActivity(intent);
                    }
                }
            });

            return holder;
        }

        @Override
        public void onBindViewHolder(LocalFragment.RecommendAdapter.ViewHolder holder, int position){
            Recommend recommend=mRecommendList.get(position);
            holder.recommendNickName.setText(recommend.getNickname());
            Glide.with(getActivity()).load(recommend.getProfileID()).into(holder.recommendProfile);
        }

        @Override
        public int getItemCount(){
            return mRecommendList.size();
        }
    }

    class LocationAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
        private List<Location> mLocationList;
        private static final int TYPE_ONE_IMAGE=0;
        private static final int TYPE_TWO_IMAGE=1;
        private static final int TYPE_THREE_IMAGE=2;
        private RequestOptions requestOptions=new RequestOptions().centerCrop();

        public List<Location> getmLocationList() {
            return mLocationList;
        }

        public LocationAdapter(List<Location> locationList){
            mLocationList=locationList;
        }

        public void LocationAdd(int position,Location location){
            mLocationList.add(position,location);
            return;
        }

        public void LocationRemoveAll(){
            mLocationList.clear();
            return;
        }

        public void setmLocationList(List<Location> mLocationList) {
            this.mLocationList = mLocationList;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
            final View view;
            final RecyclerView.ViewHolder holder;

            if(viewType==TYPE_ONE_IMAGE){   //一张图片时的布局
                view=LayoutInflater.from(parent.getContext()).inflate(R.layout.location_item_one,parent,false);
                holder=new OneViewHolder(view);
                ((OneViewHolder)holder).locationLinear.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v){
                        //跳转个人资料activity
                        int position=holder.getAdapterPosition();
                        Location location=mLocationList.get(position);
                        Intent intent=new Intent(v.getContext(),AlbumActivity.class);
                        Dao.SetIntent(intent,location.getUser_id(),location.getProfileID(),location.getIntroduction(),location.getNickname(),location.getSchool());
                        startActivity(intent);
                    }
                });

                ((OneViewHolder)holder).locationImage1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int position=holder.getAdapterPosition();
                        Location location=mLocationList.get(position);
                        if(location.getImage1ID()!=null){
                            ArrayList<String> locationPath=new ArrayList<>();
                            locationPath.add(location.getImage1ID());
                            seePicture(locationPath,0);
                        }
                    }
                });

            }else if(viewType==TYPE_TWO_IMAGE){ //两张图片时的布局
                view=LayoutInflater.from(parent.getContext()).inflate(R.layout.location_item_two,parent,false);
                holder=new TwoViewHolder(view);
                ((TwoViewHolder)holder).locationLinear.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v){
                        //跳转个人资料activity
                        int position=holder.getAdapterPosition();
                        Location location=mLocationList.get(position);
                        Intent intent=new Intent(v.getContext(),AlbumActivity.class);
                        Dao.SetIntent(intent,location.getUser_id(),location.getProfileID(),location.getIntroduction(),location.getNickname(),location.getSchool());
                        startActivity(intent);
                    }
                });
                ((TwoViewHolder)holder).locationImage1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v){
                        //跳转个人资料activity
                        int position=holder.getAdapterPosition();
                        Location location=mLocationList.get(position);
                        ArrayList<String> locationPath=new ArrayList<>();
                        locationPath.add(location.getImage1ID());
                        locationPath.add(location.getImage2ID());
                        seePicture(locationPath,0);
                    }
                });
                ((TwoViewHolder)holder).locationImage2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v){
                        //跳转个人资料activity
                        int position=holder.getAdapterPosition();
                        Location location=mLocationList.get(position);
                        ArrayList<String> locationPath=new ArrayList<>();
                        locationPath.add(location.getImage1ID());
                        locationPath.add(location.getImage2ID());
                        seePicture(locationPath,1);
                    }
                });
            }else { //三张图片时的布局
                view=LayoutInflater.from(parent.getContext()).inflate(R.layout.location_item,parent,false);
                holder=new ViewHolder(view);
                ((ViewHolder)holder).locationLinear.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v){
                        //跳转个人资料activity
                        int position=holder.getAdapterPosition();
                        Location location=mLocationList.get(position);
                        Intent intent=new Intent(v.getContext(),AlbumActivity.class);
                        Dao.SetIntent(intent,location.getUser_id(),location.getProfileID(),location.getIntroduction(),location.getNickname(),location.getSchool());
                        startActivity(intent);
                    }
                });
                ((ViewHolder)holder).locationImage1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v){
                        //跳转个人资料activity
                        int position=holder.getAdapterPosition();
                        Location location=mLocationList.get(position);
                        ArrayList<String> locationPath=new ArrayList<>();
                        locationPath.add(location.getImage1ID());
                        locationPath.add(location.getImage2ID());
                        locationPath.add(location.getImage3ID());
                        seePicture(locationPath,0);
                    }
                });
                ((ViewHolder)holder).locationImage2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v){
                        //跳转个人资料activity
                        int position=holder.getAdapterPosition();
                        Location location=mLocationList.get(position);
                        ArrayList<String> locationPath=new ArrayList<>();
                        locationPath.add(location.getImage1ID());
                        locationPath.add(location.getImage2ID());
                        locationPath.add(location.getImage3ID());
                        seePicture(locationPath,1);
                    }
                });
                ((ViewHolder)holder).locationImage3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v){
                        //跳转个人资料activity
                        int position=holder.getAdapterPosition();
                        Location location=mLocationList.get(position);
                        ArrayList<String> locationPath=new ArrayList<>();
                        locationPath.add(location.getImage1ID());
                        locationPath.add(location.getImage2ID());
                        locationPath.add(location.getImage3ID());
                        seePicture(locationPath,2);
                    }
                });
            }

            return holder;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position){
            String url="http://119.23.255.222/android";
            if(holder instanceof OneViewHolder){
                Location location=mLocationList.get(position);
                ((OneViewHolder)holder).locationNickName.setText(location.getNickname());
                ((OneViewHolder)holder).locationSchool.setText(location.getSchool());
                ((OneViewHolder)holder).locationIntroduction.setText(location.getIntroduction());
                if(location.getProfileID()!=null){
                    Glide.with(getActivity()).load(location.getProfileID()).thumbnail(0.1f).into(((OneViewHolder)holder).locationProfile);
                }
                if(location.getImage1ID()!=null){
                    Glide.with(getActivity()).load(location.getImage1ID()).thumbnail(0.1f).apply(requestOptions).into(((OneViewHolder)holder).locationImage1);
                }
            }else if(holder instanceof TwoViewHolder){
                Location location=mLocationList.get(position);
                ((TwoViewHolder)holder).locationNickName.setText(location.getNickname());
                ((TwoViewHolder)holder).locationSchool.setText(location.getSchool());
                ((TwoViewHolder)holder).locationIntroduction.setText(location.getIntroduction());
                if(location.getProfileID()!=null){
                    Glide.with(getActivity()).load(location.getProfileID()).thumbnail(0.1f).into(((TwoViewHolder)holder).locationProfile);
                }
                if(location.getImage1ID()!=null){
                    Glide.with(getActivity()).load(location.getImage1ID()).thumbnail(0.1f).apply(requestOptions).into(((TwoViewHolder)holder).locationImage1);
                }
                if(location.getImage2ID()!=null){
                    Glide.with(getActivity()).load(location.getImage2ID()).thumbnail(0.1f).apply(requestOptions).into(((TwoViewHolder)holder).locationImage2);
                }
            }else{
                Location location=mLocationList.get(position);
                ((ViewHolder)holder).locationNickName.setText(location.getNickname());
                ((ViewHolder)holder).locationSchool.setText(location.getSchool());
                ((ViewHolder)holder).locationIntroduction.setText(location.getIntroduction());
                if(location.getProfileID()!=null){
                    Glide.with(getActivity()).load(location.getProfileID()).thumbnail(0.1f).into(((ViewHolder)holder).locationProfile);
                }
                if(location.getImage1ID()!=null){
                    Glide.with(getActivity()).load(location.getImage1ID()).thumbnail(0.1f).apply(requestOptions).into(((ViewHolder)holder).locationImage1);
                }
                if(location.getImage2ID()!=null){
                    Glide.with(getActivity()).load(location.getImage2ID()).thumbnail(0.1f).apply(requestOptions).into(((ViewHolder)holder).locationImage2);
                }
                if(location.getImage3ID()!=null){
                    Glide.with(getActivity()).load(location.getImage3ID()).thumbnail(0.1f).apply(requestOptions).into(((ViewHolder)holder).locationImage3);
                }
            }
        }

        @Override
        public int getItemViewType(int position){
            Location location=mLocationList.get(position);
            if(location.getImg_type()==TYPE_ONE_IMAGE){
                return TYPE_ONE_IMAGE;
            }else if(location.getImg_type()==TYPE_TWO_IMAGE){
                return TYPE_TWO_IMAGE;
            }else{
                return TYPE_THREE_IMAGE;
            }
        }

        @Override
        public int getItemCount(){
            return mLocationList.size();
        }

        class OneViewHolder extends RecyclerView.ViewHolder{
            TextView locationNickName;
            TextView locationSchool;
            TextView locationIntroduction;
            ImageView locationProfile; //头像 类型为int 之后按照后端需求修改 暂时引用drawable中的已有图片资源，即int
            ImageView locationImage1;
            LinearLayout locationLinear;

            public OneViewHolder(View view){
                super(view);
                locationNickName=(TextView)view.findViewById(R.id.location_nickname);
                locationSchool=(TextView)view.findViewById(R.id.location_school);
                locationIntroduction=(TextView)view.findViewById(R.id.location_introduction);
                locationProfile=(ImageView)view.findViewById(R.id.location_profile);
                locationImage1=(ImageView)view.findViewById(R.id.location_image1);
                locationLinear=(LinearLayout)view.findViewById(R.id.local_linear);
            }
        }

        class TwoViewHolder extends RecyclerView.ViewHolder{
            TextView locationNickName;
            TextView locationSchool;
            TextView locationIntroduction;
            ImageView locationProfile; //头像 类型为int 之后按照后端需求修改 暂时引用drawable中的已有图片资源，即int
            ImageView locationImage1;
            ImageView locationImage2;
            LinearLayout locationLinear;

            public TwoViewHolder(View view){
                super(view);
                locationNickName=(TextView)view.findViewById(R.id.location_nickname);
                locationSchool=(TextView)view.findViewById(R.id.location_school);
                locationIntroduction=(TextView)view.findViewById(R.id.location_introduction);
                locationProfile=(ImageView)view.findViewById(R.id.location_profile);
                locationImage1=(ImageView)view.findViewById(R.id.location_image1);
                locationImage2=(ImageView)view.findViewById(R.id.location_image2);
                locationLinear=(LinearLayout)view.findViewById(R.id.local_linear);
            }
        }

        class ViewHolder extends RecyclerView.ViewHolder{
            TextView locationNickName;
            TextView locationSchool;
            TextView locationIntroduction;
            ImageView locationProfile; //头像 类型为int 之后按照后端需求修改 暂时引用drawable中的已有图片资源，即int
            ImageView locationImage1;
            ImageView locationImage2;
            ImageView locationImage3;
            LinearLayout locationLinear;

            public ViewHolder(View view){
                super(view);
                locationNickName=(TextView)view.findViewById(R.id.location_nickname);
                locationSchool=(TextView)view.findViewById(R.id.location_school);
                locationIntroduction=(TextView)view.findViewById(R.id.location_introduction);
                locationProfile=(ImageView)view.findViewById(R.id.location_profile);
                locationImage1=(ImageView)view.findViewById(R.id.location_image1);
                locationImage2=(ImageView)view.findViewById(R.id.location_image2);
                locationImage3=(ImageView)view.findViewById(R.id.location_image3);
                locationLinear=(LinearLayout)view.findViewById(R.id.local_linear);
            }
        }
    }

    public LocationAdapter getAdapter() {
        return adapter;
    }

    public void UpdatePerson(List<Location>locations){
        ((SwipeRefreshLayout)view.findViewById(R.id.swipe_location_recycler)).setRefreshing(false);
        locationRecyclerView.setLayoutAnimation(controller);
        adapter.setmLocationList(locations);
        adapter.notifyDataSetChanged();
    }

    public void CreatePerson(List<Location>locations){
        adapter.setmLocationList(locations);
        locationRecyclerView.setLayoutAnimation(controller);
        locationRecyclerView.setAdapter(adapter);
        progressLayout.setVisibility(View.GONE);
    }

    public void seePicture(ArrayList<String> url,int position){
        new PhotoPagerConfig.Builder(getActivity()).setBigImageUrls(url).setSavaImage(true).setSaveImageLocalPath(MyApplication.getStorePath()).setPosition(position).setOpenDownAnimate(true).build();
    }

    public void LoadRecommend(List<Recommend>recommends){
        recommend_adapter.getmRecommendList().addAll(1,recommends);
        recommend_adapter.notifyDataSetChanged();
    }

    //动画
    public void act() {
        final LayoutAnimationController controller = AnimationUtils.loadLayoutAnimation(getActivity(), R.anim.layout_animation_slide_bottom);
        final RecyclerView locationRecyclerView=(RecyclerView)view.findViewById(R.id.location_recycler_view);
        locationRecyclerView.setLayoutAnimation(controller);
    }
}
