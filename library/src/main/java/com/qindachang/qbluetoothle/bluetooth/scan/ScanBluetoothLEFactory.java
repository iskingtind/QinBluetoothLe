package com.qindachang.qbluetoothle.bluetooth.scan;

import android.os.Handler;

/**
 *
 * Created by qin da chang on 2016/9/21.
 */
public class ScanBluetoothLEFactory {

    public static final int JELLY = 18;
    public static final int LOLLIPOP = 21;

    public ScanBluetoothLE getScanBluetoothLE(int scanVersion, Handler handler) {
        if (scanVersion == JELLY) {
            return new JellyScanBluetoothLE(handler);
        } else if (scanVersion == LOLLIPOP) {
            return new LollipopScanBluetoothLE(handler);
        }
        return null;
    }
}
