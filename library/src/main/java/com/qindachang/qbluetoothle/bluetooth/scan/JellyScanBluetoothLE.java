package com.qindachang.qbluetoothle.bluetooth.scan;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.os.Handler;

import com.qindachang.qbluetoothle.bluetooth.bean.QinBluetoothAdapterBean;
import com.qindachang.qbluetoothle.bluetooth.constant.HandlerConstant;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Android Jelly 4.3 Scan BluetoothLE API
 * Created by qin da chang on 2016/9/21.
 */
public class JellyScanBluetoothLE extends ScanBluetoothLE {

    private BluetoothAdapter mBluetoothAdapter = QinBluetoothAdapterBean.getBluetoothAdapter();
    private Handler mHandler = new Handler();
    private boolean mScanning;
    private List<BLEScanResult> mBLEScanResultList = new ArrayList<>();
    private List<BluetoothDevice> mBluetoothDeviceList = new ArrayList<>();
    private Handler handler;

    public JellyScanBluetoothLE(Handler handler) {
        this.handler = handler;
    }

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
                    mBluetoothAdapter.stopLeScan(mLeScanCallback);
                    scanCompleteCallBack();
                }
            }, SCAN_PERIOD);
            mScanning = true;
            if (serviceUUID == null) {
                mBluetoothAdapter.startLeScan(mLeScanCallback);
            } else {
                mBluetoothAdapter.startLeScan(serviceUUID,mLeScanCallback);
            }
        } else {
            mScanning = false;
            mBluetoothAdapter.stopLeScan(mLeScanCallback);
            scanCompleteCallBack();
        }
        scanningCallBack(mScanning);
    }

    private BluetoothAdapter.LeScanCallback mLeScanCallback = new BluetoothAdapter.LeScanCallback() {
        @Override
        public void onLeScan(BluetoothDevice bluetoothDevice, int rssi, byte[] scanRecord) {
            if (!mBluetoothDeviceList.contains(bluetoothDevice)) {
                mBluetoothDeviceList.add(bluetoothDevice);
                BLEScanResult bleScanResult = new BLEScanResult();
                bleScanResult.setBluetoothDevice(bluetoothDevice);
                bleScanResult.setRssi(rssi);
                bleScanResult.setScanRecord(scanRecord);
                mBLEScanResultList.add(bleScanResult);
                ScanBean.bluetoothDevice = bluetoothDevice;
                ScanBean.rssi = rssi;
                ScanBean.scanRecord = scanRecord;
                handler.obtainMessage(HandlerConstant.SCAN_RESULT).sendToTarget();
            }
        }
    };

    private void scanningCallBack(boolean scanning) {
        if (scanning) {
            handler.obtainMessage(HandlerConstant.SCANNING, 1, 1).sendToTarget();
        }else
            handler.obtainMessage(HandlerConstant.SCANNING, 0, 0).sendToTarget();
    }

    private void scanCompleteCallBack() {
        handler.obtainMessage(HandlerConstant.SCAN_COMPLETED, mBLEScanResultList).sendToTarget();
    }
}
