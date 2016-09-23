package com.qindachang.qbluetoothle.bluetooth.scan;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.le.ScanResult;
import android.os.Build;

import java.util.List;

/**
 *
 * Created by qin da chang on 2016/9/21.
 */
public abstract class OnScanListener {

    public abstract void onScanResult(BluetoothDevice bluetoothDevice, int rssi, byte[] scanRecord);

    public void onBatchScanResults(List<ScanResult> results){
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            throw new RuntimeException("Your phone API level is lower than LOLLIPOP.21");
        }
    }

    public abstract void onScanFailed(int errorCode);

    public abstract void onScanCompleted(List<BLEScanResult> bleScanResultList);
}
