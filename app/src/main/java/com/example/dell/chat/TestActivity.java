package com.example.dell.chat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.bumptech.glide.Glide;
import com.example.dell.chat.base.BaseActivity;
import com.example.dell.chat.bean.MyApplication;
import com.example.dell.chat.bean.MyLocationListener;
import com.example.dell.chat.bean.PersonalState;
import com.example.dell.chat.bean.User;
import com.example.dell.chat.db.PersonalStateDao;
import com.example.dell.chat.db.UserDao;
import com.example.dell.chat.model.Callback;
import com.example.dell.chat.model.Moment.MomentModel;
import com.example.dell.chat.model.Moment.MomentModelImpl;
import com.example.dell.chat.presenter.LoginPresenter;
import com.example.dell.chat.view.PublishActivity;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;

import java.util.List;

public class TestActivity extends BaseActivity<TestActivity,LoginPresenter<TestActivity>> {

    public LocationClient mLocationClient = null;
    private MyLocationListener myListener = new MyLocationListener();
    //BDAbstractLocationListener为7.2版本新增的Abstract类型的监听接口
    //原有BDLocationListener接口暂时同步保留。具体介绍请参考后文中的说明


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_test);    //删除testacticvity时记得删除布局文件

    }


    @Override
    protected LoginPresenter createPresenter() {
        return new LoginPresenter();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case 5:
                if(grantResults.length>0&&grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    Log.e("test", "yes" );
                    mLocationClient.start();
                }else {
                    Log.e("test", "fail" );
                }
                break;
            default:mLocationClient.start();
        }
    }



    public void creatSelect(){
        PictureSelector.create(TestActivity.this).openGallery(PictureMimeType.ofImage()).enableCrop(true).previewImage(true).compress(true).minimumCompressSize(500).isGif(true).maxSelectNum(3).isDragFrame(true).rotateEnabled(true).hideBottomControls(false).forResult(PictureConfig.CHOOSE_REQUEST);
    }
}
