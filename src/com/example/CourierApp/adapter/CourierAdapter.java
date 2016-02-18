package com.example.CourierApp.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.CourierApp.CourierAppApplication;
import com.example.CourierApp.R;
import com.example.CourierApp.data.EmsInfo;
import com.example.CourierApp.util.StringUtil;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.List;

public class CourierAdapter extends BaseAdapter {
    private List<EmsInfo> list;
    private Context context;
    private ViewHolder holder;

    ImageLoader imageLoader = ImageLoader.getInstance();//图片加载类
    private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();

    public CourierAdapter(List<EmsInfo> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.courier_item, null);
            holder.item_cover = (ImageView) convertView.findViewById(R.id.item_cover);
            holder.item_dateline = (TextView) convertView.findViewById(R.id.item_dateline);
//            holder.item_address = (TextView) convertView.findViewById(R.id.item_address);
            holder.item_number = (TextView) convertView.findViewById(R.id.item_number);
            holder.item_person = (TextView) convertView.findViewById(R.id.item_person);
            holder.item_tel = (TextView) convertView.findViewById(R.id.item_tel);
            holder.item_storeno = (TextView) convertView.findViewById(R.id.item_storeno);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        EmsInfo child = list.get(position);
        imageLoader.displayImage("http://www.xxshequ.com/uploads/photo/" + child.getCompanypic(), holder.item_cover, CourierAppApplication.getInstance().txOptions, animateFirstListener);
        if(StringUtil.isNullOrEmpty(child.getAdd_time())){
            holder.item_dateline.setText("暂无");
        }else {
            holder.item_dateline.setText(child.getAdd_time());
        }

        holder.item_number.setText("快递单号:"+child.getSign_no());
//        holder.item_address.setText(child.getAddress());
        holder.item_person.setText("姓名:"+child.getName());
        holder.item_storeno.setText("货架号:"+child.getStore_no());
        holder.item_tel.setText("联系电话:"+child.getPhone());

        if(position % 2 == 0){
            //偶数
            convertView.setBackgroundColor(Color.argb(250, 255, 255, 255)); //颜色设置
        }else {
            convertView.setBackgroundColor(Color.argb(255, 224, 243, 250));//颜色设置
        }

        return convertView;
    }

    class ViewHolder {
        ImageView item_cover;
        TextView item_dateline;
        TextView item_address;
        TextView item_number;
        TextView item_person;
        TextView item_tel;
        TextView item_storeno;
    }
}
