package com.example.dell.chat.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dell.chat.R;
import com.example.dell.chat.bean.Chat;
import com.example.dell.chat.bean.MyApplication;
import com.example.dell.chat.presenter.ChatPresenter;
import com.example.dell.chat.base.BaseActivity;
import java.util.ArrayList;
import java.util.List;

//聊天内容Activity
public class ChatActivity extends AppCompatActivity {

    private int profile;
    private int contact_id;
    public ChatAdapter adapter;
    public ChatPresenter presenter  = new ChatPresenter(this, MyApplication.getFrag());;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        //从MsgFragment跳转过来的Intent 使用nickname获取昵称 profile获取头像
        Intent intent_msg=getIntent();
        String nickname=intent_msg.getStringExtra("nickname");
        final int contact_id = intent_msg.getIntExtra("contact_id",-1);
        this.contact_id = contact_id;
        //profile=intent_msg.getIntExtra("profile",-1);     //合并项目时 设置图片的方式改变，记得去掉注释 修改设置图片的方法



        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //初始化标题栏名称 使用nickname
        toolbar.setTitle(nickname);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        final RecyclerView chatRecyclerView=(RecyclerView)findViewById(R.id.chat_recycler_view);
        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        chatRecyclerView.setLayoutManager(layoutManager);


        adapter = new ChatAdapter();
        presenter.showChat(contact_id);  //初始化list
        chatRecyclerView.setAdapter(adapter);

        MyApplication.setChatActivity(this);

        //进入activity后默认跳转到最新的聊天记录
            chatRecyclerView.scrollToPosition(adapter.getItemCount() - 1);


        //设置点击头像跳转至个人资料Activity
        ImageView imageView_chat=(ImageView)findViewById(R.id.chat_data);
        imageView_chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //跳转个人资料activity
                Intent intent=new Intent(ChatActivity.this,AlbumActivity.class);
                startActivity(intent);
            }
        });

        //设置输入框的点击事件 当点击时 弹出输入法 并且让recyclerview滚动到底部
        final EditText editText=(EditText)findViewById(R.id.chat_input);
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
                //延迟滚动以达到在输入法弹出时 recyclerview 能够滚动到底部
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if(adapter.getItemCount()>0) {
                            chatRecyclerView.scrollToPosition(adapter.getItemCount() - 1 );
                        }
                    }
                }, 200);
            }
        });

        //顶部的刷新函数 上滑时的刷新逻辑放入此函数
        final SwipeRefreshLayout swipeRefreshLayout=(SwipeRefreshLayout)findViewById(R.id.swipe_chat_recycler);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {   //顶部刷新
                Chat chat=new Chat();
                chat.setContent("你才欠我十顿饭呢.");
                //chat.setProfileID(profile);   使用Intent传过来的值作为默认头像
                chat.setType(1);
                adapter.ChatAdd(0,chat);
                adapter.notifyItemInserted(0);
                chatRecyclerView.scrollToPosition(0);
                swipeRefreshLayout.setRefreshing(false);
            }
        });
        //刷新样式设置
        swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorWhite));
        swipeRefreshLayout.setProgressBackgroundColorSchemeColor(getResources().getColor(R.color.colorPrimary));

        //发送按钮 点击函数 发送信息的逻辑
        ImageButton imageButton_send=(ImageButton)findViewById(R.id.chat_send);
        imageButton_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!editText.getText().toString().isEmpty()){
                    Chat chat = new Chat();
                    chat.setContent(editText.getText().toString());
                    //chat.setProfileID(R.drawable.
                    // );
                    chat.setType(0);
                    adapter.ChatAdd(adapter.getItemCount(), chat);
                    if(adapter.getItemCount()>0) {
                        adapter.notifyItemInserted(adapter.getItemCount() - 1);
                        chatRecyclerView.smoothScrollToPosition(adapter.getItemCount() - 1 );
                    }
                    presenter.send(contact_id,editText.getText().toString(),0);//
                    editText.setText("");


                }
            }
        });

        //图片按钮 点击函数 发送蹄片的逻辑
        ImageButton imageButton_picture_send=(ImageButton)findViewById(R.id.chat_picture);
        imageButton_picture_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Chat chat=new Chat();
                //chat.setProfileID(R.drawable.profile);
                chat.setType(2);
                //chat.setImage(R.drawable.profile);
                adapter.ChatAdd(adapter.getItemCount(),chat);
                adapter.notifyItemInserted(adapter.getItemCount()-1);
                chatRecyclerView.smoothScrollToPosition(adapter.getItemCount()-1);
                //presenter.send(contact_id,editText.getText().toString(),2); //在第二个参数要换为发送的图片路径
            }
        });
    }

