package com.example.dell.chat.view;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
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

import com.bumptech.glide.Glide;
import com.example.dell.chat.R;
import com.example.dell.chat.bean.Chat;
import com.example.dell.chat.bean.Contact;
import com.example.dell.chat.bean.MyApplication;
import com.example.dell.chat.model.Callback;
import com.example.dell.chat.model.Execute;
import com.example.dell.chat.presenter.ChatPresenter;
import com.example.dell.chat.base.BaseActivity;
import com.example.dell.chat.tools.Dao;
import com.example.dell.chat.tools.IsInternet;
import com.example.dell.chat.tools.ThreadTask;
import com.google.gson.Gson;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;

import java.io.IOException;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

//聊天内容Activity
public class ChatActivity extends AppCompatActivity {

    private String profile;
    private int contact_id;
    public ChatAdapter adapter;
    public ChatPresenter presenter  = new ChatPresenter(this, MyApplication.getFrag());
    public RecyclerView chatRecyclerView;
    public EditText editText;
    public String GetUserInfo = "http://119.23.255.222/android/useridinfo.php";
    public Intent mIntent ;
    public Boolean internetBool=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        //从MsgFragment跳转过来的Intent 使用nickname获取昵称 profile获取头像
        Intent intent_msg=getIntent();
        String nickname=intent_msg.getStringExtra("nickname");
        final int contact_id = intent_msg.getIntExtra("contact_id",-1);
        this.contact_id = contact_id;
        this.profile=intent_msg.getStringExtra("profile");     //合并项目时 设置图片的方式改变，记得去掉注释 修改设置图片的方法

        mIntent = new Intent(ChatActivity.this, AlbumActivity.class);

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

        chatRecyclerView=(RecyclerView)findViewById(R.id.chat_recycler_view);
        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        chatRecyclerView.setLayoutManager(layoutManager);


        adapter = new ChatAdapter();
        presenter.showChat(contact_id);  //初始化list
        chatRecyclerView.setAdapter(adapter);

        MyApplication.setChatActivity(this);
        Log.e("chat activity","it's recorded");

        //进入activity后默认跳转到最新的聊天记录
            chatRecyclerView.scrollToPosition(adapter.getItemCount() - 1);


        //设置点击头像跳转至个人资料Activity
        ImageView imageView_chat=(ImageView)findViewById(R.id.chat_data);
        imageView_chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //获取对方用户个人资料
                presenter.getInfo(contact_id);
                //startActivity(mIntent);
                return;
            }
        });

        //设置输入框的点击事件 当点击时 弹出输入法 并且让recyclerview滚动到底部
        editText=(EditText)findViewById(R.id.chat_input);
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

//        //顶部的刷新函数 上滑时的刷新逻辑放入此函数
//        final SwipeRefreshLayout swipeRefreshLayout=(SwipeRefreshLayout)findViewById(R.id.swipe_chat_recycler);
//        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {   //顶部刷新
//                Chat chat=new Chat();
//                chat.setContent("你才欠我十顿饭呢.");
//                //chat.setProfileID(profile);   使用Intent传过来的值作为默认头像
//                chat.setType(1);
//                adapter.ChatAdd(0,chat);
//                adapter.notifyItemInserted(0);
//                chatRecyclerView.scrollToPosition(0);
//                swipeRefreshLayout.setRefreshing(false);
//            }
//        });
//        //刷新样式设置
//        swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorWhite));
//        swipeRefreshLayout.setProgressBackgroundColorSchemeColor(getResources().getColor(R.color.colorPrimary));

        //发送按钮 点击函数 发送信息的逻辑
        ImageButton imageButton_send=(ImageButton)findViewById(R.id.chat_send);
        imageButton_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                if(IsInternet.isNetworkAvailable(getApplication())) {
                    if (!editText.getText().toString().isEmpty()) {
                        Chat chat = new Chat();
                        chat.setContent(editText.getText().toString());
                        chat.setUser_id(MyApplication.getUser().getUser_id());
                        chat.setContact_id(contact_id);
                        chat.setTime(System.currentTimeMillis());
                        chat.setType(0);
                        //更新当前list
                        adapter.ChatAdd(adapter.getItemCount(), chat);
                        if (adapter.getItemCount() > 0) {
                            adapter.notifyItemInserted(adapter.getItemCount() - 1);
                            chatRecyclerView.smoothScrollToPosition(adapter.getItemCount() - 1);
                        }
                        //环信发送 以及更新到本地数据库
                        presenter.send(contact_id, editText.getText().toString(), 0);
                        editText.setText("");


                    }
                }
//                else {
//                    Toast.makeText(MyApplication.getChatActivity(),"网络连接异常。",Toast.LENGTH_SHORT).show();
//                }
//                }

        });

        //图片按钮 点击函数 发送图片的逻辑
        ImageButton imageButton_picture_send=(ImageButton)findViewById(R.id.chat_picture);
        imageButton_picture_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                isConnection();
