package com.example.danewexpress.Util;

import com.baidu.mapapi.model.LatLng;
import com.example.danewexpress.DataObject.Express;

import java.util.HashMap;
import java.util.Vector;

public class LatLngDataManager {
    static private HashMap<String, LatLng> LatLngMap=new HashMap<String, LatLng>();//初始化的user数据先放在Vector中
    static String[][] LatLngDataSrc={
            {"浙江义乌童店村","29.2808171707","120.0531025426"},
            {"义乌分拨中心","29.2854697573","120.0001234799"},
            {"速通物流重庆分拨中心","29.6641635454","106.6293088325"},
            {"重庆市南岸区崇文街道崇文路2号明理苑1舍","29.5382169011","106.6146176788"},
            {"福建省泉州市茶叶公司","24.8957087660","118.6061895973"},
            {"韵达快递晋江分拨中心","24.8164786730","118.5084568115"},
            {"南昌中路物流传化分拨中心","28.5736125082","115.8886901401"},
            {"江西省吉安市泰和县澄江镇嘉禾大道泽盛中心城","26.8053560028","114.9314005280"},
            {"北京市东城区王府井大街王府井书店","39.9166169885","116.4182737251"},
            {"北京世纪领航货运分拨中心","39.7966612464","116.6773342652"},
            {"重庆京东商城分拨中心","29.3379696861","106.6608998973"},
            {"重庆市沙坪坝区大学城南路55号重庆大学虎溪校区","29.5998219492","106.3057830131"},
            {"武汉市电子电器交易市场","30.5859120721","114.2923473381"},
            {"天天快递武汉分公司","30.4420550488","114.2658791999"},
            {"南京市浦口分拨中心","32.1879857705","118.6419081161"},
            {"江苏省南京市浦口区永强路8号香溢紫群雅苑2期","32.1866016334","118.7048727740"},
            {"山东省青岛市市北区浦口路好时尚毛衣店","35.8831563062","120.0437060286"},
            {"青岛物流分拨中心","36.2994135915","120.3402082145"},
            {"西藏自治区拉萨市堆龙德庆区乃琼镇124乡道安能物流区","29.6355106917","90.9934001957"},
            {"广东省广州市白云区齐富路名汇国际电子数码城","23.2005310323","113.2787479198"},
            {"广州现代物流中心","23.2519825369","113.2876397107"},
            {"上海康桥物流中心","31.1659604439","121.6406606034"},
            {"上海市浦东新区张江镇孙环路新丰村逸苑","31.1675727922","121.6570472599"}
    };

    public static void initData(){

        if(LatLngMap.size()>0) return;

        for(String[] sg:LatLngDataSrc){
            double doublelat=Double.parseDouble(sg[1]);
            double doublelng=Double.parseDouble(sg[2]);
            LatLngMap.put(sg[0],new LatLng(doublelat,doublelng));
        }
    }

    public static LatLng getLatLng(String address){
        return LatLngMap.get(address);
    }

    public static HashMap<String, LatLng> getSet(){
        return LatLngMap;
    }

    public static Vector<LatLng> getObjectVector(String packid){
        Vector<LatLng>  ret = new  Vector<LatLng>();
        Vector<Express> expresses=ExpressDataManager.getObjectVector(packid);
        for(int i=0;i<expresses.size();i++){
            String address=expresses.elementAt(i).getNowAddress();
            ret.add(LatLngMap.get(address));//获取给定地址的经纬度
        }
        return ret;
    }

}
