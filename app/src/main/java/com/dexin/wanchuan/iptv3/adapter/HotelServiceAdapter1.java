package com.dexin.wanchuan.iptv3.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dexin.wanchuan.iptv3.R;
import com.dexin.wanchuan.iptv3.bean.HotelServiceMenu;

import java.util.List;

/**
 * Created by Administrator on 2018/11/29.
 */

public class HotelServiceAdapter1 extends RecyclerView.Adapter<HotelServiceAdapter1.ViewHolder> {

    private Context mContext;
    private List<HotelServiceMenu> data;

    public HotelServiceAdapter1(Context context, List<HotelServiceMenu> data) {
        mContext = context;
        this.data = data;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.adapter_hotel_service_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.mTv_menu_name.setText(data.get(position).getName());
        holder.itemView.setFocusable(true);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView mTv_menu_name;

        public ViewHolder(View itemView) {
            super(itemView);
            mTv_menu_name = itemView.findViewById(R.id.tv_service);
        }
    }
}
