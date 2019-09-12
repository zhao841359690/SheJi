package com.sheji.sheji.bean;

import com.sheji.sheji.BaseApplication;

import java.util.List;

public class DaoUtil {
    public static void insertTarget(TargetBean targetBean) {
        BaseApplication.getDaoInstant().getTargetBeanDao().insertOrReplace(targetBean);
    }

    public static List<TargetBean> queryHeadTarget() {
        return BaseApplication.getDaoInstant().getTargetBeanDao()
                .queryBuilder()
                .where(TargetBeanDao.Properties.Type.eq(TargetBean.TYPE_HEAD)).orderDesc().list();
    }

    public static List<TargetBean> queryBodyTarget() {
        return BaseApplication.getDaoInstant().getTargetBeanDao()
                .queryBuilder()
                .where(TargetBeanDao.Properties.Type.eq(TargetBean.TYPE_BODY)).orderDesc().list();
    }

    public static List<TargetBean> queryChestTarget() {
        return BaseApplication.getDaoInstant().getTargetBeanDao()
                .queryBuilder()
                .where(TargetBeanDao.Properties.Type.eq(TargetBean.TYPE_CHEST)).orderDesc().list();
    }

    public static List<TargetBean> queryPrecisionTarget() {
        return BaseApplication.getDaoInstant().getTargetBeanDao()
                .queryBuilder()
                .where(TargetBeanDao.Properties.Type.eq(TargetBean.TYPE_PRECISION)).orderDesc().list();
    }

    public static List<TargetBean> queryAll() {
        return BaseApplication.getDaoInstant().getTargetBeanDao().loadAll();
    }
}
