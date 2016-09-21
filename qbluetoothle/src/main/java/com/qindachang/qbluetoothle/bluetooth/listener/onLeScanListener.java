package com.qindachang.qbluetoothle.bluetooth.listener;

import android.bluetooth.BluetoothDevice;

/**
 * Created by qin on 2016/8/24.
 */
public abstract class onLeScanListener {

    public abstract void onScanResult(BluetoothDevice bluetoothDevice,int rssi,byte[] scanRecord);

    public abstract void onCompleted();
}

