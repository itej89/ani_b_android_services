package Framework;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.os.Handler;
import android.util.Log;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Timer;
import java.util.TimerTask;

import Framework.DataTypes.Constants.Serial_Types;
import Framework.DataTypes.GlobalContext;
import Framework.DataTypes.Serial_Delegate;
import FrameworkInterface.PublicTypes.Device;
//import serial.PortInfo;
//import serial.Serial;
//import serial.SerialIOException;

public class Serial_Operations extends USBSerial_Operations {

    public Serial_Operations(Context context, Serial_Delegate bt_Delegate) {
        super(context,bt_Delegate);
    }

//        private final static String TAG = Serial_Operations.class.getSimpleName();
//
//        public Serial_Delegate serial_delegate;
//        private Serial ConnectedDevice;
//
//        private Context myContext;
//        Serial_Types.Serial_States CURRENT_STATE = Serial_Types.Serial_States.UNKNOWN;
//
//
//
//        private Handler mHandler = new Handler();
//
//
//
//        /**
//         * Initializes a reference to the local Bluetooth adapter.
//         *
//         * @return Return true if the initialization is successful.
//         */
//        public Serial_Operations(Context context, Serial_Delegate bt_Delegate) {
//
//            try {
//                Process p = Runtime.getRuntime().exec(new String[]{"su", "-c", "/system/bin/sh -c \"chmod 666 /dev/ttyUSB0\""});
//               // Process p = Runtime.getRuntime().exec(new String[]{"/system/bin/sh -c \"chmod 666 /dev/ttyUSB0\""});
//            }
//            catch (Exception e)
//            {
//
//            }
//            myContext = GlobalContext.context;
//            serial_delegate = bt_Delegate;
//
//
//
//        }
//
//        public void  InitializeComms()  {
//
//            CURRENT_STATE = Serial_Types.Serial_States.READY_TO_SCAN;
//            serial_delegate.serialDidChangeState(CURRENT_STATE);
//        }
//
//
//
//    Thread ScanThread;
//    Boolean DoScanning;
//        public void StartScan()
//        {
//            if(CURRENT_STATE != Serial_Types.Serial_States.SCANNING || CURRENT_STATE != Serial_Types.Serial_States.CONNECTED ||
//                    CURRENT_STATE != Serial_Types.Serial_States.CONNECTING)
//            {
//               final PortInfo[] Ports = Serial.listPorts();
//                DoScanning = true;
//                if(Ports.length >= 0)
//                {
//
//
//                    ScanThread = new Thread(new Runnable() {
//
//                        @Override
//                        public void run() {
//                            try {
//                                Thread.sleep(100);
//                                for (PortInfo p : Ports) {
//                                    if(DoScanning) {
//                                        serial_delegate.Scanning_Device_Discovered(p);
//                                    }
//                                    else
//                                    {
//                                        break;
//                                    }
//
//                                }
//                            }
//                            catch (Exception ex)
//                            {}
//                        }
//                    });
//                    ScanThread.start();
//
//
//
//                    CURRENT_STATE = Serial_Types.Serial_States.SCANNING;
//                    serial_delegate.serialDidChangeState(CURRENT_STATE);
//
//
//                }
//                else
//                {
//                    CURRENT_STATE = Serial_Types.Serial_States.POWERED_OFF;
//                    serial_delegate.serialDidChangeState(CURRENT_STATE);
//                }
//            }
//        }
//
//        public  void StopScan()
//        {
//            if(ScanThread != null)
//            {
//                DoScanning = false;
//            }
//            if(CURRENT_STATE == Serial_Types.Serial_States.SCANNING)
//            {
//                    CURRENT_STATE = Serial_Types.Serial_States.READY_TO_SCAN;
//                    serial_delegate.serialDidChangeState(CURRENT_STATE);
//            }
//        }
//
//
//
//
//    class SerialReadTask extends TimerTask {
//
//        @Override
//        public void run() {
//
//            CurrentActivity.activity.runOnUiThread(new Runnable(){
//
//                @Override
//                public void run() {
//                    try {
//                        if (ConnectedDevice != null && ConnectedDevice.isOpen()) {
//                            int AvailableDataLength = ConnectedDevice.available();
//                            if (AvailableDataLength > 0) {
//
//                                byte[] data = ConnectedDevice.read();
//                                String rxData =   new String(data);
//                                serial_delegate.RecievedString(String.format("%s", rxData));
//                                Log.d(TAG, String.format("Rx: %s", rxData));
//                            }
//                        }
//                    } catch(SerialIOException e){
//                        Log.d(TAG, "Unable to read data from Port : "+ e.getMessage());
//                        e.printStackTrace();
//                    }
//
//                }});
//        }
//
//    }
//    Timer SerialReadTimer;
//    SerialReadTask serialReadTask;
//        public Boolean Connect(final PortInfo Device)
//        {
//            if(CURRENT_STATE != Serial_Types.Serial_States.CONNECTED && CURRENT_STATE != Serial_Types.Serial_States.CONNECTING) {
//
//                CURRENT_STATE = Serial_Types.Serial_States.CONNECTING;
//                serial_delegate.serialDidChangeState(CURRENT_STATE);
//
//                if (Device == null) {
//                    Log.w(TAG, "Device not found.  Unable to connect.");
//                    return false;
//                }
//
//
//
//
//                //if need to connect to same device conntected to in previous connection
//                if (ConnectedDevice != null && Device.port.equals(ConnectedDevice.getPort())) {
//                    Log.d(TAG, "Trying to use an existing mBluetoothGatt for connection.");
//
//                    if (ConnectedDevice.isValid() && ! ConnectedDevice.isOpen()) {
//                       try {
//                           ConnectedDevice.open();
//                           if(ConnectedDevice.isOpen()) {
//                               if(SerialReadTimer != null)
//                               {
//                                   SerialReadTimer.cancel();
//                               }
//                               SerialReadTimer = new Timer();
//                               serialReadTask = new SerialReadTask();
//                               SerialReadTimer.schedule(serialReadTask, 0, 1);
//                               CURRENT_STATE = Serial_Types.Serial_States.CONNECTED;
//                               serial_delegate.serialDidChangeState(CURRENT_STATE);
//                           }
//                       }
//                       catch (Exception ex)
//                       {
//                           Log.d(TAG, "Unable to open Port : "+ ex.getMessage());
//                       }
//                    }
//
//                }
//                else {
//                    PortInfo[] Ports = Serial.listPorts();
//                    for(PortInfo port :Ports)
//                    {
//                        if(port.port.toString().equals(Device.port.toString()))
//                        {
//                            try {
//                                ConnectedDevice = new Serial.Builder(port.port, Serial.BAUDRATE_115200).create();
//                                ConnectedDevice.open();
//                                if(ConnectedDevice.isOpen()) {
//                                    if(SerialReadTimer != null)
//                                    {
//                                        SerialReadTimer.cancel();
//                                    }
//                                    SerialReadTimer = new Timer();
//                                    serialReadTask = new SerialReadTask();
//                                    SerialReadTimer.schedule(serialReadTask, 0, 1);
//                                    CURRENT_STATE = Serial_Types.Serial_States.CONNECTED;
//                                    serial_delegate.serialDidChangeState(CURRENT_STATE);
//                                }
//                                break;
//                            }
//                            catch (Exception ex)
//                            {
//                                Log.d(TAG, "Unable to create Port : "+ ex.getMessage());
//                                break;
//                            }
//                        }
//                    }
//
//                }
//                if(ConnectedDevice == null || (ConnectedDevice != null && !ConnectedDevice.isValid()) || (ConnectedDevice != null && !ConnectedDevice.isOpen()))
//                {
//                    CURRENT_STATE = Serial_Types.Serial_States.CONNECTION_TIMEOUT;
//                    serial_delegate.serialDidChangeState(CURRENT_STATE);
//                }
//
//            }
//            return true;
//        }
//
//
//
//
//
//        public void Disconnect()
//        {
//
//            if(CURRENT_STATE != Serial_Types.Serial_States.DISCONNECTING || CURRENT_STATE != Serial_Types.Serial_States.DISCONNECTED) {
//                CURRENT_STATE = Serial_Types.Serial_States.DISCONNECTING;
//                serial_delegate.serialDidChangeState(CURRENT_STATE);
//                if(SerialReadTimer != null)
//                {
//                    SerialReadTimer.cancel();
//                }
//                if (ConnectedDevice != null && ConnectedDevice.isValid() && ConnectedDevice.isOpen()) {
//                    try {
//                        ConnectedDevice.close();
//                        CURRENT_STATE = Serial_Types.Serial_States.DISCONNECTED;
//                        serial_delegate.serialDidChangeState(CURRENT_STATE);
//                    }
//                    catch (Exception ex)
//                    {
//                        Log.d(TAG, "Unable to close Port : "+ ex.getMessage());
//                    }
//                }
//
//            }
//        }
//
//        public Boolean IsConnected()
//        {
//            if(CURRENT_STATE == Serial_Types.Serial_States.CONNECTED){
//                return true;
//            }
//            else
//            {
//                return false;
//            }
//        }
//
//
//
//        //Method used to send data to peripheral
//        public void SendData(String Data)
//        {
//            Log.d(TAG, String.format("Tx: %s", new String(Data)));
//            final byte[] bytes = Data.getBytes(StandardCharsets.US_ASCII);
//            if(CURRENT_STATE == Serial_Types.Serial_States.CONNECTED) {
//                try {
//                    ConnectedDevice.write(bytes, bytes.length);
//                }
//                catch (Exception e)
//                {
//                    Log.d(TAG, "Unable to send data : "+ e.getMessage());
//                }
//
//            }
//        }

    }


