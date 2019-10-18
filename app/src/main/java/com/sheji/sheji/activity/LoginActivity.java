package com.sheji.sheji.activity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
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
import com.sheji.sheji.bean.Constant;
import com.sheji.sheji.bean.DaoUtil;
import com.sheji.sheji.dialog.TextDialog;
import com.sheji.sheji.util.SerialPortUtils;
import com.sheji.sheji.util.SharedPreferencesUtils;

/**
 * @author kk-zhaoqingfeng
 */
public class LoginActivity extends BaseActivity implements View.OnClickListener, SerialPortUtils.OnLoginDataReceiveListener {
    private RelativeLayout mBackRl;
    private LinearLayout mEtLy;
    private EditText mEquipmentNumberEt;
    private EditText mGunNumberEt;
    private Button mDetermineTv;

    private ProgressDialog progressDialog = null;

    private boolean success = false;

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case Constant.LOGIN_SUCCESS:
                    textDialog.dismiss();
                    success = true;

                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                    break;
                case Constant.LOGIN_FAIL:
                    mBackRl.setBackgroundResource(R.drawable.login_back);
                    mEtLy.setVisibility(View.VISIBLE);

                    textDialog.dismiss();
                    break;
            }

        }
    };
    private TextDialog textDialog;

    private String equipmentNumber;
    private String gunNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (!success) {
            //TODO 关闭串口
            SerialPortUtils.getInstance().closeSerialPort();
        }
    }

    private void initView() {
        mBackRl = findViewById(R.id.back_rl);
        mEtLy = findViewById(R.id.et_ly);
        mEquipmentNumberEt = findViewById(R.id.equipment_number_et);
        mGunNumberEt = findViewById(R.id.gun_number_et);

        mDetermineTv = findViewById(R.id.determine_tv);
        mDetermineTv.setOnClickListener(this);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("登陆中...");
        progressDialog.setCanceledOnTouchOutside(false);
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
        equipmentNumber = mEquipmentNumberEt.getText().toString().trim();
        gunNumber = mGunNumberEt.getText().toString().trim();
        if (TextUtils.isEmpty(equipmentNumber)) {
            Toast.makeText(this, "请在此处输入本设备编号", Toast.LENGTH_SHORT).show();
            return;
        }

        int equipment = Integer.valueOf(equipmentNumber);
        if (equipment < 1 || equipment > 20) {
            Toast.makeText(this, "请在此处输入本设备编号(编号为1-20)", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(gunNumber)) {
            Toast.makeText(this, "请在此处输入枪械编号", Toast.LENGTH_SHORT).show();
            return;
        }

//        //TODO 打开串口 绑定操作
//        // Pad发送总控台枪和计数器绑定的数据协议-申请报文
        if (SerialPortUtils.getInstance().openSerialPort() == null) {
            Toast.makeText(this, "设备打开异常,请再次点击确认按钮", Toast.LENGTH_SHORT).show();
            if ("ttyUSB0".equals(SerialPortUtils.getInstance().getPort())) {
                SerialPortUtils.getInstance().setPort("ttyUSB1");
            } else {
                SerialPortUtils.getInstance().setPort("ttyUSB0");
            }
        } else {
            progressDialog.show();

            byte[] sendByte = new byte[18];
            sendByte[0] = (byte) Integer.parseInt("CC", 16);
            sendByte[1] = (byte) Integer.parseInt("23", 16);
            sendByte[2] = (byte) Integer.parseInt("AA", 16);
            sendByte[3] = (byte) Integer.parseInt("BD", 16);

            String e = Integer.toHexString(equipment);
            sendByte[4] = (byte) Integer.parseInt("00", 16);
            sendByte[5] = (byte) Integer.parseInt("00", 16);
            sendByte[6] = (byte) Integer.parseInt("00", 16);
            sendByte[7] = (byte) Integer.parseInt(e, 16);

            String g = String.format("%8s", gunNumber).replace(' ', '0');
            sendByte[8] = (byte) Integer.parseInt(strTo16(g.substring(0, 1)), 16);
            sendByte[9] = (byte) Integer.parseInt(strTo16(g.substring(1, 2)), 16);
            sendByte[10] = (byte) Integer.parseInt(strTo16(g.substring(2, 3)), 16);
            sendByte[11] = (byte) Integer.parseInt(strTo16(g.substring(3, 4)), 16);
            sendByte[12] = (byte) Integer.parseInt(strTo16(g.substring(4, 5)), 16);
            sendByte[13] = (byte) Integer.parseInt(strTo16(g.substring(5, 6)), 16);
            sendByte[14] = (byte) Integer.parseInt(strTo16(g.substring(6, 7)), 16);
            sendByte[15] = (byte) Integer.parseInt(strTo16(g.substring(7, 8)), 16);

            sendByte[16] = (byte) Integer.parseInt("0A", 16);
            sendByte[17] = (byte) Integer.parseInt("0D", 16);

            SerialPortUtils.getInstance().sendSerialPort(sendByte);
            SerialPortUtils.getInstance().setOnLoginDataReceiveListener(this);
        }
    }

    @Override
    public void onFBReceive(boolean success) {
        progressDialog.dismiss();

        mBackRl.setBackgroundResource(R.drawable.login_state_back);
        mEtLy.setVisibility(View.GONE);
        //清空数据库
        DaoUtil.deleteAll();

        if (success) {
            textDialog = new TextDialog(this, "√ 枪号绑定成功");
            SharedPreferencesUtils.getInstance().setEquipmentNumber(equipmentNumber);
            SharedPreferencesUtils.getInstance().setGunNumber(gunNumber);

            handler.sendEmptyMessageDelayed(Constant.LOGIN_SUCCESS, 2000);
        } else {
            textDialog = new TextDialog(this, "X 枪号绑定失败\n请您重新绑定");
            handler.sendEmptyMessageDelayed(Constant.LOGIN_FAIL, 2000);
        }
        textDialog.show();
    }

    private String strTo16(String s) {
        String str = "";
        for (int i = 0; i < s.length(); i++) {
            int ch = (int) s.charAt(i);
            String s4 = Integer.toHexString(ch);
            str = str + s4;
        }
        return str;
    }
}
