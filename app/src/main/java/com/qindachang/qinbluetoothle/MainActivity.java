package com.qindachang.qinbluetoothle;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.le.ScanResult;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.qindachang.qbluetoothle.bluetooth.ble.QinBluetoothLE;
import com.qindachang.qbluetoothle.bluetooth.scan.BLEScanResult;
import com.qindachang.qbluetoothle.bluetooth.scan.OnScanCallBack;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private Button btn_scan;
    private TextView tv_notice;

    QinBluetoothLE qinBluetoothLE = new QinBluetoothLE();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn_scan = (Button) findViewById(R.id.btn_scan);
        tv_notice = (TextView) findViewById(R.id.tv_notice);

        btn_scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scan();
            }
        });

    }

    private void scan() {
        qinBluetoothLE.setScanPeriod(20000)
                .setOnBLEScanListener(new OnScanCallBack() {
                    @Override
                    public void onScanResult(BluetoothDevice bluetoothDevice, int rssi, byte[] scanRecord) {
                        Log.d("debug", "找到蓝牙设备：" + bluetoothDevice.getName());
                        Log.d("debug", "扫描状态：" + qinBluetoothLE.getScanning());
                    }

                    @Override
                    public void onBatchScanResults(List<ScanResult> results) {
                        for (int i=0;i<results.size();i++) {
                            Log.d("debug", "onBatchScanResults:" + results.get(i).getDevice().getName());
                        }
                    }

                    @Override
                    public void onScanFailed(int errorCode) {
                        Log.d("debug", "onScanFailed:" + errorCode);
                    }

                    @Override
                    public void onScanCompleted(List<BLEScanResult> bleScanResultList) {
                        Log.d("debug", "扫描结束，找到全部设备个数:" + bleScanResultList.size());
                        StringBuilder builder = new StringBuilder();
                        for (int i=0;i<bleScanResultList.size();i++) {
                            builder.append(bleScanResultList.get(i).getBluetoothDevice().getName()).append(",");
                        }

                        Log.d("debug", "他们有：" + builder.toString());
                        tv_notice.setText(builder.toString());
                        Log.d("debug", "扫描状态：" + qinBluetoothLE.getScanning());
                    }
                }).doScan();

    }
}
