package com.qindachang.qbluetoothle.bluetooth.ble;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.qindachang.qbluetoothle.bluetooth.builder.BluetoothLEConfigure;
import com.qindachang.qbluetoothle.bluetooth.scan.ScanBluetoothLE;
import com.qindachang.qbluetoothle.bluetooth.scan.ScanBluetoothLEFactory;
import com.qindachang.qbluetoothle.bluetooth.scan.OnScanCallBack;
import com.qindachang.qbluetoothle.bluetooth.type.Version;

import java.util.UUID;

/**
 * Created by qin da chang on 2016/9/21.
 */
public class QinBluetoothLE {

    private final String TAG = QinBluetoothLE.class.getName();

    private OnScanCallBack mOnScanCallBack;

    private int scanPeriod = 10000;

    private void scanBLE(boolean enable, int SCAN_PERIOD, UUID[] serviceUUID) {
        ScanBluetoothLEFactory scanBluetoothLEFactory = new ScanBluetoothLEFactory();
        ScanBluetoothLE scanBluetoothLE;
        if (BluetoothLEConfigure.getSDK_VERSION() >= 21 && Version.PHONE_SYSTEM >= 21) {
            //执行5.x的API
            Log.v(TAG, "use LOLLIPOP SDK VERSION Scan bluetoothLE API21");
            scanBluetoothLE = scanBluetoothLEFactory.getScanBluetoothLE(ScanBluetoothLEFactory.LOLLIPOP, mHandler);
        } else {
            //执行4.3的API
            Log.v(TAG, "your phone only use JELLY SDK VERSION Scan bluetoothLE API18");
            scanBluetoothLE = scanBluetoothLEFactory.getScanBluetoothLE(ScanBluetoothLEFactory.JELLY, mHandler);
        }
        scanBluetoothLE.ScanBLE(enable, SCAN_PERIOD, serviceUUID);
    }

    /**
     * setting bluetooth le scan period . if you doesn't setting about this.the default period is 10000 millis.
     */
    public QinBluetoothLE setScanPeriod(int scanPeriod) {
        this.scanPeriod = scanPeriod;
        return this;
    }

    /**
     * start bluetooth le scan.
     */
    public void doScan() {
        scanBLE(true, scanPeriod, null);
    }

    public void stopScan() {
        scanBLE(false, scanPeriod, null);
    }

    /**
     * start bluetooth le scan with use service uuid
     *
     * @param ServiceUUID string service uuid / string array
     */
    public void doScanWithServiceUUID(String[] ServiceUUID) {
        int len = ServiceUUID.length;
        UUID[] serviceUUIDs = new UUID[len];
        for (int i = 0; i < len; i++) {
            serviceUUIDs[i] = UUID.fromString(ServiceUUID[i]);
        }
        this.doScanWithServiceUUID(serviceUUIDs);
    }

    /**
     * start bluetooth le scan with use service uuid
     *
     * @param ServiceUUID service uuid / uuid array
     */
    public void doScanWithServiceUUID(UUID[] ServiceUUID) {
        scanBLE(true, scanPeriod, ServiceUUID);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public void doScanWithScanFilter() {

    }


    public QinBluetoothLE setOnBLEScanListener(OnScanCallBack onScanCallBack) {
        mOnScanCallBack = onScanCallBack;
        return this;
    }

    private Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message message) {
            switch (message.what) {
                case 1:
                    break;
            }
            return false;
        }
    });
}
