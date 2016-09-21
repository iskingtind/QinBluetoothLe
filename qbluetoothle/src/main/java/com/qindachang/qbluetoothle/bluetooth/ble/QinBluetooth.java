package com.qindachang.qbluetoothle.bluetooth.ble;

import android.util.Log;

import com.qindachang.qbluetoothle.bluetooth.builder.BluetoothLEConfigure;
import com.qindachang.qbluetoothle.bluetooth.scan.ScanBluetoothLE;
import com.qindachang.qbluetoothle.bluetooth.scan.ScanBluetoothLEFactory;
import com.qindachang.qbluetoothle.bluetooth.type.Version;

import java.util.UUID;

/**
 * Created by admin on 2016/9/21.
 */
public class QinBluetooth {

    private final String TAG = QinBluetooth.class.getName();

    private void scanBLE(boolean enable, int SCAN_PERIOD,UUID serviceUUID) {
        ScanBluetoothLEFactory scanBluetoothLEFactory = new ScanBluetoothLEFactory();
        ScanBluetoothLE scanBluetoothLE;
        if (BluetoothLEConfigure.getSDK_VERSION() >= 21 && Version.PHONE_SYSTEM >= 21) {
            //执行5.x的API
            Log.v(TAG, "use LOLLIPOP SDK VERSION Scan bluetoothLE API");
            scanBluetoothLE = scanBluetoothLEFactory.getScanBluetoothLE(ScanBluetoothLEFactory.LOLLIPOP);
        } else {
            //执行4.3的API
            Log.v(TAG, "your phone only use JELLY SDK VERSION Scan bluetoothLE API");
            scanBluetoothLE = scanBluetoothLEFactory.getScanBluetoothLE(ScanBluetoothLEFactory.JELLY);
        }
        scanBluetoothLE.ScanBLE(enable, SCAN_PERIOD);
    }

    public void doScan() {

    }

    public void doScanWithServiceUUID(String ServiceUUID) {
        this.doScanWithServiceUUID(UUID.fromString(ServiceUUID),10000);
    }

    public void doScanWithServiceUUID(String ServiceUUID,int period) {
        this.doScanWithServiceUUID(UUID.fromString(ServiceUUID),period);
    }

    public void doScanWithServiceUUID(UUID ServiceUUID) {
        this.doScanWithServiceUUID(ServiceUUID,10000);
    }

    public void doScanWithServiceUUID(UUID ServiceUUID,int period) {

    }

    public void setOnBLEScanListener() {

    }
}
