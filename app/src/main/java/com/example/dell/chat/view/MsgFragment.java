package com.example.dell.chat.view;

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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.dell.chat.R;
import com.example.dell.chat.bean.Message;
import com.example.dell.chat.bean.MyApplication;
import com.example.dell.chat.model.Chat.ChatModelImpl;
import com.example.dell.chat.presenter.MessagePresenter;
import com.example.dell.chat.tools.SwipeItemLayout;
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMMessageBody;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by Staroul on 2018/3/30.
 */

//聊天信息提示fragment
public class MsgFragment extends Fragment {


    private MessagePresenter presenter = new MessagePresenter(this);
    private MessageAdapter adapter=new MessageAdapter();;
    private RecyclerView messageRecyclerView;
    private EMMessageListener msgListener;


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view=inflater.inflate(R.layout.msg_fragment,container,false);

        Log.e("Fragment 1", "onCreateView");
        Log.e("chatmode",String.valueOf(MyApplication.getChattingMode()));
        Log.e("chat activity",MyApplication.getChatActivity()==null?"null":"not null");
        final LinearLayout progressLayout=(LinearLayout)view.findViewById(R.id.message_progress);
        progressLayout.setVisibility(View.VISIBLE);


        //动画 localFrag
        //final LayoutAnimationController controller = AnimationUtils.loadLayoutAnimation(getActivity(), R.anim.layout_animation_slide_right);
        //final RecyclerView messageRecyclerView=(RecyclerView)view.findViewById(R.id.message_recycler_view);
        //messageRecyclerView.setLayoutAnimation(controller);
        //messageRecyclerView.setAdapter(adapter);


        final LayoutAnimationController controller = AnimationUtils.loadLayoutAnimation(getActivity(), R.anim.layout_animation_slide_right);
        messageRecyclerView=(RecyclerView)view.findViewById(R.id.message_recycler_view);
        messageRecyclerView.setNestedScrollingEnabled(false);
        LinearLayoutManager layoutManager=new LinearLayoutManager(getActivity());
        messageRecyclerView.setLayoutManager(layoutManager);

        messageRecyclerView.addOnItemTouchListener(new SwipeItemLayout.OnSwipeItemTouchListener(getActivity()));

        MyApplication.setFrag(this);

        //设置适配器以及list用以显示数据
        //adapter = new MessageAdapter();
        presenter.dispContact();


        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                messageRecyclerView.setLayoutAnimation(controller);
                messageRecyclerView.setAdapter(adapter);
                progressLayout.setVisibility(View.GONE);
            }
        }, 1000);



//        //底部刷新函数
//        final NestedScrollView nestedScrollView=(NestedScrollView)view.findViewById(R.id.msg_scroll_view);
//        nestedScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
//            @Override
//            public void onScrollChange(NestedScrollView v, final int scrollX, final int scrollY, int oldScrollX, int oldScrollY) {
//                if (scrollY == (v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight())) {
//                    //底部刷新
//                    if(progressLayout.getVisibility()==View.GONE){
//                        progressLayout.setVisibility(View.VISIBLE);
//                    }
//                    final Message message=new Message();
//
//                    message.setContact_name("谢欣逗比言");
//                    message.setLatest_content("你还欠我无数顿饭呢！");
//                    //message.setLatest_time("19:61");
//                    //message.setProfileID(R.drawable.sample1);
//                    handler.postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//                            adapter.MessageAdd(adapter.getItemCount(),message);
//                            adapter.notifyItemInserted(adapter.getItemCount());
//                            progressLayout.setVisibility(View.GONE);
//                        }
//                    }, 1000);
//                }
//            }
//        });

        //上拉刷新函数
        final SwipeRefreshLayout swipeRefreshLayout=(SwipeRefreshLayout)view.findViewById(R.id.swipe_message_recycler);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {   //顶部刷新
                presenter.dispContact();
                adapter.notifyDataSetChanged();
//                Message message=new Message();
//                message.setContact_name("谢欣逗比言");
//                message.setLatest_content("你还欠我无数顿饭呢！");
//                //message.setLatest_time("19:61");
//                //message.setProfileID(R.drawable.sample1);
//
//                adapter.MessageAdd(0,message);
//                adapter.notifyItemInserted(0);
//                messageRecyclerView.scrollToPosition(0);
                  swipeRefreshLayout.setRefreshing(false);
            }
        });
        swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorWhite));
        swipeRefreshLayout.setProgressBackgroundColorSchemeColor(getResources().getColor(R.color.colorPrimary));




        //测试
//        presenter.create(93);
//        presenter.create(90);
//        presenter.create(93);
//        presenter.create(90);
//        presenter.create(95);


        return view;
    }



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
                    MyApplication.setChattingMode(message.getContact_id());
                    presenter.delContact(message.getContact_id()); //在本地数据库中删除

                }
            });

            holder.messageLinear.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    holder.messageTips.setVisibility(View.GONE);    //让新消息角标隐藏
                    int position=holder.getAdapterPosition();
                    Message message=mMessageList.get(position);

                    MyApplication.setChattingMode(message.getContact_id());
                    Log.e("chat mode","it's set");

                    presenter.clickContact(message.getContact_id()); //在本地数据库去除角标
                    //获取头像 昵称 联系人id 传值到聊天界面
                    String nickname=message.getContact_name();
                    String profile=message.getImage_path();
                    int contact_id = message.getContact_id();
                    Intent intent=new Intent(view.getContext(),ChatActivity.class);
                    intent.putExtra("nickname",nickname);
                    intent.putExtra("profile",profile);
                    intent.putExtra("contact_id",contact_id);
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
            if(message.getLatest_time()==0){
                holder.messageTime.setText("");
            }
            else {
                Date date = new Date(message.getLatest_time());
                DateFormat df = new SimpleDateFormat("HH:mm");
                String time = df.format(date);
                holder.messageTime.setText(String.valueOf(time));//时间处理
            }

            if(message.getUnread()==0){//新消息角标设置
                holder.messageTips.setVisibility(View.GONE);
            }
            else {
                holder.messageTips.setVisibility(View.VISIBLE);
            }
            Glide.with(getActivity()).load(message.getImage_path()).thumbnail(0.1f).into((holder).messageProfile);
            //解析头像图片路径
        }

        @Override
        public int getItemCount(){
            return mMessageList.size();
        }


    }

    public MessageAdapter getAdapter(){
        return adapter;
    }

    public  RecyclerView getMessageRecyclerView(){
        return messageRecyclerView;
    }

    public  MessagePresenter getPresenter(){
        return presenter;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.e("Fragment 1", "onDestroyView");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e("Fragment 1", "onDestroy");
    }

    @Override
    public void onStart() {
        super.onStart();

        Log.e("Fragment 1", "onStart");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.e("Fragment 1", "onResume");
    }

    @Override
    public void onPause() {
        super.onPause();
        //EMClient.getInstance().chatManager().removeMessageListener(msgListener);
        Log.e("Fragment 1", "onPause");
    }

    @Override
    public void onStop() {
        super.onStop();

        Log.e("Fragment 1", "onStop");
    }

    //动画
    public void act() {
        final LayoutAnimationController controller = AnimationUtils.loadLayoutAnimation(getActivity(), R.anim.layout_animation_slide_right);
        messageRecyclerView.setLayoutAnimation(controller);
    }
}
