package com.qindachang.qbluetoothle.bluetooth.scan;

import android.annotation.TargetApi;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.os.Build;
import android.os.Handler;

import com.qindachang.qbluetoothle.bluetooth.bean.QinBluetoothAdapterBean;
import com.qindachang.qbluetoothle.bluetooth.constant.HandlerConstant;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Android LOLLIPOP 5.x Scan BluetoothLE API
 * Created by qin da chang  on 2016/9/21.
 */
@TargetApi(Build.VERSION_CODES.LOLLIPOP)
public class LollipopScanBluetoothLE extends ScanBluetoothLE {

    private Handler mHandler = new Handler();
    private BluetoothLeScanner mBluetoothLeScanner = QinBluetoothAdapterBean.getBluetoothAdapter().getBluetoothLeScanner();
    private boolean mScanning;
    private List<BLEScanResult> mBLEScanResultList = new ArrayList<>();
    private List<BluetoothDevice> mBluetoothDeviceList = new ArrayList<>();

    private Handler handler;

    public LollipopScanBluetoothLE(Handler handler) {
        this.handler = handler;
    }

    private ScanCallback mScanCallback = new ScanCallback() {
        @Override
        public void onScanResult(int callbackType, ScanResult result) {
            super.onScanResult(callbackType, result);
            if (!mBluetoothDeviceList.contains(result.getDevice())) {
                mBluetoothDeviceList.add(result.getDevice());
                BLEScanResult bleScanResult = new BLEScanResult();
                bleScanResult.setBluetoothDevice(result.getDevice());
                bleScanResult.setRssi(result.getRssi());
                bleScanResult.setScanRecord(result.getScanRecord().getBytes());
                mBLEScanResultList.add(bleScanResult);
                ScanBean.bluetoothDevice = result.getDevice();
                ScanBean.rssi = result.getRssi();
                ScanBean.scanRecord = result.getScanRecord().getBytes();
                handler.obtainMessage(HandlerConstant.SCAN_RESULT).sendToTarget();
            }

//            BLEScanResult bleScanResult = new BLEScanResult();
//            bleScanResult.setBluetoothDevice(result.getDevice());
//            bleScanResult.setRssi(result.getRssi());
//            bleScanResult.setScanRecord(result.getScanRecord().getBytes());
//            mBLEScanResultList.add(bleScanResult);
//            ScanBean.bluetoothDevice = result.getDevice();
//            ScanBean.rssi = result.getRssi();
//            ScanBean.scanRecord = result.getScanRecord().getBytes();
//            handler.obtainMessage(HandlerConstant.SCAN_RESULT).sendToTarget();
        }

        @Override
        public void onBatchScanResults(List<ScanResult> results) {
            super.onBatchScanResults(results);
            handler.obtainMessage(HandlerConstant.BATCH_SCAN_RESULTS, results);
        }

        @Override
        public void onScanFailed(int errorCode) {
            super.onScanFailed(errorCode);
            handler.obtainMessage(HandlerConstant.SCAN_FAILED, errorCode);
        }
    };

    @Override
    public void ScanBLE(boolean enable, int SCAN_PERIOD, UUID[] serviceUUID) {
        mBluetoothDeviceList.clear();
        mBLEScanResultList.clear();
        if (enable) {
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mScanning = false;
                    scanningCallBack(mScanning);
                    mBluetoothLeScanner.stopScan(mScanCallback);
                    scanCompleteCallBack();
                }
            }, SCAN_PERIOD);
            mScanning = true;
            mBluetoothLeScanner.startScan(mScanCallback);
        } else {
            mScanning = false;
            mBluetoothLeScanner.stopScan(mScanCallback);
            scanCompleteCallBack();
        }
        scanningCallBack(mScanning);
    }

    private void scanningCallBack(boolean scanning) {
        if (scanning) {
            handler.obtainMessage(HandlerConstant.SCANNING, 1, 1).sendToTarget();
        } else
            handler.obtainMessage(HandlerConstant.SCANNING, 0, 0).sendToTarget();
    }

    private void scanCompleteCallBack() {
        handler.obtainMessage(HandlerConstant.SCAN_COMPLETED, mBLEScanResultList).sendToTarget();
    }

}
