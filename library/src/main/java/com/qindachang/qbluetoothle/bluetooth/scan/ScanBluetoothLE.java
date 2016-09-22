package com.qindachang.qbluetoothle.bluetooth.scan;

import java.util.UUID;

/**
 * Created by admin on 2016/9/21.
 */
public abstract class ScanBluetoothLE {
    public abstract void ScanBLE(boolean enable, int SCAN_PERIOD, UUID[] serviceUUID);
}
