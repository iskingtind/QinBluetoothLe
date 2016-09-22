package com.qindachang.qbluetoothle.bluetooth.callback;

import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;

/**
 * Created by qin on 2016/8/27.
 */
public abstract class QGattCallback {

    public abstract void onConnectionStateChange(BluetoothGatt gatt, int status, int newState);

    public abstract void onServicesDiscovered(BluetoothGatt gatt, int status);

    public abstract void onCharacteristicRead(BluetoothGatt gatt,
                                              BluetoothGattCharacteristic characteristic,
                                              int status);

    public abstract void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status);

    public abstract void onDescriptorRead(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status);

    public abstract void onCharacteristicChanged(BluetoothGatt gatt,
                                                 BluetoothGattCharacteristic characteristic);

    public abstract void onDescriptorWrite(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status);
}
