package com.example.danewexpress.DataObject;

import com.example.danewexpress.R;
import com.example.danewexpress.Util.PackDataManager;
import com.example.danewexpress.Util.UserDataManager;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
public class Express {

/**
 *

 1.String--->timestamp
         Timestamp ts = new Timestamp(System.currentTimeMillis());
         String tsStr = "2011-05-09 11:49:45";
         try {
         ts = Timestamp.valueOf(tsStr);
         System.out.println(ts);
         } catch (Exception e) {
         e.printStackTrace();
         }
 2.timestamp--->String*****************

         Timestamp ts = new Timestamp(System.currentTimeMillis());
         String tsStr = "";
         DateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
         try {
         //方法一
         tsStr = sdf.format(ts);
         System.out.println(tsStr);
         //方法二
         tsStr = ts.toString();
         System.out.println(tsStr);
         } catch (Exception e) {
         e.printStackTrace();
         }


 */

    private Pack pack;
    private String timestamp;
    private String nowAddress;//此处信息由于不需要计算运费，因此直接使用字符串\
    private String packId;
    private User expressman;

    private int ImageId= R.drawable.ic_md_truck;//默认


    private static DateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");


    public Express(String packageId,String expressmanId,Timestamp timestamp,String nowAddress){
        this.pack= PackDataManager.getObject(packageId);
        this.expressman= UserDataManager.getObject(expressmanId);
        this.packId=packageId;
        this.timestamp=sdf.format(timestamp);
        this.nowAddress=nowAddress;
    }


    /*  Getter  */
    public String getExpressPackageId(){return packId; }

    public String getTimestamp() {return timestamp;}

    public String getNowAddress(){return nowAddress;}

    public Pack getPack(){return pack;}

    public User getExpressman(){return expressman;}

    public int getImageId(){return ImageId;}


    /*  Setter  */
    public void setExpressPackageId(String packageId){this.pack= PackDataManager.getObject(packageId);this.packId=packageId;}

    public void setTimestamp(Timestamp timestamp) { this.timestamp=sdf.format(timestamp);}

    public void setNowAddress(String nowAddress){ this.nowAddress=nowAddress;}

    public void setImageId(int ImageId){this.ImageId=ImageId;}

}
