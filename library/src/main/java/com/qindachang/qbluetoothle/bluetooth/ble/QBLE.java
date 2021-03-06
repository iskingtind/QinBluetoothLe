package com.qindachang.qbluetoothle.bluetooth.ble;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import com.qindachang.qbluetoothle.bluetooth.attributes.SampleGattAttributes;
import com.qindachang.qbluetoothle.bluetooth.bean.ServicesDiscoveredBean;
import com.qindachang.qbluetoothle.bluetooth.configure.BluetoothLEConfigure;
import com.qindachang.qbluetoothle.bluetooth.callback.QGattCallback;
import com.qindachang.qbluetoothle.bluetooth.listener.onLeConnectListener;
import com.qindachang.qbluetoothle.bluetooth.listener.onLeScanListener;
import com.qindachang.qbluetoothle.bluetooth.listener.onNotificationListener;
import com.qindachang.qbluetoothle.bluetooth.listener.onWriteCharacterListener;
import com.qindachang.qbluetoothle.bluetooth.utils.FormatUtils;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

/**
 * Created by qin on 2016/8/24.
 *
 * @author Qin Da chang
 *         link - http://qindachang.github.io/
 */
public class QBLE extends QGattCallback {

    private static final String TAG = "QBLE";
    private BluetoothAdapter mBluetoothAdapter;
    private BluetoothLeService mBluetoothLeService;
    private BluetoothGatt mBluetoothGatt;
    private BluetoothGattCharacteristic mNotifyCharacteristic;

    private boolean mScanning = false;

    private List<BluetoothDevice> mBluetoothDeviceList = new ArrayList<>();

    private Context mContext;

    private Handler mHandler;

    private volatile static QBLE instance;

    private BluetoothLEConfigure mConfigure;

    public static QBLE getInstance() {
        if (instance == null) {
            synchronized (QBLE.class) {
                if (instance == null) {
                    instance = new QBLE();
                }
            }
        }
        return instance;
    }

    public void init(Context context) {
        this.mContext = context;
        if (mBluetoothAdapter == null) {
            final BluetoothManager bluetoothManager =
                    (BluetoothManager) context.getSystemService(Context.BLUETOOTH_SERVICE);
            mBluetoothAdapter = bluetoothManager.getAdapter();
        }
        mHandler = new Handler();
    }

    public void init(Context context, int sdk_version) {
        this.mContext = context;
        if (mBluetoothAdapter == null) {
            final BluetoothManager bluetoothManager =
                    (BluetoothManager) context.getSystemService(Context.BLUETOOTH_SERVICE);
            mBluetoothAdapter = bluetoothManager.getAdapter();
        }
        mHandler = new Handler();
    }

    /**
     * Ensures the bluetooth is opening.if not it will going to request open bluetooth.
     *
     * @param activity          enter your Activity context in here
     * @param REQUEST_ENABLE_BT Let me think about how to explain this..
     * @return bluetooth status about bluetoothAdapter enabled
     */
    public boolean ensuresBluetoothForResult(Activity activity, int REQUEST_ENABLE_BT) {
        if (mBluetoothAdapter.isEnabled()) {
            return true;
        } else {
            if (!mBluetoothAdapter.isEnabled()) {
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                activity.startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
            } else {
                return true;
            }
        }
        return false;
    }

