/*
ZZ          ZZ   ZZ          ZZ      ZZZZZZ     ZZZZZ            ZZZZZZZ
 ZZ        ZZ    ZZ          ZZ    ZZ      Z  Z      ZZZ      ZZZ       ZZZ
Z ZZ      ZZ     ZZ          ZZ   ZZ        ZZ         ZZ    ZZ           ZZ
ZZZ ZZ   ZZ      ZZ          ZZ  ZZ         ZZ          ZZ  ZZ             Z
ZZZZ  ZZZ        ZZ          ZZ  ZZ         ZZ          ZZ  ZZ             ZZ
ZZZZ  ZZZ        ZZ          ZZ  ZZ         ZZ          ZZ  ZZ             ZZ
ZZZ  ZZ  ZZ      ZZ          ZZ  ZZ         ZZ          ZZ  ZZ             Z
Z  ZZ     ZZ      ZZ         ZZ  ZZ         ZZ          ZZ   ZZ           ZZ
  ZZ        ZZ     ZZ       ZZ   ZZ         ZZ          ZZ    ZZ        ZZZ
ZZ           ZZ     ZZZZZZZZ     ZZ         ZZ          ZZ      ZZZZZZZZ

This proprietary material is not to be reproduced,
used or disclosed except in accordance with the
contract or upon written authorization of Xumo LLC.

Copyright (c) 2017 Xumo LLC. All rights reserved.
*/
package com.sheji.sheji.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.view.TimePickerView;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.sheji.sheji.R;

import java.util.Calendar;
import java.util.Date;

public class TargetDialog extends Dialog implements View.OnClickListener {
    private Context context;

    private TextView mErectTv;
    private TextView mLodgingTv;
    private EditText mCyclesEt;
    private TextView mCancelTv;
    private TextView mDetermineTv;

    private long erectTime = 0;
    private long lodgingTime = 0;
    private int cycles = 0;

    private OnTargetDiaLogCyclesListener onTargetDiaLogCyclesListener;

    public TargetDialog(Context context, OnTargetDiaLogCyclesListener onTargetDiaLogCyclesListener) {
        super(context, R.style.Theme_AppCompat_Dialog);
        this.context = context;
        this.onTargetDiaLogCyclesListener = onTargetDiaLogCyclesListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_target);
        setCanceledOnTouchOutside(false);

        initView();
    }

    private void initView() {
        mErectTv = findViewById(R.id.erect_tv);
        mErectTv.setOnClickListener(this);

        mLodgingTv = findViewById(R.id.lodging_tv);
        mLodgingTv.setOnClickListener(this);

        mCyclesEt = findViewById(R.id.cycles_et);
        mCyclesEt.setOnClickListener(this);

        mCancelTv = findViewById(R.id.cancel_tv);
        mCancelTv.setOnClickListener(this);

        mDetermineTv = findViewById(R.id.determine_tv);
        mDetermineTv.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.erect_tv:
                initTimePicker(v, 1);
                break;
            case R.id.lodging_tv:
                initTimePicker(v, 2);
                break;
            case R.id.cycles_et:
                break;
            case R.id.cancel_tv:
                dismiss();
                break;
            case R.id.determine_tv:
                if (TextUtils.isEmpty(mErectTv.getText().toString().trim())
                        || TextUtils.isEmpty(mLodgingTv.getText().toString().trim())
                        || TextUtils.isEmpty(mCyclesEt.getText().toString().trim())) {
                    Toast.makeText(context, "竖起维持时间，倒伏维持时间，循环次数", Toast.LENGTH_SHORT).show();
                    return;
                }
                cycles = Integer.valueOf(mCyclesEt.getText().toString().trim());
                if (cycles == 0) {
                    Toast.makeText(context, "循环次数不能为0", Toast.LENGTH_SHORT).show();
                    return;
                }
                onTargetDiaLogCyclesListener.cycles(erectTime, lodgingTime, cycles);
                dismiss();
                break;
        }
    }

    private void initTimePicker(View view, final int type) {
        Calendar date = Calendar.getInstance();
        date.set(0, 0, 0, 0, 0, 0);
        TimePickerView pvTime = new TimePickerBuilder(context, new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                long time = date.getHours() * 60 * 60 + date.getMinutes() * 60 + date.getSeconds();
                if (type == 1) {
                    erectTime = time;
                    if (time == 0) {
                        mErectTv.setText("");
                    } else {
                        mErectTv.setText(time + "秒");
                    }
                } else {
                    lodgingTime = time;
                    if (time == 0) {
                        mLodgingTv.setText("");
                    } else {
                        mLodgingTv.setText(time + "秒");
                    }
                }
            }
        }).setType(new boolean[]{false, false, false, true, true, true})
                .setDate(date)
                .setCancelText("取消")
                .setSubmitText("确定")
                .setLabel("年", "月", "日", "时", "分", "秒")
                .isDialog(true) //默认设置false ，内部实现将DecorView 作为它的父控件。
                .build();

        Dialog mDialog = pvTime.getDialog();
        if (mDialog != null) {

            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    Gravity.BOTTOM);

            params.leftMargin = 0;
            params.rightMargin = 0;
            pvTime.getDialogContainerLayout().setLayoutParams(params);

            Window dialogWindow = mDialog.getWindow();
            if (dialogWindow != null) {
                dialogWindow.setWindowAnimations(com.bigkoo.pickerview.R.style.picker_view_slide_anim);//修改动画样式
                dialogWindow.setGravity(Gravity.BOTTOM);//改成Bottom,底部显示
                dialogWindow.setDimAmount(0.1f);
            }
        }
        pvTime.show(view);
    }


    public interface OnTargetDiaLogCyclesListener {
        //循环
        public void cycles(long erectTime, long lodgingTime, int cyclesNumber);
    }
}
