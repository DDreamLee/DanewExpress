package com.example.danewexpress.DataObject;

import android.widget.ImageView;

import com.example.danewexpress.R;

public class Setting {
    private String content;
    private int ImageId= R.drawable.ic_md_hammer;

    public Setting(String content){
        this.content=content;
    }

    public Setting(String content, int ImageId){
        this.content=content;
        this.ImageId=ImageId;
    }

    /*  Getter  */

    public String getContent(){return content;}

    public int getImageId(){return ImageId;}
}
