package com.qindachang.qbluetoothle.bluetooth.listener;

import android.bluetooth.BluetoothGatt;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by qin on 2016/8/24.
 */
public abstract class onLeConnectListener {

    /**
     * 连接成功
     */
    public abstract void onConnectSuccess();

    /**
     * 连接失败
     */
    public abstract void onConnectFailure();


    public abstract void onServicesDiscovered(BluetoothGatt gatt, int status);

}
