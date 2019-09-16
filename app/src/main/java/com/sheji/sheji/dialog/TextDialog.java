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

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.widget.TextView;

import com.sheji.sheji.R;

public class TextDialog extends Dialog {
    private String text;
    private TextView mContentTv;


    public TextDialog(Activity activity, String text) {
        super(activity, R.style.Theme_AppCompat_Dialog);
        this.text = text;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_text);
        setCanceledOnTouchOutside(false);

        initView();
    }

    private void initView() {
        mContentTv = findViewById(R.id.content_tv);
        mContentTv.setText(text);
    }
}
