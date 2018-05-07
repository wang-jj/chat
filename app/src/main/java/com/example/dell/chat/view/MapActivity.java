package com.example.dell.chat.view;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.bumptech.glide.Glide;
import com.example.dell.chat.R;
import com.baidu.mapapi.map.MapView;
import com.example.dell.chat.base.BaseActivity;
import com.example.dell.chat.bean.Location;
import com.example.dell.chat.bean.MyApplication;
import com.example.dell.chat.model.Callback;
import com.example.dell.chat.presenter.MainPresenter;
import com.example.dell.chat.presenter.MapPresenter;
import com.example.dell.chat.tools.Dao;

import java.util.List;

//地图界面Activity
public class MapActivity extends BaseActivity<MapActivity,MapPresenter<MapActivity>> {
    private MapView mMapView;
    private BaiduMap mBaiduMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        mMapView = (MapView) findViewById(R.id.mmap);
        mBaiduMap = mMapView.getMap();
        LatLng ll=new LatLng(MyApplication.getLatitude(),MyApplication.getLongitude());
        MapStatusUpdate update= MapStatusUpdateFactory.newLatLng(ll);
        mBaiduMap.animateMapStatus(update);
        View popMarker = View.inflate(MapActivity.this, R.layout.point, null);//把xml转成java的View对象
        /*
        ImageView a=popMarker.findViewById(R.id.img_hotel_image);
        Glide.with(MapActivity.this).load(location.get).into(a);
        TextView b=finish();
        b.setText();
        */
        Bitmap bitmap1 = getViewBitmap(popMarker);//把view转bitmap
        BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory.fromBitmap(bitmap1);
        OverlayOptions option = new MarkerOptions()
                .position(ll)
                .icon(bitmapDescriptor);
        //在地图上添加Marker,并显示
        final Marker marker = (Marker) mBaiduMap.addOverlay(option);
        presenter.getLocation(new Callback<List<Location>>() {
            @Override
            public void execute(List<Location> datas) {
                //datas是附近的人的列表
                for(Location location:datas){
                    View popMarker = View.inflate(MapActivity.this, R.layout.point, null);//把xml转成java的View对象
                    ImageView a=popMarker.findViewById(R.id.map_profile);
                    Glide.with(MapActivity.this).load(location.getProfileID()).into(a);
                    TextView b=popMarker.findViewById(R.id.map_nick_name);
                    b.setText(location.getNickname());
                    Bundle bundle=new Bundle();//存储信息
                    bundle.putSerializable("location",location);
                    marker.setExtraInfo(bundle);//获取marker里面的location对象用marker.getExtraInfo().getSerializable("location");
                    Bitmap bitmap1 = getViewBitmap(popMarker);//把view转bitmap
                    BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory.fromBitmap(bitmap1);
                    LatLng ll=new LatLng(location.getLatitude(),location.getLongitude());
                    OverlayOptions option = new MarkerOptions().position(ll).icon(bitmapDescriptor);
                    //activity跳转
                    /*
                    Intent intent=new Intent(MapActivity.this,AlbumActivity.class);
                    Dao.SetIntent(intent,location.getUser_id(),location.getProfileID(),location.getIntroduction(),location.getNickname(),location.getSchool());
                    startActivity(intent);
                    */
                }
            }
        });
    }

    /*
    private Bitmap getViewBitmap(View addViewContent) {
        addViewContent.setDrawingCacheEnabled(true);
        addViewContent.measure(
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        addViewContent.layout(0, 0,
                addViewContent.getMeasuredWidth(),
                addViewContent.getMeasuredHeight());
        addViewContent.buildDrawingCache();
        Bitmap cacheBitmap = addViewContent.getDrawingCache();
        Bitmap bitmap = Bitmap.createBitmap(cacheBitmap);
        return bitmap;
    }
    */
    //把xml转成view
    private Bitmap getViewBitmap(View addViewContent) {

        addViewContent.setDrawingCacheEnabled(true);

        addViewContent.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED), View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        addViewContent.layout(0, 0, addViewContent.getMeasuredWidth(), addViewContent.getMeasuredHeight());

        addViewContent.buildDrawingCache();
        Bitmap cacheBitmap = addViewContent.getDrawingCache();
        Bitmap bitmap = Bitmap.createBitmap(cacheBitmap);

        return bitmap;
    }

    @Override
    protected MapPresenter createPresenter() {
        return new MapPresenter();
    }
}
