package com.example.danewexpress.BaiduMapActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;

import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.ArcOptions;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.CircleOptions;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.PolygonOptions;
import com.baidu.mapapi.map.PolylineOptions;
import com.baidu.mapapi.map.Stroke;
import com.baidu.mapapi.map.TextOptions;
import com.baidu.mapapi.map.TextureMapView;
import com.baidu.mapapi.model.LatLng;
import com.example.danewexpress.Activity.BrowseActivity;
import com.example.danewexpress.DataObject.Global;
import com.example.danewexpress.R;
import com.example.danewexpress.Util.LatLngDataManager;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

/**
 * 绘制maker，面的页
 */

public class OverlayActivity extends AppCompatActivity implements View.OnClickListener {


    private Button backButton;
    private BaiduMap mBaiduMap;
    private List<LatLng> mLatLngs = new ArrayList<LatLng>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*  去掉自带的标题   */
        ActionBar actionBar = getSupportActionBar();
        if(actionBar!=null){
            actionBar.hide();
        }

        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.activity_overlay);
        TextureMapView mMapView = (TextureMapView) findViewById(R.id.texture_map);
        mBaiduMap = mMapView.getMap();


        //放大百度地图
        mBaiduMap.setMapStatus(MapStatusUpdateFactory.zoomTo(5f));

        Log.d("*", "overlay: size"+Global.postLatLng.size());
        initPoint(Global.postLatLng);
        Log.d("*", "overlay: point");
        if(Global.postLatLng.size()>1){
            drawPolyLine(Global.postLatLng);
            Log.d("*", "overlay: line");
        }

        backButton=(Button)findViewById(R.id.title_btn_back);
        backButton.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.title_btn_back:
                Activity activity =(Activity)v.getContext();
                activity.finish();
                break;
        }
    }

    private void initPoint(Vector<LatLng> myLatLngs){

        for(LatLng latLng:myLatLngs){
            //构建Marker图标
            BitmapDescriptor bitmap = BitmapDescriptorFactory
                    .fromResource(R.drawable.icon_gcoding);

            //构建MarkerOption，用于在地图上添加Marker
            OverlayOptions option = new MarkerOptions()
                    .position(latLng)
                    .icon(bitmap);

            //在地图上添加Marker，并显示
            mBaiduMap.addOverlay(option);
        }
    }

    private void drawPolyLine(Vector<LatLng> myLatLngs){
        //绘制折线
        OverlayOptions Polyline = new PolylineOptions().width(10)
                .color(0xAAFF0000).points(myLatLngs);
        mBaiduMap.addOverlay(Polyline);
    }


}
