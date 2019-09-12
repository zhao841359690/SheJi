package com.sheji.sheji.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
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

    private RecyclerView mMainRv;

    private HeadTargetAdapter headTargetAdapter;
    private List<TargetBean> headTargetList = new ArrayList<>();

    private BodyTargetAdapter bodyTargetAdapter;
    private List<TargetBean> bodyTargetList = new ArrayList<>();

    private ChestTargetAdapter chestTargetAdapter;
    private List<TargetBean> chestTargetList = new ArrayList<>();

    private PrecisionTargetAdapter precisionTargetAdapter;
    private List<TargetBean> precisionTargetList = new ArrayList<>();

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

        mMainRv = findViewById(R.id.main_rv);
        mMainRv.setLayoutManager(new LinearLayoutManager(this));

        headTargetAdapter = new HeadTargetAdapter(this);
        bodyTargetAdapter = new BodyTargetAdapter(this);
        chestTargetAdapter = new ChestTargetAdapter(this);
        precisionTargetAdapter = new PrecisionTargetAdapter(this);

        mMainRv.setAdapter(precisionTargetAdapter);
    }

    private void initData() {
        headTargetList = DaoUtil.queryHeadTarget();
        bodyTargetList = DaoUtil.queryBodyTarget();
        chestTargetList = DaoUtil.queryChestTarget();
        precisionTargetList = DaoUtil.queryPrecisionTarget();

        headTargetAdapter.setDataList(headTargetList);
        bodyTargetAdapter.setDataList(bodyTargetList);
        chestTargetAdapter.setDataList(chestTargetList);
        precisionTargetAdapter.setDataList(precisionTargetList);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fire:
                TargetBean targetBean = new TargetBean();
                targetBean.setType(TargetBean.TYPE_HEAD);
                targetBean.setHit("01");
                targetBean.setShootingInterval("1s");
                targetBean.setTime(new SimpleDateFormat("HH;mm:ss").format(new Date(System.currentTimeMillis())));
                targetBean.setDate(new SimpleDateFormat("yyyy-MM-dd").format(new Date(System.currentTimeMillis())));

                DaoUtil.insertTarget(targetBean);
                headTargetAdapter.setDataList(DaoUtil.queryHeadTarget());
                break;
            case R.id.head_target_tv:
                mHeadTargetTv.setTextColor(getResources().getColor(R.color.gray));
                mBodyTargetTv.setTextColor(getResources().getColor(R.color.gray));
                mChestTargetTv.setTextColor(getResources().getColor(R.color.gray));
                mPrecisionTargetTv.setTextColor(getResources().getColor(R.color.gray));

                mHeadTargetTv.setTextColor(getResources().getColor(R.color.white));
                mCumulativeShotNumberHeadTv.setVisibility(View.GONE);
                mCumulativeShotNumberContentTv.setVisibility(View.GONE);

                mMainRv.setAdapter(headTargetAdapter);
                headTargetAdapter.setDataList(headTargetList);
                break;
            case R.id.body_target_tv:
                mHeadTargetTv.setTextColor(getResources().getColor(R.color.gray));
                mBodyTargetTv.setTextColor(getResources().getColor(R.color.gray));
                mChestTargetTv.setTextColor(getResources().getColor(R.color.gray));
                mPrecisionTargetTv.setTextColor(getResources().getColor(R.color.gray));

                mBodyTargetTv.setTextColor(getResources().getColor(R.color.white));
                mCumulativeShotNumberHeadTv.setVisibility(View.GONE);
                mCumulativeShotNumberContentTv.setVisibility(View.GONE);

                mMainRv.setAdapter(bodyTargetAdapter);
                bodyTargetAdapter.setDataList(bodyTargetList);
                break;
            case R.id.chest_target_tv:
                mHeadTargetTv.setTextColor(getResources().getColor(R.color.gray));
                mBodyTargetTv.setTextColor(getResources().getColor(R.color.gray));
                mChestTargetTv.setTextColor(getResources().getColor(R.color.gray));
                mPrecisionTargetTv.setTextColor(getResources().getColor(R.color.gray));

                mChestTargetTv.setTextColor(getResources().getColor(R.color.white));
                mCumulativeShotNumberHeadTv.setVisibility(View.GONE);
                mCumulativeShotNumberContentTv.setVisibility(View.GONE);

                mMainRv.setAdapter(chestTargetAdapter);
                chestTargetAdapter.setDataList(chestTargetList);
                break;
            case R.id.precision_target_tv:
                mHeadTargetTv.setTextColor(getResources().getColor(R.color.gray));
                mBodyTargetTv.setTextColor(getResources().getColor(R.color.gray));
                mChestTargetTv.setTextColor(getResources().getColor(R.color.gray));
                mPrecisionTargetTv.setTextColor(getResources().getColor(R.color.gray));

                mPrecisionTargetTv.setTextColor(getResources().getColor(R.color.white));
                mCumulativeShotNumberHeadTv.setVisibility(View.VISIBLE);
                mCumulativeShotNumberContentTv.setVisibility(View.VISIBLE);

                mMainRv.setAdapter(precisionTargetAdapter);
                precisionTargetAdapter.setDataList(precisionTargetList);
                break;
            default:
                break;
        }
    }
}
