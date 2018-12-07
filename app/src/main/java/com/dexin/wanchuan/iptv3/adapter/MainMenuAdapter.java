package com.dexin.wanchuan.iptv3.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.dexin.wanchuan.iptv3.R;
import com.dexin.wanchuan.iptv3.bean.MainMenu;

import java.util.List;

/**
 * Created by Administrator on 2018/11/29.
 */

public class MainMenuAdapter extends RecyclerView.Adapter<MainMenuAdapter.ViewHolder> {

    private Context mContext;
    private List<MainMenu> data;

    public MainMenuAdapter(Context context, List<MainMenu> data) {
        mContext = context;
        this.data = data;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.adapter_main_menu_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.itemView.setFocusable(true);
        holder.mIv_menu.setImageResource(data.get(position).getView_id());
        holder.mTv_menu_name.setText(data.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private final ImageView mIv_menu;
        private final TextView mTv_menu_name;

        public ViewHolder(View itemView) {
            super(itemView);
            mIv_menu = itemView.findViewById(R.id.iv_menu);
            mTv_menu_name = itemView.findViewById(R.id.tv_menu_name);
        }
    }
}
