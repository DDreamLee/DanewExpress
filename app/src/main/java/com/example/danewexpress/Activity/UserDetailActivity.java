package com.example.danewexpress.Activity;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.danewexpress.DataObject.Global;
import com.example.danewexpress.DataObject.User;
import com.example.danewexpress.R;
import com.example.danewexpress.Util.LoginCheck;
import com.example.danewexpress.Util.PackDataManager;
import com.example.danewexpress.Util.UserDataManager;

/*浏览、编辑(详细信息)*/
public class UserDetailActivity extends BaseActivity  implements View.OnClickListener{
    private int Mode;
    private int mLayout= R.layout.activity_detail_browse_user;
    private Button leftButton,rightButton;
    private ImageView userImage;
    private EditText
            editText_userId,
            editText_username,
            editText_phonenum,
            editText_address,
            editText_authority;

    private TextView
            textView_userId,
            textView_username,
            textView_phonenum,
            textView_address,
            textView_authority;
    private User mUser;

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
        String userId,
                username,
                phonenum,
                address,
                authority;
        switch(v.getId()){
            case R.id.title_btn_back:
                Log.d("*", "onClick: Back!");
                Activity activity=(Activity) v.getContext();
                activity.finish();
                break;
            case R.id.title_btn_add:
                //提交添加
                //检查是否存在，不存在则执行添加，并跳转Browse
                userId=editText_userId.getText().toString();
                if(PackDataManager.getObject(userId)==null){
                    username=editText_username.getText().toString();
                    phonenum=editText_phonenum.getText().toString();
                    address=editText_address.getText().toString();
                    authority=editText_authority.getText().toString();

                    if(username.length()>0
                            && LoginCheck.isMobile(phonenum)
                            && address.length()>0
                            && (authority.equals("Expressman")||authority.equals("Customer")) ){
                        String password;
                        if(authority.equals("Expressman")) password="222222";else password="111111";
                        mUser = new User(userId,
                                username,
                                phonenum,
                                password,
                                authority,
                                address);
                        //执行添加
                        UserDataManager.add2Vector(mUser);
                        //跳转
                        Intent intent = new Intent(v.getContext(), BrowseActivity.class);
                        v.getContext().startActivity(intent);
                    }else{
                        Toast.makeText(v.getContext(),"添加失败！字段值填写不合法！",Toast.LENGTH_SHORT).show();
                    }
                }else{//存在则弹出Toast
                    Toast.makeText(v.getContext(),"添加失败！账号已存在！",Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.title_btn_prompt_edit:
                //提交更新

                userId=textView_userId.getText().toString();
                authority=textView_authority.getText().toString();
                username=editText_username.getText().toString();
                phonenum=editText_phonenum.getText().toString();
                address=editText_address.getText().toString();


                if(username.length()>0
                        && LoginCheck.isMobile(phonenum)
                        && address.length()>0
                        && (authority.equals("Expressman")||authority.equals("Customer")) ){
                    String password;
                    if(authority.equals("Expressman")) password="222222";else password="111111";
                    mUser = new User(userId,
                            username,
                            phonenum,
                            password,
                            authority,
                            address);
                    //执行添加
                    UserDataManager.delete(userId);
                    UserDataManager.add2Vector(mUser);
                    //跳转
                    Intent intent = new Intent(v.getContext(), BrowseActivity.class);
                    v.getContext().startActivity(intent);
                }else{
                    Toast.makeText(v.getContext(),"更新失败！字段值填写不合法！",Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.title_btn_go_edit://跳转到更新页面
                Intent intent=new Intent(v.getContext(), UserDetailActivity.class);
                intent.putExtra(getResources().getString(R.string.Mode),R.string.mode_edit);
                startActivity(intent);
                break;
        }
    }

    private void LayoutConfig(){

        switch (Mode){
            case R.string.mode_add:
                mLayout = R.layout.activity_detail_add_user;
                break;
            case R.string.mode_edit:
                mLayout = R.layout.activity_detail_edit_user;
                break;
            case R.string.mode_browse:
                mLayout= R.layout.activity_detail_browse_user;
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
                mUser= Global.postUser;
                rightButton=(Button) findViewById(R.id.title_btn_prompt_edit);
                initEditText();

                break;
            case R.string.mode_browse:
                mUser= Global.postUser;
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
        editText_userId=(EditText)findViewById(R.id.userid_content);
        editText_authority=(EditText)findViewById(R.id.authority_content);
        editText_username=(EditText)findViewById(R.id.username_content);
        editText_phonenum=(EditText)findViewById(R.id.phonenum_content);
        editText_address=(EditText)findViewById(R.id.address_content);
        userImage=(ImageView)findViewById(R.id.user_image_content);

    }
    private void initEditText(){
        textView_userId=(TextView)findViewById(R.id.userid_content);
        textView_authority=(TextView)findViewById(R.id.authority_content);
        editText_username=(EditText)findViewById(R.id.username_content);
        editText_phonenum=(EditText)findViewById(R.id.phonenum_content);
        editText_address=(EditText)findViewById(R.id.address_content);

        userImage=(ImageView)findViewById(R.id.user_image_content);

        //设置初始值
        textView_userId.setText(mUser.getUserid());
        textView_authority.setText(mUser.getAuthority());
        editText_username.setText(mUser.getUsername());
        editText_phonenum.setText(mUser.getPhonenum());
        editText_address.setText(mUser.getAddress());

        userImage.setImageResource(mUser.getImageId());

    }
    private void initBrowseText(){
        textView_userId=(TextView)findViewById(R.id.userid_content);
        textView_authority=(TextView)findViewById(R.id.authority_content);
        textView_username=(TextView)findViewById(R.id.username_content);
        textView_phonenum=(TextView)findViewById(R.id.phonenum_content);
        textView_address=(TextView)findViewById(R.id.address_content);

        userImage=(ImageView)findViewById(R.id.user_image_content);

        //设置初始值
        textView_userId.setText(mUser.getUserid());
        textView_authority.setText(mUser.getAuthority());
        textView_username.setText(mUser.getUsername());
        textView_phonenum.setText(mUser.getPhonenum());
        textView_address.setText(mUser.getAddress());

        userImage.setImageResource(mUser.getImageId());
    }
}
