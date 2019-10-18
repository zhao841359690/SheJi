package com.sheji.sheji.activity;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.sheji.sheji.R;
import com.sheji.sheji.adpter.OrdinaryTargetAdapter;
import com.sheji.sheji.adpter.PrecisionTargetAdapter;
import com.sheji.sheji.base.BaseActivity;
import com.sheji.sheji.bean.Constant;
import com.sheji.sheji.bean.DaoUtil;
import com.sheji.sheji.bean.TargetBean;
import com.sheji.sheji.dialog.TargetDialog;
import com.sheji.sheji.util.SerialPortUtils;
import com.sheji.sheji.util.SharedPreferencesUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author kk-zhaoqingfeng
 */
public class MainActivity extends BaseActivity implements View.OnClickListener, TargetDialog.OnTargetDiaLogCyclesListener, SerialPortUtils.OnMainDataReceiveListener {
    private TextView mHeadTargetTv;
    private TextView mBodyTargetTv;
    private TextView mChestTargetTv;
    private TextView mPrecisionTargetTv;

    private ImageView mTargetIv;
    private TextView mTop10Tv;
    private TextView mRight10Tv;
    private TextView mBottom10Tv;
    private TextView mLeft10Tv;
    private TextView mTop9Tv;
    private TextView mRight9Tv;
    private TextView mBottom9Tv;
    private TextView mLeft9Tv;
    private TextView mTop8Tv;
    private TextView mRightTop8Tv;
    private TextView mRight8Tv;
    private TextView mRightBottom8Tv;
    private TextView mBottom8Tv;
    private TextView mLeftTop8Tv;
    private TextView mLeft8Tv;
    private TextView mLeftBottom8Tv;
    private TextView mTop7Tv;
    private TextView mRightTop7Tv;
    private TextView mRight7Tv;
    private TextView mRightBottom7Tv;
    private TextView mBottom7Tv;
    private TextView mLeftTop7Tv;
    private TextView mLeft7Tv;
    private TextView mLeftBottom7Tv;
    private TextView mTop6Tv;
    private TextView mRightTop6Tv;
    private TextView mRight6Tv;
    private TextView mRightBottom6Tv;
    private TextView mBottom6Tv;
    private TextView mLeftTop6Tv;
    private TextView mLeft6Tv;
    private TextView mLeftBottom6Tv;
    private TextView mHitTv;
    private RelativeLayout mMenuRl;
    private TextView mShootingDroneTv;
    private RelativeLayout mShootingDroneRl;
    private Switch mShootingDroneSwitch;
    private TextView mTargetNumberTv;
    private TextView mDateTv;
    private TextView mTimeTv;
    private Switch mFireSwitch;
    private TextView mGunNumberTv;
    private TextView mCounterNumberTv;
    private TextView mCounterRemainingBattery;
    private TextView mCumulativeShotNumberHeadTv;
    private TextView mCumulativeShotNumberContentTv;
    private TextView mCumulativeShotsTv;
    private TextView mPositionTv;
    private RecyclerView mMainRv;
    private TextView mTotalTv;
    private LinearLayout mPageLy;
    private TextView mPrePageTv;
    private TextView mFirstTv;
    private View mV1;
    private TextView mSecondTv;
    private View mV2;
    private TextView mThirdTv;
    private View mV3;
    private TextView mFourthTv;
    private View mV4;
    private TextView mFifthTv;
    private View mV5;
    private TextView mSixthTv;
    private View mV6;
    private TextView mNextPageTv;
    private View mV7;

    private OrdinaryTargetAdapter ordinaryTargetAdapter;
    private List<TargetBean> ordinaryTargetList = new ArrayList<>();
    private int totalOrdinaryTarget = 0;
    private int totalOrdinaryTargetPage = 0;
    private int ordinaryTargetPage = 0;
    private int nowOrdinaryTargetPage = 0;

    private PrecisionTargetAdapter precisionTargetAdapter;
    private List<TargetBean> precisionTargetList = new ArrayList<>();
    private int totalPrecisionTarget = 0;
    private int totalPrecisionTargetPage = 0;
    private int precisionTargetPage = 0;
    private int nowPrecisionTargetPage = 0;

    private int fireNumber;

    private int pageType = Constant.PRECISION;

    private int size = 10;

