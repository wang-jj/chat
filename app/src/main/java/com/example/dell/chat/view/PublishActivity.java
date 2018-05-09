package com.example.dell.chat.view;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.dell.chat.R;
import com.example.dell.chat.base.BaseActivity;
import com.example.dell.chat.bean.MyApplication;
import com.example.dell.chat.bean.PersonalState;
import com.example.dell.chat.presenter.LoginPresenter;
import com.example.dell.chat.presenter.MomentPresenter;
import com.example.dell.chat.tools.translate;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.sqk.emojirelease.Emoji;
import com.sqk.emojirelease.FaceFragment;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;



//动态发布Activity
public class PublishActivity extends BaseActivity<PublishActivity,MomentPresenter<PublishActivity>>  {

    private int image_num=0;
    private int CHOOSE_REQUEST=5;
    ImageView imageView1;
    ImageView imageView2;
    ImageView imageView3;
    List<LocalMedia> selectList =new ArrayList<LocalMedia>();
    EditText editText=null;
    private int flat=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish);
        presenter.setLocation();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        editText=(EditText)findViewById(R.id.moment_context);
        final ImageView imageView=(ImageView)findViewById(R.id.publish_pub);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(flat!=0){
                    return;
                }
                //发布动态逻辑
                //onBackPressed();
                PersonalState personalState=new PersonalState();
                personalState.setContent(editText.getText().toString());
                personalState.setImg_type(selectList.size());
                personalState.setUser_id(MyApplication.getUser().getUser_id());
                if(selectList.size()>0){
                    personalState.setImage1ID(getpath(selectList.get(0)));
                }
                if(selectList.size()>1){
                    personalState.setImage2ID(getpath(selectList.get(1)));
                }
                if(selectList.size()>2){
                    personalState.setImage3ID(getpath(selectList.get(2)));
                }
                presenter.Publish(personalState);
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
                    creatSelect();
                }else if(image_num>0){
                    PictureSelector.create(PublishActivity.this).externalPicturePreview(0,selectList);
                }
            }
        });
        imageView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(image_num<1){
                    //小于一时，无动作
                } else if(image_num>=1){
                    if(image_num==1){//选择第二张
                        GradientDrawable mGroupDrawable = (GradientDrawable) imageView2.getBackground();
                        mGroupDrawable.setStroke(5, Color.parseColor("#ffffff"));
                       creatSelect();
                    }else {//查看第二张
                        PictureSelector.create(PublishActivity.this).externalPicturePreview(1,selectList);
                    }
                }
            }
        });
        imageView3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(image_num<2){
                    //小于二时，无动作
                }else if(image_num>=2){
                    if(image_num==2){//选择第三张
                        GradientDrawable mGroupDrawable = (GradientDrawable) imageView3.getBackground();
                        mGroupDrawable.setStroke(5, Color.parseColor("#ffffff"));
                        creatSelect();
                    }else {//查看第三章
                        PictureSelector.create(PublishActivity.this).externalPicturePreview(2,selectList);
                    }
                }
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
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
                        RequestOptions requestOptions=new RequestOptions().centerCrop();
                        String path=getpath(selectList.get(0));
                        Glide.with(PublishActivity.this).load(path).apply(requestOptions).into(imageView1);
                    }else if(selectList.size()==2){
                        image_num=2;
                        setbackground(imageView3);
                        RequestOptions requestOptions=new RequestOptions().centerCrop();
                        String path1=getpath(selectList.get(0));
                        String path2=getpath(selectList.get(1));
                        Glide.with(PublishActivity.this).load(path1).apply(requestOptions).into(imageView1);
                        Glide.with(PublishActivity.this).load(path2).apply(requestOptions).into(imageView2);
                    }else if(selectList.size()==3){
                        image_num=3;
                        String path1=getpath(selectList.get(0));
                        String path2=getpath(selectList.get(1));
                        String path3=getpath(selectList.get(2));
                        Glide.with(PublishActivity.this).load(path1).into(imageView1);
                        Glide.with(PublishActivity.this).load(path2).into(imageView2);
                        Glide.with(PublishActivity.this).load(path3).into(imageView3);
                    }
            }
        }
    }
    public void setbackground(ImageView imageView){
        GradientDrawable mGroupDrawable = (GradientDrawable) imageView.getBackground();
        mGroupDrawable.setStroke(5, Color.parseColor("#cccccc"));
        imageView.setImageResource(R.drawable.ic_wallpaper_24dp);
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

    public void creatSelect(){
        PictureSelector.create(PublishActivity.this).openGallery(PictureMimeType.ofImage()).theme(R.style.picture_white_style).enableCrop(true).compress(true).minimumCompressSize(200).previewImage(true).isGif(true).maxSelectNum(3).isDragFrame(true).rotateEnabled(true).hideBottomControls(false).forResult(PictureConfig.CHOOSE_REQUEST);
    }

    @Override
    protected MomentPresenter createPresenter() {
        return new MomentPresenter();
    }

    public int getFlat() {
        return flat;
    }

    public void setFlat(int flat) {
        this.flat = flat;
    }

}