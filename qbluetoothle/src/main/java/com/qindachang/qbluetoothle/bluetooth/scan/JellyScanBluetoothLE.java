package com.qindachang.qbluetoothle.bluetooth.scan;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.os.Handler;

import com.qindachang.qbluetoothle.bluetooth.adapter.QinBluetoothAdapter;

/**
 * Created by admin on 2016/9/21.
 */
public class JellyScanBluetoothLE extends ScanBluetoothLE {

    private BluetoothAdapter mBluetoothAdapter = QinBluetoothAdapter.getBluetoothAdapter();
    private Handler mHandler = new Handler();
    private boolean mScanning;

    @Override
    public void ScanBLE(boolean enable, int SCAN_PERIOD) {
        if (enable) {
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mScanning = false;
                    mBluetoothAdapter.stopLeScan(mLeScanCallback);
                }
            }, SCAN_PERIOD);
            mScanning = true;
            mBluetoothAdapter.startLeScan(mLeScanCallback);
        } else {
            mScanning = false;
            mBluetoothAdapter.stopLeScan(mLeScanCallback);
        }
    }

    private BluetoothAdapter.LeScanCallback mLeScanCallback = new BluetoothAdapter.LeScanCallback() {
        @Override
        public void onLeScan(BluetoothDevice bluetoothDevice, int rssi, byte[] bytes) {

        }
    };
}
