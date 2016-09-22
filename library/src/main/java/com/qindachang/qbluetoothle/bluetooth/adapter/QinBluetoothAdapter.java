package com.qindachang.qbluetoothle.bluetooth.adapter;

import android.bluetooth.BluetoothAdapter;

/**
 * Created by admin on 2016/9/21.
 */
public class QinBluetoothAdapter {

    private static BluetoothAdapter mBluetoothAdapter;

    public static BluetoothAdapter getBluetoothAdapter() {
        return mBluetoothAdapter;
    }

    public static void setBluetoothAdapter(BluetoothAdapter bluetoothAdapter) {
        mBluetoothAdapter = bluetoothAdapter;
    }
}
