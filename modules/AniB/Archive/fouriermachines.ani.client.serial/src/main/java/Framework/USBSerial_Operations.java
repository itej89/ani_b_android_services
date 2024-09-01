package Framework;

import android.content.Context;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbManager;
import android.os.Handler;
import android.util.Log;

import com.hoho.android.usbserial.driver.UsbSerialDriver;
import com.hoho.android.usbserial.driver.UsbSerialPort;
import com.hoho.android.usbserial.driver.UsbSerialProber;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import Framework.DataTypes.Constants.Serial_Types;
import Framework.DataTypes.GlobalContext;
import Framework.DataTypes.PortInfo;
import Framework.DataTypes.Serial_Delegate;

public class USBSerial_Operations {

    private final static String TAG = Serial_Operations.class.getSimpleName();

    byte buffer[] = new byte[100];

    public Serial_Delegate serial_delegate;

    private Context myContext;
    Serial_Types.Serial_States CURRENT_STATE = Serial_Types.Serial_States.UNKNOWN;

    private Handler mHandler = new Handler();


    private UsbSerialPort ConnectedDevice;
    private UsbSerialPort FTDIPort;
    private UsbDevice FTDIUSBDevice;
    UsbSerialDriver driver;
    UsbDeviceConnection connection;
    public  static  String PortUniqueName = "FTDI_ANI_232";

    public USBSerial_Operations(Context context, Serial_Delegate bt_Delegate) {
        myContext = GlobalContext.context;
        serial_delegate = bt_Delegate;
        FindSerialDevice();

    }

public void FindSerialDevice()
{
    // Find all available drivers from attached devices.
    UsbManager manager = (UsbManager) GlobalContext.context.getSystemService(Context.USB_SERVICE);
    List<UsbSerialDriver> availableDrivers = UsbSerialProber.getDefaultProber().findAllDrivers(manager);
    if (availableDrivers.isEmpty()) {
        return;
    }

    for(int i=0; i<availableDrivers.size(); i++)
    {
        driver = availableDrivers.get(i);
        if(driver.getDevice().getVendorId() == 0x0403 && driver.getDevice().getProductId() == 0x6001)
        {
            FTDIUSBDevice = driver.getDevice();

            break;
        }
    }

}

    public void  InitializeComms()  {

        UsbManager manager = (UsbManager) GlobalContext.context.getSystemService(Context.USB_SERVICE);

        connection = manager.openDevice(driver.getDevice());
        if (connection == null) {
            // You probably need to call UsbManager.requestPermission(driver.getDevice(), ..)
            return;
        }
        else
        {
            FTDIPort = driver.getPorts().get(0);
            FTDIUSBDevice = driver.getDevice();
        }
        CURRENT_STATE = Serial_Types.Serial_States.READY_TO_SCAN;
        serial_delegate.serialDidChangeState(CURRENT_STATE);
    }


    public UsbDevice GetUSBSerialDevice()
    {

        if(FTDIUSBDevice != null)
        {
            return FTDIUSBDevice;
        }
        else
        {
            return  null;
        }
    }



    Thread ScanThread;
    Boolean DoScanning;
    public void StartScan()
    {
        if(CURRENT_STATE != Serial_Types.Serial_States.SCANNING || CURRENT_STATE != Serial_Types.Serial_States.CONNECTED ||
                CURRENT_STATE != Serial_Types.Serial_States.CONNECTING)
        {

            PortInfo FTDIPortInfo = new PortInfo(PortUniqueName, "FTDI", String.valueOf(FTDIPort.getPortNumber()));


            final PortInfo[] Ports = new PortInfo[]{FTDIPortInfo};
            DoScanning = true;
            if(Ports.length >= 0)
            {


                ScanThread = new Thread(new Runnable() {

                    @Override
                    public void run() {
                        try {
                            Thread.sleep(100);
                            for (PortInfo p : Ports) {
                                if(DoScanning) {
                                    serial_delegate.Scanning_Device_Discovered(p);
                                }
                                else
                                {
                                    break;
                                }

                            }
                        }
                        catch (Exception ex)
                        {}
                    }
                });
                ScanThread.start();



                CURRENT_STATE = Serial_Types.Serial_States.SCANNING;
                serial_delegate.serialDidChangeState(CURRENT_STATE);


            }
            else
            {
                CURRENT_STATE = Serial_Types.Serial_States.POWERED_OFF;
                serial_delegate.serialDidChangeState(CURRENT_STATE);
            }
        }
    }

    public  void StopScan()
    {
        if(ScanThread != null)
        {
            DoScanning = false;
        }
        if(CURRENT_STATE == Serial_Types.Serial_States.SCANNING)
        {
            CURRENT_STATE = Serial_Types.Serial_States.READY_TO_SCAN;
            serial_delegate.serialDidChangeState(CURRENT_STATE);
        }
    }

    class SerialReadTask extends TimerTask {

        @Override
        public void run() {

            try {
                if (ConnectedDevice != null && ConnectedDevice.IsOpen()) {


                    int numBytesRead = ConnectedDevice.read(buffer, 0);
                    if(numBytesRead > 0) {
                        byte data[] = Arrays.copyOfRange(buffer, 0 , numBytesRead);
                        String rxData = new String(data);
                        serial_delegate.RecievedString(String.format("%s", rxData));
                        Log.d(TAG, String.format("Rx: %s", rxData));
                    }

                }
            } catch(Exception e){
                Log.d(TAG, "Unable to read data from Port : "+ e.getMessage());
                e.printStackTrace();
            }

//            CurrentActivity.activity.runOnUiThread(new Runnable(){
//
//                @Override
//                public void run() {
//
//
//                }});
       }

    }

