package com.qindachang.qinbluetoothle;

import android.app.Application;

import com.qindachang.qbluetoothle.bluetooth.utils.QinBluetoothManager;

/**
 * Created by admin on 2016/9/22.
 */
public class BaseApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        QinBluetoothManager.initialize(this);

    }
}
