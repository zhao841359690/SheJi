package com.sheji.sheji.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.sheji.sheji.base.BaseApplication;

public class SharedPreferencesUtils {

    private static SharedPreferencesUtils sInstance;
    private SharedPreferences mPreferences;


    public static synchronized SharedPreferencesUtils getInstance() {
        if (sInstance == null) {
            sInstance = new SharedPreferencesUtils();
        }
        return sInstance;
    }

    private SharedPreferencesUtils() {
        mPreferences = BaseApplication.getContext().getSharedPreferences("my_shared_preference", Context.MODE_PRIVATE);
    }

    public void setGunNumber(String gunNumber) {
        mPreferences.edit().putString("gun_number", gunNumber).apply();
    }

    public String getGunNumber() {
        return mPreferences.getString("gun_number", null);
    }

    public void setEquipmentNumber(String equipmentNumber) {
        mPreferences.edit().putString("equipment_number", equipmentNumber).apply();
    }

    public String getEquipmentNumber() {
        return mPreferences.getString("equipment_number", null);
    }
}
