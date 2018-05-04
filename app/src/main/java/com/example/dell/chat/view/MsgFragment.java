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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dell.chat.R;
import com.example.dell.chat.bean.Message;
import com.example.dell.chat.presenter.MessagePresenter;
import com.example.dell.chat.tools.SwipeItemLayout;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Staroul on 2018/3/30.
 */

//聊天信息提示fragment
public class MsgFragment extends Fragment {


    private MessagePresenter presenter = new MessagePresenter(this);
    private MessageAdapter adapter;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view=inflater.inflate(R.layout.msg_fragment,container,false);

        final LinearLayout progressLayout=(LinearLayout)view.findViewById(R.id.message_progress);
        progressLayout.setVisibility(View.VISIBLE);



        final LayoutAnimationController controller = AnimationUtils.loadLayoutAnimation(getActivity(), R.anim.layout_animation_slide_right);
        final RecyclerView messageRecyclerView=(RecyclerView)view.findViewById(R.id.message_recycler_view);
        messageRecyclerView.setNestedScrollingEnabled(false);
        LinearLayoutManager layoutManager=new LinearLayoutManager(getActivity());
        messageRecyclerView.setLayoutManager(layoutManager);

        messageRecyclerView.addOnItemTouchListener(new SwipeItemLayout.OnSwipeItemTouchListener(getActivity()));

        //设置适配器以及list用以显示数据
        adapter = new MessageAdapter();
        presenter.dispContact();
        //adapter.setAdapter(getMessage());

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                messageRecyclerView.setLayoutAnimation(controller);
                messageRecyclerView.setAdapter(adapter);
                progressLayout.setVisibility(View.GONE);
            }
        }, 1000);

        //底部刷新函数
        final NestedScrollView nestedScrollView=(NestedScrollView)view.findViewById(R.id.msg_scroll_view);
        nestedScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, final int scrollX, final int scrollY, int oldScrollX, int oldScrollY) {
                if (scrollY == (v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight())) {
                    //底部刷新
                    if(progressLayout.getVisibility()==View.GONE){
                        progressLayout.setVisibility(View.VISIBLE);
                    }
                    final Message message=new Message();

                    message.setContact_name("谢欣逗比言");
                    message.setLatest_content("你还欠我无数顿饭呢！");
                    //message.setLatest_time("19:61");
                    //message.setProfileID(R.drawable.sample1);
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            adapter.MessageAdd(adapter.getItemCount(),message);
                            adapter.notifyItemInserted(adapter.getItemCount());
                            progressLayout.setVisibility(View.GONE);
                        }
                    }, 1000);
                }
            }
        });

        //上拉刷新函数
        final SwipeRefreshLayout swipeRefreshLayout=(SwipeRefreshLayout)view.findViewById(R.id.swipe_message_recycler);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {   //顶部刷新
                Message message=new Message();
                message.setContact_name("谢欣逗比言");
                message.setLatest_content("你还欠我无数顿饭呢！");
                //message.setLatest_time("19:61");
                //message.setProfileID(R.drawable.sample1);
                adapter.MessageAdd(0,message);
                adapter.notifyItemInserted(0);
                messageRecyclerView.scrollToPosition(0);
                swipeRefreshLayout.setRefreshing(false);
            }
        });
        swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorWhite));
        swipeRefreshLayout.setProgressBackgroundColorSchemeColor(getResources().getColor(R.color.colorPrimary));

        return view;
    }

//    public void setMessages(List<Message> messages){     //初始化recycler的list
//        //List<Message> messages=new ArrayList<>();
//
//
//        adapter.setAdapter(messages);
//        return ;
//    }

    public class MessageAdapter extends RecyclerView.Adapter<MsgFragment.MessageAdapter.ViewHolder>{
        private List<Message> mMessageList;

        class ViewHolder extends RecyclerView.ViewHolder{
            TextView messageNickName;
            TextView messageContent;
            TextView messageTime;
            ImageView messageProfile; //头像 类型为int 之后按照后端需求修改 暂时引用drawable中的已有图片资源，即int
            ImageView messageTips;
            Button messageDel;
            LinearLayout messageLinear;

            public ViewHolder(View view){
                super(view);
                messageNickName=(TextView)view.findViewById(R.id.message_nickname);
                messageContent=(TextView)view.findViewById(R.id.message_content);
                messageTime=(TextView)view.findViewById(R.id.message_time);
                messageProfile=(ImageView)view.findViewById(R.id.message_profile);
                messageTips=(ImageView)view.findViewById(R.id.message_tips);
                messageDel=(Button)view.findViewById(R.id.message_del);
                messageLinear=(LinearLayout)view.findViewById(R.id.message_linear);
            }
        }

        public MessageAdapter(){
            //mMessageList = null;
        }

        public void setAdapter(List<Message> list){
            mMessageList = list;
        }


        public void MessageAdd(int position,Message message){
            mMessageList.add(position,message);
            return;
        }

        public void LocationRemove(int position){
            mMessageList.remove(position);
            return;
        }

        @Override
        public MsgFragment.MessageAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
            View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.message_item,parent,false);
            final MsgFragment.MessageAdapter.ViewHolder holder=new MsgFragment.MessageAdapter.ViewHolder(view);

            //删除最近联系人
            holder.messageDel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position=holder.getAdapterPosition();
                    Message message=mMessageList.get(position);
                    mMessageList.remove(position);
                    notifyItemRemoved(position);
                    notifyItemRangeChanged(position,mMessageList.size());

                    presenter.delContact(message.getContact_id());
                }
            });

            holder.messageLinear.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    holder.messageTips.setVisibility(View.GONE);    //让新消息角标隐藏
                    int position=holder.getAdapterPosition();
                    Message message=mMessageList.get(position);
                    presenter.clickContact(message.getContact_id());
                    //获取头像 昵称 传值到聊天界面
                    String nickname=message.getContact_name();
                    String profile=message.getImage_path();
                    Intent intent=new Intent(view.getContext(),ChatActivity.class);
                    intent.putExtra("nickname",nickname);
                    intent.putExtra("profile",profile);
                    startActivity(intent);
                }
            });
            return holder;
        }

        @Override
        public void onBindViewHolder(MsgFragment.MessageAdapter.ViewHolder holder, int position){
            Message message=mMessageList.get(position);
            holder.messageNickName.setText(message.getContact_name());
            holder.messageContent.setText(message.getLatest_content());
            holder.messageTime.setText(String.valueOf(message.getLatest_time()));
            //holder.messageTips. 新消息角标设置
            //holder.messageProfile.setImageResource(message.getImage_path()); 解析图片路径
        }

        @Override
        public int getItemCount(){
            return mMessageList.size();
        }


    }

    public MessageAdapter getAdapter(){
        return adapter;
    }

}
