package com.sheji.sheji.adpter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sheji.sheji.R;
import com.sheji.sheji.bean.TargetBean;

import java.util.List;

public class BodyTargetAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private List<TargetBean> dataList;

    public BodyTargetAdapter(Context context) {
        this.context = context;
    }

    public void setDataList(List<TargetBean> dataList) {
        this.dataList = dataList;
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view;
        if (viewType == 0) {
            view = LayoutInflater.from(context).inflate(R.layout.list_item_head, viewGroup, false);
            return new HeadViewHolder(view);
        } else {
            view = LayoutInflater.from(context).inflate(R.layout.list_item, viewGroup, false);
            return new ViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        if (viewHolder instanceof ViewHolder && dataList.size() > 0) {
            ((ViewHolder) viewHolder).shootingSerialNumberTv.setText(position + "");
            ((ViewHolder) viewHolder).hitsTv.setText(dataList.get(position).getHit());
            ((ViewHolder) viewHolder).shootingIntervalTv.setText(dataList.get(position).getShootingInterval());
            ((ViewHolder) viewHolder).timeTv.setText(dataList.get(position).getTime());
            ((ViewHolder) viewHolder).dateTv.setText(dataList.get(position).getDate());
        }
    }

    @Override
    public int getItemCount() {
        return 10;
    }

    class HeadViewHolder extends RecyclerView.ViewHolder {
        HeadViewHolder(View itemView) {
            super(itemView);
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView shootingSerialNumberTv;
        private TextView hitsTv;
        private TextView shootingIntervalTv;
        private TextView timeTv;
        private TextView dateTv;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            shootingSerialNumberTv = itemView.findViewById(R.id.shooting_serial_number_tv);
            hitsTv = itemView.findViewById(R.id.hits_tv);
            shootingIntervalTv = itemView.findViewById(R.id.shooting_interval_tv);
            timeTv = itemView.findViewById(R.id.time_tv);
            dateTv = itemView.findViewById(R.id.date_tv);
        }
    }
}