//    private List<Chat> getChat(){   //初始化recyclerview的list
//        List<Chat> chatList=new ArrayList<>();
//        for(int i=1;i<=20;i++){
//            Chat chat=new Chat();
//            if((i-1)%4==0){//发送文字初始化
//                chat.setContent("你才欠我"+i+"顿饭呢.");
//                //chat.setProfileID(R.drawable.profile);
//                chat.setType(0);
//            }else if((i-1)%4==1){//接收文字初始化
//                chat.setContent("你才欠我"+i+"顿饭呢.");
//                //chat.setProfileID(R.drawable.sample1);    使用Intent传过来的值作为默认头像
//                chat.setType(1);
//            }else if((i-1)%4==2){//发送图片初始化
//                //chat.setProfileID(R.drawable.profile);
//                chat.setType(2);
//                //chat.setImage(R.drawable.profile);
//            }else{//接收图片初始化
//                //chat.setProfileID(R.drawable.sample1);    使用Intent传过来的值作为默认头像
//                chat.setType(3);
//                //chat.setImage(R.drawable.profile);
//            }
//            chatList.add(chat);
//        }
//
//        return chatList;
//    }

    //recyclerview的adapter定义
    public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
        private List<Chat> mChatList;
        private static final int TYPE_SENT=0;
        private static final int TYPE_RECEIVED=1;
        private static final int TYPE_SENT_IMAGE=2;
        private static final int TYPE_RECEIVED_IMAGE=3;

        public ChatAdapter(){
            //mChatList=chatList;
        }

        public void setAdapter(List<Chat> chatList){
            mChatList = chatList;
            return;
        }

        public void ChatAdd(int position,Chat chat){
            mChatList.add(position,chat);
            return;
        }

        public void ChatRemove(int position){
            mChatList.remove(position);
            return;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
            final View view;
            final RecyclerView.ViewHolder holder;

            if(viewType==TYPE_SENT){
                view=LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_item_right,parent,false);
                holder=new RightViewHolder(view);
            }else if(viewType==TYPE_RECEIVED){
                view=LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_item_left,parent,false);
                holder=new LeftViewHolder(view);
                ((LeftViewHolder)holder).chatProfile.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //跳转个人资料activity
                        Intent intent=new Intent(ChatActivity.this,AlbumActivity.class);
                        startActivity(intent);
                    }
                });
            }else if(viewType==TYPE_SENT_IMAGE){
                view=LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_item_right_image,parent,false);
                holder=new RightImageViewHolder(view);
            }else{
                view=LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_item_left_image,parent,false);
                holder=new LeftImageViewHolder(view);
                ((LeftImageViewHolder)holder).chatProfile.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //跳转个人资料activity
                        Intent intent=new Intent(ChatActivity.this,AlbumActivity.class);
                        startActivity(intent);
                    }
                });
            }
            return holder;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position){
            if(holder instanceof LeftViewHolder){
                Chat chat=mChatList.get(position);
                ((LeftViewHolder)holder).chatContent.setText(chat.getContent());
                ((LeftViewHolder)holder).chatProfile.setImageResource(profile);
            }else if(holder instanceof RightViewHolder){
                Chat chat=mChatList.get(position);
                ((RightViewHolder)holder).chatContent.setText(chat.getContent());
                //((RightViewHolder)holder).chatProfile.setImageResource(chat.getProfileID());
            }else if(holder instanceof LeftImageViewHolder){
                Chat chat=mChatList.get(position);
                //((LeftImageViewHolder)holder).chatImage.setImageResource(chat.getImage());
                ((LeftImageViewHolder)holder).chatProfile.setImageResource(profile);
            }else{
                Chat chat=mChatList.get(position);
                //((RightImageViewHolder)holder).chatImage.setImageResource(chat.getImage());
                //((RightImageViewHolder)holder).chatProfile.setImageResource(chat.getProfileID());
            }
        }

        @Override
        public int getItemViewType(int position){
            Chat chat=mChatList.get(position);
            if(chat.getType()==TYPE_SENT){
                return TYPE_SENT;
            }else if(chat.getType()==TYPE_RECEIVED){
                return TYPE_RECEIVED;
            }else if(chat.getType()==TYPE_SENT_IMAGE){
                return TYPE_SENT_IMAGE;
            }else{
                return TYPE_RECEIVED_IMAGE;
            }
        }

        @Override
        public int getItemCount(){
            return mChatList.size();
        }

        class LeftViewHolder extends RecyclerView.ViewHolder{
            TextView chatContent;
            ImageView chatProfile;

            public LeftViewHolder(View view){
                super(view);
                chatContent=(TextView)view.findViewById(R.id.chat_data);
                chatProfile=(ImageView)view.findViewById(R.id.chat_profile);
            }
        }

        class RightViewHolder extends RecyclerView.ViewHolder{
            TextView chatContent;
            ImageView chatProfile;

            public RightViewHolder(View view){
                super(view);
                chatContent=(TextView)view.findViewById(R.id.chat_data);
                chatProfile=(ImageView)view.findViewById(R.id.chat_profile);
            }
        }

        class LeftImageViewHolder extends RecyclerView.ViewHolder{
            ImageView chatImage;
            ImageView chatProfile;

            public LeftImageViewHolder(View view){
                super(view);
                chatImage=(ImageView)view.findViewById(R.id.chat_image);
                chatProfile=(ImageView)view.findViewById(R.id.chat_profile);
            }
        }

        class RightImageViewHolder extends RecyclerView.ViewHolder{
            ImageView chatImage;
            ImageView chatProfile;

            public RightImageViewHolder(View view){
                super(view);
                chatImage=(ImageView)view.findViewById(R.id.chat_image);
                chatProfile=(ImageView)view.findViewById(R.id.chat_profile);
            }
        }
    }
    public ChatAdapter getAdapter(){
        return adapter;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        MyApplication.setChattingMode(0);
        MyApplication.setChatActivity(null);
        Log.e("Chat", "onDestroy");
    }

    @Override
    public void onStart() {
        super.onStart();

        Log.e("Chat", "onStart");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.e("Chat", "onResume");
    }

    @Override
    public void onPause() {
        super.onPause();
        //EMClient.getInstance().chatManager().removeMessageListener(msgListener);
        Log.e("Chat", "onPause");
    }

    @Override
    public void onStop() {
        super.onStop();

        Log.e("Chat", "onStop");
    }
}
