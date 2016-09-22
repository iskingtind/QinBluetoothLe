package com.qindachang.qbluetoothle.bluetooth.scan;


import android.bluetooth.BluetoothDevice;
import android.bluetooth.le.ScanResult;

import java.util.List;

/**
 * Created by admin on 2016/9/21.
 */
public abstract class OnScanCallBack {

    public abstract void onScanResult(BluetoothDevice bluetoothDevice, int rssi, byte[] scanRecord);


    public abstract void onBatchScanResults(List<ScanResult> results);


    public abstract void onScanFailed(int errorCode);

    public abstract void onScanCompleted(List<BLEScanResult> bleScanResultList);
}
