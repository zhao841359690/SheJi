package com.sheji.sheji.activity;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.sheji.sheji.adpter.OrdinaryTargetAdapter;
import com.sheji.sheji.base.BaseActivity;
import com.sheji.sheji.R;
import com.sheji.sheji.adpter.PrecisionTargetAdapter;
import com.sheji.sheji.bean.Constant;
import com.sheji.sheji.bean.DaoUtil;
import com.sheji.sheji.bean.TargetBean;
import com.sheji.sheji.dialog.TargetDialog;
import com.sheji.sheji.dialog.TextDialog;
import com.sheji.sheji.util.SerialPortUtils;
import com.sheji.sheji.util.SharedPreferencesUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends BaseActivity implements View.OnClickListener, TargetDialog.OnTargetDiaLogCyclesListener, SerialPortUtils.OnMainDataReceiveListener {
    private Button fire;

    private TextView mHeadTargetTv;
    private TextView mBodyTargetTv;
    private TextView mChestTargetTv;
    private TextView mPrecisionTargetTv;

    private ImageView mTargetIv;
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
    private ImageView mPrePageIv;
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
    private ImageView mNextPageIv;
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
    private int totalPrecisonTargetPage = 0;
    private int precisionTargetPage = 0;
    private int nowPrecisionTargetPage = 0;

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
//                    SerialPortUtils.getInstance().sendSerialPort("CC23AADD000A0D");
                    break;
                case 2:
                    cyclesNumber--;
                    if (cyclesNumber > 0) {
                        cyclesHandler.sendEmptyMessageDelayed(2, erectTime);

                        mShootingDroneTv.setText("射击靶机起");
                        mShootingDroneSwitch.setChecked(true);
                        //TODO 总控台控制靶机起倒的协议(起来)
                        //总控台控制靶机起倒的协议(起来)
//                    SerialPortUtils.getInstance().sendSerialPort("CC23AADD010A0D");
                    }
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
//        SerialPortUtils.getInstance().closeSerialPort();
    }

    private void initView() {
        fire = findViewById(R.id.fire);
        fire.setOnClickListener(this);

        mHeadTargetTv = findViewById(R.id.head_target_tv);
        mHeadTargetTv.setOnClickListener(this);

        mBodyTargetTv = findViewById(R.id.body_target_tv);
        mBodyTargetTv.setOnClickListener(this);

        mChestTargetTv = findViewById(R.id.chest_target_tv);
        mChestTargetTv.setOnClickListener(this);

        mPrecisionTargetTv = findViewById(R.id.precision_target_tv);
        mPrecisionTargetTv.setOnClickListener(this);

        mTargetIv = findViewById(R.id.target_iv);

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

        mPrePageIv = findViewById(R.id.pre_page_iv);
        mPrePageIv.setOnClickListener(this);

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

        mNextPageIv = findViewById(R.id.next_page_iv);
        mNextPageIv.setOnClickListener(this);
        mV7 = findViewById(R.id.v7);
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
        timeHandler.sendEmptyMessage(1);
        //枪支编号
        mGunNumberTv.setText(SharedPreferencesUtils.getInstance().getGunNumber());
        //计数器编号
        mCounterNumberTv.setText("");
        //累计射击环数
        int cumulativeShotNumber = 0;
        for (TargetBean bean : DaoUtil.queryAllPrecisionTarget()) {
            cumulativeShotNumber += bean.getRingNumber();
        }
        mCumulativeShotNumberContentTv.setText(String.valueOf(cumulativeShotNumber));
        //累计射击发数
        mCumulativeShotsTv.setText(String.valueOf(totalPrecisionTarget));

        ordinaryTargetAdapter = new OrdinaryTargetAdapter(this);
        ordinaryTargetAdapter.setDataList(ordinaryTargetList);

        precisionTargetAdapter = new PrecisionTargetAdapter(this);
        precisionTargetAdapter.setDataList(precisionTargetList);

        //刷新显示列表,默认显示精度靶
        mMainRv.setLayoutManager(new LinearLayoutManager(this));
        mMainRv.setAdapter(precisionTargetAdapter);

        //一共有多少条数据
        mTotalTv.setText("共" + totalPrecisionTarget + "条，共" + totalPrecisonTargetPage + "页");

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
        totalPrecisonTargetPage = (int) Math.ceil((totalPrecisionTarget * 1.0f) / (size * 1.0f));

        ordinaryTargetList = DaoUtil.queryOrdinaryTargetByPageAndSize(ordinaryTargetPage, size);
        precisionTargetList = DaoUtil.queryPrecisionTargetByPageAndSize(precisionTargetPage, size);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fire:
                onFEReceive("70");
                switch (pageType) {
                    case Constant.HEAD:
                        onDBOrDCReceive("DB", "七环上", 7, false);
                        break;
                    case Constant.BODY:
                        onDBOrDCReceive("DB", "七环上", 7, false);
                        break;
                    case Constant.CHEST:
                        onDBOrDCReceive("DB", "七环上", 7, false);
                        break;
                    case Constant.PRECISION:
                        onDBOrDCReceive("DC", "七环上", 7, false);
                        break;
                }
                break;
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
//                    SerialPortUtils.getInstance().sendSerialPort("CC23AADD000A0D");
                } else {
                    mShootingDroneTv.setText("射击靶机起");
                    mShootingDroneSwitch.setChecked(true);
                    //TODO 总控台控制靶机起倒的协议(起来)
                    //总控台控制靶机起倒的协议(起来)
//                    SerialPortUtils.getInstance().sendSerialPort("CC23AADD010A0D");
                }
                break;
            case R.id.pre_page_iv:
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
                            sendPageNumber(totalPrecisonTargetPage, nowPrecisionTargetPage);
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
                        if (totalPrecisonTargetPage > ((nowPrecisionTargetPage + 1) * 5 + 1)) {
                            nowPrecisionTargetPage++;
                            sendPageNumber(totalPrecisonTargetPage, nowPrecisionTargetPage);
                            setBottomClick(1);
                        } else if ((((nowPrecisionTargetPage + 1) * 5 + 1) - totalPrecisonTargetPage) == 0) {
                            setBottomClick(6);
                        }
                        break;
                }
                break;
            case R.id.next_page_iv:
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
                        if (totalPrecisonTargetPage > ((nowPrecisionTargetPage + 1) * 5 + 1)) {
                            nowPrecisionTargetPage++;
                            sendPageNumber(totalPrecisonTargetPage, nowPrecisionTargetPage);
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

        mHeadTargetTv.setTextColor(getColor(R.color.gray));
        mBodyTargetTv.setTextColor(getColor(R.color.gray));
        mChestTargetTv.setTextColor(getColor(R.color.gray));
        mPrecisionTargetTv.setTextColor(getColor(R.color.gray));

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
//                    SerialPortUtils.getInstance().sendSerialPort("CC23AADE010A0D");

                    mHeadTargetTv.setTextColor(getColor(R.color.white));
                } else if (type == Constant.BODY) {
                    //PAD控制靶机靶子类型的协议
//                    SerialPortUtils.getInstance().sendSerialPort("CC23AADE020A0D");

                    mBodyTargetTv.setTextColor(getColor(R.color.white));
                } else {
                    //PAD控制靶机靶子类型的协议
//                    SerialPortUtils.getInstance().sendSerialPort("CC23AADE030A0D");

                    mChestTargetTv.setTextColor(getColor(R.color.white));
                    mTargetIv.setImageResource(R.drawable.chest_target);
                }


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

                mCumulativeShotsTv.setText(String.valueOf(totalOrdinaryTarget));
                mTotalTv.setText("共" + totalOrdinaryTarget + "条，共" + totalOrdinaryTargetPage + "页");

                setBottomClick(1);
                break;
            case Constant.PRECISION:
                //TODO PAD控制靶机靶子类型的协议
                //PAD控制靶机靶子类型的协议
