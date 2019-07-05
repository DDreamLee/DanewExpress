package com.example.danewexpress.Adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.danewexpress.DataObject.Setting;
import com.example.danewexpress.R;

import java.util.List;

public class SettingAdapter extends RecyclerView.Adapter<SettingAdapter.ViewHolder> {

    private List<Setting> mSettingList;

    static class ViewHolder extends RecyclerView.ViewHolder{/*  内部类 */
        View settingView;//保存子项最外层布局的实例
        ImageView settingImage;
        TextView settingContent;

        public ViewHolder(View view){//view:RecyclerView子项的最外层布局
            super(view);
            settingView=view;
            settingImage=(ImageView)view.findViewById(R.id.setting_image);//获取控件实例
            settingContent=(TextView)view.findViewById(R.id.setting_content);
        }
    }

    public SettingAdapter(List<Setting> settingList){
        mSettingList=settingList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {/*    创建ViewHolder实例  */
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_setting,parent,false);//引入setting_item.xml
        final ViewHolder holder = new ViewHolder(view);//获取view的ViewHolder实例
        holder.settingView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {/*子项view的点击事件*/
                int position=holder.getAdapterPosition();
                Setting setting=mSettingList.get(position);
                Toast.makeText(v.getContext(),"You clicked view "+setting.getContent(),Toast.LENGTH_SHORT).show();
            }
        });
        holder.settingImage.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {/*子项image的点击事件*/
                int position=holder.getAdapterPosition();
                Setting setting=mSettingList.get(position);
                Toast.makeText(v.getContext(),"You clicked image "+setting.getContent(),Toast.LENGTH_SHORT).show();
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {/*   对RecyclerView的子项的数据赋值   */
        Setting setting=mSettingList.get(position);
        holder.settingImage.setImageResource(setting.getImageId());
        holder.settingContent.setText(setting.getContent());
    }

    @Override
    public int getItemCount() {/*   用于告诉RecyclerView一共有多少子项 */
        return mSettingList.size();
    }
}

