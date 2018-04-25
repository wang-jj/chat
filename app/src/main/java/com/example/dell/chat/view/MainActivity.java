package com.example.dell.chat.view;


import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.internal.BottomNavigationItemView;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
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

import com.bumptech.glide.Glide;
import com.example.dell.chat.R;
import com.example.dell.chat.base.BaseActivity;
import com.example.dell.chat.bean.MyApplication;
import com.example.dell.chat.bean.User;
import com.example.dell.chat.presenter.LoginPresenter;
import com.example.dell.chat.presenter.MainPresenter;

import javax.microedition.khronos.opengles.GL;

public class MainActivity extends BaseActivity<MainActivity,MainPresenter<MainActivity>>
        implements NavigationView.OnNavigationItemSelectedListener {


    private TextView textView;
    private BottomNavigationView bottomNavigationView;
    private HomeFragment homeFragment;
    private LocalFragment localFragment;
    private MsgFragment msgFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
        Log.e("main",String.valueOf(u==null));
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
    }

    //切换fragment函数
    private void setFragment(android.support.v4.app.Fragment fragment){
        android.support.v4.app.FragmentManager fragmentManager=getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction transaction=fragmentManager.beginTransaction();
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        transaction.replace(R.id.change_fragment,fragment);
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e("MAIN", "onDestroy: " );
    }
}
