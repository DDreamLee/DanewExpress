package com.example.danewexpress.DataObject;

import com.example.danewexpress.R;

public class User{
    private String userid;
    private String password;
    private String username="Unknown";//昵称
    private String phonenum="Unknown";
    private String authority="Common";
    private String address="Unknown";
    private int ImageId=R.drawable.ic_md_person;
    static int num=0;


    public User(String userid,String phonenum,String password){
        this.userid=userid;
        this.phonenum=phonenum;
        this.password=password;
        setImageId();
    }
    public User(String userid,String password){
        this.userid=userid;
        this.password=password;
        setImageId();
    }
    public User(String userid,String username,String phonenum,String password,String authority,String address){
        this.userid=userid;
        this.username=username;
        this.phonenum=phonenum;
        this.password=password;
        this.authority=authority;
        this.address=address;
        setImageId();
    }
    public String getUserid(){
        return userid;
    }
    public String getPhonenum() { return phonenum; }
    public String getAuthority() {return authority; }
    public String getUsername(){
        return username;
    }
    public int getImageId(){
        return ImageId;
    }
    public String getPassword(){
        return password;
    }
    public String getAddress(){
        return address;
    }

    public boolean equals(User o) {
        if(this.userid.equals(o.userid)&&this.password.equals(o.password)){
            return true;
        }
        if(this.phonenum.equals(o.userid)&&this.password.equals(o.password)){
            return true;
        }
        return false;
    }

    private void setImageId() {
        //int num = (int) (1 + Math.random() * (7 - 1 + 1));
        switch (num) {
            case 0:
                ImageId = R.drawable.userimg_1;
                break;
            case 1:
                ImageId = R.drawable.userimg_2;
                break;
            case 2:
                ImageId = R.drawable.userimg_3;
                break;
            case 3:
                ImageId = R.drawable.userimg_4;
                break;
            case 4:
                ImageId = R.drawable.userimg_5;
                break;
            case 5:
                ImageId = R.drawable.userimg_6;
                break;
            case 6:
                ImageId = R.drawable.userimg_7;
                break;
            case 7:
                ImageId = R.drawable.userimg_8;
                break;
            case 8:
                ImageId = R.drawable.userimg_9;
                break;
            case 9:
                ImageId = R.drawable.userimg_10;
                break;
            case 10:
                ImageId = R.drawable.userimg_11;
                break;
            case 11:
                ImageId = R.drawable.userimg_12;
                break;
            case 12:
                ImageId = R.drawable.userimg_13;
                break;
            case 13:
                ImageId = R.drawable.userimg_14;
                break;
            case 14:
                ImageId = R.drawable.userimg_15;
                break;
            case 15:
                ImageId = R.drawable.userimg_16;
                break;
            case 16:
                ImageId = R.drawable.userimg_17;
                break;
            case 17:
                ImageId = R.drawable.userimg_18;
                break;
            case 18:
                ImageId = R.drawable.userimg_19;
                break;
            case 19:
                ImageId = R.drawable.userimg_20;
                break;
            case 20:
                ImageId = R.drawable.userimg_21;
                break;
            case 21:
                ImageId = R.drawable.userimg_22;
                break;
            case 22:
                ImageId = R.drawable.userimg_23;
                break;
        }
        num=(num+1)%23;
    }

}
