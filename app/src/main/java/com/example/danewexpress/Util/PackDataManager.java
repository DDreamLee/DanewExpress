package com.example.danewexpress.Util;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.example.danewexpress.DataObject.Address;
import com.example.danewexpress.DataObject.Global;
import com.example.danewexpress.DataObject.Pack;
import com.example.danewexpress.DataObject.User;
import com.example.danewexpress.R;

import java.util.Vector;



public class PackDataManager {
    static private Vector<Pack> PackageVector=new Vector<Pack>();//初始化的user数据先放在Vector中
    static String[][] packageDataSrc={
            {"P0000000001","3.85","13908386354","浙江省","义乌市","童店村","13638323542","重庆市","重庆市","南岸区崇文街道崇文路2号明理苑1舍"},
            {"P0000000002","2.5","17783562242","福建省","泉州市","茶叶公司","14723876354","江西省","吉安市","泰和县澄江镇嘉禾大道泽盛中心城"},
            {"P0000000003","1","19923885123","北京市","北京市","东城区王府井大街王府井书店","13678423542","重庆市","沙坪坝区","大学城南路55号重庆大学虎溪校区"},
            {"P0000000004","3.2","13594773449","湖北省","武汉市","电子电器交易市场","14798795123","江苏省","南京市","浦口区永强路8号香溢紫群雅苑2期"},
            {"P0000000005","1","16556282845","山东省","青岛市","市北区浦口路好时尚毛衣店","15730645123","西藏自治区","拉萨市","堆龙德庆区乃琼镇124乡道安能物流区"},
            {"P0000000006","10","15123347762","广东省","广州市","白云区齐富路名汇国际电子数码城","15086996354","上海市","上海市","浦东新区张江镇孙环路新丰村逸苑"}
    };


    /*Vector操作*/
    public static void initData(){
        if(PackageVector.size()>0) return;

        for(String[] PackageItem:packageDataSrc){
            String packageId=PackageItem[0];
            double weight=Double.parseDouble(PackageItem[1]);
            String SenderPhone=PackageItem[2];
            Address SenderAddress=new Address(PackageItem[3],PackageItem[4],PackageItem[5]);
            String ReceiverPhone=PackageItem[6];
            Address ReceiverAddress=new Address(PackageItem[7],PackageItem[8],PackageItem[9]);
            String PayerPhone=SenderPhone;
            Pack p=new Pack(packageId,weight,SenderPhone,SenderAddress,ReceiverPhone,ReceiverAddress,PayerPhone);

            PackageVector.add(p);
            OkHttpManager.sendPostRequestOKHttp(p);//发送数据到服务器
        }

    }

    public static Vector<Pack> getDataVector(){
        return PackageVector;
    }

    public static Pack getObject(String packageid){
        for(Pack p:PackageVector){
            if(packageid.equals(p.getPackageId())) return p;
        }
        return null;
    }

    public static void addObject(Pack newPack){/*   返回值是提示字符串的资源定位id    */
        PackageVector.add(newPack);
        OkHttpManager.sendPostRequestOKHttp(newPack);//发送数据到服务器
        Global.change=true;
    }

    public static void delete(String PackId){
        for(int i=0;i<PackageVector.size();i++){
            Pack p=PackageVector.elementAt(i);
            if(PackId.equals(p.getPackageId())) {
                OkHttpManager.sendDeletePackRequestOKHttp(p.getPackageId());//发送删除请求到服务器
                PackageVector.removeElementAt(i);
            }
        }
        Global.change=true;
    }
}
