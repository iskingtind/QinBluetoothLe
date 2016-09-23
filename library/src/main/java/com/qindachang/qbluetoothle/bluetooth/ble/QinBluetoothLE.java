package com.qindachang.qbluetoothle.bluetooth.ble;

import android.annotation.TargetApi;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.le.ScanResult;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.qindachang.qbluetoothle.bluetooth.configure.Config;
import com.qindachang.qbluetoothle.bluetooth.connect.OnConnectListener;
import com.qindachang.qbluetoothle.bluetooth.constant.HandlerConstant;
import com.qindachang.qbluetoothle.bluetooth.constant.Version;
import com.qindachang.qbluetoothle.bluetooth.scan.BLEScanResult;
import com.qindachang.qbluetoothle.bluetooth.scan.OnScanListener;
import com.qindachang.qbluetoothle.bluetooth.scan.ScanBean;
import com.qindachang.qbluetoothle.bluetooth.scan.ScanBluetoothLE;
import com.qindachang.qbluetoothle.bluetooth.scan.ScanBluetoothLEFactory;

import java.util.List;
import java.util.UUID;

/**
 *
 * Created by qin da chang on 2016/9/21.
 */
public class QinBluetoothLE {

    private final String TAG = QinBluetoothLE.class.getName();

    private OnScanListener mOnScanListener;
    private OnConnectListener mOnConnectListener;

    private int scanPeriod = 10000;

    private boolean isScanning;

    private ScanBluetoothLE scanBluetoothLE;

    private void scanBLE(boolean enable, int SCAN_PERIOD, UUID[] serviceUUID) {
        ScanBluetoothLEFactory scanBluetoothLEFactory = new ScanBluetoothLEFactory();
        if (Config.bluetoothLEConfigure.getSDK_VERSION() >= 21 && Version.PHONE_SYSTEM >= 21) {
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
     * setting bluetooth le scan period .
     * @param scanPeriod if you doesn't setting about this.the default period is 10000 millis.
     * @return QinBluetoothLE.class
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

    public void doScanWithServiceUUID(String ServiceUUID) {
        UUID[] serviceUUID = new UUID[]{UUID.fromString(ServiceUUID)};
        this.doScanWithServiceUUID(serviceUUID);
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
    private void doScanWithScanFilter() {

    }

    public QinBluetoothLE setOnBLEScanListener(OnScanListener onScanListener) {
        mOnScanListener = onScanListener;
        return this;
    }

    /**
     * Get Bluetooth scan status
     * @return Bluetooth scan status
     */
    public boolean getScanning() {
        return isScanning;
    }

    public QinBluetoothLE setOnConnectListener(OnConnectListener onConnectListener) {
        mOnConnectListener = onConnectListener;
        return this;
    }

    private boolean autoConnect = false;

    public QinBluetoothLE setAutoConnect(boolean autoConnect) {
        this.autoConnect = autoConnect;
        return this;
    }

    private BluetoothDevice mBluetoothDevice;

    public QinBluetoothLE withBluetoothDevice(BluetoothDevice bluetoothDevice) {
        this.mBluetoothDevice = bluetoothDevice;
        return this;
    }

    public void doConnect() {

    }

    private void connectBLE() {

    }


    private Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message message) {
            switch (message.what) {
                case HandlerConstant.SCAN_RESULT:
                    if (mOnScanListener != null) {
                        mOnScanListener.onScanResult(ScanBean.bluetoothDevice, ScanBean.rssi, ScanBean.scanRecord);
                    }
                    break;
                case HandlerConstant.BATCH_SCAN_RESULTS:
                    if (mOnScanListener != null&&Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        mOnScanListener.onBatchScanResults((List<ScanResult>) message.obj);
                    }
                    break;
                case HandlerConstant.SCAN_FAILED:
                    if (mOnScanListener != null) {
                        mOnScanListener.onScanFailed((Integer) message.obj);
                    }
                    break;
                case HandlerConstant.SCAN_COMPLETED:
                    List<BLEScanResult> bleScanResults = (List<BLEScanResult>) message.obj;
                    if (mOnScanListener != null) {
                        mOnScanListener.onScanCompleted(bleScanResults);
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