//                //Log.e("internet",String.valueOf(internetBool));
//                if(internetBool) {
//                    createSelect();
//                }
//                else {
//                    Toast.makeText(MyApplication.getChatActivity(),"网络连接异常。",Toast.LENGTH_SHORT).show();
//                }
                createSelect();

            }
        });
    }


    //recyclerview的adapter定义
    public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
        private List<Chat> mChatList;
        private static final int TYPE_SENT=0;
        private static final int TYPE_RECEIVED=1;
        private static final int TYPE_SENT_IMAGE=2;
        private static final int TYPE_RECEIVED_IMAGE=3;

        public List<Chat> getChatList() {
            return mChatList;
        }

        public void setChatList(List<Chat> chatList) {
            mChatList = chatList;
        }

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
                        //获取对方用户个人资料
                        presenter.getInfo(contact_id);
                    }
                });
            }else if(viewType==TYPE_SENT_IMAGE){
                view=LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_item_right_image,parent,false);
                holder=new RightImageViewHolder(view);
//                ((RightImageViewHolder)holder).chatImage.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//
//                    }
//                });
            }else{
                view=LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_item_left_image,parent,false);
                holder=new LeftImageViewHolder(view);
//                ((LeftImageViewHolder)holder).chatImage.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//
//                    }
//                });
                ((LeftImageViewHolder)holder).chatProfile.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //跳转个人资料activity
                        //获取对方用户个人资料
                        presenter.getInfo(contact_id);
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
                Glide.with(getApplication()).load(profile).thumbnail(0.1f).into(((LeftViewHolder)holder).chatProfile);
            }else if(holder instanceof RightViewHolder){
                Chat chat=mChatList.get(position);
                ((RightViewHolder)holder).chatContent.setText(chat.getContent());
                Glide.with(getApplication()).load(MyApplication.getUser().getImage_path()).thumbnail(0.1f).into(((RightViewHolder)holder).chatProfile);
            }else if(holder instanceof LeftImageViewHolder){
                Chat chat=mChatList.get(position);
                Glide.with(getApplication()).load( chat.getContent()).thumbnail(0.1f).into(((LeftImageViewHolder)holder).chatImage);
                Glide.with(getApplication()).load(profile).thumbnail(0.1f).into(((LeftImageViewHolder)holder).chatProfile);
            }else{
                Chat chat=mChatList.get(position);
                Glide.with(getApplication()).load(chat.getContent()).thumbnail(0.1f).into(((RightImageViewHolder)holder).chatImage);
                Glide.with(getApplication()).load(MyApplication.getUser().getImage_path()).thumbnail(0.1f).into(((RightImageViewHolder)holder).chatProfile);
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

    public RecyclerView getChatRecyclerView(){
        return chatRecyclerView;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        MyApplication.setChattingMode(0);
        Log.e("chat mode","it's cleared");
        MyApplication.setChatActivity(null);
        Log.e("chat activity","it's cleared");
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

    public String getpath(LocalMedia a){
        String path=a.getPath();
        if(path.substring(path.length()-3).equals("gif")){
            return path;
        }
        if(a.isCut()){//裁剪了
            path=a.getCutPath();
        }if(a.isCompressed()){//压缩了
            path=a.getCompressPath();
        }
        return  path;
    }

    public void createSelect(){
        PictureSelector.create(ChatActivity.this).openGallery(PictureMimeType.ofImage()).theme(R.style.picture_white_style).enableCrop(true).compress(true).minimumCompressSize(200).previewImage(true).isGif(true).maxSelectNum(1).isDragFrame(true).rotateEnabled(true).hideBottomControls(false).forResult(PictureConfig.CHOOSE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PictureConfig.CHOOSE_REQUEST:
                    List<LocalMedia> selectList = PictureSelector.obtainMultipleResult(data);
                    String path=getpath(selectList.get(0));//获得图片路径
                    Log.e("path", path);
                    if(path==null||path.equals("")){
                        Log.e("send image","select no image");
                    }
                    else {
                        //更新当前list
                        Chat chat = new Chat();
                        chat.setType(2);
                        chat.setContent(path);
                        chat.setUser_id(MyApplication.getUser().getUser_id());
                        chat.setContact_id(contact_id);
                        chat.setTime(System.currentTimeMillis());
                        adapter.ChatAdd(adapter.getItemCount(), chat);
                        adapter.notifyItemInserted(adapter.getItemCount() - 1);

                        chatRecyclerView.smoothScrollToPosition(adapter.getItemCount() - 1);
                        presenter.send(contact_id, path, 2); //在第二个参数要换为发送的图片路径
                    }

                    break;
            }
        }
    }

    public void connectionNetwork(final Callback<Boolean> callback) {
        ThreadTask t = new ThreadTask<Void,Void,Void>(callback, new Execute<Boolean>() {
            @Override
            public Boolean doExec() {
                boolean result = false;
                HttpURLConnection httpUrl = null;
                try {
                    httpUrl = (HttpURLConnection) new URL("http://www.baidu.com").openConnection();
                    httpUrl.setConnectTimeout(5);
                    httpUrl.connect();
                    result = true;
                } catch (IOException e) {
                } finally {
                    if (null != httpUrl) {
                        httpUrl.disconnect();
                    }
                    httpUrl = null;
                }
                return result;
            }
        });
        t.execute();

    }

    public void isConnection(){
        connectionNetwork(new Callback<Boolean>() {
            @Override
            public void execute(Boolean datas) {
                internetBool = datas;

                Log.e("internet",String.valueOf(internetBool));
                return ;
            }
        });
    }
}
