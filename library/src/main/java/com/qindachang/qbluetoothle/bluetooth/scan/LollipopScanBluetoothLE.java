package com.qindachang.qbluetoothle.bluetooth.scan;

import android.annotation.TargetApi;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.os.Build;
import android.os.Handler;

import com.qindachang.qbluetoothle.bluetooth.adapter.QinBluetoothAdapter;
import com.qindachang.qbluetoothle.bluetooth.constant.HandlerConstant;

import java.util.List;
import java.util.UUID;

/**
 * Android LOLLIPOP 5.1 Scan BluetoothLE API
 * Created by qin da chang  on 2016/9/21.
 */
@TargetApi(Build.VERSION_CODES.LOLLIPOP)
public class LollipopScanBluetoothLE extends ScanBluetoothLE {

    private Handler mHandler = new Handler();
    private BluetoothLeScanner mBluetoothLeScanner = QinBluetoothAdapter.getBluetoothAdapter().getBluetoothLeScanner();
    private ScanCallback mScanCallback = new ScanCallback() {
        @Override
        public void onScanResult(int callbackType, ScanResult result) {
            super.onScanResult(callbackType, result);
            BLEScanResult bleScanResult = new BLEScanResult();
            bleScanResult.setBluetoothDevice(result.getDevice());
            bleScanResult.setRssi(result.getRssi());
            bleScanResult.setScanRecord(result.getScanRecord().getBytes());
            handler.obtainMessage(HandlerConstant.SCAN_RESULT, bleScanResult).sendToTarget();
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

    private Handler handler;

    public LollipopScanBluetoothLE(Handler handler) {
        this.handler = handler;
    }

    @Override
    public void ScanBLE(boolean enable, int SCAN_PERIOD, UUID[] serviceUUID) {
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
