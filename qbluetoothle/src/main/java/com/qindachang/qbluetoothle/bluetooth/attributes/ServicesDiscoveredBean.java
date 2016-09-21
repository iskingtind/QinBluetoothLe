package com.qindachang.qbluetoothle.bluetooth.attributes;

import android.bluetooth.BluetoothGatt;

/**
 * Created by admin on 2016/8/31.
 */
public class ServicesDiscoveredBean {

    private static BluetoothGatt mBluetoothGatt;
    private static int mStatus;

    public static BluetoothGatt getBluetoothGatt() {
        return mBluetoothGatt;
    }

    public static void setBluetoothGatt(BluetoothGatt bluetoothGatt) {
        mBluetoothGatt = bluetoothGatt;
    }

    public static int getStatus() {
        return mStatus;
    }

    public static void setStatus(int status) {
        mStatus = status;
    }
}
