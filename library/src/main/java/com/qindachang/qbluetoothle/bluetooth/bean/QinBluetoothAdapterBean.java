package com.qindachang.qbluetoothle.bluetooth.bean;

import android.bluetooth.BluetoothAdapter;

/**
 *
 * Created by qin da chang on 2016/9/21.
 */
public class QinBluetoothAdapterBean {

    private static BluetoothAdapter mBluetoothAdapter;

    public static BluetoothAdapter getBluetoothAdapter() {
        return mBluetoothAdapter;
    }

    public static void setBluetoothAdapter(BluetoothAdapter bluetoothAdapter) {
        mBluetoothAdapter = bluetoothAdapter;
    }
}
