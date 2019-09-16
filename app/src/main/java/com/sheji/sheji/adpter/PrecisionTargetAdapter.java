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
        if (dataList.get(position).getPrecisionRingNumber().equals("00 00 00 01")) {
            viewHolder.positionTv.setText("10环上");
        } else if (dataList.get(position).getPrecisionRingNumber().equals("00 00 00 02")) {
            viewHolder.positionTv.setText("10环右");
        } else if (dataList.get(position).getPrecisionRingNumber().equals("00 00 00 04")) {
            viewHolder.positionTv.setText("10环下");
        } else if (dataList.get(position).getPrecisionRingNumber().equals("00 00 00 08")) {
            viewHolder.positionTv.setText("10环左");
        } else if (dataList.get(position).getPrecisionRingNumber().equals("00 00 00 10")) {
            viewHolder.positionTv.setText("9环上");
        } else if (dataList.get(position).getPrecisionRingNumber().equals("00 00 00 20")) {
            viewHolder.positionTv.setText("9环右");
        } else if (dataList.get(position).getPrecisionRingNumber().equals("00 00 00 40")) {
            viewHolder.positionTv.setText("9环下");
        } else if (dataList.get(position).getPrecisionRingNumber().equals("00 00 00 80")) {
            viewHolder.positionTv.setText("9环左");
        } else if (dataList.get(position).getPrecisionRingNumber().equals("00 00 01 00")) {
            viewHolder.positionTv.setText("8环上");
        } else if (dataList.get(position).getPrecisionRingNumber().equals("00 00 02 00")) {
            viewHolder.positionTv.setText("8环右上");
        } else if (dataList.get(position).getPrecisionRingNumber().equals("00 00 03 00")) {
            viewHolder.positionTv.setText("8环右");
        } else if (dataList.get(position).getPrecisionRingNumber().equals("00 00 04 00")) {
            viewHolder.positionTv.setText("8环右下");
        } else if (dataList.get(position).getPrecisionRingNumber().equals("00 00 10 00")) {
            viewHolder.positionTv.setText("8环下");
        } else if (dataList.get(position).getPrecisionRingNumber().equals("00 00 20 00")) {
            viewHolder.positionTv.setText("8环左下");
        } else if (dataList.get(position).getPrecisionRingNumber().equals("00 00 40 00")) {
            viewHolder.positionTv.setText("8环左");
        } else if (dataList.get(position).getPrecisionRingNumber().equals("00 00 80 00")) {
            viewHolder.positionTv.setText("8环左上");
        } else if (dataList.get(position).getPrecisionRingNumber().equals("00 01 00 00")) {
            viewHolder.positionTv.setText("7环上");
        } else if (dataList.get(position).getPrecisionRingNumber().equals("00 02 00 00")) {
            viewHolder.positionTv.setText("7环右上");
        } else if (dataList.get(position).getPrecisionRingNumber().equals("00 04 00 00")) {
            viewHolder.positionTv.setText("7环右");
        } else if (dataList.get(position).getPrecisionRingNumber().equals("00 08 00 00")) {
            viewHolder.positionTv.setText("7环右下");
        } else if (dataList.get(position).getPrecisionRingNumber().equals("00 10 00 00")) {
            viewHolder.positionTv.setText("7环下");
        } else if (dataList.get(position).getPrecisionRingNumber().equals("00 20 00 00")) {
            viewHolder.positionTv.setText("7环左下");
        } else {
            viewHolder.positionTv.setText("");
        }
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
