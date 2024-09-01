package FrameworkInterface.InterfaceImplementation;

import android.hardware.usb.UsbDevice;

import java.util.ArrayList;
import java.util.List;

import Framework.DataTypes.Constants.Serial_Types;
import Framework.DataTypes.GlobalContext;
import Framework.DataTypes.PortInfo;
import Framework.DataTypes.Serial_Delegate;
import Framework.Serial_Operations;
import FrameworkInterface.CommsAccess;
import FrameworkInterface.PublicTypes.Constants.CommsStates;
import FrameworkInterface.PublicTypes.Device;

public class SerialAccess implements Serial_Delegate, CommsAccess {

    List<PortInfo> Devices = new ArrayList<PortInfo>() ;

    FrameworkInterface.PublicTypes.CommsConvey notify_Comms;
    Serial_Operations Serial;

    public  SerialAccess()
    {
        Serial = new Serial_Operations(GlobalContext.context, this);

    }

    //Manadatory Call before anyother functionality call


    public void RecievedString(String data)
    {
        notify_Comms.stringRecieved(data);
    }

    public void serialDidChangeState(Serial_Types.Serial_States State)
    {
        switch (State)
        {
            case READY_TO_SCAN:
                notify_Comms.serialStataChanged(CommsStates.READY_TO_SCAN);
                break;
            case CONNECTED:
                notify_Comms.serialStataChanged(CommsStates.CONNECTED);
                break;
            case CONNECTION_TIMEOUT:
                notify_Comms.serialStataChanged(CommsStates.CONNECTION_TIMEOUT);
                break;
            case CONNECTING:
                notify_Comms.serialStataChanged(CommsStates.CONNECTING);
                break;
            case DISCONNECTING:
                notify_Comms.serialStataChanged(CommsStates.DISCONNECTING);
                break;
            case DISCONNECTED:
                notify_Comms.serialStataChanged(CommsStates.DISCONNECTED);
                break;
            case POWERED_OFF:
                notify_Comms.serialStataChanged(CommsStates.POWERED_OFF);
                break;
            case POWERED_ON:
                notify_Comms.serialStataChanged(CommsStates.POWERED_ON);
                break;
            default:
                break;
        }

    }

    public void Scanning_Device_Discovered(PortInfo Device)
    {
        int len=Devices.size();
        for(int i=0; i<len; i++) {
            if (Devices.get(i).port == Device.port) {
                return;
            }
        }
        Devices.add(Device);

        FrameworkInterface.PublicTypes.Device peripheral = new FrameworkInterface.PublicTypes.Device();
        peripheral.Name = Device.port;
        peripheral.UUID = Device.hardwareId;

        notify_Comms.newDeviceDiscovered(peripheral);
    }



    //CommsAccess Protocol Implementation
    public void InitializeComms()
    {
        Serial.InitializeComms();
    }

    public void SetCommsDelegate(FrameworkInterface.PublicTypes.CommsConvey delegate)
    {
        notify_Comms = delegate;
    }

    public void StartScan()
    {
        Devices.clear();
        Serial.StartScan();
    }

    public void  StopScan()
    {

        Serial.StopScan();
    }


    public void  ConnectToDevice(FrameworkInterface.PublicTypes.Device peripheral)
    {
        for (PortInfo device: Devices) {
            if(device.hardwareId == peripheral.UUID) {
                Serial.Connect(device);
                break;
            }
        }

    }

    public UsbDevice GetUSBSerialDevice()
    {
       return Serial.GetUSBSerialDevice();
    }

    public  Boolean IsConnectedToPeripheral()
    {
        return  Serial.IsConnected();
    }

    public  void DisconnectDevice()
    {
        Serial.Disconnect();
    }

    public void  SendString(String Data)
    {
        Serial.SendData(Data);
    }
    //End of CommsAccess Protocol Implementation
}
