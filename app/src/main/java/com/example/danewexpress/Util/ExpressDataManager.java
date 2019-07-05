package com.example.danewexpress.Util;

import com.example.danewexpress.DataObject.Express;
import com.example.danewexpress.DataObject.Global;
import com.example.danewexpress.DataObject.User;

import java.sql.Timestamp;
import java.util.Vector;

public class ExpressDataManager {

    static String[][] expressDataSrc={
            {"P0000000001","浙江义乌童店村"},
            {"P0000000001","义乌分拨中心"},
            {"P0000000001","速通物流重庆分拨中心"},
            {"P0000000001","重庆市南岸区崇文街道崇文路2号明理苑1舍"},
            {"P0000000002","福建省泉州市茶叶公司"},
            {"P0000000002","韵达快递晋江分拨中心"},
            {"P0000000002","南昌中路物流传化分拨中心"},
            {"P0000000002","江西省吉安市泰和县澄江镇嘉禾大道泽盛中心城"},
            {"P0000000003","北京市东城区王府井大街王府井书店"},
            {"P0000000003","北京世纪领航货运分拨中心"},
            {"P0000000003","重庆京东商城分拨中心"},
            {"P0000000003","重庆市沙坪坝区大学城南路55号重庆大学虎溪校区"},
            {"P0000000004","武汉市电子电器交易市场"},
            {"P0000000004","天天快递武汉分公司"},
            {"P0000000004","南京市浦口分拨中心"},
            {"P0000000004","江苏省南京市浦口区永强路8号香溢紫群雅苑2期"},
            {"P0000000005","山东省青岛市市北区浦口路好时尚毛衣店"},
            {"P0000000005","青岛物流分拨中心"},
            {"P0000000005","西藏自治区拉萨市堆龙德庆区乃琼镇124乡道安能物流区"},
            {"P0000000006","广东省广州市白云区齐富路名汇国际电子数码城"},
            {"P0000000006","广州现代物流中心"},
            {"P0000000006","上海康桥物流中心"},
            {"P0000000006","上海市浦东新区张江镇孙环路新丰村逸苑"}
    };


    static private Vector<Express> ExpressVector=new Vector<Express>();//初始化的user数据先放在Vector中

    /*Vector操作*/
    public static void initData(){
        if(ExpressVector.size()>0) return;

        for(String[] ExpressItem:expressDataSrc) {
            String expressPackageId=ExpressItem[0];
            String nowAddress=ExpressItem[1];

            /*随机一个快递员*/
            int num=(int)(1+Math.random()*(20-1+1));//1~20的随机整数
            String expressmanId=String.format("E%08d",num);

            /*地址一样的快递员*/
            if(UserDataManager.getDataVector().size()==0) UserDataManager.initData();
            Vector<User> temp=UserDataManager.getDataVector();
            for(User u:temp){
                if(u.getAddress().equals(nowAddress)&&u.getAuthority().equals("Expressman"))  {expressmanId=u.getUserid();break;}
            }

            Express e=new Express(expressPackageId,expressmanId,new Timestamp(System.currentTimeMillis()),nowAddress);
            ExpressVector.add(e);
            OkHttpManager.sendPostRequestOKHttp(e);//发送数据到服务器
        }

    }

    public static Vector<Express> getDataVector(){
        return ExpressVector;
    }

    public static Express getObject(String packageid,int index){/*返回与packageid匹配的第index条运单记录*/
        Vector<Express> temp=new Vector<Express>();

        for(Express e:ExpressVector){
            if(packageid.equals(e.getExpressPackageId())) temp.add(e);
        }

        return temp.elementAt(index);
    }

    public static Vector<Express> getObjectVector(String packageid){/*返回与packageid匹配的运单记录Vector*/
        Vector<Express> ret=new Vector<Express>();

        for(Express e:ExpressVector){
            if(packageid.equals(e.getExpressPackageId())) ret.add(e);
        }
        return ret;
    }

    public static void addObject(Express newExpress){
        ExpressVector.add(newExpress);
        OkHttpManager.sendPostRequestOKHttp(newExpress);//发送数据到服务器
        Global.change=true;
    }
    public static void delete(Express express){
        for(int i=0;i<ExpressVector.size();i++){
            Express e=ExpressVector.elementAt(i);
            if(express.getExpressPackageId().equals(e.getExpressPackageId())&&express.getTimestamp().equals(e.getTimestamp())) {
                OkHttpManager.sendDeleteExpressRequestOKHttp(e.getExpressPackageId());//发送删除请求到服务器
                ExpressVector.removeElementAt(i);
                break;
            }
        }
        Global.change=true;
    }
}
