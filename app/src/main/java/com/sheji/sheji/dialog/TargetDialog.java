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
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.sheji.sheji.R;


/**
 * @author kk-zhaoqingfeng
 */
public class TargetDialog extends Dialog implements View.OnClickListener {
    private Context context;

    private EditText mErectEt;
    private EditText mLodgingEt;
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
        getWindow().setBackgroundDrawableResource(R.color.translate);

        initView();
    }

    private void initView() {
        mErectEt = findViewById(R.id.erect_et);

        mLodgingEt = findViewById(R.id.lodging_et);

        mCyclesEt = findViewById(R.id.cycles_et);

        mCancelTv = findViewById(R.id.cancel_tv);
        mCancelTv.setOnClickListener(this);

        mDetermineTv = findViewById(R.id.determine_tv);
        mDetermineTv.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cancel_tv:
                dismiss();
                break;
            case R.id.determine_tv:
                if (TextUtils.isEmpty(mErectEt.getText().toString().trim())
                        || TextUtils.isEmpty(mLodgingEt.getText().toString().trim())
                        || TextUtils.isEmpty(mCyclesEt.getText().toString().trim())) {
                    Toast.makeText(context, "竖起维持时间，倒伏维持时间，循环次数不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                erectTime = Integer.valueOf(mErectEt.getText().toString().trim());
                lodgingTime = Integer.valueOf(mLodgingEt.getText().toString().trim());
                cycles = Integer.valueOf(mCyclesEt.getText().toString().trim());
                if (erectTime == 0 || lodgingTime == 0 || cycles == 0) {
                    Toast.makeText(context, "竖起维持时间，倒伏维持时间，循环次数不能为0", Toast.LENGTH_SHORT).show();
                    return;
                }
                onTargetDiaLogCyclesListener.cycles(erectTime, lodgingTime, cycles);
                dismiss();
                break;
        }
    }


    public interface OnTargetDiaLogCyclesListener {
        //循环
        public void cycles(long erectTime, long lodgingTime, int cyclesNumber);
    }
}