//                SerialPortUtils.getInstance().sendSerialPort("CC23AADE040A0D");

                mPrecisionTargetTv.setTextColor(getColor(R.color.white));
                mTargetIv.setImageResource(R.drawable.precision_target);

                mCumulativeShotNumberHeadTv.setVisibility(View.VISIBLE);
                mCumulativeShotNumberContentTv.setVisibility(View.VISIBLE);
                int cumulativeShotNumber = 0;
                for (TargetBean bean : DaoUtil.queryAllPrecisionTarget()) {
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

                mCumulativeShotsTv.setText(String.valueOf(totalPrecisionTarget));
                mTotalTv.setText("共" + totalPrecisionTarget + "条，共" + totalPrecisonTargetPage + "页");

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
                sendPageNumber(totalPrecisonTargetPage, nowPrecisionTargetPage);
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

            mSixthTv.setText("...");
        }
    }

    @Override
    public void cycles(long erectTime, long lodgingTime, int cyclesNumber) {
        this.erectTime = erectTime;
        this.lodgingTime = lodgingTime;
        this.cyclesNumber = cyclesNumber;

        cyclesHandler.removeMessages(1);
        cyclesHandler.removeMessages(2);

        mShootingDroneTv.setText("射击靶机起");
        mShootingDroneSwitch.setChecked(true);
        //TODO 总控台控制靶机起倒的协议(起来)
        //总控台控制靶机起倒的协议(起来)
//                    SerialPortUtils.getInstance().sendSerialPort("CC23AADD010A0D");
        cyclesHandler.sendEmptyMessageDelayed(1, erectTime);
    }

    @Override
    public void onFEReceive(String electricQuantity) {
        mFireSwitch.setChecked(true);
        if ("255".equals(electricQuantity)) {
            mCounterRemainingBattery.setText("电量未测出");
        } else {
            mCounterRemainingBattery.setText(electricQuantity + "%");
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
            targetBean.setShootingInterval("1s");
            targetBean.setTime(new SimpleDateFormat("HH:mm:ss").format(new Date(System.currentTimeMillis())));
            targetBean.setDate(new SimpleDateFormat("yyyy-MM-dd").format(new Date(System.currentTimeMillis())));
            DaoUtil.insertTarget(targetBean);
        } else {//精准靶
            targetBean = new TargetBean();
            targetBean.setType(TargetBean.TYPE_PRECISION);
            targetBean.setNumber(DaoUtil.queryAllPrecisionTarget().size() + 1);
            targetBean.setHit(hit);
            targetBean.setPrecisionRingNumber(position);
            targetBean.setRingNumber(precisionNumber);
            targetBean.setShootingInterval("1s");
            targetBean.setTime(new SimpleDateFormat("HH:mm:ss").format(new Date(System.currentTimeMillis())));
            targetBean.setDate(new SimpleDateFormat("yyyy-MM-dd").format(new Date(System.currentTimeMillis())));

            DaoUtil.insertTarget(targetBean);
        }
        setTabClick(pageType);
    }
}
