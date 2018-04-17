package com.example.dell.chat.view;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.dell.chat.R;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;

import java.util.ArrayList;
import java.util.List;



//动态发布Activity
public class PublishActivity extends AppCompatActivity {

    private int image_num=0;
    private int CHOOSE_REQUEST=5;
    ImageView imageView1;
    ImageView imageView2;
    ImageView imageView3;
    List<LocalMedia> selectList =new ArrayList<LocalMedia>();

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

        imageView1=(ImageView)findViewById(R.id.publish_image1);
        imageView2=(ImageView)findViewById(R.id.publish_image2);
        imageView3=(ImageView)findViewById(R.id.publish_image3);

        GradientDrawable groupDrawable = (GradientDrawable) imageView1.getBackground();
        groupDrawable.setStroke(5, Color.parseColor("#cccccc"));
        GradientDrawable mGroupDrawable = (GradientDrawable) imageView2.getBackground();
        mGroupDrawable.setStroke(5, Color.parseColor("#ffffff"));
        mGroupDrawable = (GradientDrawable) imageView3.getBackground();
        mGroupDrawable.setStroke(5, Color.parseColor("#ffffff"));

        imageView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(image_num==0) {
                    GradientDrawable mGroupDrawable = (GradientDrawable) imageView1.getBackground();
                    mGroupDrawable.setStroke(5, Color.parseColor("#ffffff"));
                    /*
                    mGroupDrawable = (GradientDrawable) imageView2.getBackground();
                    mGroupDrawable.setStroke(5, Color.parseColor("#cccccc"));
                    imageView2.setImageResource(R.drawable.ic_wallpaper_24dp);
                    */
                    PictureSelector.create(PublishActivity.this).openGallery(PictureMimeType.ofImage()).isGif(true).maxSelectNum(3).enableCrop(true).isDragFrame(false).rotateEnabled(true).hideBottomControls(true).forResult(PictureConfig.CHOOSE_REQUEST);
                }
                //设置图片一
                //imageView1.setImageResource(R.drawable.sample1);
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
                        /*
                        mGroupDrawable = (GradientDrawable) imageView3.getBackground();
                        mGroupDrawable.setStroke(5, Color.parseColor("#cccccc"));
                        imageView3.setImageResource(R.drawable.ic_wallpaper_24dp);
                        image_num+=1;
                        */
                        PictureSelector.create(PublishActivity.this).openGallery(PictureMimeType.ofImage()).isGif(true).maxSelectNum(2).forResult(PictureConfig.CHOOSE_REQUEST);
                    }
                    //设置图片二
                    //imageView2.setImageResource(R.drawable.sample2);
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
                        PictureSelector.create(PublishActivity.this).openGallery(PictureMimeType.ofImage()).isGif(true).maxSelectNum(1).forResult(PictureConfig.CHOOSE_REQUEST);
                    }
                    //设置图片三
                    //imageView3.setImageResource(R.drawable.sample2);
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e("PublishActivity","yes");
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PictureConfig.CHOOSE_REQUEST:
                    List<LocalMedia> a = PictureSelector.obtainMultipleResult(data);
                    if(a!=null){
                        selectList.addAll(a);
                    }
                    if(selectList==null){//没选择
                        GradientDrawable groupDrawable = (GradientDrawable) imageView1.getBackground();
                        groupDrawable.setStroke(5, Color.parseColor("#cccccc"));
                    }else if(selectList.size()==1){
                        image_num=1;
                        setbackground(imageView2);
                        Glide.with(PublishActivity.this).load(selectList.get(0).getPath()).into(imageView1);
                    }else if(selectList.size()==2){
                        image_num=2;
                        setbackground(imageView3);
                        Glide.with(PublishActivity.this).load(selectList.get(0).getPath()).into(imageView1);
                        Glide.with(PublishActivity.this).load(selectList.get(1).getPath()).into(imageView2);
                    }else if(selectList.size()==3){
                        image_num=3;
                        Glide.with(PublishActivity.this).load(selectList.get(0).getPath()).into(imageView1);
                        Glide.with(PublishActivity.this).load(selectList.get(1).getPath()).into(imageView2);
                        Glide.with(PublishActivity.this).load(selectList.get(2).getPath()).into(imageView3);
                    }
                    Log.e("PublishActivity", String.valueOf(image_num) );
            }
        }
    }
    public void setbackground(ImageView imageView){
        GradientDrawable mGroupDrawable = (GradientDrawable) imageView.getBackground();
        mGroupDrawable.setStroke(5, Color.parseColor("#cccccc"));
        imageView.setImageResource(R.drawable.ic_wallpaper_24dp);
    }
}