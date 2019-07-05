package com.example.danewexpress.Adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.danewexpress.DataObject.Express;
import com.example.danewexpress.DataObject.Pack;
import com.example.danewexpress.R;

import java.util.List;

public class ExpressAdapter  extends RecyclerView.Adapter<ExpressAdapter.ViewHolder> {

    private List<Express> mExpressList;

    static class ViewHolder extends RecyclerView.ViewHolder{/*  内部类 */
        View expressView;//保存子项最外层布局的实例
        ImageView expressImage;
        TextView expressPackId,time,startAddress,nowAddress,endAddress;

        public ViewHolder(View view){//view:RecyclerView子项的最外层布局
            super(view);
            expressView=view;

            /*获取控件实例*/
            expressImage=(ImageView)view.findViewById(R.id.express_image);

            expressPackId=(TextView)view.findViewById(R.id.expressPackId_content);
            nowAddress=(TextView)view.findViewById(R.id.nowAddress_content);
            time=(TextView)view.findViewById(R.id.time_content);
            startAddress=(TextView)view.findViewById(R.id.startAddress_content);
            endAddress=(TextView)view.findViewById(R.id.endAddress_content);
        }
    }

    public ExpressAdapter(List<Express> expressList){
        mExpressList=expressList;
    }

    @NonNull
    @Override
    public ExpressAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {/*    创建ViewHolder实例  */
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_express,parent,false);//引入express_item.xml
        final ExpressAdapter.ViewHolder holder = new ExpressAdapter.ViewHolder(view);//获取view的ViewHolder实例
        /*
        holder.expressView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {//子项view的点击事件
                int position=holder.getAdapterPosition();
                Express express=mExpressList.get(position);
                Global.postExpress=express;

                //跳转到浏览界面
                Intent intent = new Intent(v.getContext(), ExpressDetailActivity.class);
                intent.putExtra(v.getContext().getResources().getString(R.string.Mode), R.string.mode_browse);
                v.getContext().startActivity(intent);
                //Toast.makeText(v.getContext(),"You clicked view "+express.getExpressPackageId(),Toast.LENGTH_SHORT).show();
            }
        });
        holder.expressView.setOnLongClickListener(new View.OnLongClickListener() {//子项view的长按删除事件
            @Override
            public boolean onLongClick(View v) {
                int position=holder.getAdapterPosition();
                Express express=mExpressList.get(position);
                ExpressDataManager.delete(express.getExpressPackageId());

                return false;
            }
        });

        holder.expressImage.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {子项image的点击事件
                int position=holder.getAdapterPosition();
                Express express=mExpressList.get(position);
                Global.postExpress=express;

                //跳转到浏览界面
                Intent intent = new Intent(v.getContext(), ExpressDetailActivity.class);
                intent.putExtra(v.getContext().getResources().getString(R.string.Mode), R.string.mode_browse);
                v.getContext().startActivity(intent);

                //Toast.makeText(v.getContext(),"You clicked image "+express.getExpressPackageId(),Toast.LENGTH_SHORT).show();
            }
        });
        */
        return holder;
    }

    @Override
    public void onBindViewHolder(ExpressAdapter.ViewHolder holder, int position) {/*   对RecyclerView的子项的数据赋值   */
        Express express=mExpressList.get(position);
        //Pack mPack= PackDataManager.getObject(express.getExpressPackageId());//通过运单号定位对应的包裹
        Pack mPack=express.getPack();
        holder.expressImage.setImageResource(express.getImageId());

        holder.expressPackId.setText(express.getExpressPackageId());
        holder.nowAddress.setText(express.getNowAddress());
        holder.time.setText(express.getTimestamp());
        holder.startAddress.setText(mPack.getSenderAddress());
        holder.endAddress.setText(mPack.getReceiverAddress());

    }

    @Override
    public int getItemCount() {/*   用于告诉RecyclerView一共有多少子项 */
        return mExpressList.size();
    }
}
