package com.example.danewexpress.Util;

import com.example.danewexpress.DataObject.Pack;
import com.example.danewexpress.DataObject.Setting;
import com.example.danewexpress.R;

import java.util.Vector;

public class SettingDataManager {
        static private Vector<Setting> SettingVector=new Vector<Setting>();//初始化的user数据先放在Vector中

    static String[] SettingDataSrc={"切换用户","退出程序"};
    public static void initData(){

        if(SettingVector.size()>0) return;

        for(String s:SettingDataSrc){
            Setting st=new Setting(s);
            SettingVector.add(st);
        }

    }
    public static Vector<Setting> getDataVector(){
        return SettingVector;
    }
}
