package com.example.danewexpress.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mapapi.map.Overlay;
import com.example.danewexpress.BaiduMapActivity.OverlayActivity;
import com.example.danewexpress.DataObject.Express;
import com.example.danewexpress.DataObject.Global;
import com.example.danewexpress.DataObject.Pack;
import com.example.danewexpress.DataObject.User;
import com.example.danewexpress.R;
import com.example.danewexpress.Util.ExpressDataManager;
import com.example.danewexpress.Util.LatLngDataManager;
import com.example.danewexpress.Util.PackDataManager;
import com.example.danewexpress.Util.UserDataManager;

import java.sql.Timestamp;

/*浏览、编辑(详细信息)*/
public class ExpressDetailActivity extends BaseActivity implements View.OnClickListener{
    private int Mode;
    private int mLayout= R.layout.activity_detail_browse_express;
    private Button leftButton,rightButton;
    private ImageView expressImage;
    //all
    private TextView pub_textView_startAddress,pub_textView_endAddress,pub_textView_time,pub_textView_expressman_name,pub_textView_expressman_phonenum;
    //add
    private EditText add_editText_expressPackId,add_editText_nowAddress,add_editText_expressmanId;
    //edit:与add区别在于 运单号不可修改
    private TextView edit_textView_expressPackId;
    private EditText edit_editText_nowAddress,edit_editText_expressmanId;
    //browse
    private TextView browse_textView_expressPackId,browse_textView_nowAddress,browse_textView_expressmanId;
    private Express mExpress;
    private User mExpressman;
    private Pack mPPack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent=getIntent();
        Mode=intent.getIntExtra(getResources().getString(R.string.Mode),R.string.mode_browse);//浏览 编辑 新增 三种

        LayoutConfig();// 动态配置布局

        ViewConfig();// 动态配置控件

