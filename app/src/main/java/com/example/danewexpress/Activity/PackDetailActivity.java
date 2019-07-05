package com.example.danewexpress.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.danewexpress.BaiduMapActivity.OverlayActivity;
import com.example.danewexpress.DataObject.Address;
import com.example.danewexpress.DataObject.Global;
import com.example.danewexpress.DataObject.Pack;
import com.example.danewexpress.R;
import com.example.danewexpress.Util.LatLngDataManager;
import com.example.danewexpress.Util.LoginCheck;
import com.example.danewexpress.Util.PackDataManager;

/*浏览、编辑(详细信息)*/
public class PackDetailActivity extends BaseActivity  implements View.OnClickListener{
    private int Mode;
    private int mLayout= R.layout.activity_detail_browse_pack;
    private ImageView img;
    private Button leftButton,rightButton;
    //add
    private EditText editText_packId;
    //edit
    private TextView textView_packId;
    //add+edit
    private EditText
            editText_SenderPhone,
            editText_SenderAddress,
            editText_ReceiverPhone,
            editText_ReceiverAddress,
            editText_expenses,
            editText_PayerPhone,
            editText_PaymentStatus;
    //browse
    private TextView
            textView_SenderPhone,
            textView_SenderAddress,
            textView_ReceiverPhone,
            textView_ReceiverAddress,
            textView_expenses,
            textView_PayerPhone,
            textView_PaymentStatus;



