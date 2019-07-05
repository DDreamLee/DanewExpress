package com.example.danewexpress.Adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.danewexpress.DataObject.User;
import com.example.danewexpress.R;

import java.util.List;

public class UserAdapter  extends RecyclerView.Adapter<UserAdapter.ViewHolder> {

    private List<User> mUserList;

    static class ViewHolder extends RecyclerView.ViewHolder{/*  内部类 */
        View userView;//保存子项最外层布局的实例
        ImageView userImage;
        TextView userId,userName,userPhonenum,userAuthority;

        public ViewHolder(View view){//view:RecyclerView子项的最外层布局
            super(view);
            userView=view;
            userImage=(ImageView)view.findViewById(R.id.user_image);//获取控件实例
            userId=(TextView)view.findViewById(R.id.user_id_content);
            userName=(TextView)view.findViewById(R.id.user_name_content);
            userPhonenum=(TextView)view.findViewById(R.id.user_phonenum_content);
            userAuthority=(TextView)view.findViewById(R.id.user_authority_content);
        }
    }

    public UserAdapter(List<User> userList){
        mUserList=userList;
    }

    @NonNull
    @Override
    public UserAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {/*    创建ViewHolder实例  */
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_user,parent,false);//引入user_item.xml
        final UserAdapter.ViewHolder holder = new UserAdapter.ViewHolder(view);//获取view的ViewHolder实例
        /*
        holder.userView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {//子项view的点击事件
                int position=holder.getAdapterPosition();
                User user=mUserList.get(position);
                Toast.makeText(v.getContext(),"You clicked view "+user.getUserid(),Toast.LENGTH_SHORT).show();
            }
        });
        holder.userImage.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {//子项image的点击事件
                int position=holder.getAdapterPosition();
                User user=mUserList.get(position);
                Toast.makeText(v.getContext(),"You clicked image "+user.getUserid(),Toast.LENGTH_SHORT).show();
            }
        });
        */
        return holder;
    }

    @Override
    public void onBindViewHolder(UserAdapter.ViewHolder holder, int position) {/*   对RecyclerView的子项的数据赋值   */
        User user=mUserList.get(position);
        holder.userImage.setImageResource(user.getImageId());
        holder.userId.setText(user.getUserid());
        holder.userName.setText(user.getUsername());
        holder.userPhonenum.setText(user.getPhonenum());
        holder.userAuthority.setText(user.getAuthority());
    }

    @Override
    public int getItemCount() {/*   用于告诉RecyclerView一共有多少子项 */
        return mUserList.size();
    }
}