        /*  去掉自带的标题   */
        ActionBar actionBar = getSupportActionBar();
        if(actionBar!=null){
            actionBar.hide();
        }



    }
    @Override
    public void onClick(View v) {
        Intent intent;
        switch(v.getId()){
            case R.id.title_btn_back:
                Activity activity=(Activity) v.getContext();
                activity.finish();
                break;
            case R.id.title_btn_add://提交添加

                //检查是否存在，存在则执行添加，并跳转Browse
                if(mPPack!=null&&mExpressman!=null){
                    mExpress=new Express(
                            mPPack.getPackageId(),
                            mExpressman.getUserid(),
                            new Timestamp(System.currentTimeMillis()),
                            add_editText_nowAddress.getText().toString()
                    );
                    //执行添加
                    ExpressDataManager.addObject(mExpress);
                    //跳转
                    intent = new Intent(v.getContext(),BrowseActivity.class);
                    v.getContext().startActivity(intent);
                }else{//不存在则弹出Toast
                    Toast.makeText(v.getContext(),R.string.toast_express_add_not_exist,Toast.LENGTH_SHORT).show();
                }

                break;
            case R.id.title_btn_prompt_edit:
                //提交更新
                Log.d("*", "onClick: 1");
                //检查快递员是否存在，存在则执行更新
                String expressmanId=edit_editText_expressmanId.getText().toString();
                if(UserDataManager.getObject(expressmanId)!=null){
                    Log.d("*", "onClick: 1-1");
                    mExpressman=UserDataManager.getObject(expressmanId);//更新快递员信息
                    Log.d("*", "onClick: 1-2");
                    mExpress=new Express(
                            mPPack.getPackageId(),
                            mExpressman.getUserid(),
                            new Timestamp(System.currentTimeMillis()),
                            edit_editText_nowAddress.getText().toString()
                    );
                    Log.d("*", "onClick: 2");
                    //执行添加
                    ExpressDataManager.delete(mExpress);
                    ExpressDataManager.addObject(mExpress);
                    Log.d("*", "onClick: 2-1");
                    //跳转
                    intent = new Intent(v.getContext(),BrowseActivity.class);
                    v.getContext().startActivity(intent);

                }else{//不存在则弹出toast
                    Log.d("*", "onClick: 2-2");
                    Toast.makeText(v.getContext(),R.string.toast_express_edit_not_exist,Toast.LENGTH_SHORT).show();
                }

                //执行更新：先找到对应元素，再remove之，再add之
                break;
            case R.id.title_btn_go_edit://跳转到更新页面
                intent=new Intent(v.getContext(), ExpressDetailActivity.class);
                intent.putExtra(getResources().getString(R.string.Mode),R.string.mode_edit);
                v.getContext().startActivity(intent);
                break;
            case R.id.express_image:
                intent=new Intent(v.getContext(), OverlayActivity.class);
                Global.postLatLng.clear();
                Global.postLatLng.add(LatLngDataManager.getLatLng(Global.postExpress.getNowAddress()));
                startActivity(intent);
                break;
        }
    }
    private void LayoutConfig(){
        Log.d("*", "LayoutConfig: start");
        switch (Mode){
            case R.string.mode_add:
                mLayout = R.layout.activity_detail_add_express;
                break;
            case R.string.mode_edit:
                mLayout = R.layout.activity_detail_edit_express;
                break;
            case R.string.mode_browse:
                mLayout= R.layout.activity_detail_browse_express;
                break;
        }
        setContentView(mLayout);//设置布局文件

    }
    private void ViewConfig(){//初始化一些控件
        switch (Mode){
            case R.string.mode_add:
                rightButton=(Button) findViewById(R.id.title_btn_add);
                initAddText();

                break;
            case R.string.mode_edit:
                mExpress= Global.postExpress;
                mExpressman=mExpress.getExpressman();
                mPPack=mExpress.getPack();
                rightButton=(Button) findViewById(R.id.title_btn_prompt_edit);
                initEditText();

                break;
            case R.string.mode_browse:
                mExpress= Global.postExpress;
                mExpressman=mExpress.getExpressman();
                mPPack=mExpress.getPack();
                //Button
                rightButton=(Button) findViewById(R.id.title_btn_go_edit);
                initBrowseText();


                break;
        }

        leftButton=(Button) findViewById(R.id.title_btn_back);
        /*  按钮监听器   */
        leftButton.setOnClickListener(this);
        rightButton.setOnClickListener(this);
    }
    private void initAddText(){
        //TextView
        pub_textView_startAddress=(TextView) findViewById(R.id.startAddress_content);
        pub_textView_endAddress=(TextView) findViewById(R.id.endAddress_content);
        pub_textView_time=(TextView) findViewById(R.id.time_content);//显示当前时间
        pub_textView_expressman_name=(TextView)findViewById(R.id.expressmanName_content);
        pub_textView_expressman_phonenum=(TextView)findViewById(R.id.expressmanPhone_content);

        //Edittext
        add_editText_expressPackId =(EditText)findViewById(R.id.packageId_content);
        add_editText_nowAddress=(EditText)findViewById(R.id.nowAddress_content);
        add_editText_expressmanId=(EditText)findViewById(R.id.expressmanId_content);

        /*  修改运单号之后要更新起始地址的TextView */
        add_editText_expressPackId.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void afterTextChanged(Editable editable) {
                String packId=add_editText_expressPackId.getText().toString();
                mPPack= PackDataManager.getObject(packId);
                if(mPPack!=null){//若运单号正确,更新起始地址
                    pub_textView_startAddress.setText(mPPack.getSenderAddress());
                    pub_textView_endAddress.setText(mPPack.getReceiverAddress());
                }
                //更新时间
                long sysTime = System.currentTimeMillis();
                CharSequence sysTimeStr = DateFormat.format("yyyy/MM/dd hh:mm:ss", sysTime);
                pub_textView_time.setText(sysTimeStr);
            }
        });

        /*  修改快递员ID后要更新快递员的NAME、PHONENUM的TextView    */
        add_editText_expressmanId.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void afterTextChanged(Editable editable) {
                String expressmanId=add_editText_expressmanId.getText().toString();
                mExpressman= UserDataManager.getObject(expressmanId);
                if(mExpressman!=null){//若快递员ID正确,更新快递员的NAME、PHONENUM
                    pub_textView_expressman_name.setText(mExpressman.getUsername());
                    pub_textView_expressman_phonenum.setText(mExpressman.getPhonenum());
                }
                //更新时间
                //long sysTime = System.currentTimeMillis();
                //CharSequence sysTimeStr = DateFormat.format("yyyy/MM/dd hh:mm:ss", sysTime);
                //pub_textView_time.setText(sysTimeStr);
            }
        });

        /*  修改当前地址后更新时间的TextView    */
        add_editText_nowAddress.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void afterTextChanged(Editable editable) {
                //更新时间
                long sysTime = System.currentTimeMillis();
                CharSequence sysTimeStr = DateFormat.format("yyyy/MM/dd hh:mm:ss", sysTime);
                pub_textView_time.setText(sysTimeStr);
            }
        });
    }
    private void initEditText(){
        //TextView
        pub_textView_startAddress=(TextView) findViewById(R.id.startAddress_content);
        pub_textView_endAddress=(TextView) findViewById(R.id.endAddress_content);
        pub_textView_time=(TextView) findViewById(R.id.time_content);//显示当前时间
        pub_textView_expressman_name=(TextView)findViewById(R.id.expressmanName_content);
        pub_textView_expressman_phonenum=(TextView)findViewById(R.id.expressmanPhone_content);
        edit_textView_expressPackId =(TextView)findViewById(R.id.packageId_content);

        //Edittext
        edit_editText_expressmanId=(EditText)findViewById(R.id.expressmanId_content);
        edit_editText_nowAddress=(EditText)findViewById(R.id.nowAddress_content);

        //编辑模式的话所有框框都有初始值!!!!!!!!!!!!
        pub_textView_startAddress.setText(mPPack.getSenderAddress());
        pub_textView_endAddress.setText(mPPack.getReceiverAddress());
        pub_textView_time.setText(mExpress.getTimestamp());
        pub_textView_expressman_name.setText(mExpressman.getUsername());
        pub_textView_expressman_phonenum.setText(mExpressman.getPhonenum());
        edit_textView_expressPackId.setText(mPPack.getPackageId());

        edit_editText_expressmanId.setText(mExpressman.getUserid());
        edit_editText_nowAddress.setText(mExpress.getNowAddress());

        /*  修改快递员ID后要更新快递员的NAME、PHONENUM的TextView    */
        edit_editText_expressmanId.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void afterTextChanged(Editable editable) {
                Log.d("*", "afterTextChanged:0");
                String expressmanId=edit_editText_expressmanId.getText().toString();
                User tempExpressman=UserDataManager.getObject(expressmanId);
                if(tempExpressman!=null){//若快递员ID正确,更新快递员的NAME、PHONENUM
                    Log.d("*", "afterTextChanged: 1");
                    mExpressman=tempExpressman;
                    pub_textView_expressman_name.setText(mExpressman.getUsername());
                    pub_textView_expressman_phonenum.setText(mExpressman.getPhonenum());
                }
                //更新时间
                //long sysTime = System.currentTimeMillis();
                //CharSequence sysTimeStr = DateFormat.format("yyyy/MM/dd hh:mm:ss", sysTime);
                //pub_textView_time.setText(sysTimeStr);
            }
        });


        /*  修改当前地址后更新时间的TextView    */
        edit_editText_nowAddress.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void afterTextChanged(Editable editable) {
                //更新时间
                long sysTime = System.currentTimeMillis();
                CharSequence sysTimeStr = DateFormat.format("yyyy/MM/dd hh:mm:ss", sysTime);
                pub_textView_time.setText(sysTimeStr);
            }
        });

    }
    private void initBrowseText(){
        //TextView
        pub_textView_startAddress=(TextView) findViewById(R.id.startAddress_content);
        pub_textView_endAddress=(TextView) findViewById(R.id.endAddress_content);
        pub_textView_time=(TextView) findViewById(R.id.time_content);//显示当前时间
        pub_textView_expressman_name=(TextView)findViewById(R.id.expressmanName_content);
        pub_textView_expressman_phonenum=(TextView)findViewById(R.id.expressmanPhone_content);
        //Edittext
        browse_textView_expressPackId=(TextView) findViewById(R.id.packageId_content);
        browse_textView_expressmanId=(TextView) findViewById(R.id.expressmanId_content);
        browse_textView_nowAddress=(TextView) findViewById(R.id.nowAddress_content);


        //浏览模式的话所有框框都有初始值!!!!!!!!!!!!
        pub_textView_startAddress.setText(mPPack.getSenderAddress());
        pub_textView_endAddress.setText(mPPack.getReceiverAddress());
        pub_textView_time.setText(mExpress.getTimestamp());
        pub_textView_expressman_name.setText(mExpressman.getUsername());
        pub_textView_expressman_phonenum.setText(mExpressman.getPhonenum());

        browse_textView_expressPackId.setText(mPPack.getPackageId());
        browse_textView_expressmanId.setText(mExpressman.getUserid());
        browse_textView_nowAddress.setText(mExpress.getNowAddress());

        expressImage=(ImageView)findViewById(R.id.express_image);
        expressImage.setOnClickListener(this);

    }
}
