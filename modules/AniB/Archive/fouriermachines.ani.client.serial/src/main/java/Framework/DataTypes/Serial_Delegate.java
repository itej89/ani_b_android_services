package Framework.DataTypes;

import android.bluetooth.BluetoothDevice;

import Framework.DataTypes.Constants.Serial_Types;

public interface Serial_Delegate {
    public void RecievedString(String data);
    public void serialDidChangeState(Serial_Types.Serial_States State);
    public void Scanning_Device_Discovered(PortInfo Device);
}
