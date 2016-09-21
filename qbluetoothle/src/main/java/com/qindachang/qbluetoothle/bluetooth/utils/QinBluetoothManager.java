package com.qindachang.qbluetoothle.bluetooth.utils;

import android.content.Context;

import com.qindachang.qbluetoothle.bluetooth.ble.QBLE;
import com.qindachang.qbluetoothle.bluetooth.builder.BluetoothLEConfigure;
import com.qindachang.qbluetoothle.bluetooth.type.Version;


/**
 * Created by qin on 2016/8/24.
 */
public class QinBluetoothManager {

    public static void initialize(Context context) {
        initVersion();
    }

    public static void initialize(Context context, BluetoothLEConfigure configure) {
        initVersion();
    }

    private static void initVersion() {
        Version.PHONE_SYSTEM = BuildUtils.getSystemAPILevel();
    }

}
