package com.example.dell.chat.view;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
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
import android.widget.TextView;
import android.widget.Toast;

import com.example.dell.chat.R;
import com.example.dell.chat.bean.Collect;

import java.util.ArrayList;
import java.util.List;

//收藏Activity 暂时没有加入的功能 这个不着急弄
public class CollectActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collect);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        final RecyclerView collectRecyclerView=(RecyclerView)findViewById(R.id.collect_recycler_view);
        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        collectRecyclerView.setLayoutManager(layoutManager);
        final CollectAdapter adapter=new CollectAdapter(getCollect());
        collectRecyclerView.setAdapter(adapter);
        final LayoutAnimationController controller = AnimationUtils.loadLayoutAnimation(this, R.anim.layout_animation_slide_bottom);
        collectRecyclerView.setLayoutAnimation(controller);

        collectRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if(newState==RecyclerView.SCROLL_STATE_IDLE){   //底部刷新
                    Collect collect=new Collect();
                    collect.setNickname("麦梓逗比旗");
                    collect.setContent("超级喜欢这种类型的小猫！");
                    collect.setProfileID(R.drawable.profile);
                    collect.setImage1ID(R.drawable.sample1);
                    collect.setImage2ID(R.drawable.sample2);
                    collect.setImage3ID(R.drawable.sample3);
                    collect.setImg_type(2);
                    collect.setCollect_time("09:43");
                    adapter.CollectAdd(adapter.getItemCount(),collect);
                    adapter.notifyItemInserted(adapter.getItemCount());
                    collectRecyclerView.scrollToPosition(adapter.getItemCount());
                }
            }
        });

    }

    private List<Collect> getCollect(){
        List<Collect> collectList=new ArrayList<>();
        for(int i=1;i<=10;i++){
            Collect collect=new Collect();
            collect.setNickname("麦梓逗比旗"+i);
            collect.setContent("超级喜欢这种类型的小猫！");
            collect.setProfileID(R.drawable.profile);
            if((i-1)%3==0){
                collect.setImage1ID(R.drawable.sample1);
                collect.setImg_type(0);
            }else if((i-1)%3==1){
                collect.setImage1ID(R.drawable.sample1);
                collect.setImage2ID(R.drawable.sample2);
                collect.setImg_type(1);
            }else{
                collect.setImage1ID(R.drawable.sample1);
                collect.setImage2ID(R.drawable.sample2);
                collect.setImage3ID(R.drawable.sample3);
                collect.setImg_type(2);
            }
            collect.setCollect_time("09:43");
            collectList.add(collect);
        }
        return collectList;
    }

    class CollectAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
        private List<Collect> mCollectList;
        private static final int TYPE_ONE_IMAGE=0;
        private static final int TYPE_TWO_IMAGE=1;
        private static final int TYPE_THREE_IMAGE=2;

        public CollectAdapter(List<Collect> collectList){
            mCollectList=collectList;
        }

        public void CollectAdd(int position,Collect collect){
            mCollectList.add(position,collect);
            return;
        }

        public void StateRemove(int position){
            mCollectList.remove(position);
            return;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
            final View view;
            final RecyclerView.ViewHolder holder;
            if(viewType==TYPE_ONE_IMAGE){
                view=LayoutInflater.from(parent.getContext()).inflate(R.layout.collect_item_one,parent,false);
                holder=new OneViewHolder(view);
            }else if(viewType==TYPE_TWO_IMAGE){
                view=LayoutInflater.from(parent.getContext()).inflate(R.layout.collect_item_two,parent,false);
                holder=new TwoViewHolder(view);
            }else{
                view=LayoutInflater.from(parent.getContext()).inflate(R.layout.collect_item,parent,false);
                holder=new ViewHolder(view);
            }
            return holder;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position){
            Collect collect=mCollectList.get(position);
            if(holder instanceof OneViewHolder){
                ((OneViewHolder)holder).collectNickName.setText(collect.getNickname());
                ((OneViewHolder)holder).collectContent.setText(collect.getContent());
                ((OneViewHolder)holder).collectTime.setText(collect.getCollect_time());
                ((OneViewHolder)holder).collectProfile.setImageResource(collect.getProfileID());
                ((OneViewHolder)holder).collectImage1.setImageResource(collect.getImage1ID());
            }else if(holder instanceof TwoViewHolder){
                ((TwoViewHolder)holder).collectNickName.setText(collect.getNickname());
                ((TwoViewHolder)holder).collectContent.setText(collect.getContent());
                ((TwoViewHolder)holder).collectTime.setText(collect.getCollect_time());
                ((TwoViewHolder)holder).collectProfile.setImageResource(collect.getProfileID());
                ((TwoViewHolder)holder).collectImage1.setImageResource(collect.getImage1ID());
                ((TwoViewHolder)holder).collectImage2.setImageResource(collect.getImage2ID());
            }else{
                ((ViewHolder)holder).collectNickName.setText(collect.getNickname());
                ((ViewHolder)holder).collectContent.setText(collect.getContent());
                ((ViewHolder)holder).collectTime.setText(collect.getCollect_time());
                ((ViewHolder)holder).collectProfile.setImageResource(collect.getProfileID());
                ((ViewHolder)holder).collectImage1.setImageResource(collect.getImage1ID());
                ((ViewHolder)holder).collectImage2.setImageResource(collect.getImage2ID());
                ((ViewHolder)holder).collectImage3.setImageResource(collect.getImage3ID());
            }
        }

        @Override
        public int getItemViewType(int position){
            Collect collect=mCollectList.get(position);
            if(collect.getImg_type()==TYPE_ONE_IMAGE){
                return TYPE_ONE_IMAGE;
            }else if(collect.getImg_type()==TYPE_TWO_IMAGE){
                return TYPE_TWO_IMAGE;
            }else{
                return TYPE_THREE_IMAGE;
            }
        }

        @Override
        public int getItemCount(){
            return mCollectList.size();
        }

        class OneViewHolder extends RecyclerView.ViewHolder{
            TextView collectNickName;
            TextView collectContent;
            TextView collectTime;
            ImageView collectProfile; //头像 类型为int 之后按照后端需求修改 暂时引用drawable中的已有图片资源，即int
            ImageView collectImage1;

            public OneViewHolder(View view){
                super(view);
                collectNickName=(TextView)view.findViewById(R.id.collect_nickname);
                collectContent=(TextView)view.findViewById(R.id.collect_content);
                collectTime=(TextView)view.findViewById(R.id.collect_time);
                collectProfile=(ImageView)view.findViewById(R.id.collect_profile);
                collectImage1=(ImageView)view.findViewById(R.id.collect_image1);
            }
        }

        class TwoViewHolder extends RecyclerView.ViewHolder{
            TextView collectNickName;
            TextView collectContent;
            TextView collectTime;
            ImageView collectProfile; //头像 类型为int 之后按照后端需求修改 暂时引用drawable中的已有图片资源，即int
            ImageView collectImage1;
            ImageView collectImage2;

            public TwoViewHolder(View view){
                super(view);
                collectNickName=(TextView)view.findViewById(R.id.collect_nickname);
                collectContent=(TextView)view.findViewById(R.id.collect_content);
                collectTime=(TextView)view.findViewById(R.id.collect_time);
                collectProfile=(ImageView)view.findViewById(R.id.collect_profile);
                collectImage1=(ImageView)view.findViewById(R.id.collect_image1);
                collectImage2=(ImageView)view.findViewById(R.id.collect_image2);
            }
        }

        class ViewHolder extends RecyclerView.ViewHolder{
            TextView collectNickName;
            TextView collectContent;
            TextView collectTime;
            ImageView collectProfile; //头像 类型为int 之后按照后端需求修改 暂时引用drawable中的已有图片资源，即int
            ImageView collectImage1;
            ImageView collectImage2;
            ImageView collectImage3;

            public ViewHolder(View view){
                super(view);
                collectNickName=(TextView)view.findViewById(R.id.collect_nickname);
                collectContent=(TextView)view.findViewById(R.id.collect_content);
                collectTime=(TextView)view.findViewById(R.id.collect_time);
                collectProfile=(ImageView)view.findViewById(R.id.collect_profile);
                collectImage1=(ImageView)view.findViewById(R.id.collect_image1);
                collectImage2=(ImageView)view.findViewById(R.id.collect_image2);
                collectImage3=(ImageView)view.findViewById(R.id.collect_image3);
            }
        }
    }

}
