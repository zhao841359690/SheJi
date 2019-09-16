package com.sheji.sheji;

import android.app.Application;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.sheji.sheji.bean.DaoMaster;
import com.sheji.sheji.bean.DaoSession;

public class BaseApplication extends Application {
    private static Context context;
    private static DaoSession daoSession;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        setUpDatabase();
    }

    private void setUpDatabase() {
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "target.db", null);
        SQLiteDatabase db = helper.getWritableDatabase();
        DaoMaster daoMaster = new DaoMaster(db);
        daoSession = daoMaster.newSession();
    }

    public static DaoSession getDaoInstant() {
        return daoSession;
    }

    public static Context getContext() {
        return context;
    }
}
