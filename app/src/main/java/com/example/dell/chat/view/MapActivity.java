package com.example.dell.chat.view;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.LocationClient;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.CircleOptions;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.BaiduMap.OnMapClickListener;
import com.baidu.mapapi.map.BaiduMap.OnMapStatusChangeListener;
import com.baidu.mapapi.map.BaiduMap.OnMarkerClickListener;
import com.baidu.mapapi.model.LatLng;
import com.bumptech.glide.Glide;
import com.example.dell.chat.R;
import com.baidu.mapapi.map.MapView;
import com.example.dell.chat.base.BaseActivity;
import com.example.dell.chat.bean.Album;
import com.example.dell.chat.bean.Location;
import com.example.dell.chat.bean.MyApplication;
import com.example.dell.chat.model.Callback;
import com.example.dell.chat.presenter.MainPresenter;
import com.example.dell.chat.presenter.MapPresenter;
import com.example.dell.chat.tools.CircleImageView;
import com.example.dell.chat.tools.Dao;
import com.google.gson.Gson;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
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
        MapStatus mapStatus=new MapStatus.Builder().zoom(30).build();//设置地图初始化大小
        MapStatusUpdate mapStatusUpdate=MapStatusUpdateFactory.newMapStatus(mapStatus);
        mBaiduMap.setMapStatus(mapStatusUpdate);
        LatLng ll=new LatLng(MyApplication.getLatitude(),MyApplication.getLongitude());
        MapStatusUpdate update= MapStatusUpdateFactory.newLatLng(ll);
        mBaiduMap.animateMapStatus(update);
        //View popMarker = View.inflate(MapActivity.this, R.layout.point, null);//把xml转成java的View对象 step1
        /*
        ImageView a=popMarker.findViewById(R.id.img_hotel_image);
        Glide.with(MapActivity.this).load(location.get).into(a);
        TextView b=finish();
        b.setText();
        */

        // Bitmap bitmap1 = getViewBitmap(popMarker);//把view转bitmap  step2
        //  BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory.fromBitmap(bitmap1); //step2
        // OverlayOptions option = new MarkerOptions() //step3
        //         .position(ll)
        //         .icon(bitmapDescriptor);
        //在地图上添加Marker,并显示
        //final Marker marker = (Marker) mBaiduMap.addOverlay(option);
        addOverlays();




    }

    private void addOverlays(){
        presenter.getLocation(new Callback<List<Location>>() {
            @Override
            public void execute(List<Location> datas) {
                mBaiduMap.clear();
                /*Toast.makeText(MapActivity.this,"I am here",Toast.LENGTH_LONG).show();*/ //it says that the program has entered the function
                //datas是附近的人的列表
                Log.e("location", new Gson().toJson(datas));
                Marker marker;
                OverlayOptions options1;
                for(final Location location:datas){
                    Log.e("location", new Gson().toJson(location));
                    View popMarker = View.inflate(MapActivity.this, R.layout.point, null);//把xml转成java的View对象
                    ImageView IV=popMarker.findViewById(R.id.map_profile);//user iv to present the data of the class,then send it to the glide function
                    if(location.getGender()==1){
                        IV.setImageResource(R.drawable.girl);
                    }
                    else {
                        IV.setImageResource(R.drawable.boy);
                    }
                    TextView TV=popMarker.findViewById(R.id.map_nick_name);
                    TV.setText(location.getNickname());


                    Bitmap bitmap1 = getViewBitmap(popMarker);//把view转bitmap
                    BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory.fromBitmap(bitmap1);
                    LatLng ll=new LatLng(location.getLongitude(),location.getLatitude());
                    options1 = new MarkerOptions().position(ll).icon(bitmapDescriptor);
                    marker =(Marker) mBaiduMap.addOverlay(options1) ;
                    Bundle bundle=new Bundle();
                    bundle.putSerializable("location",location);
                    marker.setExtraInfo(bundle);
                }
                mBaiduMap.setOnMarkerClickListener(new OnMarkerClickListener() {
                    @Override
                    public boolean onMarkerClick(final Marker marker) {
                        //Bundle bundle=marker.getExtraInfo();
                        // Location location1=(Location) bundle.getSerializable("location");
                        Location location1=(Location)marker.getExtraInfo().get("location");
                        //从marker中获取info信息
                        //将信息显示在界面上
                        Intent intent = new Intent(MapActivity.this, AlbumActivity.class);
                        Dao.SetIntent(intent,location1.getUser_id(),
                                location1.getProfileID(),
                                location1.getIntroduction(),
                                location1.getNickname(),
                                location1.getSchool());
                        startActivity(intent);
                        return true;
                    }
                    //activity跳转
                    /*
                    Intent intent=new Intent(MapActivity.this,AlbumActivity.class);
                    Dao.SetIntent(intent,location.getUser_id(),location.getProfileID(),location.getIntroduction(),location.getNickname(),location.getSchool());
                    startActivity(intent);
                    */
                });



            }
        });

    }

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

    @Override
    protected void onStop(){
        super.onStop();
        mBaiduMap.setMyLocationEnabled(false);
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    protected void onResume(){
        super.onResume();
        mMapView.onResume();
    }

    @Override
    protected void onPause(){
        super.onPause();
        mMapView.onPause();
    }

}
