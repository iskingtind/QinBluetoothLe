package com.qindachang.qbluetoothle.bluetooth.scan;

/**
 * Created by admin on 2016/9/21.
 */
public class ScanBluetoothLEFactory {

    public static final int JELLY = 18;
    public static final int LOLLIPOP = 21;

    public ScanBluetoothLE getScanBluetoothLE(int scanVersion) {
        if (scanVersion == JELLY) {
            return new JellyScanBluetoothLE();
        } else if (scanVersion == LOLLIPOP) {
            return new LollipopScanBluetoothLE();
        }
        return null;
    }
}
