package com.qindachang.qbluetoothle.bluetooth.ble;

import android.annotation.TargetApi;
import android.bluetooth.le.ScanResult;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.qindachang.qbluetoothle.bluetooth.configure.BluetoothLEConfigure;
import com.qindachang.qbluetoothle.bluetooth.constant.HandlerConstant;
import com.qindachang.qbluetoothle.bluetooth.scan.BLEScanResult;
import com.qindachang.qbluetoothle.bluetooth.scan.ScanBean;
import com.qindachang.qbluetoothle.bluetooth.scan.ScanBluetoothLE;
import com.qindachang.qbluetoothle.bluetooth.scan.ScanBluetoothLEFactory;
import com.qindachang.qbluetoothle.bluetooth.scan.OnScanCallBack;
import com.qindachang.qbluetoothle.bluetooth.type.Version;

import java.util.List;
import java.util.UUID;

/**
 * Created by qin da chang on 2016/9/21.
 */
public class QinBluetoothLE {

    private final String TAG = QinBluetoothLE.class.getName();

    private OnScanCallBack mOnScanCallBack;

    private int scanPeriod = 10000;

    private boolean isScanning;

    private ScanBluetoothLE scanBluetoothLE;

    private void scanBLE(boolean enable, int SCAN_PERIOD, UUID[] serviceUUID) {
        ScanBluetoothLEFactory scanBluetoothLEFactory = new ScanBluetoothLEFactory();
        if (BluetoothLEConfigure.getSDK_VERSION() >= 21 && Version.PHONE_SYSTEM >= 21) {
            //5.x API
            Log.v(TAG, "use LOLLIPOP SDK VERSION Scan bluetoothLE API21");
            scanBluetoothLE = scanBluetoothLEFactory.getScanBluetoothLE(ScanBluetoothLEFactory.LOLLIPOP, mHandler);
        } else {
            //4.3 API
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
        if (scanBluetoothLE != null) {
            scanBluetoothLE.ScanBLE(false, scanPeriod, null);
        }
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

    /**
     * This method requires more than Android's LOLLIPOP system can be used
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public void doScanWithScanFilter() {

    }

    public QinBluetoothLE setOnBLEScanListener(OnScanCallBack onScanCallBack) {
        mOnScanCallBack = onScanCallBack;
        return this;
    }

    /**
     * Get Bluetooth scan status
     */
    public boolean getScanning() {
        return isScanning;
    }

    private Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message message) {
            switch (message.what) {
                case HandlerConstant.SCAN_RESULT:
                    if (mOnScanCallBack != null) {
                        mOnScanCallBack.onScanResult(ScanBean.bluetoothDevice, ScanBean.rssi, ScanBean.scanRecord);
                    }
                    break;
                case HandlerConstant.BATCH_SCAN_RESULTS:
                    if (mOnScanCallBack != null&&Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        mOnScanCallBack.onBatchScanResults((List<ScanResult>) message.obj);
                    }
                    break;
                case HandlerConstant.SCAN_FAILED:
                    if (mOnScanCallBack != null) {
                        mOnScanCallBack.onScanFailed((Integer) message.obj);
                    }
                    break;
                case HandlerConstant.SCAN_COMPLETED:
                    List<BLEScanResult> bleScanResults = (List<BLEScanResult>) message.obj;
                    if (mOnScanCallBack != null) {
                        mOnScanCallBack.onScanCompleted(bleScanResults);
                    }
                    break;
                case HandlerConstant.SCANNING:
                    isScanning = message.arg1 != 0;
                    break;
            }
            return false;
        }
    });
}
