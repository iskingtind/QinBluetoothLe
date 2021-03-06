package com.qindachang.qbluetoothle.bluetooth.utils;

import android.os.Build;

/**
 * Created by admin on 2016/9/21.
 */
public class BuildUtils {
    /**
     * 获取手机系统版本
     * @return int of phone system version
     */
    public static int getSystemAPILevel() {
        return Build.VERSION.SDK_INT;
    }

    /**
     * 获取手机型号
     * @return string of phone model
     */
    public static String getPhoneModel() {
        return Build.MODEL;
    }
}
