package com.example.danewexpress.BaiduMapActivity;

import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AutoCompleteTextView;
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
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.TextureMapView;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.model.LatLngBounds;
import com.baidu.mapapi.search.core.CityInfo;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiBoundSearchOption;
import com.baidu.mapapi.search.poi.PoiCitySearchOption;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiDetailSearchResult;
import com.baidu.mapapi.search.poi.PoiIndoorResult;
import com.baidu.mapapi.search.poi.PoiNearbySearchOption;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.baidu.mapapi.search.poi.PoiSortType;
import com.example.danewexpress.R;

import java.util.ArrayList;
import java.util.List;

import com.example.danewexpress.utils.ImageUtils;

/**
 * POI搜索页
 */

public class PoiActivity extends AppCompatActivity implements View.OnClickListener {
    public LocationClient mLocationClient;//
    private EditText mEtCity;
    private AutoCompleteTextView mActvSearchkey;
    private Button mBtnSearch, mBtnSearchNearby, mBtnSearchBound;
    private PoiSearch mPoiSearch;
    private BaiduMap mBaiduMap;
    private List<LatLng> latLngs = new ArrayList<>();
    private boolean isFirstLocate =true;
    private double latitude,longitude;
    private TextView messageText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mLocationClient = new LocationClient(getApplicationContext());
        mLocationClient.registerLocationListener(new PoiActivity.MyLocationListener());

        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.activity_poi);
        TextureMapView mMapView = (TextureMapView) findViewById(R.id.texture_map);
        mBaiduMap = mMapView.getMap();

        initDate();

        //创建POI检索实例
        mPoiSearch = PoiSearch.newInstance();
        //创建POI检索监听者；

        //设置POI检索监听者；
        mPoiSearch.setOnGetPoiSearchResultListener(poiListener);
    }

    private void initDate() {
        mEtCity = (EditText) findViewById(R.id.city);

        mActvSearchkey = (AutoCompleteTextView) findViewById(R.id.searchkey);
        mBtnSearch = (Button) findViewById(R.id.search);
        mBtnSearchNearby = (Button) findViewById(R.id.searchNearby);
        mBtnSearchBound = (Button) findViewById(R.id.searchBound);

        mBtnSearch.setOnClickListener(this);
        mBtnSearchNearby.setOnClickListener(this);
        mBtnSearchBound.setOnClickListener(this);

        messageText=(TextView)findViewById(R.id.text_message);

        List<String> permissionList = new ArrayList<>();
        if (ContextCompat.checkSelfPermission(PoiActivity.this, android.Manifest.
                permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(android.Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if (ContextCompat.checkSelfPermission(PoiActivity.this, android.Manifest.
                permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(android.Manifest.permission.READ_PHONE_STATE);
        }
        if (ContextCompat.checkSelfPermission(PoiActivity.this, android.Manifest.
                permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (!permissionList.isEmpty()) {
            String[] permissions = permissionList.toArray(new String[permissionList.
                    size()]);
            ActivityCompat.requestPermissions(PoiActivity.this, permissions, 1);
        } else {
            requestLocation();
        }

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.search:
                //发起检索请求；
                mPoiSearch.searchInCity((new PoiCitySearchOption())
                        .city(mEtCity.getText().toString().trim())
                        .keyword(mActvSearchkey.getText().toString().trim())
                        .pageNum(0));
                break;
            case R.id.searchNearby:
                //发起检索请求；
                mPoiSearch.searchNearby((new PoiNearbySearchOption())
                        .location(new LatLng(latitude,longitude))
                        .sortType(PoiSortType.distance_from_near_to_far)
                        .keyword(mActvSearchkey.getText().toString().trim())
                        .radius(2000)
                        .pageNum(0)
                );
                break;
            case R.id.searchBound:
                LatLng southwest = new LatLng( latitude, longitude );
                LatLng northeast = new LatLng( latitude, longitude);
                LatLngBounds searchbound = new LatLngBounds.Builder()
                        .include(southwest)
                        .include(northeast)
                        .build();
                mPoiSearch.searchInBound(new PoiBoundSearchOption()
                        .bound(searchbound)
                        .keyword(mActvSearchkey.getText().toString().trim()));
                break;
        }
    }

    OnGetPoiSearchResultListener poiListener = new OnGetPoiSearchResultListener() {
        @Override
        public void onGetPoiDetailResult(PoiDetailSearchResult poiDetailSearchResult) {

        }

        public void onGetPoiResult(PoiResult result) {
            if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
                //详情检索失败
                // result.error请参考SearchResult.ERRORNO
                Toast.makeText(PoiActivity.this, "未搜索到POI数据", Toast.LENGTH_SHORT).show();
            }
            if (result.error == SearchResult.ERRORNO.NO_ERROR) {
                //获取POI检索结果
                Toast.makeText(PoiActivity.this, "已搜索到POI数据", Toast.LENGTH_SHORT).show();
                mBaiduMap.clear();
                List<PoiInfo> allPoi = result.getAllPoi();
                for (int i = 0; i < allPoi.size(); i++) {
                    Resources res = getResources();
                    Bitmap bitmap = BitmapFactory.decodeResource(res, R.drawable.icon_gcoding);
                    Bitmap bitmap1 = ImageUtils.drawTextToCenter(PoiActivity.this, bitmap, "" + i, 20, Color.BLACK);

                    OverlayOptions options = new MarkerOptions()
                            .position(result.getAllPoi().get(i).location)
                            .title(result.getAllPoi().get(i).name + ":" + result.getAllPoi().get(i).address)
                            .icon(BitmapDescriptorFactory.fromBitmap(bitmap1));
                    mBaiduMap.addOverlay(options);


                }
                mBaiduMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
                    @Override
                    public boolean onMarkerClick(Marker marker) {
                        Toast.makeText(PoiActivity.this, marker.getTitle(), Toast.LENGTH_SHORT).show();
                        return true;
                    }
                });
            }
            if (result.error == SearchResult.ERRORNO.AMBIGUOUS_KEYWORD) {

                // 当输入关键字在本市没有找到，但在其他城市找到时，返回包含该关键字信息的城市列表
                String strInfo = "在";
                for (CityInfo cityInfo : result.getSuggestCityList()) {
                    strInfo += cityInfo.city;
                    strInfo += ",";
                }
                strInfo += "找到结果";
                Toast.makeText(PoiActivity.this, strInfo, Toast.LENGTH_LONG)
                        .show();
            }
        }

        public void onGetPoiDetailResult(PoiDetailResult result) {
            //获取Place详情页检索结果
            if (result.error != SearchResult.ERRORNO.NO_ERROR) {
                Toast.makeText(PoiActivity.this, "抱歉，未找到结果", Toast.LENGTH_SHORT)
                        .show();
            } else {
                Toast.makeText(PoiActivity.this, result.getName() + ": " + result.getAddress(), Toast.LENGTH_SHORT)
                        .show();
            }
        }

        @Override
        public void onGetPoiIndoorResult(PoiIndoorResult poiIndoorResult) {

        }
    };

    @Override
    protected void onDestroy() {
        //释放POI检索实例；
        mLocationClient.stop();
        //mBaiduMap.setMyLocationEnabled(false);//在程序退出时要将这个显示定位点功能关掉
        mPoiSearch.destroy();
        super.onDestroy();
    }

    private void requestLocation() {
        initLocation();
        mLocationClient.start();
    }
    private void initLocation() {
        LocationClientOption option = new LocationClientOption();
        option.setScanSpan(5000);
        option.setIsNeedAddress(true);
        option.setLocationMode(LocationClientOption.LocationMode.Device_Sensors);//将定位模式设置成传感器模式
        mLocationClient.setLocOption(option);

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
            messageText.setText(currentPosition);
            if (location.getLocType() == BDLocation.TypeGpsLocation
                    ||location.getLocType() == BDLocation.TypeNetWorkLocation) {

                navigateTo(location);//将可视化地图移动到我的位置
            }

        }

    }


    private void navigateTo(BDLocation location) {



        /*  将BDLocation 对象中的地理位置信息取出并封装到LatLng 对象中，
            然后调用MapStatusUpdateFactory的newLatLng() 方法并将LatLng 对象传入，接着将返回的
            MapStatusUpdate 对象作为参数传入到BaiduMap的animateMapStatus() 方法当中*/
        if (isFirstLocate) {//防止多次调用animateMapStatus()

            LatLng ll = new LatLng(location.getLatitude(), location.getLongitude());
            MapStatusUpdate update = MapStatusUpdateFactory.newLatLng(ll);
            mBaiduMap.animateMapStatus(update);
            update = MapStatusUpdateFactory.zoomTo(16f);
            mBaiduMap.animateMapStatus(update);
            isFirstLocate = false;

        }



    }

}
