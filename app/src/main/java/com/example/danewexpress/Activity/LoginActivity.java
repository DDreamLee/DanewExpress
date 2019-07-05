package com.example.danewexpress.Activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.danewexpress.R;
import com.example.danewexpress.Util.LoginCheck;
import com.example.danewexpress.Util.UserDataManager;
import com.example.danewexpress.DataObject.Global;
import com.mob.MobSDK;

import java.util.Timer;
import java.util.TimerTask;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

public class LoginActivity extends BaseActivity  implements View.OnClickListener{

    private static Button buttonLogin,buttonBack,buttonLogon,buttonVericode;
    private static EditText editText_userid,editText_password,editText_phonenum,editText_vericode;
    private static LinearLayout layoutVericode,layoutPhonenum;
    private static ProgressDialog dialog;
    private TimerTask tt;
    private Timer tm;
    private int TIME = 60;//倒计时60秒
    public String country = "86";//中国区号
    private static final int CODE_REPEAT = 1;
    private String phone;
    private String VerificationCode;
    private String userid;
    private String password;
    private String phonenum;

    //吐司小方法
    private void toast(final String str) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(LoginActivity.this, str, Toast.LENGTH_SHORT).show();
            }
        });
    }


    Handler hd = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == CODE_REPEAT) {
                buttonVericode.setEnabled(true);
                buttonLogon.setEnabled(true);
                tm.cancel();
                tt.cancel();
                TIME = 60;
                buttonVericode.setText("重新发送验证码");
            } else {
                buttonVericode.setText("重新发送验证码");
            }
        }
    };

