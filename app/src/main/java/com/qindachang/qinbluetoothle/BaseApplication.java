package com.qindachang.qinbluetoothle;

import android.app.Application;

import com.qindachang.qbluetoothle.bluetooth.configure.BluetoothLEConfigure;
import com.qindachang.qbluetoothle.bluetooth.constant.Version;
import com.qindachang.qbluetoothle.bluetooth.utils.QinBluetoothManager;

/**
 *
 * Created by qin da chang on 2016/9/22.
 */
public class BaseApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        BluetoothLEConfigure configure = new BluetoothLEConfigure.Builder()
                .setAPISDKVersion(Version.AUTO).create();
        QinBluetoothManager.initialize(this,configure);

    }
}