    /**
     * Scan bluetooth LE
     *
     * @param enable      set scan status, if true will start scan.
     * @param SCAN_PERIOD scanning period ,millis
     */
    public void scanBLE(boolean enable, int SCAN_PERIOD) {
        if (enable) {
            mBluetoothDeviceList.clear();

            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mScanning = false;
                    mBluetoothAdapter.stopLeScan(mLeScanCallback);
                    if (mOnLeScanListener != null) {
                        mOnLeScanListener.onCompleted();
                    }
                }
            }, SCAN_PERIOD);
            mScanning = true;
            mBluetoothAdapter.startLeScan(mLeScanCallback);
        } else {
            mScanning = false;
            mBluetoothAdapter.stopLeScan(mLeScanCallback);
        }
    }

    /**
     * refresh the remote device cache.
     *
     * @return refreshDeviceCache successful
     */
    public boolean refreshDeviceCache() {
        try {
            Method e = BluetoothGatt.class.getMethod("refresh", new Class[0]);
            if (e != null) {
                boolean success = ((Boolean) e.invoke(this.getBluetoothGatt(), new Object[0])).booleanValue();
                Log.i(TAG, "Refreshing result: " + success);
                return success;
            }
        } catch (Exception var3) {
            Log.e(TAG, "An exception occured while refreshing device", var3);
        }
        return false;
    }

    /**
     * get the bluetoothGatt.
     * Is it useful?
     *
     * @return this
     */
    public BluetoothGatt getBluetoothGatt() {
        return this.mBluetoothGatt;
    }

    /**
     * Scan according to the specified UUID
     *
     * @param uuid        specified UUID
     * @param enable      set scan status, if true will start scan.
     * @param SCAN_PERIOD scanning period ,millis
     */
    public void scanBLEWithUUID(UUID[] uuid, boolean enable, int SCAN_PERIOD) {
        if (enable) {
            mBluetoothDeviceList.clear();
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mScanning = false;
                    mBluetoothAdapter.stopLeScan(mLeScanCallback);
                    if (mOnLeScanListener != null) {
                        mOnLeScanListener.onCompleted();
                    }
                }
            }, SCAN_PERIOD);
            mScanning = true;
            mBluetoothAdapter.startLeScan(uuid, mLeScanCallback);
        } else {
            mScanning = false;
            mBluetoothAdapter.stopLeScan(mLeScanCallback);
        }
    }

    /**
     *
     */
    public void stopScanning() {
        mBluetoothAdapter.stopLeScan(mLeScanCallback);
        if (mOnLeScanListener != null) {
            mOnLeScanListener.onCompleted();
        }
        mScanning = false;
    }


    private BluetoothDevice mDevice;
    private int mRssi;
    private byte[] mScanRecord;

    private BluetoothAdapter.LeScanCallback mLeScanCallback =
            new BluetoothAdapter.LeScanCallback() {

                @Override
                public void onLeScan(BluetoothDevice device, int rssi, byte[] scanRecord) {
                    if (mOnLeScanListener != null) {
                        if (!mBluetoothDeviceList.contains(device)) {
                            mDevice = device;
                            mRssi = rssi;
                            mScanRecord = scanRecord;
                            mBluetoothDeviceList.add(device);
                            Observable.just(5)
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(new Action1<Integer>() {
                                        @Override
                                        public void call(Integer integer) {
                                            mOnLeScanListener.onScanResult(mDevice, mRssi, mScanRecord);
                                        }
                                    });

                        }
                    }
                }
            };


    private onLeScanListener mOnLeScanListener;

    private boolean ScanEnable;
    private int SCAN_PERIOD;

    /**
     * Register a callback to be invoked when this phone is scanning.
     *
     * @param SCAN_PERIOD      scanning period ,millis
     * @param onLeScanListener The callback that will run
     * @return this
     */
    public QBLE setOnLeScanListener(int SCAN_PERIOD, onLeScanListener onLeScanListener) {
        this.mOnLeScanListener = onLeScanListener;
        this.ScanEnable = true;
        this.SCAN_PERIOD = SCAN_PERIOD;
        return this;
    }

    /**
     * Register a callback to be invoked when this phone is scanning.
     *
     * @param onLeScanListener The callback that will run
     * @return this
     */
    public QBLE setOnLeScanListener(onLeScanListener onLeScanListener) {
        this.setOnLeScanListener(15000, onLeScanListener);
        return this;
    }

    public QBLE unLeScanListener() {
        this.mOnLeScanListener = null;
        return this;
    }

    /**
     * Will start executing the scan.
     */
    public void doScan() {
        scanBLE(ScanEnable, SCAN_PERIOD);
        QinBluetoothLE qinBluetoothLE = new QinBluetoothLE();
    }

    /**
     * @param uuids uuid[]
     *              Will start executing the scan with Service UUID
     */
    public void doScanWithServiceUUID(UUID[] uuids) {
        scanBLEWithUUID(uuids, ScanEnable, SCAN_PERIOD);
    }


    /**
     * Gets the scan results of the Bluetooth device
     *
     * @return List of BluetoothDevice
     */
    public List<BluetoothDevice> getScanBluetoothDeviceList() {
        return mBluetoothDeviceList;
    }

    /**
     * Get Bluetooth scan status
     *
     * @return true，scanning；false，Of course not
     */
    public boolean getScanning() {
        return mScanning;
    }

    /**
     * Connect Bluetooth.
     *
     * @param bluetoothDevice BluetoothDevice
     * @param auto            auto connect to bluetooth le
     */
    public void connectBLE(BluetoothDevice bluetoothDevice, boolean auto) {
        this.setBluetoothDevice(bluetoothDevice);
        Intent gattServiceIntent = new Intent(mContext, BluetoothLeService.class);
        mContext.bindService(gattServiceIntent, mServiceConnection, Context.BIND_AUTO_CREATE);
        //registerReceiver();
        if (mBluetoothLeService != null) {
            boolean result = mBluetoothLeService.connect(mBluetoothDevice.getAddress(), auto);
            Log.d("debug", "Connect request result=" + result);
        }
    }

    /**
     * Connect Bluetooth.You must call the code { withBluetoothDevice(BluetoothDevice bluetoothDevice) }
     * before this method.Otherwise it will produce unexpected results.
     */
    public void connectBLE() {
        this.connectBLE(mBluetoothDevice, connectAuto);
    }

    private BluetoothDevice mBluetoothDevice;

    private void setBluetoothDevice(BluetoothDevice bluetoothDevice) {
        this.mBluetoothDevice = bluetoothDevice;
    }


    private final ServiceConnection mServiceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName componentName, IBinder service) {
            mBluetoothLeService = ((BluetoothLeService.LocalBinder) service).getService();
            setBLECallBackListener();
            if (!mBluetoothLeService.initialize()) {
                Log.e(TAG, "Unable to initialize Bluetooth");
            }
            // Automatically connects to the device upon successful start-up initialization.

            mBluetoothLeService.connect(mBluetoothDevice.getAddress(), connectAuto);
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            mBluetoothLeService = null;
        }
    };


    public void setBLECallBackListener() {
        mBluetoothLeService.setGattListener(this);
    }

    /**
     * @param bluetoothDevice bluetoothDevice
     *                        Set the BluetoothDevice for Connecting bluetooth.
     *                        When you call code { connectBLE() } ,You need to call this code before it.
     * @return this
     */
    public QBLE withBluetoothDevice(BluetoothDevice bluetoothDevice) {
        this.mBluetoothDevice = bluetoothDevice;
        return this;
    }

    private onLeConnectListener mOnLeConnectListener;

    /**
     * Register a callback to be invoked when this phone is connecting bluetooth.
     *
     * @param o the callback that will run.
     * @return this
     */
    public QBLE setOnLeConnectListener(onLeConnectListener o) {
        this.mOnLeConnectListener = o;
        return this;
    }

    /**
     * @return this
     */
    public QBLE unLeConnectListener() {
        this.mOnLeConnectListener = null;
        return this;
    }

    private boolean connectAuto = false;

    /**
     * Set QBLE to automatically connect Bluetooth when Bluetooth is disconnected
     *
     * @param auto true is automatically
     * @return this
     */
    public QBLE setAuto(boolean auto) {
        this.connectAuto = auto;
        return this;
    }

    /**
     * just do Connect.
     */
    public void doConnect() {
        connectBLE();
    }

    /**
     * @param b BluetoothDevice
     *          do connect bluetooth with using BluetoothDevice.
     */
    public void doConnect(BluetoothDevice b) {
        connectBLE(b, connectAuto);
    }

    private boolean mConnected = false;

    /**
     * Get the bluetooth connected status.
     *
     * @return Connected
     */
    public boolean getConnected() {
        return mConnected;
    }

    private BroadcastReceiver mGattUpdateReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothLeService.ACTION_GATT_CONNECTED.equals(action)) {

                mConnected = true;
                mBluetoothGatt = mBluetoothLeService.getBluetoothGatt();
                if (mOnLeConnectListener != null) {
                    mOnLeConnectListener.onConnectSuccess();
                }
            } else if (BluetoothLeService.ACTION_GATT_DISCONNECTED.equals(action)) {

                mConnected = false;
                if (mOnLeConnectListener != null) {
                    mOnLeConnectListener.onConnectFailure();
                }
            } else if (BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED.equals(action)) {

                // displayGattService(mBluetoothLeService.getSupportedGattServices());
            } else if (BluetoothLeService.ACTION_DATA_AVAILABLE.equals(action)) {


            } else if (BluetoothLeService.EXTRA_DATA.equals(action)) {

            }
        }
    };


    private void registerReceiver() {
        mContext.registerReceiver(mGattUpdateReceiver, makeGattUpdateIntentFilter());
    }

    private void unregisterReceiver() {
        mContext.unregisterReceiver(mGattUpdateReceiver);
    }

    private void onDestroy() {
        mContext.unbindService(mServiceConnection);
        mBluetoothLeService = null;
    }

    private static IntentFilter makeGattUpdateIntentFilter() {
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_CONNECTED);
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_DISCONNECTED);
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED);
        intentFilter.addAction(BluetoothLeService.ACTION_DATA_AVAILABLE);
        return intentFilter;
    }

    private ArrayList<ArrayList<BluetoothGattCharacteristic>> mGattCharacteristics = new ArrayList<>();

    public static final String LIST_NAME = "NAME";
    public static final String LIST_UUID = "UUID";


    //
    //
    private void displayGattService(List<BluetoothGattService> gattServices) {
        if (gattServices == null) {
            Log.d("debug", "gattServices is null");
            return;
        }
        String uuid = null;
        String unknownServiceString = "Unknown service";
        String unknownCharaString = "Unknown characteristic";

        ArrayList<HashMap<String, String>> gattServiceData = new ArrayList<>();

        ArrayList<ArrayList<HashMap<String, String>>> gattCharacteristicData =
                new ArrayList<>();
        for (BluetoothGattService gattService : gattServices) {
            HashMap<String, String> currentServiceData = new HashMap<>();
            uuid = gattService.getUuid().toString();
            currentServiceData.put(
                    LIST_NAME, SampleGattAttributes.lookup(uuid, unknownServiceString)
            );
            currentServiceData.put(
                    LIST_UUID, uuid
            );
            gattServiceData.add(currentServiceData);

            ArrayList<HashMap<String, String>> gattCharacteristicGroupData =
                    new ArrayList<>();
            List<BluetoothGattCharacteristic> gattCharacteristics =
                    gattService.getCharacteristics();
            ArrayList<BluetoothGattCharacteristic> charas =
                    new ArrayList<>();

            for (BluetoothGattCharacteristic gattCharacteristic : gattCharacteristics) {
                charas.add(gattCharacteristic);
                HashMap<String, String> currentCharaData = new HashMap<>();
                uuid = gattCharacteristic.getUuid().toString();
                currentCharaData.put(
                        LIST_NAME, SampleGattAttributes.lookup(uuid, unknownCharaString));
                currentCharaData.put(LIST_UUID, uuid);
                gattCharacteristicGroupData.add(currentCharaData);
            }
            mGattCharacteristics.add(charas);
            gattCharacteristicData.add(gattCharacteristicGroupData);
        }

    }

    /**
     * Enable Bluetooth feature notifications about Characteristic.
     *
     * @param enable                   is open this Characteristic Notification ?
     * @param serviceUUIDString        service UUID use String
     * @param characteristicUUIDString characteristic UUID use String
     * @param onNotificationListener   onNotificationListener
     * @return this
     */
    public QBLE enableCharacteristicNotification(boolean enable, String serviceUUIDString,
                                                 String characteristicUUIDString,
                                                 onNotificationListener onNotificationListener) {
        this.enableCharacteristicNotification(enable, UUID.fromString(serviceUUIDString),
                UUID.fromString(characteristicUUIDString), onNotificationListener);
        return this;
    }

    /**
     * Enable Bluetooth feature notifications about Characteristic.
     *
     * @param enable                 is open this Characteristic Notification ?
     * @param serviceUUID            service UUID use UUID
     * @param characteristicUUID     characteristic UUID use UUID
     * @param onNotificationListener onNotificationListener
     */
    public void enableCharacteristicNotification(boolean enable, UUID serviceUUID,
                                                 UUID characteristicUUID,
                                                 onNotificationListener onNotificationListener) {
        BluetoothGattService service = mBluetoothGatt.getService(serviceUUID);
        BluetoothGattCharacteristic characteristic = service.getCharacteristic(characteristicUUID);
        setCharacteristicNotification(characteristic, enable);
        if (onNotificationListener != null) this.mOnNotificationListener = onNotificationListener;
    }

    /**
     * @param characteristic characteristic
     * @param enabled        enabled
     */
    private void setCharacteristicNotification(BluetoothGattCharacteristic characteristic,
                                               boolean enabled) {
        int charaProp = characteristic.getProperties();
//        if ((charaProp | BluetoothGattCharacteristic.PROPERTY_READ) > 0) {
//            if (mNotifyCharacteristic != null) {
//
//                mBluetoothLeService.setCharacteristicNotification(
//                        mNotifyCharacteristic, false);
//                mNotifyCharacteristic = null;
//            }
//            mBluetoothLeService.readCharacteristic(characteristic);
//        }
        if ((charaProp | BluetoothGattCharacteristic.PROPERTY_NOTIFY) > 0) {
            mNotifyCharacteristic = characteristic;
            mBluetoothLeService.setCharacteristicNotification(characteristic, enabled);
        }

    }

    /**
     * Enable Bluetooth feature notifications about many Characteristics.
     *
     * @param enable                      enable
     * @param serviceUUIDString           serviceUUIDString
     * @param arrCharacteristicUUIDString arrCharacteristicUUIDString
     */
    public void enableAllCharacteristicNotification(boolean enable, String serviceUUIDString,
                                                    String[] arrCharacteristicUUIDString) {
        BluetoothGattService service = mBluetoothGatt.getService(UUID.fromString(serviceUUIDString));
        List<BluetoothGattCharacteristic> characteristicList = new ArrayList<>();
        int count = arrCharacteristicUUIDString.length;
        for (int i = 0; i < count; i++) {
            BluetoothGattCharacteristic characteristic =
                    service.getCharacteristic(UUID.fromString(arrCharacteristicUUIDString[i]));
            characteristicList.add(i, characteristic);
        }
        mBluetoothLeService.setAllCharacteristicNotification(characteristicList, enable);
    }

    /**
     * Enable Bluetooth feature notifications about many Characteristics.
     *
     * @param enable                      enable
     * @param serviceUUIDString           serviceUUIDString
     * @param arrCharacteristicUUIDString arrCharacteristicUUIDString
     * @param onNotificationListener      onNotificationListener
     */
    public void enableAllCharacteristicNotification(boolean enable, String serviceUUIDString,
                                                    String[] arrCharacteristicUUIDString,
                                                    onNotificationListener onNotificationListener) {
        BluetoothGattService service = mBluetoothGatt.getService(UUID.fromString(serviceUUIDString));
        List<BluetoothGattCharacteristic> characteristicList = new ArrayList<>();
        int count = arrCharacteristicUUIDString.length;
        for (int i = 0; i < count; i++) {
            BluetoothGattCharacteristic characteristic =
                    service.getCharacteristic(UUID.fromString(arrCharacteristicUUIDString[i]));
            characteristicList.add(i, characteristic);
        }
        if (onNotificationListener != null) this.mOnNotificationListener = onNotificationListener;
        mBluetoothLeService.setAllCharacteristicNotification(characteristicList, enable);
    }

    public QBLE unNotificationListener() {
        mOnNotificationListener = null;
        return this;
    }

    private onNotificationListener mOnNotificationListener;


    public QBLE setOnNotificationListener(onNotificationListener onNotificationListener) {
        this.mOnNotificationListener = onNotificationListener;
        return this;
    }

    private onWriteCharacterListener mOnWriteCharacterListener;

    public QBLE setOnWriteCharacterListener(onWriteCharacterListener onWriteCharacterListener) {
        this.mOnWriteCharacterListener = onWriteCharacterListener;
        return this;
    }

    /**
     * Write data to bluetooth Characteristic
     *
     * @param data                     data
     * @param serviceUUIDString        serviceUUIDString
     * @param characteristicUUIDString characteristicUUIDString
     */
    public void writeDataToCharacteristic(byte[] data, String serviceUUIDString,
                                          String characteristicUUIDString) {
        this.writeDataToCharacteristic(data, UUID.fromString(serviceUUIDString),
                UUID.fromString(characteristicUUIDString),
                null);
    }

    /**
     * Write data to bluetooth Characteristic
     *
     * @param data                     data
     * @param serviceUUIDString        serviceUUIDString
     * @param characteristicUUIDString characteristicUUIDString
     * @param onWriteCharacterListener Write CharacterListener
     */
    public void writeDataToCharacteristic(byte[] data, String serviceUUIDString,
                                          String characteristicUUIDString,
                                          onWriteCharacterListener onWriteCharacterListener) {
        this.writeDataToCharacteristic(data, UUID.fromString(serviceUUIDString),
                UUID.fromString(characteristicUUIDString),
                onWriteCharacterListener);
    }

    /**
     * Write data to bluetooth Characteristic
     *
     * @param data                     data
     * @param serviceUUID              serviceUUID
     * @param characteristicUUID       characteristicUUID
     * @param onWriteCharacterListener Write CharacterListener
     */
    public void writeDataToCharacteristic(byte[] data, UUID serviceUUID, UUID characteristicUUID,
                                          onWriteCharacterListener onWriteCharacterListener) {
        BluetoothGattService service = mBluetoothGatt.getService(serviceUUID);
        BluetoothGattCharacteristic characteristic = service.getCharacteristic(characteristicUUID);
        characteristic.setValue(data);
        mBluetoothLeService.writeCharacteristic(characteristic);
        if (onWriteCharacterListener != null)
            this.mOnWriteCharacterListener = onWriteCharacterListener;
    }

    /**
     * @return this
     */
    public QBLE unWriteChatacterListener() {
        mOnWriteCharacterListener = null;
        return this;
    }

    /**
     * close bluetoothGatt of bluetooth connection.Its will close whole bluetooth.
     */
    public void close() {
        if (mBluetoothLeService != null) {
            mBluetoothLeService.close();
        }
    }

    /**
     * Disconnects an existing connection or cancel a pending connection.
     */
    public void disConnect() {
        if (mBluetoothLeService != null) {
            mBluetoothLeService.disconnect();
        }
    }

    //
    //The following callback from BluetoothLEService
    //

    @Override
    public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
        if (newState == BluetoothProfile.STATE_CONNECTED) {

            mConnected = true;
            mBluetoothGatt = mBluetoothLeService.getBluetoothGatt();
            stopScanning();
            Log.d(TAG, "Connection State Change be connected!");
            if (mOnLeConnectListener != null) {
                Observable.just(3)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Action1<Integer>() {
                            @Override
                            public void call(Integer integer) {
                                mOnLeConnectListener.onConnectSuccess();
                            }
                        });
            }
        } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {

            mConnected = false;
            Log.d(TAG, "Connection State Change be disconnect!");
            if (mOnLeConnectListener != null) {
                Observable.just(2)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Action1<Integer>() {
                            @Override
                            public void call(Integer integer) {
                                mOnLeConnectListener.onConnectFailure();
                            }
                        });
            }
        }
    }

    @Override
    public void onServicesDiscovered(BluetoothGatt gatt, int status) {
        if (status == BluetoothGatt.GATT_SUCCESS) {

            ServicesDiscoveredBean.setBluetoothGatt(gatt);
            ServicesDiscoveredBean.setStatus(status);

            if (mOnLeConnectListener != null) {
                Observable.just(1)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Action1<Integer>() {
                            @Override
                            public void call(Integer integer) {
                                mOnLeConnectListener.onServicesDiscovered(
                                        ServicesDiscoveredBean.getBluetoothGatt(),
                                        ServicesDiscoveredBean.getStatus()
                                );
                            }
                        });

            }

            // displayGattService(mBluetoothLeService.getSupportedGattServices());
        } else {

            Log.e(TAG, "do not discovered the ble services");
        }
    }


    @Override
    public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
        if (status == BluetoothGatt.GATT_SUCCESS) {

            Log.d("debug", "onCharacteristicRead: " + FormatUtils.byte2HexString(characteristic.getValue()));
        } else if (status == BluetoothGatt.GATT_FAILURE) {

        }
    }

    @Override
    public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
        if (status == BluetoothGatt.GATT_SUCCESS) {

            if (mOnWriteCharacterListener != null) {
                mOnWriteCharacterListener.onSuccess(characteristic);
            }
        } else {

            if (mOnWriteCharacterListener != null) {
                mOnWriteCharacterListener.onFailure();
            }
        }
    }

    @Override
    public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {

        if (mOnNotificationListener != null) {
            Observable.just(characteristic)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1<BluetoothGattCharacteristic>() {
                        @Override
                        public void call(BluetoothGattCharacteristic characteristic) {
                            if (mOnNotificationListener != null) {
                                mOnNotificationListener.onSuccess(characteristic);
                            }
                        }
                    });
        }

    }

    @Override
    public void onDescriptorRead(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
        if (status == BluetoothGatt.GATT_SUCCESS) {


        } else if (status == BluetoothGatt.GATT_FAILURE) {

        }
    }

    @Override
    public void onDescriptorWrite(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
        if (status == BluetoothGatt.GATT_SUCCESS) {

        } else if (status == BluetoothGatt.GATT_FAILURE) {

        }

    }


}