    boolean IsReadSerial = false;
    boolean IsSerialReading = false;

    public void StartSerialReadLoop()
    {
        IsSerialReading = true;
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                while (IsReadSerial)
                {
                    try {
                        if (ConnectedDevice != null && ConnectedDevice.IsOpen()) {


                            int numBytesRead = ConnectedDevice.read(buffer, 0);
                            if(numBytesRead > 0) {
                                byte data[] = Arrays.copyOfRange(buffer, 0 , numBytesRead);
                                String rxData = new String(data);
                                serial_delegate.RecievedString(String.format("%s", rxData));
                                Log.d(TAG, String.format("Rx: %s", rxData));
                            }

                        }
                    } catch(Exception e){
                        Log.d(TAG, "Unable to read data from Port : "+ e.getMessage());
                        e.printStackTrace();
                    }

                    try {
                        Thread.sleep(1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                IsSerialReading = false;
            }});
            t.start();
    }


//    Timer SerialReadTimer;
//    USBSerial_Operations.SerialReadTask serialReadTask;
    public Boolean Connect(final PortInfo Device)
    {
        if(CURRENT_STATE != Serial_Types.Serial_States.CONNECTED && CURRENT_STATE != Serial_Types.Serial_States.CONNECTING) {

            CURRENT_STATE = Serial_Types.Serial_States.CONNECTING;
            serial_delegate.serialDidChangeState(CURRENT_STATE);

            if (Device == null) {
                Log.w(TAG, "Device not found.  Unable to connect.");
                return false;
            }




            //if need to connect to same device conntected to in previous connection
            if (ConnectedDevice != null && Device.hardwareId.equals(ConnectedDevice.getPortNumber())) {
                Log.d(TAG, "Trying to use an existing mBluetoothGatt for connection.");

                if (ConnectedDevice.IsOpen()) {
                    try {

                        if(!IsSerialReading) {
                            IsReadSerial = true;
                            StartSerialReadLoop();
                        }

//                            if(SerialReadTimer != null)
//                            {
//                                SerialReadTimer.cancel();
//                            }
//                            SerialReadTimer = new Timer();
//                            serialReadTask = new USBSerial_Operations.SerialReadTask();
//                            SerialReadTimer.schedule(serialReadTask, 0, 1);
                            CURRENT_STATE = Serial_Types.Serial_States.CONNECTED;
                            serial_delegate.serialDidChangeState(CURRENT_STATE);

                    }
                    catch (Exception ex)
                    {
                        Log.d(TAG, "Unable to open Port : "+ ex.getMessage());
                    }
                }

            }
            else {

                try {
                    FTDIPort.open(connection);
                    FTDIPort.setParameters(115200, 8, UsbSerialPort.STOPBITS_1, UsbSerialPort.PARITY_NONE);
//                    if(SerialReadTimer != null)
//                    {
//                        SerialReadTimer.cancel();
//                    }
//                    SerialReadTimer = new Timer();
//                    serialReadTask = new USBSerial_Operations.SerialReadTask();
//                    SerialReadTimer.schedule(serialReadTask, 0, 1);
                    if(!IsSerialReading) {
                        IsReadSerial = true;
                        StartSerialReadLoop();
                    }
                    CURRENT_STATE = Serial_Types.Serial_States.CONNECTED;
                    serial_delegate.serialDidChangeState(CURRENT_STATE);
                    ConnectedDevice =  FTDIPort;
                }
                catch (Exception e) {
                    Log.d(TAG, "Unable to create Port : "+ e.getMessage());

                    try {
                        ConnectedDevice = null;
                        FTDIPort.close();
                    }
                    catch (IOException eIO)
                    {
                        Log.d(TAG, "Unable to create Port : "+ eIO.getMessage());
                    }

                }

            }
            if(ConnectedDevice == null || (ConnectedDevice != null && !ConnectedDevice.IsOpen()))
            {
                CURRENT_STATE = Serial_Types.Serial_States.CONNECTION_TIMEOUT;
                serial_delegate.serialDidChangeState(CURRENT_STATE);
            }

        }
        return true;
    }





    public void Disconnect()
    {

        if(CURRENT_STATE != Serial_Types.Serial_States.DISCONNECTING || CURRENT_STATE != Serial_Types.Serial_States.DISCONNECTED) {
            CURRENT_STATE = Serial_Types.Serial_States.DISCONNECTING;
            serial_delegate.serialDidChangeState(CURRENT_STATE);
//            if(SerialReadTimer != null)
//            {
//                SerialReadTimer.cancel();
//            }
            IsReadSerial = false;
            while (IsSerialReading);
            if (ConnectedDevice != null && ConnectedDevice.IsOpen()) {
                try {
                    ConnectedDevice.close();
                    CURRENT_STATE = Serial_Types.Serial_States.DISCONNECTED;
                    serial_delegate.serialDidChangeState(CURRENT_STATE);
                }
                catch (Exception ex)
                {
                    Log.d(TAG, "Unable to close Port : "+ ex.getMessage());
                }
            }

        }
    }

    public Boolean IsConnected()
    {
        if(CURRENT_STATE == Serial_Types.Serial_States.CONNECTED){
            return true;
        }
        else
        {
            return false;
        }
    }



    //Method used to send data to peripheral
    public void SendData(String Data)
    {
        Log.d(TAG, String.format("Tx: %s", new String(Data)));
        final byte[] bytes = Data.getBytes(StandardCharsets.US_ASCII);
        if(CURRENT_STATE == Serial_Types.Serial_States.CONNECTED) {
            try {
                ConnectedDevice.write(bytes, 3000);
            }
            catch (Exception e)
            {
                Log.d(TAG, "Unable to send data : "+ e.getMessage());
            }

        }
    }

}




