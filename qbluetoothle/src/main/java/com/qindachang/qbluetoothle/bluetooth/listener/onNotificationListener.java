package com.qindachang.qbluetoothle.bluetooth.listener;

import android.bluetooth.BluetoothGattCharacteristic;

/**
 * Created by qin on 2016/8/27.
 */
public interface onNotificationListener {
    void onSuccess(BluetoothGattCharacteristic characteristic);

    void onFailure();
}
