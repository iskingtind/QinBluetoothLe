package com.qindachang.qbluetoothle.bluetooth.configure;

import com.qindachang.qbluetoothle.bluetooth.annotations.APIVersion;

/**
 *
 * Created by qin da chang on 2016/9/21.
 */
public class BluetoothLEConfigure {

    private int SDK_VERSION = 18;

    public int getSDK_VERSION() {
        return SDK_VERSION;
    }

    public static class Builder {
        private int sdk_version;

        public BluetoothLEConfigure.Builder setAPISDKVersion(@APIVersion int version) {
            this.sdk_version = version;
            return this;
        }

        public BluetoothLEConfigure create() {
            BluetoothLEConfigure configure = new BluetoothLEConfigure();
            this.build(configure);
            return configure;
        }

        public void build(BluetoothLEConfigure configure) {
            configure.SDK_VERSION = this.sdk_version;
        }
    }


}