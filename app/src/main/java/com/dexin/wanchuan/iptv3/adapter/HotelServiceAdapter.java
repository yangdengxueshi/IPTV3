package com.dexin.wanchuan.iptv3.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.dexin.wanchuan.iptv3.R;
import com.dexin.wanchuan.iptv3.bean.HotelServiceMenu;

import java.util.List;

/**
 * Created by Administrator on 2018/11/30.
 */

public class HotelServiceAdapter extends BaseAdapter {

    private Context mContext;
    private List<HotelServiceMenu> data;

    public HotelServiceAdapter(Context context, List<HotelServiceMenu> data) {
        mContext = context;
        this.data = data;
    }


    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public HotelServiceMenu getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        HotelServiceMenu item = getItem(position);
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.adapter_hotel_service_layout, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.tv_name = convertView.findViewById(R.id.tv_service);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.tv_name.setText(item.getName());
        return convertView;
    }


    class ViewHolder {
        TextView tv_name;
    }
}
