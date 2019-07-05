package com.example.danewexpress.Util;

import android.util.Log;
import com.example.danewexpress.DataObject.User;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*  处理用户登录/注册中的一些检查 */

public class LoginCheck {

    public static boolean isUserExist(Vector<User> ExistUserSet,String userid){

        for(User existUser:ExistUserSet){
            if(userid.equals(existUser.getUserid())) {
                return true;
            }
        }
        Log.d("*", "isUserExist: ");
        return false;

    }

    public static boolean isPasswordCorrect(Vector<User> ExistUserSet,String userid,String password){
        User checkUser=new User(userid,password);
        for(User existUser:ExistUserSet){
            if(existUser.equals(checkUser)) {
                return true;
            }
        }
        Log.d("*", "isPasswordCorrect: ");
        return false;
    }

    public static boolean isMobile(String mobile) {
        if (mobile.length() != 11) {
            return false;
        } else {
            /**
             * 移动号段正则表达式
             */
            String pat1 = "^((13[4-9])|(147)|(15[0-2,7-9])|(178)|(18[2-4,7-8]))\\d{8}|(1705)\\d{7}$";
            /**
             * 联通号段正则表达式
             */
            String pat2 = "^((13[0-2])|(145)|(15[5-6])|(176)|(18[5,6]))\\d{8}|(1709)\\d{7}$";
            /**
             * 电信号段正则表达式
             */
            String pat3 = "^((133)|(153)|(177)|(18[0,1,9])|(149))\\d{8}$";
            /**
             * 虚拟运营商正则表达式
             */
            String pat4 = "^((170))\\d{8}|(1718)|(1719)\\d{7}$";

            Pattern pattern1 = Pattern.compile(pat1);
            Matcher match1 = pattern1.matcher(mobile);
            boolean isMatch1 = match1.matches();
            if (isMatch1) {
                return true;
            }
            Pattern pattern2 = Pattern.compile(pat2);
            Matcher match2 = pattern2.matcher(mobile);
            boolean isMatch2 = match2.matches();
            if (isMatch2) {
                return true;
            }
            Pattern pattern3 = Pattern.compile(pat3);
            Matcher match3 = pattern3.matcher(mobile);
            boolean isMatch3 = match3.matches();
            if (isMatch3) {
                return true;
            }
            Pattern pattern4 = Pattern.compile(pat4);
            Matcher match4 = pattern4.matcher(mobile);
            boolean isMatch4 = match4.matches();
            if (isMatch4) {
                return true;
            }
            return false;
        }
    }

    public static boolean isPassWordLegal(String password) {

        if (password.length() > 0) {
            //判断是否有空格字符串
            for (int t = 0; t < password.length(); t++) {
                String b = password.substring(t, t + 1);
                if (b.equals(" ")) {
                    System.out.println("有空字符串");
                    return false;
                }
            }


            //判断是否有汉字
            int count = 0;
            String regEx = "[\\u4e00-\\u9fa5]";
            Pattern p = Pattern.compile(regEx);
            Matcher m = p.matcher(password);
            while (m.find()) {
                for (int i = 0; i <= m.groupCount(); i++) {
                    count = count + 1;
                }
            }

            if(count>0){
                System.out.println("有汉字");
                return false;
            }


            //判断是否是字母和数字
            int numberCounter = 0;
            for (int i = 0; i < password.length(); i++) {
                char c = password.charAt(i);
                if (!Character.isLetterOrDigit(c)) {
                    return false;
                }
                if (Character.isDigit(c)) {
                    numberCounter++;
                }
            }

        } else {
            return false;
        }
        return true;
    }

}
