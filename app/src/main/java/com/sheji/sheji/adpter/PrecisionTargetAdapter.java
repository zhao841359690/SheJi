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

public class PrecisionTargetAdapter extends RecyclerView.Adapter<PrecisionTargetAdapter.ViewHolder> {
    private Context context;
    private List<TargetBean> dataList;

    public PrecisionTargetAdapter(Context context) {
        this.context = context;
    }

    public void setDataList(List<TargetBean> dataList) {
        this.dataList = dataList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public PrecisionTargetAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_item_precision, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PrecisionTargetAdapter.ViewHolder viewHolder, int position) {
        viewHolder.shootingSerialNumberTv.setText(dataList.get(position).getNumber() + "");
        if (dataList.get(position).getHit()) {
            viewHolder.hitsTv.setText("是");
            viewHolder.hitsTv.setTextColor(context.getColor(R.color.black));
        } else {
            viewHolder.hitsTv.setText("否");
            viewHolder.hitsTv.setTextColor(context.getColor(R.color.red));
        }
        viewHolder.positionTv.setText(dataList.get(position).getPrecisionRingNumber());
        viewHolder.shootingIntervalTv.setText(dataList.get(position).getShootingInterval());
        viewHolder.timeTv.setText(dataList.get(position).getTime());
        viewHolder.dateTv.setText(dataList.get(position).getDate());
    }

    @Override
    public int getItemCount() {
        return dataList == null ? 0 : dataList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView shootingSerialNumberTv;
        private TextView hitsTv;
        private TextView positionTv;
        private TextView shootingIntervalTv;
        private TextView timeTv;
        private TextView dateTv;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            shootingSerialNumberTv = itemView.findViewById(R.id.shooting_serial_number_tv);
            hitsTv = itemView.findViewById(R.id.hits_tv);
            positionTv = itemView.findViewById(R.id.position);
            shootingIntervalTv = itemView.findViewById(R.id.shooting_interval_tv);
            timeTv = itemView.findViewById(R.id.time_tv);
            dateTv = itemView.findViewById(R.id.date_tv);
        }
    }
}