//    回调
    EventHandler eh = new EventHandler(){
    @Override
    public void afterEvent(int event, int result, Object data) {
//        super.afterEvent(i, i1, o);
        if (result == SMSSDK.RESULT_COMPLETE) {
            if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
                toast("验证成功");
                //Intent intent=new Intent(LoginActivity.this, BrowseActivity.class);
                //startActivity(intent);

                int addResult= UserDataManager.add2Vector(userid,phonenum,password);//进行用户添加操作，返回结果

                if(addResult==R.string.toast_add_success){

                    //showLogonProgressBar();
                    Global.setUser(userid);           //设置用户登录全局变量
                    Intent intent=new Intent(LoginActivity.this, BrowseActivity.class);
                    startActivity(intent);
                }else{
                    // addResult存放弹出消息对应的资源标识符
                    //Toast.makeText(LoginActivity.this,addResult,Toast.LENGTH_SHORT).show();
                }
            } else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
                toast("获取验证码成功");
            } else if (event ==SMSSDK.EVENT_GET_SUPPORTED_COUNTRIES) {
             //返回支持发送验证码的国家列表
            }
        }else{//错误等在这里（包括验证失败）
            //错误码请参照http://wiki.mob.com/android-api-错误码参考/这里我就不再继续写了
            ((Throwable)data).printStackTrace();
            String str = data.toString();
            toast("验证失败");
        }
    }
};
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        buttonLogin=(Button) findViewById(R.id.button_login);
        buttonBack=(Button) findViewById(R.id.button_back);
        buttonLogon=(Button) findViewById(R.id.button_logon);
        buttonVericode=(Button) findViewById(R.id.button_sendvericode);
        editText_userid =(EditText) findViewById(R.id.username);
        editText_password=(EditText) findViewById(R.id.password);
        editText_phonenum=(EditText) findViewById(R.id.phonenum);
        editText_vericode=(EditText) findViewById(R.id.vericode);
        layoutVericode = (LinearLayout) findViewById(R.id.layout_vericode);
        layoutPhonenum = (LinearLayout) findViewById(R.id.layout_phonenum);

        buttonLogin.setOnClickListener(this);
        buttonLogon.setOnClickListener(this);
        buttonBack.setOnClickListener(this);
        buttonVericode.setOnClickListener(this);

        MobSDK.init(this, "2b62c81ab3288", "73889b9df34ff12cb9c26a915780fcec");
        SMSSDK.registerEventHandler(eh); //注册短信回调（记得销毁，避免泄露内存）


        editText_userid.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable editable) {
                if(editText_userid.getText().length()==0){
                    editText_userid.setHint(getResources().getString(R.string.invalid_username));
                    editText_userid.setHintTextColor(getResources().getColor(R.color.red));
                }else{
                    editText_userid.setHint(getResources().getString(R.string.prompt_username));
                    editText_userid.setHintTextColor(getResources().getColor(R.color.gray));
                }
            }
        });

        editText_password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable editable) {
                if(editText_password.getText().length()<5){
                    editText_password.setHint(getResources().getString(R.string.invalid_password));
                    editText_password.setHintTextColor(getResources().getColor(R.color.red));
                }else{
                    editText_password.setHint(getResources().getString(R.string.prompt_password));
                    editText_password.setHintTextColor(getResources().getColor(R.color.gray));
                }
            }
        });

        editText_phonenum.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable editable) {
                if(editText_phonenum.getText().length()!=11){
                    editText_phonenum.setHint(getResources().getString(R.string.invalid_phonenum));
                    editText_phonenum.setHintTextColor(getResources().getColor(R.color.red));
                }else{
                    editText_phonenum.setHint(getResources().getString(R.string.prompt_phonenum));
                    editText_phonenum.setHintTextColor(getResources().getColor(R.color.gray));
                }
            }
        });

        UserDataManager.initData();
    }

    @Override
    protected void onResume() {

        if(dialog!=null) dialog.dismiss();
        Intent intent=getIntent();
        boolean exit=intent.getBooleanExtra("Exit",false);
        if(exit) finish();
        super.onResume();
    }

            //弹窗确认下发
    private void alterWarning() {
        //构造器
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("提示"); //设置标题
        builder.setMessage("我们将要发送到" + phone + "验证"); //设置内容
        builder.setIcon(R.mipmap.ic_launcher);//设置图标，图片id即可
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            //设置确定按钮
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss(); //关闭dialog
                //通过sdk发送短信验证（请求获取短信验证码，在监听（eh）中返回）
                SMSSDK.getVerificationCode(country, phone);
//                phone = editText_phonenum.getText().toString().trim().replaceAll("/s","");

                //做倒计时操作
                Toast.makeText(LoginActivity.this, "已发送" + which, Toast.LENGTH_SHORT).show();
                buttonVericode.setEnabled(false);
                buttonLogon.setEnabled(true);
                tm = new Timer();
                tt = new TimerTask() {
                    @Override
                    public void run() {
                        hd.sendEmptyMessage(TIME--);
                    }
                };
                tm.schedule(tt,0,1000);
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() { //设置取消按钮
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                Toast.makeText(LoginActivity.this, "已取消" + which, Toast.LENGTH_SHORT).show();
            }
        });
        //参数都设置完成了，创建并显示出来
        builder.create().show();
    }

    @Override
    public void onClick(View v) {
        phone = editText_phonenum.getText().toString().trim().replaceAll("/s","");
        buttonVericode.setEnabled(true);
        switch (v.getId()){
            case R.id.button_sendvericode:
                alterWarning();
                break;
            case R.id.button_login://按下登录键
                pressLogin(v);
                break;
            case R.id.button_logon://按下注册键
//                alterWarning();
                pressLogon(v);
//                VerificationCode = editText_vericode.getText().toString().trim().replaceAll("/s","");
//                SMSSDK.submitVerificationCode(country,phone,VerificationCode);
                break;
            case R.id.button_back://按下返回登录键
                pressBack();
                break;
            default:
                break;
        }

    }

    private void pressLogin(View v){        /*按下登录键*/
        showLoginProgressBar();
        Log.d("*", "before ");
        if(!checkEditText(1)){
            dialog.dismiss();
            Toast.makeText(v.getContext(),R.string.toast_login_illegal,Toast.LENGTH_SHORT).show();
        }else{
            String userid= editText_userid.getText().toString();
            String password=editText_password.getText().toString();
            if(LoginCheck.isPasswordCorrect(UserDataManager.getDataVector(),userid,password)){
                Log.d("*", "success ");
                Global.setUser(userid);           /*  设置用户登录全局变量  */
                Toast.makeText(v.getContext(),R.string.toast_login_success,Toast.LENGTH_SHORT).show();
                //Intent intent=new Intent(LoginActivity.this, MainActivity.class);
                Intent intent=new Intent(LoginActivity.this, BrowseActivity.class);/* 假做1 */
                Log.d("*", "intent ");
                startActivity(intent);
            }else{
                Log.d("*", "fail ");
                dialog.dismiss();
                Toast.makeText(v.getContext(),R.string.toast_login_wrongpsw,Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void pressLogon(View v){        /*按下注册键*/
        if(layoutVericode.getVisibility()!=View.VISIBLE && layoutPhonenum.getVisibility()!=View.VISIBLE){
            //更新界面
            layoutVericode.setVisibility(View.VISIBLE);
            layoutPhonenum.setVisibility(View.VISIBLE);
            buttonLogin.setVisibility(View.GONE);
            buttonBack.setVisibility(View.VISIBLE);
        }else{

            if(!checkEditText(2)){
                Toast.makeText(v.getContext(),R.string.toast_logon_illegal,Toast.LENGTH_SHORT).show();
            }else{
                userid= editText_userid.getText().toString();
                password=editText_password.getText().toString();
                phonenum=editText_phonenum.getText().toString();

                VerificationCode = editText_vericode.getText().toString().trim().replaceAll("/s","");
                SMSSDK.submitVerificationCode(country,phone,VerificationCode);
//                int addResult= UserDataManager.add2Vector(userid,phonenum,password);/*  进行用户添加操作，返回结果   */
//
//                if(addResult==R.string.toast_add_success){
//                    showLogonProgressBar();
//                    Global.setUser(userid);           /*  设置用户登录全局变量  */
//                    Toast.makeText(v.getContext(),R.string.toast_login_success,Toast.LENGTH_SHORT).show();
//                    //Intent intent=new Intent(LoginActivity.this, MainActivity.class);
//                    Intent intent=new Intent(LoginActivity.this, BrowseActivity.class);/* 假做2 */
//                    startActivity(intent);
//                }else{
//                    /*  addResult存放弹出消息对应的资源标识符   */
//                    Toast.makeText(v.getContext(),addResult,Toast.LENGTH_SHORT).show();
//                }
            }

        }
    }

    private void pressBack(){//按下返回登录键
        layoutVericode.setVisibility(View.GONE);
        layoutPhonenum.setVisibility(View.GONE);
        buttonBack.setVisibility(View.GONE);
        buttonLogin.setVisibility(View.VISIBLE);
    }

    private boolean checkEditText(int type){//检查是否有空格没填
        int size_login=2,size_logon=3;
        int cnt=0;
        if(editText_userid.getText().length()==0){
            editText_userid.setHint(getResources().getString(R.string.invalid_username));
            editText_userid.setHintTextColor(getResources().getColor(R.color.red));
        }else{
            editText_userid.setHint(getResources().getString(R.string.prompt_username));
            editText_userid.setHintTextColor(getResources().getColor(R.color.gray));
            cnt++;
        }
        Log.d("*username", String.valueOf(cnt));
        if(editText_password.getText().length()<5){
            editText_password.setHint(getResources().getString(R.string.invalid_password));
            editText_password.setHintTextColor(getResources().getColor(R.color.red));
        }else{
            editText_password.setHint(getResources().getString(R.string.prompt_password));
            editText_password.setHintTextColor(getResources().getColor(R.color.gray));
            cnt++;
        }
        Log.d("*password", String.valueOf(cnt));
        if(type==1){//login
            if(cnt<size_login) return false;
            else return true;
        }
        //logon
        if(editText_phonenum.getText().length()!=11){
            editText_phonenum.setHint(getResources().getString(R.string.invalid_phonenum));
            editText_phonenum.setHintTextColor(getResources().getColor(R.color.red));
        }else{
            editText_phonenum.setHint(getResources().getString(R.string.prompt_phonenum));
            editText_phonenum.setHintTextColor(getResources().getColor(R.color.gray));
            cnt++;
        }
        Log.d("*phonenum", String.valueOf(cnt));
        if(cnt<size_logon) return false;
        else return true;
    }

    private void showLoginProgressBar() {                             /*  带进度条的Dialog */
        dialog = new ProgressDialog(this);
        dialog.setTitle("登录");
        dialog.setMessage("正在连接服务器...");
        dialog.setCancelable(false);//配置是否能通过Back键取消，若false，则需要用对象的dismiss()来关闭对话框,适用于程序满足一定条件时关闭该框
        dialog.show();
        //先show()再改颜色
        //dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.BLUE);
        //dialog.getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(Color.BLACK);
    }

    private void showLogonProgressBar() {                             /*  带进度条的Dialog */
        dialog = new ProgressDialog(this);
        dialog.setTitle("注册成功");
        dialog.setMessage("正在自动登录...");
        dialog.setCancelable(false);//配置是否能通过Back键取消，若false，则需要用对象的dismiss()来关闭对话框,适用于程序满足一定条件时关闭该框
        dialog.show();
        //先show()再改颜色
        //dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.BLUE);
        //dialog.getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(Color.BLACK);
    }

    //销毁短信注册
    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 注销回调接口registerEventHandler必须和unregisterEventHandler配套使用，否则可能造成内存泄漏。
        SMSSDK.unregisterEventHandler(eh);
    }

}
