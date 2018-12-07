package com.dexin.wanchuan.iptv3.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.dexin.wanchuan.iptv3.R;
import com.dexin.wanchuan.iptv3.bean.GlideRoundTransform;
import com.dexin.wanchuan.iptv3.bean.LiveTV;
import com.dexin.wanchuan.iptv3.util.IptvSP;

import java.util.List;

public class LiveProgramListAdapter extends BaseAdapter {

    private List<LiveTV> list;
    private Context context;
    private IptvSP sp;
    private RequestManager glideRequest;

    public LiveProgramListAdapter(Context context, List<LiveTV> list) {
        this.list = list;
        this.context = context;
        glideRequest = Glide.with(context);
        this.sp = new IptvSP(context);
    }

    public void refresh(List<LiveTV> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public int getCount() {
        return list.size();
    }

    public Object getItem(int position) {
        return list.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    @SuppressLint("ViewHolder")
    public View getView(int position, View view, ViewGroup parent) {
        LiveTV item = list.get(position);
        view = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(
                R.layout.program_list_item, null);
        final ImageView imageView = view.findViewById(R.id.imageView);
        TextView nameTextView = view.findViewById(R.id.nameTextView);
        TextView numberTextView = view
                .findViewById(R.id.numberTextView);
        numberTextView.setText((position + 1) + ".");
        nameTextView.setText(item.getName());
        if (item.isImageVisible() && !item.getImagePath().equals("")) {
            imageView.setVisibility(View.VISIBLE);
            String imagePath = sp.getImagePath(item.getImagePath());
            //            String imagePath = "http://192.168.200.199:8030/iptv2/manager/" + (item.getImagePath());
            glideRequest.load(imagePath).transform(new GlideRoundTransform(context)).into(imageView);//最终服务器调试接口
        } else {
            imageView.setVisibility(View.GONE);
        }
        return view;
    }
}