    @SuppressLint("HandlerLeak")
    private Handler timeHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            timeHandler.removeMessages(1);
            mDateTv.setText(new SimpleDateFormat("yyyy-MM-dd").format(new Date(System.currentTimeMillis())));
            mTimeTv.setText(new SimpleDateFormat("HH：mm：ss").format(new Date(System.currentTimeMillis())));
            timeHandler.sendEmptyMessageDelayed(1, 1000);
        }
    };


    private long erectTime = 0;
    private long lodgingTime = 0;
    private int cyclesNumber = 0;

    @SuppressLint("HandlerLeak")
    private Handler cyclesHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    cyclesHandler.sendEmptyMessageDelayed(2, lodgingTime);

                    mShootingDroneTv.setText("射击靶机倒");
                    mShootingDroneSwitch.setChecked(false);

                    //TODO 总控台控制靶机起倒的协议(倒下)
                    //总控台控制靶机起倒的协议(倒下)
                    if (SerialPortUtils.getInstance().openSerialPort() == null) {
                        Toast.makeText(MainActivity.this, "设备打开异常,正在尝试重新打开设备", Toast.LENGTH_SHORT).show();
                        SerialPortUtils.getInstance().openSerialPort();
                    } else {
                        byte[] sendByte = new byte[7];
                        sendByte[0] = (byte) Integer.parseInt("CC", 16);
                        sendByte[1] = (byte) Integer.parseInt("23", 16);
                        sendByte[2] = (byte) Integer.parseInt("AA", 16);
                        sendByte[3] = (byte) Integer.parseInt("DD", 16);

                        sendByte[4] = (byte) Integer.parseInt("00", 16);

                        sendByte[5] = (byte) Integer.parseInt("0A", 16);
                        sendByte[6] = (byte) Integer.parseInt("0D", 16);

                        SerialPortUtils.getInstance().sendSerialPort(sendByte);
                    }
                    break;
                case 2:
                    cyclesNumber--;
                    if (cyclesNumber > 0) {
                        cyclesHandler.sendEmptyMessageDelayed(1, erectTime);

                        mShootingDroneTv.setText("射击靶机起");
                        mShootingDroneSwitch.setChecked(true);
                        //TODO 总控台控制靶机起倒的协议(起来)
                        //总控台控制靶机起倒的协议(起来)
                        if (SerialPortUtils.getInstance().openSerialPort() == null) {
                            Toast.makeText(MainActivity.this, "设备打开异常,正在尝试重新打开设备", Toast.LENGTH_SHORT).show();
                            SerialPortUtils.getInstance().openSerialPort();
                        } else {
                            byte[] sendByte = new byte[7];
                            sendByte[0] = (byte) Integer.parseInt("CC", 16);
                            sendByte[1] = (byte) Integer.parseInt("23", 16);
                            sendByte[2] = (byte) Integer.parseInt("AA", 16);
                            sendByte[3] = (byte) Integer.parseInt("DD", 16);

                            sendByte[4] = (byte) Integer.parseInt("01", 16);

                            sendByte[5] = (byte) Integer.parseInt("0A", 16);
                            sendByte[6] = (byte) Integer.parseInt("0D", 16);

                            SerialPortUtils.getInstance().sendSerialPort(sendByte);
                        }
                    }
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //TODO PAD控制靶机靶子类型的协议
        //PAD控制靶机靶子类型的协议
        if (SerialPortUtils.getInstance().openSerialPort() == null) {
            Toast.makeText(MainActivity.this, "设备打开异常,正在尝试重新打开设备", Toast.LENGTH_SHORT).show();
            SerialPortUtils.getInstance().openSerialPort();
        } else {
            byte[] sendByte = new byte[11];
            sendByte[0] = (byte) Integer.parseInt("CC", 16);
            sendByte[1] = (byte) Integer.parseInt("23", 16);
            sendByte[2] = (byte) Integer.parseInt("AA", 16);
            sendByte[3] = (byte) Integer.parseInt("DE", 16);

            int equipment = Integer.valueOf(SharedPreferencesUtils.getInstance().getEquipmentNumber());
            String e = Integer.toHexString(equipment);
            sendByte[4] = (byte) Integer.parseInt("00", 16);
            sendByte[5] = (byte) Integer.parseInt("00", 16);
            sendByte[6] = (byte) Integer.parseInt("00", 16);
            sendByte[7] = (byte) Integer.parseInt(e, 16);

            sendByte[8] = (byte) Integer.parseInt("04", 16);

            sendByte[9] = (byte) Integer.parseInt("0A", 16);
            sendByte[10] = (byte) Integer.parseInt("0D", 16);

            SerialPortUtils.getInstance().sendSerialPort(sendByte);
            SerialPortUtils.getInstance().setOnMainDataReceiveListener(this);
        }
        initView();
        initData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        timeHandler.removeMessages(1);

        cyclesHandler.removeMessages(1);
        cyclesHandler.removeMessages(2);
        //TODO 关闭串口
        SerialPortUtils.getInstance().closeSerialPort();
    }

    private void initView() {
        mHeadTargetTv = findViewById(R.id.head_target_tv);
        mHeadTargetTv.setOnClickListener(this);

        mBodyTargetTv = findViewById(R.id.body_target_tv);
        mBodyTargetTv.setOnClickListener(this);

        mChestTargetTv = findViewById(R.id.chest_target_tv);
        mChestTargetTv.setOnClickListener(this);

        mPrecisionTargetTv = findViewById(R.id.precision_target_tv);
        mPrecisionTargetTv.setOnClickListener(this);

        mTargetIv = findViewById(R.id.target_iv);

        mTop10Tv = findViewById(R.id.top10_tv);
        mRight10Tv = findViewById(R.id.right10_tv);
        mBottom10Tv = findViewById(R.id.bottom10_tv);
        mLeft10Tv = findViewById(R.id.left10_tv);
        mTop9Tv = findViewById(R.id.top9_tv);
        mRight9Tv = findViewById(R.id.right9_tv);
        mBottom9Tv = findViewById(R.id.bottom9_tv);
        mLeft9Tv = findViewById(R.id.left9_tv);
        mTop8Tv = findViewById(R.id.top8_tv);
        mRightTop8Tv = findViewById(R.id.rightTop8_tv);
        mRight8Tv = findViewById(R.id.right8_tv);
        mRightBottom8Tv = findViewById(R.id.rightBottom8_tv);
        mBottom8Tv = findViewById(R.id.bottom8_tv);
        mLeftTop8Tv = findViewById(R.id.leftTop8_tv);
        mLeft8Tv = findViewById(R.id.left8_tv);
        mLeftBottom8Tv = findViewById(R.id.leftBottom8_tv);
        mTop7Tv = findViewById(R.id.top7_tv);
        mRightTop7Tv = findViewById(R.id.rightTop7_tv);
        mRight7Tv = findViewById(R.id.right7_tv);
        mRightBottom7Tv = findViewById(R.id.rightBottom7_tv);
        mBottom7Tv = findViewById(R.id.bottom7_tv);
        mLeftTop7Tv = findViewById(R.id.leftTop7_tv);
        mLeft7Tv = findViewById(R.id.left7_tv);
        mLeftBottom7Tv = findViewById(R.id.leftBottom7_tv);
        mTop6Tv = findViewById(R.id.top6_tv);
        mRightTop6Tv = findViewById(R.id.rightTop6_tv);
        mRight6Tv = findViewById(R.id.right6_tv);
        mRightBottom6Tv = findViewById(R.id.rightBottom6_tv);
        mBottom6Tv = findViewById(R.id.bottom6_tv);
        mLeftTop6Tv = findViewById(R.id.leftTop6_tv);
        mLeft6Tv = findViewById(R.id.left6_tv);
        mLeftBottom6Tv = findViewById(R.id.leftBottom6_tv);

        mHitTv = findViewById(R.id.hit_tv);
        mTargetNumberTv = findViewById(R.id.target_number_tv);

        mMenuRl = findViewById(R.id.menu_rl);
        mMenuRl.setOnClickListener(this);

        mShootingDroneTv = findViewById(R.id.shooting_drone_tv);
        mShootingDroneRl = findViewById(R.id.shooting_drone_rl);
        mShootingDroneRl.setOnClickListener(this);
        mShootingDroneSwitch = findViewById(R.id.shooting_drone_switch);

        mDateTv = findViewById(R.id.date_tv);
        mTimeTv = findViewById(R.id.time_tv);

        mFireSwitch = findViewById(R.id.fire_switch);

        mGunNumberTv = findViewById(R.id.gun_number_tv);

        mCounterNumberTv = findViewById(R.id.counter_number_tv);
        mCounterRemainingBattery = findViewById(R.id.counter_remaining_battery);
        mCumulativeShotNumberHeadTv = findViewById(R.id.cumulative_shot_number_head_tv);
        mCumulativeShotNumberContentTv = findViewById(R.id.cumulative_shot_number_content_tv);
        mCumulativeShotsTv = findViewById(R.id.cumulative_shots_tv);
        mPositionTv = findViewById(R.id.position_tv);

        mMainRv = findViewById(R.id.main_rv);

        mPageLy = findViewById(R.id.page_ly);
        mTotalTv = findViewById(R.id.total_tv);

        mPrePageTv = findViewById(R.id.pre_page_tv);
        mPrePageTv.setOnClickListener(this);

        mFirstTv = findViewById(R.id.first_tv);
        mFirstTv.setOnClickListener(this);
        mV1 = findViewById(R.id.v1);

        mSecondTv = findViewById(R.id.second_tv);
        mSecondTv.setOnClickListener(this);
        mV2 = findViewById(R.id.v2);

        mThirdTv = findViewById(R.id.third_tv);
        mThirdTv.setOnClickListener(this);
        mV3 = findViewById(R.id.v3);

        mFourthTv = findViewById(R.id.fourth_tv);
        mFourthTv.setOnClickListener(this);
        mV4 = findViewById(R.id.v4);

        mFifthTv = findViewById(R.id.fifth_tv);
        mFifthTv.setOnClickListener(this);
        mV5 = findViewById(R.id.v5);

        mSixthTv = findViewById(R.id.sixth_tv);
        mSixthTv.setOnClickListener(this);
        mV6 = findViewById(R.id.v6);

        mNextPageTv = findViewById(R.id.next_page_tv);
        mNextPageTv.setOnClickListener(this);
        mV7 = findViewById(R.id.v7);

        hiddenRedPoint();
    }

    private void initData() {
        getDataFromDatabase();

        if (totalPrecisionTarget > 0) {
            if (DaoUtil.queryAllPrecisionTarget().get(0).getHit()) {
                mHitTv.setText("已命中");
            } else {
                mHitTv.setText("未命中");
            }
        } else {
            mHitTv.setText("");
        }
        //靶道号
        mTargetNumberTv.setText("靶道号--" + SharedPreferencesUtils.getInstance().getEquipmentNumber() + "号靶");
        //时间
        timeHandler.sendEmptyMessage(1);
        //枪支编号
        mGunNumberTv.setText(SharedPreferencesUtils.getInstance().getGunNumber());
        //计数器编号
        mCounterNumberTv.setText(SharedPreferencesUtils.getInstance().getEquipmentNumber());
        //累计射击环数
        int cumulativeShotNumber = 0;
        for (TargetBean bean : DaoUtil.queryAllPrecisionTarget()) {
            cumulativeShotNumber += bean.getRingNumber();
        }
        mCumulativeShotNumberContentTv.setText(String.valueOf(cumulativeShotNumber));
        //累计射击发数
        mCumulativeShotsTv.setText("0");

        ordinaryTargetAdapter = new OrdinaryTargetAdapter(this);
        ordinaryTargetAdapter.setDataList(ordinaryTargetList);

        precisionTargetAdapter = new PrecisionTargetAdapter(this);
        precisionTargetAdapter.setDataList(precisionTargetList);

        //刷新显示列表,默认显示精度靶
        mMainRv.setLayoutManager(new LinearLayoutManager(this));
        mMainRv.setAdapter(precisionTargetAdapter);

        //一共有多少条数据
        mTotalTv.setText("共" + totalPrecisionTarget + "条，共" + totalPrecisionTargetPage + "页");

        sendPageNumber(totalPrecisionTarget, nowPrecisionTargetPage);
        setBottomClick(1);
    }

    /**
     * 从数据库获取数据
     */
    private void getDataFromDatabase() {
        totalOrdinaryTarget = DaoUtil.queryAllOrdinaryTarget().size();
        totalOrdinaryTargetPage = (int) Math.ceil((totalOrdinaryTarget * 1.0f) / (size * 1.0f));
        totalPrecisionTarget = DaoUtil.queryAllPrecisionTarget().size();
        totalPrecisionTargetPage = (int) Math.ceil((totalPrecisionTarget * 1.0f) / (size * 1.0f));

        ordinaryTargetList = DaoUtil.queryOrdinaryTargetByPageAndSize(ordinaryTargetPage, size);
        precisionTargetList = DaoUtil.queryPrecisionTargetByPageAndSize(precisionTargetPage, size);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //头靶
            case R.id.head_target_tv:
                pageType = Constant.HEAD;
                setTabClick(pageType);
                break;
            //身靶
            case R.id.body_target_tv:
                pageType = Constant.BODY;
                setTabClick(pageType);
                break;
            //胸靶
            case R.id.chest_target_tv:
                pageType = Constant.CHEST;
                setTabClick(pageType);
                break;
            //精度靶
            case R.id.precision_target_tv:
                pageType = Constant.PRECISION;
                setTabClick(pageType);
                break;
            case R.id.menu_rl:
                TargetDialog dialog = new TargetDialog(this, this);
                dialog.show();
                break;
            //控制靶机起倒
            case R.id.shooting_drone_rl:
                if (mShootingDroneSwitch.isChecked()) {
                    mShootingDroneTv.setText("射击靶机倒");
                    mShootingDroneSwitch.setChecked(false);

                    //TODO 总控台控制靶机起倒的协议(倒下)
                    //总控台控制靶机起倒的协议(倒下)
                    if (SerialPortUtils.getInstance().openSerialPort() == null) {
                        Toast.makeText(MainActivity.this, "设备打开异常,正在尝试重新打开设备", Toast.LENGTH_SHORT).show();
                        SerialPortUtils.getInstance().openSerialPort();
                    } else {
                        byte[] sendByte = new byte[7];
                        sendByte[0] = (byte) Integer.parseInt("CC", 16);
                        sendByte[1] = (byte) Integer.parseInt("23", 16);
                        sendByte[2] = (byte) Integer.parseInt("AA", 16);
                        sendByte[3] = (byte) Integer.parseInt("DD", 16);

                        sendByte[4] = (byte) Integer.parseInt("00", 16);

                        sendByte[5] = (byte) Integer.parseInt("0A", 16);
                        sendByte[6] = (byte) Integer.parseInt("0D", 16);

                        SerialPortUtils.getInstance().sendSerialPort(sendByte);
                    }
                } else {
                    mShootingDroneTv.setText("射击靶机起");
                    mShootingDroneSwitch.setChecked(true);
                    //TODO 总控台控制靶机起倒的协议(起来)
                    //总控台控制靶机起倒的协议(起来)
                    if (SerialPortUtils.getInstance().openSerialPort() == null) {
                        Toast.makeText(MainActivity.this, "设备打开异常,正在尝试重新打开设备", Toast.LENGTH_SHORT).show();
                        SerialPortUtils.getInstance().openSerialPort();
                    } else {
                        byte[] sendByte = new byte[7];
                        sendByte[0] = (byte) Integer.parseInt("CC", 16);
                        sendByte[1] = (byte) Integer.parseInt("23", 16);
                        sendByte[2] = (byte) Integer.parseInt("AA", 16);
                        sendByte[3] = (byte) Integer.parseInt("DD", 16);

                        sendByte[4] = (byte) Integer.parseInt("01", 16);

                        sendByte[5] = (byte) Integer.parseInt("0A", 16);
                        sendByte[6] = (byte) Integer.parseInt("0D", 16);

                        SerialPortUtils.getInstance().sendSerialPort(sendByte);
                    }
                }
                break;
            case R.id.pre_page_tv:
                switch (pageType) {
                    case Constant.HEAD:
                    case Constant.BODY:
                    case Constant.CHEST:
                        if (nowOrdinaryTargetPage > 0) {
                            nowOrdinaryTargetPage--;
                            sendPageNumber(totalOrdinaryTargetPage, nowOrdinaryTargetPage);
                            setBottomClick(5);
                        }
                        break;
                    case Constant.PRECISION:
                        if (nowPrecisionTargetPage > 0) {
                            nowPrecisionTargetPage--;
                            sendPageNumber(totalPrecisionTargetPage, nowPrecisionTargetPage);
                            setBottomClick(5);
                        }
                        break;
                }
                break;
            case R.id.first_tv:
                setBottomClick(1);
                break;
            case R.id.second_tv:
                setBottomClick(2);
                break;
            case R.id.third_tv:
                setBottomClick(3);
                break;
            case R.id.fourth_tv:
                setBottomClick(4);
                break;
            case R.id.fifth_tv:
                setBottomClick(5);
                break;
            case R.id.sixth_tv:
                switch (pageType) {
                    case Constant.HEAD:
                    case Constant.BODY:
                    case Constant.CHEST:
                        if (totalOrdinaryTargetPage > ((nowOrdinaryTargetPage + 1) * 5 + 1)) {
                            nowOrdinaryTargetPage++;
                            sendPageNumber(totalOrdinaryTargetPage, nowOrdinaryTargetPage);
                            setBottomClick(1);
                        } else if ((((nowOrdinaryTargetPage + 1) * 5 + 1) - totalOrdinaryTargetPage) == 0) {
                            setBottomClick(6);
                        }
                        break;
                    case Constant.PRECISION:
                        if (totalPrecisionTargetPage > ((nowPrecisionTargetPage + 1) * 5 + 1)) {
                            nowPrecisionTargetPage++;
                            sendPageNumber(totalPrecisionTargetPage, nowPrecisionTargetPage);
                            setBottomClick(1);
                        } else if ((((nowPrecisionTargetPage + 1) * 5 + 1) - totalPrecisionTargetPage) == 0) {
                            setBottomClick(6);
                        }
                        break;
                }
                break;
            case R.id.next_page_tv:
                switch (pageType) {
                    case Constant.HEAD:
                    case Constant.BODY:
                    case Constant.CHEST:
                        if (totalOrdinaryTargetPage > ((nowOrdinaryTargetPage + 1) * 5 + 1)) {
                            nowOrdinaryTargetPage++;
                            sendPageNumber(totalOrdinaryTargetPage, nowOrdinaryTargetPage);
                            setBottomClick(1);
                        }
                        break;
                    case Constant.PRECISION:
                        if (totalPrecisionTargetPage > ((nowPrecisionTargetPage + 1) * 5 + 1)) {
                            nowPrecisionTargetPage++;
                            sendPageNumber(totalPrecisionTargetPage, nowPrecisionTargetPage);
                            setBottomClick(1);
                        }
                        break;
                }
                break;
            default:
                break;
        }
    }

    /**
     * 头靶/身靶/胸靶/精度靶点击事件
     *
     * @param type 头靶/身靶/胸靶/精度靶
     */
    private void setTabClick(int type) {
        getDataFromDatabase();

        mHeadTargetTv.setTextColor(getColor(R.color.lightWhite));
        mBodyTargetTv.setTextColor(getColor(R.color.lightWhite));
        mChestTargetTv.setTextColor(getColor(R.color.lightWhite));
        mPrecisionTargetTv.setTextColor(getColor(R.color.lightWhite));

        mCumulativeShotNumberHeadTv.setVisibility(View.GONE);
        mCumulativeShotNumberContentTv.setVisibility(View.GONE);
        mPositionTv.setVisibility(View.GONE);
        switch (type) {
            case Constant.HEAD:
            case Constant.BODY:
            case Constant.CHEST:
                //TODO PAD控制靶机靶子类型的协议
                if (type == Constant.HEAD) {
                    //PAD控制靶机靶子类型的协议
                    if (SerialPortUtils.getInstance().openSerialPort() == null) {
                        Toast.makeText(MainActivity.this, "设备打开异常,正在尝试重新打开设备", Toast.LENGTH_SHORT).show();
                        SerialPortUtils.getInstance().openSerialPort();
                    } else {
                        byte[] sendByte = new byte[11];
                        sendByte[0] = (byte) Integer.parseInt("CC", 16);
                        sendByte[1] = (byte) Integer.parseInt("23", 16);
                        sendByte[2] = (byte) Integer.parseInt("AA", 16);
                        sendByte[3] = (byte) Integer.parseInt("DE", 16);

                        int equipment = Integer.valueOf(SharedPreferencesUtils.getInstance().getEquipmentNumber());
                        String e = Integer.toHexString(equipment);
                        sendByte[4] = (byte) Integer.parseInt("00", 16);
                        sendByte[5] = (byte) Integer.parseInt("00", 16);
                        sendByte[6] = (byte) Integer.parseInt("00", 16);
                        sendByte[7] = (byte) Integer.parseInt(e, 16);

                        sendByte[8] = (byte) Integer.parseInt("01", 16);

                        sendByte[9] = (byte) Integer.parseInt("0A", 16);
                        sendByte[10] = (byte) Integer.parseInt("0D", 16);

                        SerialPortUtils.getInstance().sendSerialPort(sendByte);
                    }

                    mHeadTargetTv.setTextColor(getColor(R.color.white));
                    mTargetIv.setImageResource(R.drawable.head_target);
                } else if (type == Constant.BODY) {
                    //PAD控制靶机靶子类型的协议
                    if (SerialPortUtils.getInstance().openSerialPort() == null) {
                        Toast.makeText(MainActivity.this, "设备打开异常,正在尝试重新打开设备", Toast.LENGTH_SHORT).show();
                        SerialPortUtils.getInstance().openSerialPort();
                    } else {
                        byte[] sendByte = new byte[11];
                        sendByte[0] = (byte) Integer.parseInt("CC", 16);
                        sendByte[1] = (byte) Integer.parseInt("23", 16);
                        sendByte[2] = (byte) Integer.parseInt("AA", 16);
                        sendByte[3] = (byte) Integer.parseInt("DE", 16);

                        int equipment = Integer.valueOf(SharedPreferencesUtils.getInstance().getEquipmentNumber());
                        String e = Integer.toHexString(equipment);
                        sendByte[4] = (byte) Integer.parseInt("00", 16);
                        sendByte[5] = (byte) Integer.parseInt("00", 16);
                        sendByte[6] = (byte) Integer.parseInt("00", 16);
                        sendByte[7] = (byte) Integer.parseInt(e, 16);

                        sendByte[8] = (byte) Integer.parseInt("02", 16);

                        sendByte[9] = (byte) Integer.parseInt("0A", 16);
                        sendByte[10] = (byte) Integer.parseInt("0D", 16);

                        SerialPortUtils.getInstance().sendSerialPort(sendByte);
                    }

                    mBodyTargetTv.setTextColor(getColor(R.color.white));
                    mTargetIv.setImageResource(R.drawable.body_target);
                } else {
                    //PAD控制靶机靶子类型的协议
                    if (SerialPortUtils.getInstance().openSerialPort() == null) {
                        Toast.makeText(MainActivity.this, "设备打开异常,正在尝试重新打开设备", Toast.LENGTH_SHORT).show();
                        SerialPortUtils.getInstance().openSerialPort();
                    } else {
                        byte[] sendByte = new byte[11];
                        sendByte[0] = (byte) Integer.parseInt("CC", 16);
                        sendByte[1] = (byte) Integer.parseInt("23", 16);
                        sendByte[2] = (byte) Integer.parseInt("AA", 16);
                        sendByte[3] = (byte) Integer.parseInt("DE", 16);

                        int equipment = Integer.valueOf(SharedPreferencesUtils.getInstance().getEquipmentNumber());
                        String e = Integer.toHexString(equipment);
                        sendByte[4] = (byte) Integer.parseInt("00", 16);
                        sendByte[5] = (byte) Integer.parseInt("00", 16);
                        sendByte[6] = (byte) Integer.parseInt("00", 16);
                        sendByte[7] = (byte) Integer.parseInt(e, 16);

                        sendByte[8] = (byte) Integer.parseInt("03", 16);

                        sendByte[9] = (byte) Integer.parseInt("0A", 16);
                        sendByte[10] = (byte) Integer.parseInt("0D", 16);

                        SerialPortUtils.getInstance().sendSerialPort(sendByte);
                    }

                    mChestTargetTv.setTextColor(getColor(R.color.white));
                    mTargetIv.setImageResource(R.drawable.chest_target);
                }

                hiddenRedPoint();

                mMainRv.setAdapter(ordinaryTargetAdapter);
                ordinaryTargetAdapter.setDataList(ordinaryTargetList);

                if (totalOrdinaryTarget > 0) {
                    if (DaoUtil.queryAllOrdinaryTarget().get(0).getHit()) {
                        mHitTv.setText("已命中");
                    } else {
                        mHitTv.setText("未命中");
                    }
                } else {
                    mHitTv.setText("");
                }

                mTotalTv.setText("共" + totalOrdinaryTarget + "条，共" + totalOrdinaryTargetPage + "页");

                setBottomClick(1);
                break;
            case Constant.PRECISION:
                //TODO PAD控制靶机靶子类型的协议
                //PAD控制靶机靶子类型的协议
                if (SerialPortUtils.getInstance().openSerialPort() == null) {
                    Toast.makeText(MainActivity.this, "设备打开异常,正在尝试重新打开设备", Toast.LENGTH_SHORT).show();
                    SerialPortUtils.getInstance().openSerialPort();
                } else {
                    byte[] sendByte = new byte[11];
                    sendByte[0] = (byte) Integer.parseInt("CC", 16);
                    sendByte[1] = (byte) Integer.parseInt("23", 16);
                    sendByte[2] = (byte) Integer.parseInt("AA", 16);
                    sendByte[3] = (byte) Integer.parseInt("DE", 16);

                    int equipment = Integer.valueOf(SharedPreferencesUtils.getInstance().getEquipmentNumber());
                    String e = Integer.toHexString(equipment);
                    sendByte[4] = (byte) Integer.parseInt("00", 16);
                    sendByte[5] = (byte) Integer.parseInt("00", 16);
                    sendByte[6] = (byte) Integer.parseInt("00", 16);
                    sendByte[7] = (byte) Integer.parseInt(e, 16);

                    sendByte[8] = (byte) Integer.parseInt("04", 16);

                    sendByte[9] = (byte) Integer.parseInt("0A", 16);
                    sendByte[10] = (byte) Integer.parseInt("0D", 16);

                    SerialPortUtils.getInstance().sendSerialPort(sendByte);
                }

                mPrecisionTargetTv.setTextColor(getColor(R.color.white));
                mTargetIv.setImageResource(R.drawable.precision_target);

                mCumulativeShotNumberHeadTv.setVisibility(View.VISIBLE);
                mCumulativeShotNumberContentTv.setVisibility(View.VISIBLE);
                int cumulativeShotNumber = 0;
                for (TargetBean bean : DaoUtil.queryAllPrecisionTarget()) {
                    showRedPoint(bean.getPrecisionRingNumber());
                    cumulativeShotNumber += bean.getRingNumber();
                }
                mCumulativeShotNumberContentTv.setText(String.valueOf(cumulativeShotNumber));

                mPositionTv.setVisibility(View.VISIBLE);
                mMainRv.setAdapter(precisionTargetAdapter);
                precisionTargetAdapter.setDataList(precisionTargetList);

                if (totalPrecisionTarget > 0) {
                    if (DaoUtil.queryAllPrecisionTarget().get(0).getHit()) {
                        mHitTv.setText("已命中                        " + DaoUtil.queryAllPrecisionTarget().get(0).getPrecisionRingNumber());
                    } else {
                        mHitTv.setText("未命中");
                    }
                } else {
                    mHitTv.setText("");
                }

                mTotalTv.setText("共" + totalPrecisionTarget + "条，共" + totalPrecisionTargetPage + "页");

                setBottomClick(1);
                break;
            default:
                break;
        }
    }


    /**
     * 底部页码点击事件
     *
     * @param position 选中的是第几条
     */
    private void setBottomClick(int position) {
        mFirstTv.setBackgroundColor(Color.TRANSPARENT);
        mFirstTv.setTextColor(getColor(R.color.black));
        mSecondTv.setBackgroundColor(Color.TRANSPARENT);
        mSecondTv.setTextColor(getColor(R.color.black));
        mThirdTv.setBackgroundColor(Color.TRANSPARENT);
        mThirdTv.setTextColor(getColor(R.color.black));
        mFourthTv.setBackgroundColor(Color.TRANSPARENT);
        mFourthTv.setTextColor(getColor(R.color.black));
        mFifthTv.setBackgroundColor(Color.TRANSPARENT);
        mFifthTv.setTextColor(getColor(R.color.black));
        mSixthTv.setBackgroundColor(Color.TRANSPARENT);
        mSixthTv.setTextColor(getColor(R.color.black));

        switch (pageType) {
            case Constant.HEAD:
            case Constant.BODY:
            case Constant.CHEST:
                sendPageNumber(totalOrdinaryTargetPage, nowOrdinaryTargetPage);
                break;
            case Constant.PRECISION:
                sendPageNumber(totalPrecisionTargetPage, nowPrecisionTargetPage);
                break;
        }

        switch (position) {
            case 1:
                mFirstTv.setBackgroundColor(getColor(R.color.green));
                mFirstTv.setTextColor(getColor(R.color.white));
                mV1.setVisibility(View.GONE);
                if (mSecondTv.getVisibility() == View.VISIBLE) {
                    mV2.setVisibility(View.GONE);
                } else {
                    mV7.setVisibility(View.GONE);
                }
                break;
            case 2:
                mSecondTv.setBackgroundColor(getColor(R.color.green));
                mSecondTv.setTextColor(getColor(R.color.white));
                mV2.setVisibility(View.GONE);
                if (mThirdTv.getVisibility() == View.VISIBLE) {
                    mV3.setVisibility(View.GONE);
                } else {
                    mV7.setVisibility(View.GONE);
                }
                break;
            case 3:
                mThirdTv.setBackgroundColor(getColor(R.color.green));
                mThirdTv.setTextColor(getColor(R.color.white));
                mV3.setVisibility(View.GONE);
                if (mFourthTv.getVisibility() == View.VISIBLE) {
                    mV4.setVisibility(View.GONE);
                } else {
                    mV7.setVisibility(View.GONE);
                }
                break;
            case 4:
                mFourthTv.setBackgroundColor(getColor(R.color.green));
                mFourthTv.setTextColor(getColor(R.color.white));
                mV4.setVisibility(View.GONE);
                if (mFifthTv.getVisibility() == View.VISIBLE) {
                    mV5.setVisibility(View.GONE);
                } else {
                    mV7.setVisibility(View.GONE);
                }
                break;
            case 5:
                mFifthTv.setBackgroundColor(getColor(R.color.green));
                mFifthTv.setTextColor(getColor(R.color.white));
                mV5.setVisibility(View.GONE);
                if (mSixthTv.getVisibility() == View.VISIBLE) {
                    mV6.setVisibility(View.GONE);
                } else {
                    mV7.setVisibility(View.GONE);
                }
                break;
            case 6:
                mSixthTv.setBackgroundColor(getColor(R.color.green));
                mSixthTv.setTextColor(getColor(R.color.white));
                mV6.setVisibility(View.GONE);
                mV7.setVisibility(View.GONE);
                break;
        }

        switch (pageType) {
            case Constant.HEAD:
            case Constant.BODY:
            case Constant.CHEST:
                ordinaryTargetPage = nowOrdinaryTargetPage * 5 + (position - 1);
                ordinaryTargetList = DaoUtil.queryOrdinaryTargetByPageAndSize(ordinaryTargetPage, size);
                ordinaryTargetAdapter.setDataList(ordinaryTargetList);
                break;
            case Constant.PRECISION:
                precisionTargetPage = nowPrecisionTargetPage * 5 + (position - 1);
                precisionTargetList = DaoUtil.queryPrecisionTargetByPageAndSize(precisionTargetPage, size);
                precisionTargetAdapter.setDataList(precisionTargetList);
                break;
        }
    }

    /**
     * 设置底部页码
     *
     * @param totalPage 总页数
     * @param nowPage   当前在第几页
     */
    private void sendPageNumber(int totalPage, int nowPage) {
        mPageLy.setVisibility(View.VISIBLE);
        mFirstTv.setText(String.valueOf((nowPage * 5 + 1)));
        mV1.setVisibility(View.VISIBLE);
        mSecondTv.setVisibility(View.GONE);
        mSecondTv.setText(String.valueOf((nowPage * 5 + 2)));
        mV2.setVisibility(View.GONE);
        mThirdTv.setVisibility(View.GONE);
        mThirdTv.setText(String.valueOf((nowPage * 5 + 3)));
        mV3.setVisibility(View.GONE);
        mFourthTv.setVisibility(View.GONE);
        mFourthTv.setText(String.valueOf((nowPage * 5 + 4)));
        mV4.setVisibility(View.GONE);
        mFifthTv.setVisibility(View.GONE);
        mFifthTv.setText(String.valueOf((nowPage * 5 + 5)));
        mV5.setVisibility(View.GONE);
        mSixthTv.setVisibility(View.GONE);
        mV6.setVisibility(View.GONE);
        mV7.setVisibility(View.VISIBLE);

        if (totalPage == 0) {
            mPageLy.setVisibility(View.GONE);
        } else if (totalPage <= ((nowPage + 1) * 5 + 1)) {
            if ((((nowPage + 1) * 5 + 1) - totalPage) == 5) {
                mSecondTv.setVisibility(View.GONE);
                mV2.setVisibility(View.GONE);
                mThirdTv.setVisibility(View.GONE);
                mV3.setVisibility(View.GONE);
                mFourthTv.setVisibility(View.GONE);
                mV4.setVisibility(View.GONE);
                mFifthTv.setVisibility(View.GONE);
                mV5.setVisibility(View.GONE);
                mSixthTv.setVisibility(View.GONE);
                mV6.setVisibility(View.GONE);
            } else if ((((nowPage + 1) * 5 + 1) - totalPage) == 4) {
                mSecondTv.setVisibility(View.VISIBLE);
                mV2.setVisibility(View.VISIBLE);
                mThirdTv.setVisibility(View.GONE);
                mV3.setVisibility(View.GONE);
                mFourthTv.setVisibility(View.GONE);
                mV4.setVisibility(View.GONE);
                mFifthTv.setVisibility(View.GONE);
                mV5.setVisibility(View.GONE);
                mSixthTv.setVisibility(View.GONE);
                mV6.setVisibility(View.GONE);
            } else if ((((nowPage + 1) * 5 + 1) - totalPage) == 3) {
                mSecondTv.setVisibility(View.VISIBLE);
                mV2.setVisibility(View.VISIBLE);
                mThirdTv.setVisibility(View.VISIBLE);
                mV3.setVisibility(View.VISIBLE);
                mFourthTv.setVisibility(View.GONE);
                mV4.setVisibility(View.GONE);
                mFifthTv.setVisibility(View.GONE);
                mV5.setVisibility(View.GONE);
                mSixthTv.setVisibility(View.GONE);
                mV6.setVisibility(View.GONE);
            } else if ((((nowPage + 1) * 5 + 1) - totalPage) == 2) {
                mSecondTv.setVisibility(View.VISIBLE);
                mV2.setVisibility(View.VISIBLE);
                mThirdTv.setVisibility(View.VISIBLE);
                mV3.setVisibility(View.VISIBLE);
                mFourthTv.setVisibility(View.VISIBLE);
                mV4.setVisibility(View.VISIBLE);
                mFifthTv.setVisibility(View.GONE);
                mV5.setVisibility(View.GONE);
                mSixthTv.setVisibility(View.GONE);
                mV6.setVisibility(View.GONE);
            } else if ((((nowPage + 1) * 5 + 1) - totalPage) == 1) {
                mSecondTv.setVisibility(View.VISIBLE);
                mV2.setVisibility(View.VISIBLE);
                mThirdTv.setVisibility(View.VISIBLE);
                mV3.setVisibility(View.VISIBLE);
                mFourthTv.setVisibility(View.VISIBLE);
                mV4.setVisibility(View.VISIBLE);
                mFifthTv.setVisibility(View.VISIBLE);
                mV5.setVisibility(View.VISIBLE);
                mSixthTv.setVisibility(View.GONE);
                mV6.setVisibility(View.GONE);
            } else {
                mSecondTv.setVisibility(View.VISIBLE);
                mV2.setVisibility(View.VISIBLE);
                mThirdTv.setVisibility(View.VISIBLE);
                mV3.setVisibility(View.VISIBLE);
                mFourthTv.setVisibility(View.VISIBLE);
                mV4.setVisibility(View.VISIBLE);
                mFifthTv.setVisibility(View.VISIBLE);
                mV5.setVisibility(View.VISIBLE);
                mSixthTv.setVisibility(View.VISIBLE);
                mV6.setVisibility(View.VISIBLE);

                mSixthTv.setText(String.valueOf(((nowPage + 1) * 5 + 1)));
            }
        } else {
            mSecondTv.setVisibility(View.VISIBLE);
            mV2.setVisibility(View.VISIBLE);
            mThirdTv.setVisibility(View.VISIBLE);
            mV3.setVisibility(View.VISIBLE);
            mFourthTv.setVisibility(View.VISIBLE);
            mV4.setVisibility(View.VISIBLE);
            mFifthTv.setVisibility(View.VISIBLE);
            mV5.setVisibility(View.VISIBLE);
            mSixthTv.setVisibility(View.VISIBLE);
            mV6.setVisibility(View.VISIBLE);

            mSixthTv.setText("···");
        }
    }

    private void showRedPoint(String precisionRingNumber) {
        if ("10环上".equals(precisionRingNumber)) {
            mTop10Tv.setVisibility(View.VISIBLE);
        } else if ("10环右".equals(precisionRingNumber)) {
            mRight10Tv.setVisibility(View.VISIBLE);
        } else if ("10环下".equals(precisionRingNumber)) {
            mBottom10Tv.setVisibility(View.VISIBLE);
        } else if ("10环左".equals(precisionRingNumber)) {
            mLeft10Tv.setVisibility(View.VISIBLE);
        } else if ("9环上".equals(precisionRingNumber)) {
            mTop9Tv.setVisibility(View.VISIBLE);
        } else if ("9环右".equals(precisionRingNumber)) {
            mRight9Tv.setVisibility(View.VISIBLE);
        } else if ("9环下".equals(precisionRingNumber)) {
            mBottom9Tv.setVisibility(View.VISIBLE);
        } else if ("9环左".equals(precisionRingNumber)) {
            mLeft9Tv.setVisibility(View.VISIBLE);
        } else if ("8环上".equals(precisionRingNumber)) {
            mTop8Tv.setVisibility(View.VISIBLE);
        } else if ("8环右上".equals(precisionRingNumber)) {
            mRightTop8Tv.setVisibility(View.VISIBLE);
        } else if ("8环右".equals(precisionRingNumber)) {
            mRight8Tv.setVisibility(View.VISIBLE);
        } else if ("8右下".equals(precisionRingNumber)) {
            mRightBottom8Tv.setVisibility(View.VISIBLE);
        } else if ("8环下".equals(precisionRingNumber)) {
            mBottom8Tv.setVisibility(View.VISIBLE);
        } else if ("8左下".equals(precisionRingNumber)) {
            mLeftBottom8Tv.setVisibility(View.VISIBLE);
        } else if ("8环左".equals(precisionRingNumber)) {
            mLeft8Tv.setVisibility(View.VISIBLE);
        } else if ("8环左上".equals(precisionRingNumber)) {
            mLeftTop8Tv.setVisibility(View.VISIBLE);
        } else if ("7环上".equals(precisionRingNumber)) {
            mTop7Tv.setVisibility(View.VISIBLE);
        } else if ("7环右上".equals(precisionRingNumber)) {
            mRightTop7Tv.setVisibility(View.VISIBLE);
        } else if ("7环右".equals(precisionRingNumber)) {
            mRight7Tv.setVisibility(View.VISIBLE);
        } else if ("7右下".equals(precisionRingNumber)) {
            mRightBottom7Tv.setVisibility(View.VISIBLE);
        } else if ("7环下".equals(precisionRingNumber)) {
            mBottom7Tv.setVisibility(View.VISIBLE);
        } else if ("7左下".equals(precisionRingNumber)) {
            mLeftBottom7Tv.setVisibility(View.VISIBLE);
        } else if ("7环左".equals(precisionRingNumber)) {
            mLeft7Tv.setVisibility(View.VISIBLE);
        } else if ("7环左上".equals(precisionRingNumber)) {
            mLeftTop7Tv.setVisibility(View.VISIBLE);
        } else if ("6环上".equals(precisionRingNumber)) {
            mTop6Tv.setVisibility(View.VISIBLE);
        } else if ("6环右上".equals(precisionRingNumber)) {
            mRightTop6Tv.setVisibility(View.VISIBLE);
        } else if ("6环右".equals(precisionRingNumber)) {
            mRight6Tv.setVisibility(View.VISIBLE);
        } else if ("6右下".equals(precisionRingNumber)) {
            mRightBottom6Tv.setVisibility(View.VISIBLE);
        } else if ("6环下".equals(precisionRingNumber)) {
            mBottom6Tv.setVisibility(View.VISIBLE);
        } else if ("6左下".equals(precisionRingNumber)) {
            mLeftBottom6Tv.setVisibility(View.VISIBLE);
        } else if ("6环左".equals(precisionRingNumber)) {
            mLeft6Tv.setVisibility(View.VISIBLE);
        } else if ("6环左上".equals(precisionRingNumber)) {
            mLeftTop6Tv.setVisibility(View.VISIBLE);
        }
    }

    private void hiddenRedPoint() {
        mTop10Tv.setVisibility(View.INVISIBLE);
        mRight10Tv.setVisibility(View.INVISIBLE);
        mBottom10Tv.setVisibility(View.INVISIBLE);
        mLeft10Tv.setVisibility(View.INVISIBLE);
        mTop9Tv.setVisibility(View.INVISIBLE);
        mRight9Tv.setVisibility(View.INVISIBLE);
        mBottom9Tv.setVisibility(View.INVISIBLE);
        mLeft9Tv.setVisibility(View.INVISIBLE);
        mTop8Tv.setVisibility(View.INVISIBLE);
        mRightTop8Tv.setVisibility(View.INVISIBLE);
        mRight8Tv.setVisibility(View.INVISIBLE);
        mRightBottom8Tv.setVisibility(View.INVISIBLE);
        mBottom8Tv.setVisibility(View.INVISIBLE);
        mLeftTop8Tv.setVisibility(View.INVISIBLE);
        mLeft8Tv.setVisibility(View.INVISIBLE);
        mLeftBottom8Tv.setVisibility(View.INVISIBLE);
        mTop7Tv.setVisibility(View.INVISIBLE);
        mRightTop7Tv.setVisibility(View.INVISIBLE);
        mRight7Tv.setVisibility(View.INVISIBLE);
        mRightBottom7Tv.setVisibility(View.INVISIBLE);
        mBottom7Tv.setVisibility(View.INVISIBLE);
        mLeftTop7Tv.setVisibility(View.INVISIBLE);
        mLeft7Tv.setVisibility(View.INVISIBLE);
        mLeftBottom7Tv.setVisibility(View.INVISIBLE);
        mTop6Tv.setVisibility(View.INVISIBLE);
        mRightTop6Tv.setVisibility(View.INVISIBLE);
        mRight6Tv.setVisibility(View.INVISIBLE);
        mRightBottom6Tv.setVisibility(View.INVISIBLE);
        mBottom6Tv.setVisibility(View.INVISIBLE);
        mLeftTop6Tv.setVisibility(View.INVISIBLE);
        mLeft6Tv.setVisibility(View.INVISIBLE);
        mLeftBottom6Tv.setVisibility(View.INVISIBLE);
    }

    @Override
    public void cycles(long erectTime, long lodgingTime, int cyclesNumber) {
        this.erectTime = erectTime * 1000;
        this.lodgingTime = lodgingTime * 1000;
        this.cyclesNumber = cyclesNumber;

        cyclesHandler.removeMessages(1);
        cyclesHandler.removeMessages(2);

        mShootingDroneTv.setText("射击靶机起");
        mShootingDroneSwitch.setChecked(true);
        //TODO 总控台控制靶机起倒的协议(起来)
        //总控台控制靶机起倒的协议(起来)
        if (SerialPortUtils.getInstance().openSerialPort() == null) {
            Toast.makeText(MainActivity.this, "设备打开异常,正在尝试重新打开设备", Toast.LENGTH_SHORT).show();
            SerialPortUtils.getInstance().openSerialPort();
        } else {
            byte[] sendByte = new byte[7];
            sendByte[0] = (byte) Integer.parseInt("CC", 16);
            sendByte[1] = (byte) Integer.parseInt("23", 16);
            sendByte[2] = (byte) Integer.parseInt("AA", 16);
            sendByte[3] = (byte) Integer.parseInt("DD", 16);

            sendByte[4] = (byte) Integer.parseInt("01", 16);

            sendByte[5] = (byte) Integer.parseInt("0A", 16);
            sendByte[6] = (byte) Integer.parseInt("0D", 16);

            SerialPortUtils.getInstance().sendSerialPort(sendByte);
        }
        cyclesHandler.sendEmptyMessageDelayed(1, this.erectTime);
    }

    @Override
    public void onFEReceive(String fire, String electricQuantity) {
        mFireSwitch.setChecked(true);
        fireNumber += Integer.valueOf(fire);
        //累计射击发数
        mCumulativeShotsTv.setText(String.valueOf(fireNumber));

        if ("FF".equals(electricQuantity)) {
            mCounterRemainingBattery.setText("电量未测出");
        } else {
            if ("64".equals(electricQuantity)) {
                mCounterRemainingBattery.setText("100%");
            } else {
                if ("0".equals(electricQuantity.substring(0, 1))) {
                    mCounterRemainingBattery.setText(electricQuantity.substring(1, 2) + "%");
                } else {
                    mCounterRemainingBattery.setText(electricQuantity + "%");
                }
            }
        }
    }

    @Override
    public void onDBOrDCReceive(String type, String position, int precisionNumber, boolean hit) {
        TargetBean targetBean;
        if ("DB".equals(type)) {//普通靶
            targetBean = new TargetBean();
            targetBean.setType(TargetBean.TYPE_ORDINARY);
            targetBean.setNumber(DaoUtil.queryAllOrdinaryTarget().size() + 1);
            targetBean.setHit(hit);
            if (DaoUtil.queryAllOrdinaryTarget().size() > 0) {
                Date lastDate = DaoUtil.queryAllOrdinaryTarget().get(0).getNowTime();
                Date nowDate = new Date(System.currentTimeMillis());
                long diff = nowDate.getTime() - lastDate.getTime();
                targetBean.setShootingInterval(diff / 1000 + "s");
            } else {
                targetBean.setShootingInterval("");
            }
            targetBean.setNowTime(new Date(System.currentTimeMillis()));
            targetBean.setTime(new SimpleDateFormat("HH:mm:ss").format(new Date(System.currentTimeMillis())));
            targetBean.setDate(new SimpleDateFormat("yyyy-MM-dd").format(new Date(System.currentTimeMillis())));
            DaoUtil.insertTarget(targetBean);

            if (pageType != Constant.PRECISION) {
                setTabClick(pageType);
            }
        } else {//精准靶
            targetBean = new TargetBean();
            targetBean.setType(TargetBean.TYPE_PRECISION);
            targetBean.setNumber(DaoUtil.queryAllPrecisionTarget().size() + 1);
            targetBean.setHit(hit);
            targetBean.setPrecisionRingNumber(position);
            targetBean.setRingNumber(precisionNumber);
            if (DaoUtil.queryAllPrecisionTarget().size() > 0) {
                Date lastDate = DaoUtil.queryAllPrecisionTarget().get(0).getNowTime();
                Date nowDate = new Date(System.currentTimeMillis());
                long diff = nowDate.getTime() - lastDate.getTime();
                targetBean.setShootingInterval(diff / 1000 + "s");
            } else {
                targetBean.setShootingInterval("");
            }
            targetBean.setNowTime(new Date(System.currentTimeMillis()));
            targetBean.setTime(new SimpleDateFormat("HH:mm:ss").format(new Date(System.currentTimeMillis())));
            targetBean.setDate(new SimpleDateFormat("yyyy-MM-dd").format(new Date(System.currentTimeMillis())));

            DaoUtil.insertTarget(targetBean);

            if (pageType == Constant.PRECISION) {
                setTabClick(pageType);
            }
        }
    }
}
