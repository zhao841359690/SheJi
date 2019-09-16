package com.sheji.sheji.bean;

import com.sheji.sheji.base.BaseApplication;

import java.util.List;

public class DaoUtil {
    public static void insertTarget(TargetBean targetBean) {
        BaseApplication.getDaoInstant().getTargetBeanDao().insertOrReplace(targetBean);
    }

    public static List<TargetBean> queryAllHeadTarget() {
        return BaseApplication.getDaoInstant().getTargetBeanDao()
                .queryBuilder()
                .where(TargetBeanDao.Properties.Type.eq(TargetBean.TYPE_HEAD))
                .orderDesc(TargetBeanDao.Properties.Id)
                .list();
    }

    public static List<TargetBean> queryHeadTargetByPageAndSize(int page, int size) {
        return BaseApplication.getDaoInstant().getTargetBeanDao()
                .queryBuilder()
                .where(TargetBeanDao.Properties.Type.eq(TargetBean.TYPE_HEAD))
                .offset(page * size)
                .limit(size)
                .orderDesc(TargetBeanDao.Properties.Id)
                .list();
    }

    public static List<TargetBean> queryAllBodyTarget() {
        return BaseApplication.getDaoInstant().getTargetBeanDao()
                .queryBuilder()
                .where(TargetBeanDao.Properties.Type.eq(TargetBean.TYPE_BODY))
                .orderDesc(TargetBeanDao.Properties.Id)
                .list();
    }

    public static List<TargetBean> queryBodyTargetByPageAndSize(int page, int size) {
        return BaseApplication.getDaoInstant().getTargetBeanDao()
                .queryBuilder()
                .where(TargetBeanDao.Properties.Type.eq(TargetBean.TYPE_BODY))
                .offset(page * size)
                .limit(size)
                .orderDesc(TargetBeanDao.Properties.Id)
                .list();
    }

    public static List<TargetBean> queryAllChestTarget() {
        return BaseApplication.getDaoInstant().getTargetBeanDao()
                .queryBuilder()
                .where(TargetBeanDao.Properties.Type.eq(TargetBean.TYPE_CHEST))
                .orderDesc(TargetBeanDao.Properties.Id)
                .list();
    }

    public static List<TargetBean> queryChestTargetByPageAndSize(int page, int size) {
        return BaseApplication.getDaoInstant().getTargetBeanDao()
                .queryBuilder()
                .where(TargetBeanDao.Properties.Type.eq(TargetBean.TYPE_CHEST))
                .offset(page * size)
                .limit(size)
                .orderDesc(TargetBeanDao.Properties.Id)
                .list();
    }

    public static List<TargetBean> queryAllPrecisionTarget() {
        return BaseApplication.getDaoInstant().getTargetBeanDao()
                .queryBuilder()
                .where(TargetBeanDao.Properties.Type.eq(TargetBean.TYPE_PRECISION))
                .orderDesc(TargetBeanDao.Properties.Id)
                .list();
    }

    public static List<TargetBean> queryPrecisionTargetByPageAndSize(int page, int size) {
        return BaseApplication.getDaoInstant().getTargetBeanDao()
                .queryBuilder()
                .where(TargetBeanDao.Properties.Type.eq(TargetBean.TYPE_PRECISION))
                .offset(page * size)
                .limit(size)
                .orderDesc(TargetBeanDao.Properties.Id)
                .list();
    }

    public static void deleteAll() {
        BaseApplication.getDaoInstant().getTargetBeanDao().deleteAll();
    }
}
