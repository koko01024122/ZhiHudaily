package com.example.administrator.zhihudaily.UI.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.administrator.zhihudaily.R;
import com.example.administrator.zhihudaily.UI.Activity.HomeActivity;
import com.example.administrator.zhihudaily.UI.Bean.Item;

import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/5/22 0022.
 */

public class InfoListAdapter extends RecyclerView.Adapter<InfoListAdapter.InfoViewHolder> {
    private ArrayList<Item> mData;//用于储存数据
    private Context mContext;//上下文
    InfoViewHolder holder=null; //viewholde，可以提高recycleview的性能




    public  InfoListAdapter(ArrayList<Item> data,Context context) {

        //构造方法，用于接收上下文和展示数据

        this.mData = data;
        this.mContext=context;
    }

    @Override
    public InfoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (headView!=null && viewType==TYPE_HEADER) return new InfoViewHolder(headView);
        holder=new InfoViewHolder(LayoutInflater.from(mContext).inflate(R.layout.info_item,parent,false));
        return holder;
    }

    @Override
    public void onBindViewHolder(InfoViewHolder holder, int position) {
        //此方法内可以对布局中的控件进行操作
        if (getItemViewType(position)==TYPE_HEADER) return;
        final int pos=getRealPosition(holder);

        holder.title.setText(mData.get(pos).getTitle());//

        Glide.with(mContext).load(mData.get(pos).getImgurl()).into(holder.img);
    }


    @Override
    public int getItemCount() {

        //获取数据长度

        return headView==null? mData.size():mData.size()+1;
    }

    class InfoViewHolder extends RecyclerView.ViewHolder {
        //此部分我也难以用语言来解释，诸位可以搜索下网上各路大牛的详解
        //我也需要学习


        TextView title;//标题
        ImageView img;//显示的图片
        TextView headTitle;//头部标题

        public InfoViewHolder(View itemView) {
            super(itemView);

            title= (TextView) itemView.findViewById(R.id.item_title);
            img= (ImageView) itemView.findViewById(R.id.item_image);
            headTitle= (TextView) itemView.findViewById(R.id.item_headtitle);
        }
    }
    public static final int TYPE_HEADER = 0;//显示headvuiew
    public static final int TYPE_NORMAL = 1;//显示普通的item
    private View headView;//这家伙就是Banner

    public void setHeadView(View headView){
        this.headView=headView;
        notifyItemInserted(0);
    }

    public View getHeadView(){
        return headView;
    }

    @Override
    public int getItemViewType(int position) {
        if (headView==null)
            return TYPE_NORMAL;
        if (position==0)
            return TYPE_HEADER;
        return TYPE_NORMAL;
    }

    private int getRealPosition(RecyclerView.ViewHolder holder) {
        int position=holder.getLayoutPosition();
        return headView==null? position:position-1;
    }
}
