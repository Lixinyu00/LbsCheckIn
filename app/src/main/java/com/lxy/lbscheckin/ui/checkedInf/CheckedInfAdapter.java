package com.lxy.lbscheckin.ui.checkedInf;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lxy.lbscheckin.R;
import com.lxy.lbscheckin.data.model.CheckIn;

import java.util.List;

/**
 * Created by LXY on 2018/6/1.
 */

public class CheckedInfAdapter extends RecyclerView.Adapter<CheckedInfAdapter.CheckedInfviewholder> {

    private List<CheckIn> list;

    public void setData(List<CheckIn> list) {
        this.list = list;
    }

    @Override
    public CheckedInfviewholder onCreateViewHolder(ViewGroup parent, int viewType) {
        View convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recycler_checked, parent, false);
        CheckedInfviewholder holder = new CheckedInfviewholder(convertView);
        return holder;
    }

    @Override
    public void onBindViewHolder(CheckedInfviewholder holder, int position) {
        CheckIn checkIn=list.get(position);
        holder.tv_item_name.setText(checkIn.getUser()+"");
        holder.tv_item_time.setText(checkIn.getUpdatedAt()+"");
        if (checkIn.getType()==0){
            holder.tv_item_type.setText("签退");
        }else {
            holder.tv_item_type.setText("签到");
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class CheckedInfviewholder extends RecyclerView.ViewHolder {
        TextView tv_item_name;
        TextView tv_item_type;
        TextView tv_item_time;

        public CheckedInfviewholder(View itemView) {
            super(itemView);
            tv_item_name = itemView.findViewById(R.id.tv_item_name);
            tv_item_type = itemView.findViewById(R.id.tv_item_type);
            tv_item_time = itemView.findViewById(R.id.tv_item_time);
        }
    }
}
