package FrameworkInterface;

import android.hardware.usb.UsbDevice;

public interface CommsAccess {

    public void SetCommsDelegate(FrameworkInterface.PublicTypes.CommsConvey delegate);
    public void InitializeComms();
    public void StartScan();
    public void StopScan();
    public void ConnectToDevice(FrameworkInterface.PublicTypes.Device peripheral);
    public Boolean IsConnectedToPeripheral();

    public void DisconnectDevice();

    public UsbDevice GetUSBSerialDevice();


    public void SendString(String Data);
}
