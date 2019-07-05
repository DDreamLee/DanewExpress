package com.example.danewexpress.DataObject;


/*  便于划分省  */
public class Address {
    String 省,市,详细地址;
    String 完整地址;

    public Address(String all){
        this.完整地址 =all;
    }
    public Address(String 省,String 市,String 详细地址){
        this.省=省;
        this.市=市;
        this.详细地址=详细地址;
        this.完整地址 =省+市+详细地址;
    }
    public String getString(){
        return 完整地址;
    }
}
