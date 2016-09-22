package com.qindachang.qbluetoothle.bluetooth.utils;

import android.bluetooth.BluetoothManager;
import android.content.Context;

import com.qindachang.qbluetoothle.bluetooth.adapter.QinBluetoothAdapter;
import com.qindachang.qbluetoothle.bluetooth.configure.BluetoothLEConfigure;
import com.qindachang.qbluetoothle.bluetooth.type.Version;


/**
 * Created by qin on 2016/8/24.
 */
public class QinBluetoothManager {

    public static void initialize(Context context) {
        initVersion();
        initBluetoothAdapter(context);
    }

    public static void initialize(Context context, BluetoothLEConfigure configure) {
        initVersion();
        initBluetoothAdapter(context);
    }

    private static void initVersion() {
        Version.PHONE_SYSTEM = BuildUtils.getSystemAPILevel();
    }

    private static void initBluetoothAdapter(Context context) {
        final BluetoothManager bluetoothManager =
                (BluetoothManager) context.getSystemService(Context.BLUETOOTH_SERVICE);
        QinBluetoothAdapter.setBluetoothAdapter(bluetoothManager.getAdapter());
    }

}
