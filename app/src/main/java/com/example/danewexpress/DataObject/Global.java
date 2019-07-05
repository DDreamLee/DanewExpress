package com.example.danewexpress.DataObject;

import com.baidu.mapapi.model.LatLng;
import com.example.danewexpress.Util.UserDataManager;

import java.util.Vector;

public class Global {
    /*  保存一些全局变量    */
    public static Object PresentDataObject;
    public static boolean change=false;//是否需要刷新recyclerView
    public static User nowUser;
    public static int databaseVersion=1;//数据库版本号
    public static int size_user_C=20,size_user_E=20,size_pack=6;//用于自动编号

    /*  用于活动之间传递数据元素信息  */

    public static User postUser;
    public static Pack postPack;
    public static Express postExpress;
    public static Vector<LatLng> postLatLng=new Vector<LatLng>();

    public static void setUser(String userid){
        nowUser= UserDataManager.getObject(userid);
    }
}
