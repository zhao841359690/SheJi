package com.sheji.sheji.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.sheji.sheji.base.BaseActivity;
import com.sheji.sheji.R;
import com.sheji.sheji.bean.DaoUtil;
import com.sheji.sheji.dialog.TextDialog;
import com.sheji.sheji.util.SharedPreferencesUtils;


public class LoginActivity extends BaseActivity implements View.OnClickListener {
    private RelativeLayout mBackRl;
    private LinearLayout mEtLy;
    private EditText mGunNumberEt;
    private Button mDetermineTv;

    private boolean login = true;

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    textDialog.dismiss();

                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                    break;
                case 2:
                    mBackRl.setBackgroundResource(R.drawable.login_back);
                    mEtLy.setVisibility(View.VISIBLE);

                    textDialog.dismiss();
                    break;
            }

        }
    };
    private TextDialog textDialog;
    private String gunNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initView();
    }

    private void initView() {
        mBackRl = findViewById(R.id.back_rl);
        mEtLy = findViewById(R.id.et_ly);
        mGunNumberEt = findViewById(R.id.gun_number_et);
        mDetermineTv = findViewById(R.id.determine_tv);
        mDetermineTv.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.determine_tv:
                submit();
                break;
        }
    }

    private void submit() {
        gunNumber = mGunNumberEt.getText().toString().trim();
        if (TextUtils.isEmpty(gunNumber)) {
            Toast.makeText(this, "请在此处输入枪械编号", Toast.LENGTH_SHORT).show();
            return;
        }

        mBackRl.setBackgroundResource(R.drawable.login_state_back);
        mEtLy.setVisibility(View.GONE);

        if (login) {
            textDialog = new TextDialog(this, "√ 枪号绑定成功");
            if (!gunNumber.equals(SharedPreferencesUtils.getInstance().getGunNumber())) {
                DaoUtil.deleteAll();
            }
            SharedPreferencesUtils.getInstance().setGunNumber(gunNumber);

            handler.sendEmptyMessageDelayed(1, 1000);
        } else {
            textDialog = new TextDialog(this, "X 枪号绑定失败\n请您重新绑定");
            handler.sendEmptyMessageDelayed(2, 1000);
        }
        textDialog.show();
    }
}
