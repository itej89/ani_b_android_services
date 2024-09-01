package Framework.DataTypes.Delegates.UI;

import android.hardware.usb.UsbDevice;

import FrameworkInterface.PublicTypes.Machine;

public interface UIMAINConvey {
    public void AppStarted();
    public void UserSelectedAMachine(Machine Device);
    public boolean IsInitialized();

}
