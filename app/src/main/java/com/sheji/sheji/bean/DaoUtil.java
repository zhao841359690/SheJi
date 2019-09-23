package com.sheji.sheji.bean;

import com.sheji.sheji.base.BaseApplication;

import java.util.List;

/**
 * @author kk-zhaoqingfeng
 */
public class DaoUtil {
    public static void insertTarget(TargetBean targetBean) {
        BaseApplication.getDaoInstant().getTargetBeanDao().insertOrReplace(targetBean);
    }

    public static List<TargetBean> queryAllOrdinaryTarget() {
        return BaseApplication.getDaoInstant().getTargetBeanDao()
                .queryBuilder()
                .where(TargetBeanDao.Properties.Type.eq(TargetBean.TYPE_ORDINARY))
                .orderDesc(TargetBeanDao.Properties.Id)
                .list();
    }

    public static List<TargetBean> queryOrdinaryTargetByPageAndSize(int page, int size) {
        return BaseApplication.getDaoInstant().getTargetBeanDao()
                .queryBuilder()
                .where(TargetBeanDao.Properties.Type.eq(TargetBean.TYPE_ORDINARY))
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
