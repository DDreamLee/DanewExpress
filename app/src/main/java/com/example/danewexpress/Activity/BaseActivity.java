package com.example.danewexpress.Activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.danewexpress.Util.ActivityCollector;

/***    基类，其它所有活动都继承自此类，方便调试    ***/

public class BaseActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savesInstanceState){
        super.onCreate(savesInstanceState);
        Log.d("*    BaseActivity", getClass().getSimpleName());
        ActivityCollector.addActivity(this);                            //加入ActivityCollector管理的活动集合
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityCollector.removeActivity(this);                          //移出ActivityCollector管理的活动集合
    }
}
