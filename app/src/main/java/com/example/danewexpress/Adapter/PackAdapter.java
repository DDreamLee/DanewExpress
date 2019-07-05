package com.example.danewexpress.Adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.danewexpress.DataObject.Pack;
import com.example.danewexpress.R;

import java.util.List;

public class PackAdapter extends RecyclerView.Adapter<PackAdapter.ViewHolder> {

    private List<Pack> mPackageList;

    static class ViewHolder extends RecyclerView.ViewHolder{/*  内部类 */
        View packageView;//保存子项最外层布局的实例
        ImageView packageImage;
        TextView packageId,expenses,SenderPhone,ReceiverPhone,ReceiverAddress,PaymentStatus;

        public ViewHolder(View view){//view:RecyclerView子项的最外层布局
            super(view);
            packageView=view;
            /*获取控件实例*/
            packageImage=(ImageView)view.findViewById(R.id.package_image);

            packageId=(TextView)view.findViewById(R.id.packageId_content);
            expenses=(TextView)view.findViewById(R.id.expenses_content);
            SenderPhone=(TextView)view.findViewById(R.id.SenderPhone_content);
            ReceiverPhone=(TextView)view.findViewById(R.id.ReceiverPhone_content);
            ReceiverAddress=(TextView)view.findViewById(R.id.ReceiverAddress_content);
            PaymentStatus=(TextView)view.findViewById(R.id.PaymentStatus_content);
        }
    }

    public PackAdapter(List<Pack> packageList){
        mPackageList=packageList;
    }

    @NonNull
    @Override
    public PackAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {/*    创建ViewHolder实例  */
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_pack,parent,false);//引入package_item.xml
        final PackAdapter.ViewHolder holder = new PackAdapter.ViewHolder(view);//获取view的ViewHolder实例
        /*
        holder.packageView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {//子项view的点击事件
                int position=holder.getAdapterPosition();
                Pack pack=mPackageList.get(position);
                Toast.makeText(v.getContext(),"You clicked view "+pack.getPackageId(),Toast.LENGTH_SHORT).show();
            }
        });
        holder.packageImage.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {//子项image的点击事件
                int position=holder.getAdapterPosition();
                Pack pack=mPackageList.get(position);
                Toast.makeText(v.getContext(),"You clicked image "+pack.getPackageId(),Toast.LENGTH_SHORT).show();
            }
        });
        */
        return holder;
    }

    @Override
    public void onBindViewHolder(PackAdapter.ViewHolder holder, int position) {/*   对RecyclerView的子项的数据赋值   */
        Pack pack=mPackageList.get(position);
        holder.packageImage.setImageResource(pack.getImageId());
        holder.packageId.setText(pack.getPackageId());
        holder.expenses.setText(pack.getExpenses());
        holder.SenderPhone.setText(pack.getSenderPhone());
        holder.ReceiverPhone.setText(pack.getReceiverPhone());
        holder.ReceiverAddress.setText(pack.getReceiverAddress());
        holder.PaymentStatus.setText(pack.getPaymentStatus());
    }

    @Override
    public int getItemCount() {/*   用于告诉RecyclerView一共有多少子项 */
        return mPackageList.size();
    }
}
