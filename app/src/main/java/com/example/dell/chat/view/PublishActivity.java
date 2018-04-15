package com.example.dell.chat.view;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

import com.example.dell.chat.R;

//动态发布Activity
public class PublishActivity extends AppCompatActivity {

    private int image_num=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        final ImageView imageView=(ImageView)findViewById(R.id.publish_pub);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //发布动态逻辑
                onBackPressed();
            }
        });

        final ImageView imageView1=(ImageView)findViewById(R.id.publish_image1);
        final ImageView imageView2=(ImageView)findViewById(R.id.publish_image2);
        final ImageView imageView3=(ImageView)findViewById(R.id.publish_image3);

        GradientDrawable groupDrawable = (GradientDrawable) imageView1.getBackground();
        groupDrawable.setStroke(5, Color.parseColor("#cccccc"));

        imageView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(image_num==0) {
                    GradientDrawable mGroupDrawable = (GradientDrawable) imageView1.getBackground();
                    mGroupDrawable.setStroke(5, Color.parseColor("#ffffff"));
                    mGroupDrawable = (GradientDrawable) imageView2.getBackground();
                    mGroupDrawable.setStroke(5, Color.parseColor("#cccccc"));
                    imageView2.setImageResource(R.drawable.ic_wallpaper_24dp);
                    image_num+=1;
                }
                //设置图片一
                imageView1.setImageResource(R.drawable.sample1);
            }
        });
        imageView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(image_num<1){
                    //小于一时，无动作
                } else if(image_num>=1){
                    if(image_num==1){
                        GradientDrawable mGroupDrawable = (GradientDrawable) imageView2.getBackground();
                        mGroupDrawable.setStroke(5, Color.parseColor("#ffffff"));
                        mGroupDrawable = (GradientDrawable) imageView3.getBackground();
                        mGroupDrawable.setStroke(5, Color.parseColor("#cccccc"));
                        imageView3.setImageResource(R.drawable.ic_wallpaper_24dp);
                        image_num+=1;
                    }
                    //设置图片二
                    imageView2.setImageResource(R.drawable.sample2);
                }
            }
        });
        imageView3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(image_num<2){
                    //小于二时，无动作
                }else if(image_num>=2){
                    if(image_num==2){
                        GradientDrawable mGroupDrawable = (GradientDrawable) imageView3.getBackground();
                        mGroupDrawable.setStroke(5, Color.parseColor("#ffffff"));
                        image_num+=1;
                    }
                    //设置图片三
                    imageView3.setImageResource(R.drawable.sample2);
                }
            }
        });
    }
}
