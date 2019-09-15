package com.sheji.sheji.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sheji.sheji.BaseActivity;
import com.sheji.sheji.R;
import com.sheji.sheji.adpter.BodyTargetAdapter;
import com.sheji.sheji.adpter.ChestTargetAdapter;
import com.sheji.sheji.adpter.HeadTargetAdapter;
import com.sheji.sheji.adpter.PrecisionTargetAdapter;
import com.sheji.sheji.bean.DaoUtil;
import com.sheji.sheji.bean.TargetBean;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends BaseActivity implements View.OnClickListener {
    private Button fire;

    private TextView mHeadTargetTv;
    private TextView mBodyTargetTv;
    private TextView mChestTargetTv;
    private TextView mPrecisionTargetTv;

    private TextView mHitTv;
    private TextView mTargetNumberTv;
    private TextView mDateTv;
    private TextView mTimeTv;
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
    private EditText mPageEt;
    private ImageView mPrePageIv;
    private TextView mFirstTv;
    private TextView mSecondTv;
    private TextView mThirdTv;
    private TextView mFourthTv;
    private TextView mFifthTv;
    private TextView mSixthTv;
    private ImageView mNextPageIv;

    private View mV1;
    private View mV2;
    private View mV3;
    private View mV4;
    private View mV5;
    private View mV6;
    private View mV7;

    private HeadTargetAdapter headTargetAdapter;
    private List<TargetBean> headTargetList = new ArrayList<>();
    private int totalHeadTarget = 0;
    private int totalHeadTatgetPage = 0;
    private int headTargetPage = 0;
    private int nowHeadTargetPage = 0;

    private BodyTargetAdapter bodyTargetAdapter;
    private List<TargetBean> bodyTargetList = new ArrayList<>();
    private int totalBodyTarget = 0;
    private int totalBodyTatgetPage = 0;
    private int bodyTargetPage = 0;
    private int nowBodyTargetPage = 0;

    private ChestTargetAdapter chestTargetAdapter;
    private List<TargetBean> chestTargetList = new ArrayList<>();
    private int totalChestTarget = 0;
    private int totalChestTatgetPage = 0;
    private int chestTargetPage = 0;
    private int nowChestTargetPage = 0;

    private PrecisionTargetAdapter precisionTargetAdapter;
    private List<TargetBean> precisionTargetList = new ArrayList<>();
    private int totalPrecisionTarget = 0;
    private int totalPrecisonTatgetPage = 0;
    private int precisionTargetPage = 0;
    private int nowPrecisionTargetPage = 0;

    private int pageType = TargetBean.TYPE_PRECISION;

    private int size = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initData();
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

        mHitTv = findViewById(R.id.hit_tv);
        mTargetNumberTv = findViewById(R.id.target_number_tv);

        mDateTv = findViewById(R.id.date_tv);
        mDateTv.setText(new SimpleDateFormat("yyyy-MM-dd").format(new Date(System.currentTimeMillis())));

        mTimeTv = findViewById(R.id.time_tv);
        mTimeTv.setText(new SimpleDateFormat("HH：mm：ss").format(new Date(System.currentTimeMillis())));

        mGunNumberTv = findViewById(R.id.gun_number_tv);
        mCounterNumberTv = findViewById(R.id.counter_number_tv);
        mCounterRemainingBattery = findViewById(R.id.counter_remaining_battery);
        mCumulativeShotNumberHeadTv = findViewById(R.id.cumulative_shot_number_head_tv);
        mCumulativeShotNumberContentTv = findViewById(R.id.cumulative_shot_number_content_tv);
        mCumulativeShotsTv = findViewById(R.id.cumulative_shots_tv);
        mPositionTv = findViewById(R.id.position_tv);

        mMainRv = findViewById(R.id.main_rv);
        mMainRv.setLayoutManager(new LinearLayoutManager(this));

        headTargetAdapter = new HeadTargetAdapter(this);
        bodyTargetAdapter = new BodyTargetAdapter(this);
        chestTargetAdapter = new ChestTargetAdapter(this);
        precisionTargetAdapter = new PrecisionTargetAdapter(this);

        mMainRv.setAdapter(precisionTargetAdapter);

        mTotalTv = findViewById(R.id.total_tv);

        mPageEt = findViewById(R.id.page_et);
        mPageEt.setOnClickListener(this);

        mPrePageIv = findViewById(R.id.pre_page_iv);
        mPrePageIv.setOnClickListener(this);

        mFirstTv = findViewById(R.id.first_tv);
        mFirstTv.setOnClickListener(this);

        mSecondTv = findViewById(R.id.second_tv);
        mSecondTv.setOnClickListener(this);

        mThirdTv = findViewById(R.id.third_tv);
        mThirdTv.setOnClickListener(this);

        mFourthTv = findViewById(R.id.fourth_tv);
        mFourthTv.setOnClickListener(this);

        mFifthTv = findViewById(R.id.fifth_tv);
        mFifthTv.setOnClickListener(this);

        mSixthTv = findViewById(R.id.sixth_tv);
        mSixthTv.setOnClickListener(this);

        mNextPageIv = findViewById(R.id.next_page_iv);
        mNextPageIv.setOnClickListener(this);
        mV1 = findViewById(R.id.v1);
        mV1.setOnClickListener(this);
        mV2 = findViewById(R.id.v2);
        mV2.setOnClickListener(this);
        mV3 = findViewById(R.id.v3);
        mV3.setOnClickListener(this);
        mV4 = findViewById(R.id.v4);
        mV4.setOnClickListener(this);
        mV5 = findViewById(R.id.v5);
        mV5.setOnClickListener(this);
        mV6 = findViewById(R.id.v6);
        mV6.setOnClickListener(this);
        mV7 = findViewById(R.id.v7);
        mV7.setOnClickListener(this);
        mPageLy = findViewById(R.id.page_ly);
        mPageLy.setOnClickListener(this);
    }

    private void initData() {
        totalHeadTarget = DaoUtil.queryAllHeadTarget().size();
        totalHeadTatgetPage = (int) Math.ceil((totalHeadTarget * 1.0f) / (size * 1.0f));
        totalBodyTarget = DaoUtil.queryAllBodyTarget().size();
        totalBodyTatgetPage = (int) Math.ceil((totalBodyTarget * 1.0f) / (size * 1.0f));
        totalChestTarget = DaoUtil.queryAllChestTarget().size();
        totalChestTatgetPage = (int) Math.ceil((totalChestTarget * 1.0f) / (size * 1.0f));
        totalPrecisionTarget = DaoUtil.queryAllPrecisionTarget().size();
        totalPrecisonTatgetPage = (int) Math.ceil((totalPrecisionTarget * 1.0f) / (size * 1.0f));

        headTargetList = DaoUtil.queryHeadTargetByPageAndSize(headTargetPage, size);
        bodyTargetList = DaoUtil.queryBodyTargetByPageAndSize(bodyTargetPage, size);
        chestTargetList = DaoUtil.queryChestTargetByPageAndSize(chestTargetPage, size);
        precisionTargetList = DaoUtil.queryPrecisionTargetByPageAndSize(precisionTargetPage, size);

        headTargetAdapter.setDataList(headTargetList);
        bodyTargetAdapter.setDataList(bodyTargetList);
        chestTargetAdapter.setDataList(chestTargetList);
        precisionTargetAdapter.setDataList(precisionTargetList);

        mTotalTv.setText("共" + DaoUtil.queryAllPrecisionTarget().size() + "条，每页");

        sendPageNumber(totalPrecisionTarget, nowPrecisionTargetPage);
        setBottomClick(1);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fire:
                TargetBean targetBean = new TargetBean();
                targetBean.setType(pageType);
                targetBean.setHit("01");
                targetBean.setShootingInterval("1s");
                targetBean.setTime(new SimpleDateFormat("HH:mm:ss").format(new Date(System.currentTimeMillis())));
                targetBean.setDate(new SimpleDateFormat("yyyy-MM-dd").format(new Date(System.currentTimeMillis())));

                DaoUtil.insertTarget(targetBean);

                switch (pageType) {
                    case TargetBean.TYPE_HEAD:
                        headTargetList = DaoUtil.queryHeadTargetByPageAndSize(headTargetPage, size);
                        headTargetAdapter.setDataList(headTargetList);
                        break;
                    case TargetBean.TYPE_BODY:
                        bodyTargetList = DaoUtil.queryBodyTargetByPageAndSize(bodyTargetPage, size);
                        bodyTargetAdapter.setDataList(bodyTargetList);
                        break;
                    case TargetBean.TYPE_CHEST:
                        chestTargetList = DaoUtil.queryChestTargetByPageAndSize(chestTargetPage, size);
                        chestTargetAdapter.setDataList(chestTargetList);
                        break;
                    case TargetBean.TYPE_PRECISION:
                        precisionTargetList = DaoUtil.queryPrecisionTargetByPageAndSize(precisionTargetPage, size);
                        precisionTargetAdapter.setDataList(precisionTargetList);
                        break;
                }
                break;
            //头靶
            case R.id.head_target_tv:
                pageType = TargetBean.TYPE_HEAD;
                setTabClick(pageType);
                break;
            //身靶
            case R.id.body_target_tv:
                pageType = TargetBean.TYPE_BODY;
                setTabClick(pageType);
                break;
            //胸靶
            case R.id.chest_target_tv:
                pageType = TargetBean.TYPE_CHEST;
                setTabClick(pageType);
                break;
            //精度靶
            case R.id.precision_target_tv:
                pageType = TargetBean.TYPE_PRECISION;
                setTabClick(pageType);
                break;
            case R.id.page_et:
                break;
            case R.id.pre_page_iv:
                switch (pageType) {
                    case TargetBean.TYPE_HEAD:
                        if (nowHeadTargetPage > 0) {
                            nowHeadTargetPage--;
                            sendPageNumber(totalHeadTatgetPage, nowHeadTargetPage);
                            setBottomClick(5);
                        }
                        break;
                    case TargetBean.TYPE_BODY:
                        if (nowBodyTargetPage > 0) {
                            nowBodyTargetPage--;
                            sendPageNumber(totalBodyTatgetPage, nowBodyTargetPage);
                            setBottomClick(5);
                        }
                        break;
                    case TargetBean.TYPE_CHEST:
                        if (nowChestTargetPage > 0) {
                            nowChestTargetPage--;
                            sendPageNumber(totalChestTatgetPage, nowChestTargetPage);
                            setBottomClick(5);
                        }
                        break;
                    case TargetBean.TYPE_PRECISION:
                        if (nowPrecisionTargetPage > 0) {
                            nowPrecisionTargetPage--;
                            sendPageNumber(totalPrecisonTatgetPage, nowPrecisionTargetPage);
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
                    case TargetBean.TYPE_HEAD:
                        if (totalHeadTatgetPage > ((nowHeadTargetPage + 1) * 5 + 1)) {
                            nowHeadTargetPage++;
                            sendPageNumber(totalHeadTatgetPage, nowHeadTargetPage);
                            setBottomClick(1);
                        } else if ((((nowHeadTargetPage + 1) * 5 + 1) - totalHeadTatgetPage) == 0) {
                            setBottomClick(6);
                        }
                        break;
                    case TargetBean.TYPE_BODY:
                        if (totalBodyTatgetPage > ((nowBodyTargetPage + 1) * 5 + 1)) {
                            nowBodyTargetPage++;
                            sendPageNumber(totalBodyTatgetPage, nowBodyTargetPage);
                            setBottomClick(1);
                        } else if ((((nowBodyTargetPage + 1) * 5 + 1) - totalBodyTatgetPage) == 0) {
                            setBottomClick(6);
                        }
                        break;
                    case TargetBean.TYPE_CHEST:
                        if (totalChestTatgetPage > ((nowChestTargetPage + 1) * 5 + 1)) {
                            nowChestTargetPage++;
                            sendPageNumber(totalChestTatgetPage, nowChestTargetPage);
                            setBottomClick(1);
                        } else if ((((nowChestTargetPage + 1) * 5 + 1) - totalChestTatgetPage) == 0) {
                            setBottomClick(6);
                        }
                        break;
                    case TargetBean.TYPE_PRECISION:
                        if (totalPrecisonTatgetPage > ((nowPrecisionTargetPage + 1) * 5 + 1)) {
                            nowPrecisionTargetPage++;
                            sendPageNumber(totalPrecisonTatgetPage, nowPrecisionTargetPage);
                            setBottomClick(1);
                        } else if ((((nowPrecisionTargetPage + 1) * 5 + 1) - totalPrecisonTatgetPage) == 0) {
                            setBottomClick(6);
                        }
                        break;
                }
                break;
            case R.id.next_page_iv:
                switch (pageType) {
                    case TargetBean.TYPE_HEAD:
                        if (totalHeadTatgetPage > ((nowHeadTargetPage + 1) * 5 + 1)) {
                            nowHeadTargetPage++;
                            sendPageNumber(totalHeadTatgetPage, nowHeadTargetPage);
                            setBottomClick(1);
                        }
                        break;
                    case TargetBean.TYPE_BODY:
                        if (totalBodyTatgetPage > ((nowBodyTargetPage + 1) * 5 + 1)) {
                            nowBodyTargetPage++;
                            sendPageNumber(totalBodyTatgetPage, nowBodyTargetPage);
                            setBottomClick(1);
                        }
                        break;
                    case TargetBean.TYPE_CHEST:
                        if (totalChestTatgetPage > ((nowChestTargetPage + 1) * 5 + 1)) {
                            nowChestTargetPage++;
                            sendPageNumber(totalChestTatgetPage, nowChestTargetPage);
                            setBottomClick(1);
                        }
                        break;
                    case TargetBean.TYPE_PRECISION:
                        if (totalPrecisonTatgetPage > ((nowPrecisionTargetPage + 1) * 5 + 1)) {
                            nowPrecisionTargetPage++;
                            sendPageNumber(totalPrecisonTatgetPage, nowPrecisionTargetPage);
                            setBottomClick(1);
                        }
                        break;
                }
                break;
            default:
                break;
        }
    }

    private void setTabClick(int type) {
        mHeadTargetTv.setTextColor(getColor(R.color.gray));
        mBodyTargetTv.setTextColor(getColor(R.color.gray));
        mChestTargetTv.setTextColor(getColor(R.color.gray));
        mPrecisionTargetTv.setTextColor(getColor(R.color.gray));

        mCumulativeShotNumberHeadTv.setVisibility(View.GONE);
        mCumulativeShotNumberContentTv.setVisibility(View.GONE);
        mPositionTv.setVisibility(View.GONE);
        switch (type) {
            case TargetBean.TYPE_HEAD:
                mHeadTargetTv.setTextColor(getColor(R.color.white));

                mMainRv.setAdapter(headTargetAdapter);
                headTargetAdapter.setDataList(headTargetList);

                mTotalTv.setText("共" + DaoUtil.queryAllHeadTarget().size() + "条，每页");

                sendPageNumber(totalHeadTatgetPage, nowHeadTargetPage);
                setBottomClick(1);
                break;
            case TargetBean.TYPE_BODY:
                mBodyTargetTv.setTextColor(getColor(R.color.white));

                mMainRv.setAdapter(bodyTargetAdapter);
                bodyTargetAdapter.setDataList(bodyTargetList);

                mTotalTv.setText("共" + DaoUtil.queryAllBodyTarget().size() + "条，每页");

                sendPageNumber(totalBodyTatgetPage, nowBodyTargetPage);
                setBottomClick(1);
                break;
            case TargetBean.TYPE_CHEST:
                mChestTargetTv.setTextColor(getColor(R.color.white));

                mMainRv.setAdapter(chestTargetAdapter);
                chestTargetAdapter.setDataList(chestTargetList);

                mTotalTv.setText("共" + DaoUtil.queryAllChestTarget().size() + "条，每页");

                sendPageNumber(totalChestTatgetPage, nowChestTargetPage);
                setBottomClick(1);
                break;
            case TargetBean.TYPE_PRECISION:
                mPrecisionTargetTv.setTextColor(getColor(R.color.white));
                mCumulativeShotNumberHeadTv.setVisibility(View.VISIBLE);
                mCumulativeShotNumberContentTv.setVisibility(View.VISIBLE);

                mPositionTv.setVisibility(View.VISIBLE);

                mMainRv.setAdapter(precisionTargetAdapter);
                precisionTargetAdapter.setDataList(precisionTargetList);

                mTotalTv.setText("共" + DaoUtil.queryAllPrecisionTarget().size() + "条，每页");

                sendPageNumber(totalPrecisonTatgetPage, nowPrecisionTargetPage);
                setBottomClick(1);
                break;
            default:
                break;
        }
    }

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
            case TargetBean.TYPE_HEAD:
                sendPageNumber(totalHeadTatgetPage, nowHeadTargetPage);
                break;
            case TargetBean.TYPE_BODY:
                sendPageNumber(totalBodyTatgetPage, nowBodyTargetPage);
                break;
            case TargetBean.TYPE_CHEST:
                sendPageNumber(totalChestTatgetPage, nowChestTargetPage);
                break;
            case TargetBean.TYPE_PRECISION:
                sendPageNumber(totalPrecisonTatgetPage, nowPrecisionTargetPage);
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
            case TargetBean.TYPE_HEAD:
                headTargetPage = nowHeadTargetPage * 5 + (position - 1);
                headTargetList = DaoUtil.queryHeadTargetByPageAndSize(headTargetPage, size);
                headTargetAdapter.setDataList(headTargetList);
                break;
            case TargetBean.TYPE_BODY:
                bodyTargetPage = nowBodyTargetPage * 5 + (position - 1);
                bodyTargetList = DaoUtil.queryBodyTargetByPageAndSize(bodyTargetPage, size);
                bodyTargetAdapter.setDataList(bodyTargetList);
                break;
            case TargetBean.TYPE_CHEST:
                chestTargetPage = nowChestTargetPage * 5 + (position - 1);
                chestTargetList = DaoUtil.queryChestTargetByPageAndSize(chestTargetPage, size);
                chestTargetAdapter.setDataList(chestTargetList);
                break;
            case TargetBean.TYPE_PRECISION:
                precisionTargetPage = nowPrecisionTargetPage * 5 + (position - 1);
                precisionTargetList = DaoUtil.queryPrecisionTargetByPageAndSize(precisionTargetPage, size);
                precisionTargetAdapter.setDataList(precisionTargetList);
                break;
        }
    }

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
}
