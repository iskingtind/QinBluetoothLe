package com.qindachang.qbluetoothle.bluetooth.utils;

import android.content.Context;

import com.qindachang.qbluetoothle.bluetooth.ble.QBLE;


/**
 * Created by qin on 2016/8/24.
 */
public class QinBluetoothUtils {

    public static void initialize(Context context) {
        QBLE.getInstance().init(context);
    }

}
