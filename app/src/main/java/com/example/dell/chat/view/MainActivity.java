package com.example.dell.chat.view;


import android.Manifest;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.provider.SyncStateContract;
import android.support.annotation.NonNull;
import android.support.design.internal.BottomNavigationItemView;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.bumptech.glide.Glide;
import com.example.dell.chat.R;
import com.example.dell.chat.base.BaseActivity;
import com.example.dell.chat.base.PermissionListener;
import com.example.dell.chat.bean.MyApplication;
import com.example.dell.chat.bean.PersonalState;
import com.example.dell.chat.bean.User;
import com.example.dell.chat.db.PersonalStateDao;
import com.example.dell.chat.presenter.LoginPresenter;
import com.example.dell.chat.presenter.MainPresenter;
import com.example.dell.chat.tools.Dao;
import com.example.dell.chat.tools.Notify;
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMMessageBody;

import java.util.ArrayList;
import java.util.List;

import javax.microedition.khronos.opengles.GL;

import freemarker.template.utility.Constants;

public class MainActivity extends BaseActivity<MainActivity,MainPresenter<MainActivity>>
        implements NavigationView.OnNavigationItemSelectedListener {


    private TextView textView;
    private BottomNavigationView bottomNavigationView;
    private HomeFragment homeFragment;
    private LocalFragment localFragment;
    private MsgFragment msgFragment;
    private android.support.v4.app.Fragment currentFragment;
    private LocationClient locationClient;
    private EMMessageListener msgListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sendLocation();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //初始化侧滑drawer的数据
        User u= MyApplication.getUser();
        View headerView = navigationView.getHeaderView(0);
        ImageView imageView_drawer_profile=(ImageView)headerView.findViewById(R.id.drawer_profile);
        Glide.with(MainActivity.this).load(u.getImage_path()).into(imageView_drawer_profile);
        //imageView_drawer_profile.setImageResource(R.drawable.profile);
        TextView textView_drawer_nickname=(TextView)headerView.findViewById(R.id.drawer_nickname);
        textView_drawer_nickname.setText(u.getUser_name());
        TextView textView_drawer_school=(TextView)headerView.findViewById(R.id.drawer_school);
        textView_drawer_school.setText(u.getSchool());

        bottomNavigationView=(BottomNavigationView)findViewById(R.id.navigation);
        textView=(TextView)findViewById(R.id.toolbar_title);

        final ImageView imageView=(ImageView)findViewById(R.id.state_publish);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this,PublishActivity.class);
                startActivity(intent);
            }
        });

        final ImageView imageView1=(ImageView)findViewById(R.id.state_map);
        imageView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this,MapActivity.class);
                startActivity(intent);
            }
        });

        BottomNavigationMenuView menuView=(BottomNavigationMenuView)bottomNavigationView.getChildAt(0);
        View tab = menuView.getChildAt(2);
        BottomNavigationItemView itemView = (BottomNavigationItemView) tab;
        final View badge = LayoutInflater.from(this).inflate(R.layout.menu_tips, menuView, false);
        itemView.addView(badge);

        badge.setVisibility(View.VISIBLE);          //新消息通知角标 VISIBLE 表示有新通知

        homeFragment=new HomeFragment();
        localFragment=new LocalFragment();
        msgFragment=new MsgFragment();

        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        int id = item.getItemId();
                        if(id == R.id.bottom_home){
                            textView.setText("首页");
                            setFragment(homeFragment);              //点击切换fragment
                            imageView.setVisibility(View.VISIBLE);  //设置toolbar右上角的图标
                            imageView1.setVisibility(View.GONE);
                        }else if(id==R.id.bottom_local){
                            textView.setText("附近的人");
                            setFragment(localFragment);
                            imageView.setVisibility(View.GONE);
                            imageView1.setVisibility(View.VISIBLE);
                        }else if(id==R.id.bottom_message){
                            textView.setText("最近联系人");
                            setFragment(msgFragment);
                            imageView.setVisibility(View.GONE);
                            imageView1.setVisibility(View.GONE);
                            badge.setVisibility(View.GONE);         //新消息通知角标 GONE表示点击后让角标消失
                        }
                        return true;
                    }
                }
        );

        //设置初始碎片
        setFragment(homeFragment);
        imageView.setVisibility(View.VISIBLE);
        imageView1.setVisibility(View.GONE);

        // 创建接收消息监听器
        msgListener = new EMMessageListener() {
            @Override
            public void onMessageReceived(List<EMMessage> messages) {
                int i = messages.size();
                Log.e("message",String.valueOf(i));
                ArrayList<Integer>com=new ArrayList<>();
                for(int c = 0;c < i;c++){
                    EMMessage msg = messages.get(c);
                    String sender = msg.getFrom();
                    String receiver = msg.getTo();
                    long time = msg.getMsgTime();
                    EMMessageBody body = msg.getBody();
                    EMMessage.Type type = msg.getType();

                    //判断是否为评论的内容
                    if(body.toString().contains("这是一条评论11546325:")){
                        com.add(new Integer(c));
                        String result=body.toString();
                        result.substring(19);
                        String[]ss=new String[2];
                        ss=result.split(",.iojn",2);
                        PersonalStateDao personalStateDao=MyApplication.getDao().getPersonalStateDao();
                        PersonalState personalState=personalStateDao.queryBuilder().where(PersonalStateDao.Properties.Personalstate_id.eq(Integer.parseInt(ss[0])),PersonalStateDao.Properties.Holder_id.eq(MyApplication.getUser().getUser_id())).unique();
                        if(personalState!=null){
                            Notify.createCommentNofity(ss[1],personalState);
                        }
                    }

                    Log.d("time", String.valueOf(time));
                    Log.d("type", String.valueOf(type));
                    Log.d("body", body.toString());
                    Log.d("sender", sender);
                    Log.d("receiver", receiver);
                }
                for(int h=com.size()-1;h>=0;h--){
                    messages.remove(com.get(h).intValue());
                }
                //收到消息
                if(messages.size()>0){
                    msgFragment.getPresenter().receive(messages);
                }
                Log.e("mainactivity", "on start" );
            }

            @Override
            public void onCmdMessageReceived(List<EMMessage> messages) {
                //收到透传消息
            }

            @Override
            public void onMessageRead(List<EMMessage> messages) {
                //收到已读回执
            }

            @Override
            public void onMessageDelivered(List<EMMessage> message) {
                //收到已送达回执
            }
            @Override
            public void onMessageRecalled(List<EMMessage> messages) {
                //消息被撤回
            }

            @Override
            public void onMessageChanged(EMMessage message, Object change) {
                //消息状态变动
            }
        };
        EMClient.getInstance().chatManager().addMessageListener(msgListener);
        MyApplication.setListener(msgListener);
        Log.e("listener","it is set");

    }


    //切换fragment函数
    private void setFragment(android.support.v4.app.Fragment fragment){
        /*
        android.support.v4.app.FragmentManager fragmentManager=getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction transaction=fragmentManager.beginTransaction();
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        transaction.replace(R.id.change_fragment,fragment);
        transaction.commit();
        */

        if(fragment==currentFragment){
            return;
        }
        android.support.v4.app.FragmentManager fragmentManager=getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction transaction=fragmentManager.beginTransaction();
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        if(!fragment.isAdded()){
            transaction.add(R.id.change_fragment,fragment);
        }else {
            transaction.show(fragment);
            if(fragment==homeFragment){
                homeFragment.act();
            }
        }
        if(currentFragment!=null){
            transaction.hide(currentFragment);
        }
        currentFragment=fragment;
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        transaction.commit();

    }

    @Override
    public void onBackPressed() {
        /*
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
        */
        super.onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_data) {
            Intent intent=new Intent(MainActivity.this,DataActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_album) {
            Intent intent=new Intent(MainActivity.this,AlbumActivity.class);
            User u=MyApplication.getUser();
            Dao.SetIntent(intent,u.getUser_id(),u.getImage_path(),u.getUser_motto(),u.getUser_name(),u.getSchool());
            startActivity(intent);
        } else if (id == R.id.nav_collect) {
            Intent intent=new Intent(MainActivity.this,CollectActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_setting) {

        } else if (id == R.id.nav_cancel) {
            presenter.SignOut();
            //finish();
        }

        item.setChecked(false);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected MainPresenter createPresenter() {
        return new MainPresenter();
    }


    private void requestPermission(){
        requestRunTimePermission(new String[]{
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.READ_PHONE_STATE,
                        Manifest.permission.RECORD_AUDIO,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE}
                , new PermissionListener() {
                    @Override
                    public void onGranted() {  //所有权限授权成功

                    }

                    @Override
                    public void onGranted(List<String> grantedPermission) { //授权失败权限集合

                    }

                    @Override
                    public void onDenied(List<String> deniedPermission) { //授权成功权限集合

                    }
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        Log.e("locations", String.valueOf(1) );
        locationClient.stop();
    }

    @Override
    protected  void onStop(){
        super.onStop();
        //EMClient.getInstance().chatManager().removeMessageListener(msgListener);
        Log.e("mainactivity", "on stop" );
    }

    @Override
    protected void onStart(){
        super.onStart();

    }

    //位置
    private void sendLocation(){
        locationClient=new LocationClient(MyApplication.getContext());
        locationClient.registerLocationListener(new BDAbstractLocationListener() {//设置回调
            @Override
            public void onReceiveLocation(BDLocation bdLocation) {
                String addr = bdLocation.getAddrStr();//获取地址
                double latitude = bdLocation.getLatitude();    //获取纬度信息
                double longitude = bdLocation.getLongitude();    //获取经度信息
                Log.e("locations", String.valueOf(latitude) );
                Log.e("locations", String.valueOf(longitude) );
                MyApplication.setLongitude(longitude);
                MyApplication.setLatitude(latitude);
                presenter.SendLocation(latitude,longitude);
            }
        });
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        option.setIsNeedAddress(true);//地址
        option.setCoorType("BD09ll");
        option.setScanSpan(5*60*1000);
        option.setOpenGps(false);
        locationClient.setLocOption(option);
        locationClient.start();
    }
}
