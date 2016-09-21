package com.qindachang.qbluetoothle.bluetooth.scan;

import android.annotation.TargetApi;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.os.Build;
import android.os.Handler;

import com.qindachang.qbluetoothle.bluetooth.adapter.QinBluetoothAdapter;

import java.util.List;

/**
 * Created by admin on 2016/9/21.
 */
@TargetApi(Build.VERSION_CODES.LOLLIPOP)
public class LollipopScanBluetoothLE extends ScanBluetoothLE {

    private Handler mHandler = new Handler();
    private BluetoothLeScanner mBluetoothLeScanner = QinBluetoothAdapter.getBluetoothAdapter().getBluetoothLeScanner();
    private ScanCallback mScanCallback = new ScanCallback() {
        @Override
        public void onScanResult(int callbackType, ScanResult result) {
            super.onScanResult(callbackType, result);
        }

        @Override
        public void onBatchScanResults(List<ScanResult> results) {
            super.onBatchScanResults(results);
        }

        @Override
        public void onScanFailed(int errorCode) {
            super.onScanFailed(errorCode);
        }
    };

    private boolean mScanning;

    @Override
    public void ScanBLE(boolean enable, int SCAN_PERIOD) {
        if (enable) {
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mScanning = false;
                    mBluetoothLeScanner.stopScan(mScanCallback);
                }
            }, SCAN_PERIOD);
            mScanning = true;
            mBluetoothLeScanner.startScan(mScanCallback);
        } else {
            mScanning = false;
            mBluetoothLeScanner.stopScan(mScanCallback);
        }

    }


}
