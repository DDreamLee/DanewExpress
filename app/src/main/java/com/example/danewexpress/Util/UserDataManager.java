package com.example.danewexpress.Util;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.danewexpress.DataObject.Global;
import com.example.danewexpress.DataObject.Pack;
import com.example.danewexpress.DataObject.User;
import com.example.danewexpress.R;

import java.util.Vector;

/*
初始化user数据存放在Vector中
初始化数据库中的user表
*/

public class UserDataManager{


    static int size_customer=20,size_expressman=20,size_finance=2,size_humanresource=2,size_customerservice=2,size_admin=2;//各种用户的数量

    static String[][] userDataSrc={
            {"C00000001","111111","白居易","13908386354","Customer","浙江义乌童店村"},
            {"C00000002","111111","杜甫","13896086284","Customer","重庆市南岸区崇文街道崇文路2号明理苑1舍"},
            {"C00000003","111111","李白","17783562242","Customer","福建省泉州市茶叶公司"},
            {"C00000004","111111","刘禹锡","18502365845","Customer","江西省吉安市泰和县澄江镇嘉禾大道泽盛中心城"},
            {"C00000005","111111","韦应物","19923885123","Customer","北京市东城区王府井大街王府井书店"},
            {"C00000006","111111","李商隐","14798797762","Customer","重庆市沙坪坝区大学城南路55号重庆大学虎溪校区"},
            {"C00000007","111111","杜牧","13594773449","Customer","武汉市电子电器交易市场"},
            {"C00000008","111111","刘长卿","13883833542","Customer","江苏省南京市浦口区永强路8号香溢紫群雅苑2期"},
            {"C00000009","111111","王维","16556282845","Customer","山东省青岛市市北区浦口路好时尚毛衣店"},
            {"C00000010","111111","贾岛","15730645123","Customer","西藏自治区拉萨市堆龙德庆区乃琼镇124乡道安能物流区"},
            {"C00000011","111111","岑参","15123347762","Customer","广东省广州市白云区齐富路名汇国际电子数码城"},
            {"C00000012","111111","孟郊","15086996354","Customer","上海市浦东新区张江镇孙环路新丰村逸苑"},
            {"C00000013","111111","温庭筠","13983276284","Customer","浙江义乌童店村"},
            {"C00000014","111111","韦庄","13638323542","Customer","重庆市南岸区崇文街道崇文路2号明理苑1舍"},
            {"C00000015","111111","韩愈","13883432389","Customer","福建省泉州市茶叶公司"},
            {"C00000016","111111","孟浩然","14723876354","Customer","江西省吉安市泰和县澄江镇嘉禾大道泽盛中心城"},
            {"C00000017","111111","李端","13436086284","Customer","北京市东城区王府井大街王府井书店"},
            {"C00000018","111111","李贺","13678423542","Customer","重庆市沙坪坝区大学城南路55号重庆大学虎溪校区"},
            {"C00000019","111111","司空图","19923002845","Customer","武汉市电子电器交易市场"},
            {"C00000020","111111","王昌龄","14798795123","Customer","江苏省南京市浦口区永强路8号香溢紫群雅苑2期"},
            {"E00000001","222222","高适","13594777762","Expressman","义乌分拨中心"},
            {"E00000002","222222","张九龄","13883833449","Expressman","速通物流重庆分拨中心"},
            {"E00000003","222222","司空曙","16556283542","Expressman","韵达快递晋江分拨中心"},
            {"E00000004","222222","荷马","15730642845","Expressman","南昌中路物流传化分拨中心"},
            {"E00000005","222222","但丁","15086755123","Expressman","北京世纪领航货运分拨中心"},
            {"E00000006","222222","歌德","13983277762","Expressman","重庆京东商城分拨中心"},
            {"E00000007","222222","拜伦","13638325123","Expressman","天天快递武汉分公司"},
            {"E00000008","222222","莎士比亚","13883437762","Expressman","南京市浦口分拨中心"},
            {"E00000009","222222","雨果","14724573349","Expressman","青岛物流分拨中心"},
            {"E00000010","222222","泰戈尔","13436087762","Expressman","广州现代物流中心"},
            {"E00000011","222222","列夫·托尔斯泰","13896083449","Expressman","上海康桥物流中心"},
            {"E00000012","222222","高尔基","17783183542","Expressman","义乌分拨中心"},
            {"E00000013","222222","鲁迅","18002382845","Expressman","速通物流重庆分拨中心"},
            {"E00000014","222222","茅盾","19923005123","Expressman","韵达快递晋江分拨中心"},
            {"E00000015","222222","巴金","15730643542","Expressman","南昌中路物流传化分拨中心"},
            {"E00000016","222222","沈从文","15123342845","Expressman","北京世纪领航货运分拨中心"},
            {"E00000017","222222","丁玲","15086995123","Expressman","重庆京东商城分拨中心"},
            {"E00000018","222222","王朔","13983272845","Expressman","天天快递武汉分公司"},
            {"E00000019","222222","苏童","13436085123","Expressman","南京市浦口分拨中心"},
            {"E00000020","222222","倪匡","13678427762","Expressman","青岛物流分拨中心"},
            {"CS0000001","cscscs","梁羽生","14723873449","CustomerService","Unknown"},
            {"CS0000002","cscscs","莫言","14798793542","CustomerService","Unknown"},
            {"F00000001","ffffff","贾平凹","13594772845","Finance","Unknown"},
            {"F00000002","ffffff","寒木","15086993542","Finance","Unknown"},
            {"H00000001","hhhhhh","金庸","13983272389","HumanResource","Unknown"},
            {"H00000002","hhhhhh","古龙","13638326354","HumanResource","Unknown"},
            {"Admin1","aaaaaa","琼瑶","13883436284","Admin","Unknown"},
            {"Admin2","aaaaaa","钱钟书","14723873542","Admin","Unknown"}
    };




