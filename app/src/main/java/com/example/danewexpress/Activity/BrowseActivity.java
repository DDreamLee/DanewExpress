package com.example.danewexpress.Activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.danewexpress.Adapter.ExpressAdapter;
import com.example.danewexpress.Adapter.PackAdapter;
import com.example.danewexpress.Adapter.SettingAdapter;
import com.example.danewexpress.DataObject.Express;
import com.example.danewexpress.DataObject.Global;
import com.example.danewexpress.DataObject.Pack;
import com.example.danewexpress.DataObject.Setting;
import com.example.danewexpress.R;
import com.example.danewexpress.Util.ActivityCollector;
import com.example.danewexpress.Util.ExpressDataManager;
import com.example.danewexpress.Util.LatLngDataManager;
import com.example.danewexpress.Util.PackDataManager;
import com.example.danewexpress.Util.SettingDataManager;
import com.example.danewexpress.Util.UserDataManager;
import com.example.danewexpress.DataObject.User;
import com.example.danewexpress.Adapter.UserAdapter;


import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

/*  搜索、浏览(简略信息)   */

public class BrowseActivity extends BaseActivity implements View.OnClickListener{
    private List<User> userList=new ArrayList<User>();
    private List<Pack> packList=new ArrayList<Pack>();
    private List<Express> expressList=new ArrayList<Express>();
    private List<Setting> settingList=new ArrayList<Setting>();
    private SearchView searchView;
    private FloatingActionButton floatingActionButton;
    private RecyclerView recyclerView;
    private GestureDetector mGestureDetector;
    private UserAdapter Uadapter;
    private PackAdapter Padapter;
    private ExpressAdapter Eadapter;
    private SettingAdapter Sadapter;
    private boolean del=false,open=false;

