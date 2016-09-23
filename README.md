#Android BluetoothLe library

This is the Android BluetoothLe library, which allows you to be very convenient to use Android's low-power Bluetooth API and you do not need to care about the specific details of the implementation. Currently the project is beginning to write. Later it will bring you great convenience. X:D

##How to use


### Using QinBluetoothLE in your application.

If you are building with Gradle, simply add the following line to the dependencies section of your build.gradle file:

    compile 'com.qindachang:QinBluetoothLe:0.0.1'

### Initialize

In your Application.java or MainActivity.java ,first initialize:

    QinBluetoothManager.initialize(this);

###Scan BluetoothLe

####① Just scan

	QBLE.getInstance().setOnLeScanListener(new onLeScanListener() {
	            @Override
	            public void onScanResult(BluetoothDevice bluetoothDevice, int rssi, byte[] scanRecord) {
	                
	            }
	
	            @Override
	            public void onCompleted() {
	                
	            }
	        }).doScan();

####② Scan with ServiceUUID

        QBLE.getInstance().setOnLeScanListener(new onLeScanListener() {
            @Override
            public void onScanResult(BluetoothDevice bluetoothDevice, int rssi, byte[] scanRecord) {
                
            }

            @Override
            public void onCompleted() {
                
            }
        }).doScanWithServiceUUID(UUID);

###Connect BluetoothLe

	QBLE.getInstance()
	                .setAuto(true)//is auto connect the bluetooth,will Connect automatically when disconnected if true .
	                .withBluetoothDevice(mBluetoothDevice) //Device you want to connect to
	                .setOnLeConnectListener(new onLeConnectListener() {
	                    @Override
	                    public void onConnectSuccess() {
	                        
	                    }
	
	                    @Override
	                    public void onConnectFailure() {
	                        
	                    }
	
	                    @Override
	                    public void onServicesDiscovered(BluetoothGatt gatt, int status) {
	                        
	                    }
	                }).doConnect();

###Get bluetooth connected status

	QBLE.getInstance().getConnected();

###Write data to Characteristic

	QBLE.getInstance().writeDataToCharacteristic(byte[] data, UUID_SERVICE, UUID_CHARACTER_WRITE, new onWriteCharacterListener() {
	                @Override
	                public void onSuccess(BluetoothGattCharacteristic characteristic) {
	                    
	                }
	
	                @Override
	                public void onFailure() {
	
	                }
	            });

####or

	QBLE.getInstance().writeDataToCharacteristic(byte[] data, UUID_SERVICE, UUID_CHARACTER_WRITE);

### Enable characteristic notification

if you just wanna to receive one notification form characteristic. you can do like this.

	QBLE.getInstance().enableCharacteristicNotification(
	                boolean enable,
	                UUID_SERVICE,
	                UUID_CHARACTER,
	                new onNotificationListener() {
	                    @Override
	                    public void onSuccess(BluetoothGattCharacteristic characteristic) {
	                        
	                    }
	
	                    @Override
	                    public void onFailure() {
	
	                    }
	                });

or you wanna to receive many notificationssssss, you can do like the following code.

such as :

open heart rate characteristic notification

	QBLE.getInstance().enableCharacteristicNotification(
	                enable,
	                UUID_SERVICE,
	                HEART_RATE_UUID,
	                null);

the next ,open ecg characteristic notification

	QBLE.getInstance().enableCharacteristicNotification(
	                enable,
	                UUID_SERVICE,
	                HEART_ECG_UUID,
	                null);

finally, receive all notification

	QBLE.getInstance().setOnNotificationListener(new onNotificationListener() {
	            @Override
	            public void onSuccess(BluetoothGattCharacteristic characteristic) {
	                if (characteristic.getUuid().toString().equals(HEART_RATE_UUID)) {
	                    
	                } else if (characteristic.getUuid().toString().equals(HEART_ECG_UUID)) {
	                   
	                } 
	            }
	
	            @Override
	            public void onFailure() {
	
	            }
	        });




At present, the library can do the function of these, I will continue to improve it, and add the OTA. If I can help you, please give me a start.  X:D