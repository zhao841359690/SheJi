package com.sheji.sheji.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.sheji.sheji.base.BaseActivity;
import com.sheji.sheji.R;
import com.sheji.sheji.bean.Constant;
import com.sheji.sheji.bean.DaoUtil;
import com.sheji.sheji.dialog.TextDialog;
import com.sheji.sheji.util.SerialPortUtils;
import com.sheji.sheji.util.SharedPreferencesUtils;

import java.util.ArrayList;
import java.util.List;

import android_serialport_api.SerialPortFinder;

public class LoginActivity extends BaseActivity implements View.OnClickListener, SerialPortUtils.OnLoginDataReceiveListener {
    private RelativeLayout mBackRl;
    private LinearLayout mEtLy;
    private EditText mEquipmentNumberEt;
    private EditText mGunNumberEt;
    private Spinner mNodeSp;
    private Button mDetermineTv;

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

        mNodeSp = findViewById(R.id.node_sp);
        mNodeSp.setPrompt("选择设备节点");
        SerialPortFinder mSerialPortFinder = new SerialPortFinder();
        List<String> allNode = new ArrayList<String>();
        String[] mStr = mSerialPortFinder.getAllDevicesName();
        for (int i = 0; i < mStr.length; i++) {
            allNode.add(mStr[i]);
        }
        ArrayAdapter<String> node_adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, allNode);
        node_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mNodeSp.setAdapter(node_adapter);
        mNodeSp.setSelection(0, true);
        mNodeSp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                SerialPortUtils.getInstance().setPort((String) mNodeSp.getSelectedItem());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

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

        if (gunNumber.length() != 6) {
            Toast.makeText(this, "请在此处输入枪械编号(6位)", Toast.LENGTH_SHORT).show();
            return;
        }
        //TODO 打开串口 绑定操作
        // Pad发送总控台枪和计数器绑定的数据协议-申请报文
        if (SerialPortUtils.getInstance().openSerialPort() == null) {
            Toast.makeText(this, "设备打开异常,正在尝试重新打开设备", Toast.LENGTH_SHORT).show();
            SerialPortUtils.getInstance().openSerialPort();
        } else {
            SerialPortUtils.getInstance().sendSerialPort("CC23AABD0-65535" + gunNumber + equipmentNumber + "0A0D");
            SerialPortUtils.getInstance().setOnLoginDataReceiveListener(this);
        }
    }

    @Override
    public void onFBReceive(boolean success) {
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
}
