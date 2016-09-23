package com.qindachang.qbluetoothle.bluetooth.connect;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.os.Handler;
import android.util.Log;

import com.qindachang.qbluetoothle.bluetooth.bean.QinBluetoothAdapterBean;

/**
 * Created by admin on 2016/9/23.
 */
public class ConnectBluetoothLE {

    private String TAG = ConnectBluetoothLE.class.getSimpleName();
    private String mBluetoothDeviceAddress;

    private int mConnectionState = STATE_DISCONNECTED;
    private static final int STATE_DISCONNECTED = 0;
    private static final int STATE_CONNECTING = 1;
    private static final int STATE_CONNECTED = 2;

    private Handler handler;
    private BluetoothAdapter mBluetoothAdapter = QinBluetoothAdapterBean.getBluetoothAdapter();
    private BluetoothGatt mBluetoothGatt;

    public ConnectBluetoothLE(Handler handler) {
        this.handler = handler;
    }

    public boolean connect(BluetoothDevice bluetoothDevice, boolean autoConnect) {
        if (bluetoothDevice == null) {
            throw new RuntimeException("The bluetoothDevice is null, use method 'withBluetoothDevice(BluetoothDevice bluetoothDevice)' before it.");
        }
        String MAC_Address = bluetoothDevice.getAddress();
        if (mBluetoothAdapter == null || MAC_Address == null) {
            Log.w(TAG, "BluetoothAdapter not initialized or unspecified address.");
            return false;
        }
        if (mBluetoothDeviceAddress != null && MAC_Address.equals(mBluetoothDeviceAddress)
                && mBluetoothGatt != null) {
            Log.d(TAG, "Trying to use an existing mBluetoothGatt for connection.");
            if (mBluetoothGatt.connect()) {
                mConnectionState = STATE_CONNECTING;
                return true;
            } else {
                return false;
            }
        }
        return false;
    }

}