    static private Vector<User> UserVector =new Vector<User>();//初始化的user数据先放在Vector中


    /*Vector操作*/
    public static void initData(){/*    初始化用户数据,放在Vector */

        if(UserVector.size()>0) return;

        for(String[] UserItem:userDataSrc){
            /*
            {"userid","password","username","phonenum","authority","address"},
            */
            String userid=UserItem[0];
            String password=UserItem[1];
            String username=UserItem[2];
            String phonenum=UserItem[3];
            String authority=UserItem[4];
            String address=UserItem[5];

            User u=new User(userid,username,phonenum,password,authority,address);
            UserVector.add(u);
            OkHttpManager.sendPostRequestOKHttp(u);//发送数据到服务器
        }


    }

    public static Vector<User> getDataVector(){
        return UserVector;
    }

    public static User getObject(String userid){//通过id或电话号码查找用户
        for(User u:UserVector){
            if(userid.equals(u.getUserid())) return u;
            if(!userid.equals("Unknown") && userid.equals(u.getPhonenum())) return u;
        }
        Log.d("*", "getObject: null");
        return null;
    }

    public static int add2Vector(String userid, String phonenum, String password){/*   返回值是提示字符串的资源定位id    */

        User tempUser;
        int type;
        if(LoginCheck.isMobile(userid)){//user:自动分配ID
            userid=String.format("C%08d",size_customer+1);
            tempUser=new  User(userid,phonenum,password);
            type=1;
        }else{//expressman
            tempUser=new User(userid,phonenum,password);
            type=2;
        }

        if(LoginCheck.isUserExist(UserVector,tempUser.getUserid())) {   //失败1：用户已存在
            return R.string.toast_add_exist;
        }else if(LoginCheck.isPassWordLegal(tempUser.getUserid())
                && LoginCheck.isPassWordLegal(tempUser.getPassword())){ //成功：用户名和密码均合法
            UserVector.add(tempUser);
            if(type==1) size_customer++;
            else if(type==2) size_expressman++;
            return R.string.toast_add_success;

        }else{                                                          //失败2：用户名或密码不合法
            return R.string.toast_add_illegal;
        }
    }

    public static void add2Vector(User newUser){
        UserVector.add(newUser);
        Global.change=true;
    }

    public static void delete(String userId){
        for(int i=0;i<UserVector.size();i++){
            User u=UserVector.elementAt(i);
            if(userId.equals(u.getUserid())) {
                OkHttpManager.sendDeleteUserRequestOKHttp(u.getUserid());//发送删除请求到服务器
                UserVector.removeElementAt(i);
            }
        }
        Global.change=true;
    }

}

