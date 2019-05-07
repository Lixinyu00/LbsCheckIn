package com.lxy.lbscheckin.ui.wifiCheckIn;

import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lxy.lbscheckin.R;

import java.util.List;

/**
 * Created by LXY on 2018/5/29.
 */

public class CheckInAdapter extends RecyclerView.Adapter<CheckInAdapter.CheckInviewholder> {
    private final String TAG="CheckInAdapter";

    private List<ScanResult> list;
    private OnItemClickListener listener;

    public void setData(List<ScanResult> list) {
        this.list = list;
    }

    @Override
    public CheckInviewholder onCreateViewHolder(ViewGroup parent, int viewType) {
        View convertView= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recycler_wifi,parent,false);
        CheckInviewholder holder=new CheckInviewholder(convertView);
        return holder;
    }

    @Override
    public void onBindViewHolder(CheckInviewholder holder, final int position) {
        int wifi_level;
        holder.tv_wifi_id.setText(list.get(position).SSID+" ");
        wifi_level=WifiManager.calculateSignalLevel(list.get(position).level,5);
        holder.tv_level.setText(wifi_level+"");
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onClick(view,position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class CheckInviewholder extends RecyclerView.ViewHolder {
        TextView tv_wifi_id;
        TextView tv_level;

        public CheckInviewholder(View itemView) {
            super(itemView);
            tv_wifi_id=itemView.findViewById(R.id.tv_wifi_id);
            tv_level=itemView.findViewById(R.id.tv_level);
        }
    }

    public void setOnItemClickListener(OnItemClickListener listener){
            this.listener=listener;
    }

    public interface OnItemClickListener{
        void onClick(View v, int position);
    }
}
