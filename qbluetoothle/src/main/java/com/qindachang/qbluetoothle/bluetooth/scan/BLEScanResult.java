package com.qindachang.qbluetoothle.bluetooth.scan;

import android.bluetooth.BluetoothDevice;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by admin on 2016/9/22.
 */
public class BLEScanResult implements Parcelable {
    private BluetoothDevice mBluetoothDevice;
    private int rssi;
    private byte[] scanRecord;

    public BluetoothDevice getBluetoothDevice() {
        return mBluetoothDevice;
    }

    public void setBluetoothDevice(BluetoothDevice bluetoothDevice) {
        mBluetoothDevice = bluetoothDevice;
    }

    public int getRssi() {
        return rssi;
    }

    public void setRssi(int rssi) {
        this.rssi = rssi;
    }

    public byte[] getScanRecord() {
        return scanRecord;
    }

    public void setScanRecord(byte[] scanRecord) {
        this.scanRecord = scanRecord;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.mBluetoothDevice, flags);
        dest.writeInt(this.rssi);
        dest.writeByteArray(this.scanRecord);
    }

    public BLEScanResult() {
    }

    protected BLEScanResult(Parcel in) {
        this.mBluetoothDevice = in.readParcelable(BluetoothDevice.class.getClassLoader());
        this.rssi = in.readInt();
        this.scanRecord = in.createByteArray();
    }

    public static final Parcelable.Creator<BLEScanResult> CREATOR = new Parcelable.Creator<BLEScanResult>() {
        @Override
        public BLEScanResult createFromParcel(Parcel source) {
            return new BLEScanResult(source);
        }

        @Override
        public BLEScanResult[] newArray(int size) {
            return new BLEScanResult[size];
        }
    };
}
