package com.qindachang.qbluetoothle.bluetooth.builder;

/**
 * Created by admin on 2016/9/21.
 */
public class BluetoothLEConfigure {

    private static int SDK_VERSION = 18;

    public static void setSDK_VERSION(int sdk_version) {
        SDK_VERSION = sdk_version;
    }

    public static int getSDK_VERSION() {
        return SDK_VERSION;
    }

}
