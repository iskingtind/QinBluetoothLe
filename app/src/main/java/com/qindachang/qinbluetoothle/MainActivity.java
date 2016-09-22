package com.qindachang.qinbluetoothle;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.le.ScanResult;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.qindachang.qbluetoothle.bluetooth.ble.QinBluetoothLE;
import com.qindachang.qbluetoothle.bluetooth.scan.BLEScanResult;
import com.qindachang.qbluetoothle.bluetooth.scan.OnScanCallBack;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private Button btn_scan;
    private TextView tv_notice;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn_scan = (Button) findViewById(R.id.btn_scan);
        tv_notice = (TextView) findViewById(R.id.tv_notice);

        StringBuilder builder = new StringBuilder();

        QinBluetoothLE qinBluetoothLE = new QinBluetoothLE();
        qinBluetoothLE.setScanPeriod(20000)
                .setOnBLEScanListener(new OnScanCallBack() {
                    @Override
                    public void onScanResult(BluetoothDevice bluetoothDevice, int rssi, byte[] scanRecord) {

                    }

                    @Override
                    public void onBatchScanResults(List<ScanResult> results) {

                    }

                    @Override
                    public void onScanFailed(int errorCode) {

                    }

                    @Override
                    public void onScanCompleted(List<BLEScanResult> bleScanResultList) {

                    }
                }).doScan();
    }
}
