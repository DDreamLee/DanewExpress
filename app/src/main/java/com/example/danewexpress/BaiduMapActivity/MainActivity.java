package com.example.danewexpress.BaiduMapActivity;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.model.LatLngBounds;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiBoundSearchOption;
import com.baidu.mapapi.search.poi.PoiCitySearchOption;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiDetailSearchOption;
import com.baidu.mapapi.search.poi.PoiDetailSearchResult;
import com.baidu.mapapi.search.poi.PoiIndoorResult;
import com.baidu.mapapi.search.poi.PoiNearbySearchOption;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.example.danewexpress.R;

import java.util.ArrayList;
import java.util.List;

import com.example.danewexpress.utils.PoiOverlay;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    public LocationClient mLocationClient;//
    private TextView positionText;//位置文字
    private MapView mapView;//地图视图
    private BaiduMap baiduMap;//地图的总控制器
    private boolean isFirstLocate = true;//防止多次调用animateMapStatus()
    private PoiSearch mPoiSearch;
    private EditText startText,endText;
    private Button commitButton;
    private double latitude,longitude;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mLocationClient = new LocationClient(getApplicationContext());
        mLocationClient.registerLocationListener(new MyLocationListener());
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.activity_main);
        mapView = (MapView) findViewById(R.id.bmapView);
        baiduMap = mapView.getMap();//通过mapView获得BaiduMap实例
        //baiduMap.setMyLocationEnabled(true);//要使用显示位置点的功能必须要用此方法开启
        mPoiSearch = PoiSearch.newInstance();
        mPoiSearch.setOnGetPoiSearchResultListener(poiSearchListener);
        positionText = (TextView) findViewById(R.id.position_text_view);
        startText=(EditText) findViewById(R.id.text_start);
        endText=(EditText) findViewById(R.id.text_end);
        commitButton=(Button)findViewById(R.id.btn_commit);
        commitButton.setOnClickListener(this);
        positionText.setText("?");

        List<String> permissionList = new ArrayList<>();

        if (ContextCompat.checkSelfPermission(MainActivity.this, android.Manifest.
                permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(android.Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if (ContextCompat.checkSelfPermission(MainActivity.this, android.Manifest.
                permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(android.Manifest.permission.READ_PHONE_STATE);
        }
        if (ContextCompat.checkSelfPermission(MainActivity.this, android.Manifest.
                permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (!permissionList.isEmpty()) {
            String[] permissions = permissionList.toArray(new String[permissionList.
                    size()]);
            ActivityCompat.requestPermissions(MainActivity.this, permissions, 1);
        } else {
            requestLocation();
        }
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.btn_commit://发起检索请求
                //citySearch(10);
                nearbySearch(1);
                break;
            default:
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mLocationClient.stop();
        mapView.onDestroy();
        mPoiSearch.destroy();
        //baiduMap.setMyLocationEnabled(false);//在程序退出时要将这个显示定位点功能关掉
    }

    private void requestLocation() {
        initLocation();
        mLocationClient.start();
    }

    private void initLocation() {
        LocationClientOption option = new LocationClientOption();
        //option.setScanSpan(5000);
        option.setIsNeedAddress(true);
        option.setLocationMode(LocationClientOption.LocationMode.Device_Sensors);//将定位模式设置成传感器模式
        mLocationClient.setLocOption(option);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0) {
                    for (int result : grantResults) {
                        if (result != PackageManager.PERMISSION_GRANTED) {
                            Toast.makeText(this, "必须同意所有权限才能使用本程序",
                                    Toast.LENGTH_SHORT).show();
                            finish();
                            return;
                        }
                    }
                    requestLocation();
                } else {
                    Toast.makeText(this, "发生未知错误", Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;
            default:
        }
    }

    /**
     * 实现定位监听 位置一旦有所改变就会调用这个方法
     * 可以在这个方法里面获取到定位之后获取到的一系列数据
     */
    public class MyLocationListener implements BDLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {
            longitude=location.getLongitude();
            latitude=location.getLatitude();
            StringBuilder currentPosition = new StringBuilder();
            currentPosition.append("纬度：").append(location.getLatitude()).append("\n");
            currentPosition.append("经线：").append(location.getLongitude()).append("\n");
            /*currentPosition.append("国家：").append(location.getCountry()).append("\n");
            currentPosition.append("省：").append(location.getProvince()).append("\n");
            currentPosition.append("市：").append(location.getCity()).append("\n");
            currentPosition.append("区：").append(location.getDistrict()).append("\n");
            currentPosition.append("街道：").append(location.getStreet()).append("\n");*/
            currentPosition.append("定位方式：");
            if (location.getLocType() == BDLocation.TypeGpsLocation) {
                currentPosition.append("GPS");
            } else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {
                currentPosition.append("网络");
            } else {
                currentPosition.append("未知");
            }
            positionText.setText(currentPosition);
            if (location.getLocType() == BDLocation.TypeGpsLocation
                    ||location.getLocType() == BDLocation.TypeNetWorkLocation) {

                navigateTo(location);//将可视化地图移动到我的位置
            }

        }

    }

    private void navigateTo(BDLocation location) {

         /*  构建MyLocationData，将Location中包含的经度和纬度分别封装到了MyLocationData.Builder当中，
            最后把MyLocationData设置到了BaiduMap的setMyLocationData() 方法当中。*/

        /*  注意这段逻辑必须写在isFirstLocate这个if 条件语句的外面，
            因为让地图移动到我们当前的位置只需要在第一次定位的时候执行，
            但是设备在地图上显示的位置却应该是随着设备的移动而实时改变的*/
        /*MyLocationData.Builder locationBuilder = new MyLocationData.Builder();
        locationBuilder.latitude(location.getLatitude());
        locationBuilder.longitude(location.getLongitude());
        MyLocationData locationData = locationBuilder.build();
        baiduMap.setMyLocationData(locationData);*/

        /*  将BDLocation 对象中的地理位置信息取出并封装到LatLng 对象中，
            然后调用MapStatusUpdateFactory的newLatLng() 方法并将LatLng 对象传入，接着将返回的
            MapStatusUpdate 对象作为参数传入到BaiduMap的animateMapStatus() 方法当中*/
        if (isFirstLocate) {//防止多次调用animateMapStatus()

            LatLng ll = new LatLng(location.getLatitude(), location.getLongitude());
            MapStatusUpdate update = MapStatusUpdateFactory.newLatLng(ll);
            baiduMap.animateMapStatus(update);
            update = MapStatusUpdateFactory.zoomTo(16f);
            baiduMap.animateMapStatus(update);
            isFirstLocate = false;

        }



    }


    private OnGetPoiSearchResultListener poiSearchListener = new OnGetPoiSearchResultListener() {
        @Override
        public void onGetPoiResult(PoiResult poiResult) {
            if (poiResult != null &&poiResult.error == SearchResult.ERRORNO.NO_ERROR) {
                //positionText.setText(String.valueOf(poiResult.getTotalPageNum()));

                /*double lat=poiResult.getAllPoi().get(0).location.latitude,lon=poiResult.getAllPoi().get(0).location.longitude;
                MyLocationData.Builder locationBuilder = new MyLocationData.Builder();
                locationBuilder.latitude(lat);
                locationBuilder.longitude(lon);
                MyLocationData locationData = locationBuilder.build();
                baiduMap.setMyLocationData(locationData);

                LatLng ll = new LatLng(lat,lon );
                MapStatusUpdate update = MapStatusUpdateFactory.newLatLng(ll);
                baiduMap.animateMapStatus(update);
                update = MapStatusUpdateFactory.zoomTo(16f);
                baiduMap.animateMapStatus(update);*/


                baiduMap.clear();
                MyPoiOverlay poiOverlay = new MyPoiOverlay(baiduMap);
                poiOverlay.setData(poiResult);// 设置POI数据
                baiduMap.setOnMarkerClickListener(poiOverlay);

                for(PoiInfo info:poiResult.getAllPoi()){
                    positionText.setText(String.valueOf(poiResult.getTotalPageNum()));
                }

                poiOverlay.addToMap();// 将所有的overlay添加到地图上
                poiOverlay.zoomToSpan();

                int totalPage = poiResult.getTotalPageNum();// 获取总分页数
                Toast.makeText(
                        MainActivity.this,
                        "总共查到" + poiResult.getTotalPoiNum() + "个兴趣点, 分为"
                                + totalPage + "页", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(MainActivity.this, "未找到结果",
                        Toast.LENGTH_LONG).show();
            }
        }
        @Override
        public void onGetPoiDetailResult(PoiDetailSearchResult poiDetailSearchResult) {
            if (poiDetailSearchResult.error != SearchResult.ERRORNO.NO_ERROR) {
                Toast.makeText(MainActivity.this, "抱歉，未找到结果",
                        Toast.LENGTH_SHORT).show();
            } else {// 正常返回结果的时候，此处可以获得很多相关信息
                Toast.makeText(
                        MainActivity.this,
                        poiDetailSearchResult.toString() ,
                        Toast.LENGTH_LONG).show();
            }
        }
        @Override
        public void onGetPoiIndoorResult(PoiIndoorResult poiIndoorResult) {

        }
        //废弃
        @Override
        public void onGetPoiDetailResult(PoiDetailResult poiDetailResult) {

        }
    };

    /**
     * 城市内搜索
     */
    private void citySearch(int page) {
        // 设置检索参数
        PoiCitySearchOption citySearchOption = new PoiCitySearchOption();
        citySearchOption.city(startText.getText().toString());// 城市
        citySearchOption.keyword(endText.getText().toString());// 关键字
        citySearchOption.pageCapacity(15);// 默认每页10条
        citySearchOption.pageNum(page);// 分页编号
        // 发起检索请求
        mPoiSearch.searchInCity(citySearchOption);
    }

    /**
     * 范围检索
     */
    private void boundSearch(int page) {
        PoiBoundSearchOption boundSearchOption = new PoiBoundSearchOption();
        LatLng southwest = new LatLng(latitude - 0.01, longitude - 0.012);// 西南
        LatLng northeast = new LatLng(latitude + 0.01, longitude + 0.012);// 东北
        LatLngBounds bounds = new LatLngBounds.Builder().include(southwest)
                .include(northeast).build();// 得到一个地理范围对象
        boundSearchOption.bound(bounds);// 设置poi检索范围
        boundSearchOption.keyword(startText.getText().toString());// 检索关键字
        boundSearchOption.pageNum(page);
        mPoiSearch.searchInBound(boundSearchOption);// 发起poi范围检索请求
    }

    /**
     * 附近检索
     */
    private void nearbySearch(int page) {
        PoiNearbySearchOption nearbySearchOption = new PoiNearbySearchOption();
        nearbySearchOption.location(new LatLng(latitude, longitude));
        nearbySearchOption.keyword(startText.getText().toString());
        nearbySearchOption.radius(10000);// 检索半径，单位是米
        nearbySearchOption.pageNum(page);
        mPoiSearch.searchNearby(nearbySearchOption);// 发起附近检索请求
    }
    class MyPoiOverlay extends PoiOverlay {
        public MyPoiOverlay(BaiduMap arg0) {
            super(arg0);
        }
        @Override
        public boolean onPoiClick(int arg0) {
            super.onPoiClick(arg0);
            PoiInfo poiInfo = getPoiResult().getAllPoi().get(arg0);
            // 检索poi详细信息
            mPoiSearch.searchPoiDetail(new PoiDetailSearchOption()
                    .poiUid(poiInfo.uid));
            return true;
        }
    }


}