    private int PresentDataObject= R.string.present_obj_user;//目前页面显示的数据对象
    private BottomNavigationView bottomNavigationView;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener=
            new BottomNavigationView.OnNavigationItemSelectedListener() {//导航栏监听器
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    item.setChecked(true);//使得具有选中效果
                    switch(item.getItemId()){
                        case R.id.nav_item_personal:
                            Log.d("*", "OnClick：nav_item_personal");
                            PresentDataObject=R.string.present_obj_user;
                            refreshUser();
                            break;
                        case R.id.nav_item_package:
                            Log.d("*", "OnClick：nav_item_package");
                            PresentDataObject=R.string.present_obj_pack;
                            refreshPack();
                            //recyclerView.setAdapter(Padapter);
                            break;
                        case R.id.nav_item_express:
                            Log.d("*", "OnClick：nav_item_express");
                            PresentDataObject=R.string.present_obj_express;
                            refreshExpress();
                            //recyclerView.setAdapter(Eadapter);
                            break;
                        case R.id.nav_item_settings:
                            Log.d("*", "OnClick：nav_item_settings");
                            PresentDataObject=R.string.present_obj_setting;
                            recyclerView.setAdapter(Sadapter);
                            break;
                    }
                    return false;
                }
            };

    //长按事件：删除
    private OnItemLongClickListener mOnItemLongClickListener = new OnItemLongClickListener() {
        @Override
        public void onItemLongClick(int position, View childView) {
            switch(PresentDataObject){
                case R.string.present_obj_user:
                    User u=userList.get(position);
                    UserDataManager.delete(u.getUserid());
                    refreshUser();
                    Toast.makeText(childView.getContext(), "成功删除：" +u.getUserid(), Toast.LENGTH_SHORT).show();

                    break;
                case R.string.present_obj_pack:
                    Pack pack=packList.get(position);
                    PackDataManager.delete(pack.getPackageId());
                    refreshPack();
                    Toast.makeText(childView.getContext(), "成功删除：" + pack.getPackageId(), Toast.LENGTH_SHORT).show();

                    break;
                case R.string.present_obj_express:

                    Express express=expressList.get(position);
                    ExpressDataManager.delete(express);
                    refreshExpress();
                    Toast.makeText(childView.getContext(), "成功删除：" + express.getExpressPackageId()+"\n"+express.getTimestamp(), Toast.LENGTH_SHORT).show();

                    break;
            }
        }
    };
    //单击事件：浏览
    private OnItemClickListener mOnItemClickListener = new OnItemClickListener() {
        @Override
        public void onItemClick(int position, View childView) {
            //Toast.makeText(getApplication(), "单击:" + position, Toast.LENGTH_SHORT).show();



            //跳转到浏览界面
            switch (PresentDataObject){
                case R.string.present_obj_user:
                    User u=userList.get(position);
                    Global.postUser=u;
                    browseUser(childView);
                    break;
                case R.string.present_obj_pack:
                    Pack p=packList.get(position);
                    Global.postPack=p;
                    browsePack(childView);
                    break;
                case R.string.present_obj_express:
                    Express express=expressList.get(position);
                    Global.postExpress=express;
                    browseExpress(childView);
                    break;
                case R.string.present_obj_setting:
                    switch (position){
                        case 0:
                            finish();
                            break;
                        case 1:
                            ActivityCollector.finishAll();
                            break;
                    }

                    break;

            }
        }
    };
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        LatLngDataManager.initData();

        /*  隐藏自带标题栏 */
        ActionBar actionBar = getSupportActionBar();
        if(actionBar!=null){
            actionBar.hide();
        }

        setContentView(R.layout.activity_browse);//设置布局文件

        /*  RecyclerView    */
        initAdapter();      //初始化Adapter
        recyclerView =(RecyclerView) findViewById(R.id.recycler_view);
        //recyclerView.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addOnItemTouchListener(new RecyclerView.SimpleOnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
                if (mGestureDetector.onTouchEvent(e)) {
                    return true;
                }
                return false;
            }
        });

        mGestureDetector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener() {

            //长按事件
            @Override
            public void onLongPress(MotionEvent e) {
                super.onLongPress(e);
                if (mOnItemLongClickListener != null) {
                    View childView = recyclerView.findChildViewUnder(e.getX(), e.getY());
                    if (childView != null) {
                        int position = recyclerView.getChildLayoutPosition(childView);
                        mOnItemLongClickListener.onItemLongClick(position, childView);
                    }
                }
            }

            //单击事件
            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                if (mOnItemClickListener != null) {
                    View childView = recyclerView.findChildViewUnder(e.getX(), e.getY());
                    if (childView != null) {
                        int position = recyclerView.getChildLayoutPosition(childView);
                        mOnItemClickListener.onItemClick(position, childView);
                        return true;
                    }
                }

                return super.onSingleTapUp(e);
            }
        });






        recyclerView.setAdapter(Uadapter);

        /*  BottomNavigationView    */
        bottomNavigationView=(BottomNavigationView) findViewById(R.id.nav_view);
        bottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);//为导航栏注册监听器

        /*  SearchView  */
        searchView=(SearchView) findViewById(R.id.searchView);

        // 设置搜索文本监听
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            // 当点击搜索按钮时触发该方法
            @Override public boolean onQueryTextSubmit(String query) {
                EditText searchViewEditText=(EditText)findViewById(android.support.v7.appcompat.R.id.search_src_text);
                switch(PresentDataObject){
                    case R.string.present_obj_user:
                        User queryUser=UserDataManager.getObject(query);
                        if(queryUser!=null){
                            userList= new Vector<User>();
                            userList.add(queryUser);
                            Uadapter = new UserAdapter(userList);
                            recyclerView.setAdapter(Uadapter);
                        }else{
                            searchViewEditText.setText("");
                            searchViewEditText.setHint(getResources().getString(R.string.toast_search_not_found));
                        }
                        break;
                    case R.string.present_obj_pack:
                        Pack queryPack=PackDataManager.getObject(query);
                        if(queryPack!=null){
                            packList= new Vector<Pack>();
                            packList.add(queryPack);
                            Padapter = new PackAdapter(packList);
                            recyclerView.setAdapter(Padapter);
                        }else{
                            searchViewEditText.setText("");
                            searchViewEditText.setHint(getResources().getString(R.string.toast_search_not_found));
                        }
                        break;
                    case R.string.present_obj_express:
                        Vector<Express> queryExpressVector=ExpressDataManager.getObjectVector(query);
                        if(queryExpressVector.size()>0){
                            expressList=queryExpressVector;
                            Eadapter = new ExpressAdapter(expressList);
                            recyclerView.setAdapter(Eadapter);
                        }else{
                            searchViewEditText.setText("");
                            searchViewEditText.setHint(getResources().getString(R.string.toast_search_not_found));
                        }
                        break;
                }
                return false;
            }
            // 当搜索内容改变时触发该方法
            @Override public boolean onQueryTextChange(String newText) {
                EditText searchViewEditText=(EditText)findViewById(android.support.v7.appcompat.R.id.search_src_text);
                searchViewEditText.setHint(getResources().getString(R.string.search_hint));
                return false;
            }
        });

        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                EditText searchViewEditText=(EditText)findViewById(android.support.v7.appcompat.R.id.search_src_text);
                searchViewEditText.setText("");
                searchView.clearFocus();
                switch(PresentDataObject){
                    case R.string.present_obj_user:
                        refreshUser();
                        break;
                    case R.string.present_obj_pack:
                        refreshPack();
                        break;
                    case R.string.present_obj_express:
                        refreshExpress();
                        break;
                }
                return false;
            }
        });




        /*  FloatingActionButton    */
        floatingActionButton=(FloatingActionButton) findViewById(R.id.FloatingActionButton);
        floatingActionButton.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.FloatingActionButton:                                                                 /*   添加键 */
                if(PresentDataObject==R.string.present_obj_setting){
                    //不知道怎么在导航栏事件中隐藏这玩意。。。fk
                    Toast.makeText(v.getContext(),R.string.go_out_add,Toast.LENGTH_SHORT).show();
                }else {
                    Intent intent= new Intent(v.getContext(), UserDetailActivity.class);//user;

                    /*  根据当前显示的数据对象选择特定的Activity进行跳转    */
                    switch (PresentDataObject){
                        case R.string.present_obj_user:
                            intent= new Intent(v.getContext(), UserDetailActivity.class);//user
                            break;
                        case R.string.present_obj_pack:
                            intent= new Intent(v.getContext(), PackDetailActivity.class);//Pack
                            break;
                        case R.string.present_obj_express:
                            intent= new Intent(v.getContext(), ExpressDetailActivity.class);//Express
                            break;
                    }

                    /*  设定模式    */
                    intent.putExtra(getResources().getString(R.string.Mode), R.string.mode_add);
                    startActivity(intent);
                }
                break;
            default:
                break;
        }
    }


    private void initAdapter(){

        //正常时候要去掉1！！！！！
        //UserDataManager.initDatabase(this);//初始化数据库（包括建表，插入初始数据
        //PackDataManager.initDatabase(this);
        //ExpressDataManager.initDatabase(this);

        UserDataManager.initData();
        PackDataManager.initData();
        ExpressDataManager.initData();
        SettingDataManager.initData();

        userList= UserDataManager.getDataVector();
        packList= PackDataManager.getDataVector();
        expressList=ExpressDataManager.getDataVector();
        settingList=SettingDataManager.getDataVector();

        /*
        //Log.d("*", "initList: "+userList.size());
        //Log.d("*", "initList: "+packList.size());
        //Log.d("*", "initList: "+expressList.size());
        for(Pack p:packList){
            Log.d("***", p.getMessage());
        }
        */

        Uadapter = new UserAdapter(userList);
        Padapter = new PackAdapter(packList);
        Eadapter = new ExpressAdapter(expressList);
        Sadapter = new SettingAdapter(settingList);
    }
    @Override
    protected void onResume() {
        super.onResume();

        if(Global.change==false) return;

        //刷新！
        /*  RecyclerView    */
        initAdapter();      //初始化Adapter

        switch (PresentDataObject){
            case R.string.present_obj_user:
                recyclerView.setAdapter(Uadapter);
                break;
            case R.string.present_obj_pack:
                recyclerView.setAdapter(Padapter);
                break;
            case R.string.present_obj_express:
                recyclerView.setAdapter(Eadapter);
                break;
        }
    }
    private void browseUser(View childView){
        Intent intent = new Intent(childView.getContext(), UserDetailActivity.class);
        intent.putExtra(childView.getContext().getResources().getString(R.string.Mode), R.string.mode_browse);
        childView.getContext().startActivity(intent);
    }
    private void browsePack(View childView){
        Intent intent = new Intent(childView.getContext(), PackDetailActivity.class);
        intent.putExtra(childView.getContext().getResources().getString(R.string.Mode), R.string.mode_browse);
        childView.getContext().startActivity(intent);
    }
    private void browseExpress(View childView){
        Intent intent = new Intent(childView.getContext(), ExpressDetailActivity.class);
        intent.putExtra(childView.getContext().getResources().getString(R.string.Mode), R.string.mode_browse);
        childView.getContext().startActivity(intent);
    }
    private void refreshUser(){
        userList= UserDataManager.getDataVector();
        Uadapter = new UserAdapter(userList);
        recyclerView.setAdapter(Uadapter);
    }
    private void refreshPack(){
        packList= PackDataManager.getDataVector();
        Padapter = new PackAdapter(packList);
        recyclerView.setAdapter(Padapter);
    }
    private void refreshExpress(){
        expressList= ExpressDataManager.getDataVector();
        Eadapter = new ExpressAdapter(expressList);
        recyclerView.setAdapter(Eadapter);
    }
    //长按事件接口
    public interface OnItemLongClickListener {
        public void onItemLongClick(int position, View childView);
    }
    //单击事件接口
    public interface OnItemClickListener {
        public void onItemClick(int position, View childView);
    }

}