    private Pack mPack;


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
        String packId,
                expenses,
                SenderPhone,
                SenderAddress,
                ReceiverPhone,
                ReceiverAddress,
                PayerPhone;
        Intent intent;
        switch(v.getId()){
            case R.id.title_btn_back:
                Log.d("*", "onClick: Back!");
                Activity activity=(Activity) v.getContext();
                activity.finish();
                break;
            case R.id.title_btn_add:
                //提交添加
                //检查是否存在，不存在则执行添加，并跳转Browse
                packId=editText_packId.getText().toString();
                if(PackDataManager.getObject(packId)==null){
                    expenses=editText_expenses.getText().toString();
                    SenderPhone=editText_SenderPhone.getText().toString();
                    SenderAddress=editText_SenderAddress.getText().toString();
                    ReceiverPhone=editText_ReceiverPhone.getText().toString();
                    ReceiverAddress=editText_ReceiverAddress.getText().toString();
                    PayerPhone=editText_PayerPhone.getText().toString();

                    if(expenses.length()>0
                            && LoginCheck.isMobile(SenderPhone)
                            && SenderAddress.length()>0
                            && LoginCheck.isMobile(ReceiverPhone)
                            && ReceiverAddress.length()>0
                            && LoginCheck.isMobile(PayerPhone)) {
                        mPack = new Pack(packId, expenses, SenderPhone, new Address(SenderAddress), ReceiverPhone, new Address(ReceiverAddress), PayerPhone);
                        //执行添加
                        PackDataManager.addObject(mPack);
                        //跳转
                        intent = new Intent(v.getContext(), BrowseActivity.class);
                        v.getContext().startActivity(intent);
                    }else{
                        Toast.makeText(v.getContext(),"添加失败！字段值填写不合法！",Toast.LENGTH_SHORT).show();
                    }
                }else{//存在则弹出Toast
                    Toast.makeText(v.getContext(),"添加失败！订单号已存在！",Toast.LENGTH_SHORT).show();
                }

                break;
            case R.id.title_btn_prompt_edit:
                //提交更新

                packId=textView_packId.getText().toString();
                expenses=editText_expenses.getText().toString();
                SenderPhone=editText_SenderPhone.getText().toString();
                SenderAddress=editText_SenderAddress.getText().toString();
                ReceiverPhone=editText_ReceiverPhone.getText().toString();
                ReceiverAddress=editText_ReceiverAddress.getText().toString();
                PayerPhone=editText_PayerPhone.getText().toString();
                //检查字段长度
                if(expenses.length()>0
                        && LoginCheck.isMobile(SenderPhone)
                        && SenderAddress.length()>0
                        && LoginCheck.isMobile(ReceiverPhone)
                        && ReceiverAddress.length()>0
                        && LoginCheck.isMobile(PayerPhone)){

                    //实例化对象
                    mPack=new Pack(packId,expenses,SenderPhone,new Address(SenderAddress),ReceiverPhone,new Address(ReceiverAddress),PayerPhone);

                    //执行更新
                    PackDataManager.delete(packId);
                    PackDataManager.addObject(mPack);
                    //跳转
                    intent = new Intent(v.getContext(),BrowseActivity.class);
                    v.getContext().startActivity(intent);

                }else{//弹出toast
                    Toast.makeText(v.getContext(),"更新失败！字段值填写不合法！",Toast.LENGTH_SHORT).show();
                }

                break;
            case R.id.title_btn_go_edit://跳转到更新页面
                intent=new Intent(v.getContext(), PackDetailActivity.class);
                intent.putExtra(getResources().getString(R.string.Mode),R.string.mode_edit);
                startActivity(intent);
                break;
            case R.id.package_image:
                intent=new Intent(v.getContext(), OverlayActivity.class);
                Global.postLatLng= LatLngDataManager.getObjectVector(Global.postPack.getPackageId());
                startActivity(intent);
                break;
        }
    }

    private void LayoutConfig(){

        switch (Mode){
            case R.string.mode_add:
                mLayout = R.layout.activity_detail_add_pack;

                break;
            case R.string.mode_edit:
                mLayout = R.layout.activity_detail_edit_pack;

                break;
            case R.string.mode_browse:
                mLayout= R.layout.activity_detail_browse_pack;

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
                mPack= Global.postPack;
                rightButton=(Button) findViewById(R.id.title_btn_prompt_edit);
                initEditText();

                break;
            case R.string.mode_browse:
                mPack= Global.postPack;
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
        editText_packId=(EditText) findViewById(R.id.packageId_content);
        editText_SenderPhone=(EditText) findViewById(R.id.SenderPhone_content);
        editText_SenderAddress=(EditText) findViewById(R.id.SenderAddress_content);
        editText_ReceiverPhone=(EditText) findViewById(R.id.ReceiverPhone_content);
        editText_ReceiverAddress=(EditText) findViewById(R.id.ReceiverAddress_content);
        editText_expenses=(EditText) findViewById(R.id.expenses_content);
        editText_PayerPhone=(EditText) findViewById(R.id.PayerPhone_content);
        editText_PaymentStatus=(EditText) findViewById(R.id.PaymentStatus_content);
    }
    private void initEditText(){
        textView_packId=(TextView)findViewById(R.id.packageId_content);
        editText_SenderPhone=(EditText) findViewById(R.id.SenderPhone_content);
        editText_SenderAddress=(EditText) findViewById(R.id.SenderAddress_content);
        editText_ReceiverPhone=(EditText) findViewById(R.id.ReceiverPhone_content);
        editText_ReceiverAddress=(EditText) findViewById(R.id.ReceiverAddress_content);
        editText_expenses=(EditText) findViewById(R.id.expenses_content);
        editText_PayerPhone=(EditText) findViewById(R.id.PayerPhone_content);
        editText_PaymentStatus=(EditText) findViewById(R.id.PaymentStatus_content);

        //框框都有初始值!!!!!!!!!!!!
        textView_packId.setText(mPack.getPackageId());
        editText_SenderPhone.setText(mPack.getSenderPhone());
        editText_SenderAddress.setText(mPack.getSenderAddress());
        editText_ReceiverPhone.setText(mPack.getReceiverPhone());
        editText_ReceiverAddress.setText(mPack.getReceiverAddress());
        editText_expenses.setText(mPack.getExpenses());
        editText_PayerPhone.setText(mPack.getPayerPhone());
        editText_PaymentStatus.setText(mPack.getPaymentStatus());
    }
    private void initBrowseText(){
        textView_packId=(TextView)findViewById(R.id.packageId_content);
        textView_SenderPhone=(TextView)findViewById(R.id.SenderPhone_content);
        textView_SenderAddress=(TextView)findViewById(R.id.SenderAddress_content);
        textView_ReceiverPhone=(TextView)findViewById(R.id.ReceiverPhone_content);
        textView_ReceiverAddress=(TextView)findViewById(R.id.ReceiverAddress_content);
        textView_expenses=(TextView)findViewById(R.id.expenses_content);
        textView_PayerPhone=(TextView)findViewById(R.id.PayerPhone_content);
        textView_PaymentStatus=(TextView)findViewById(R.id.PaymentStatus_content);

        //浏览模式的话所有框框都有初始值!!!!!!!!!!!!
        textView_packId.setText(mPack.getPackageId());
        textView_SenderPhone.setText(mPack.getSenderPhone());
        textView_SenderAddress.setText(mPack.getSenderAddress());
        textView_ReceiverPhone.setText(mPack.getReceiverPhone());
        textView_ReceiverAddress.setText(mPack.getReceiverAddress());
        textView_expenses.setText(mPack.getExpenses());
        textView_PayerPhone.setText(mPack.getPayerPhone());
        textView_PaymentStatus.setText(mPack.getPaymentStatus());

        img=(ImageView)findViewById(R.id.package_image);
        img.setOnClickListener(this);
    }
}
