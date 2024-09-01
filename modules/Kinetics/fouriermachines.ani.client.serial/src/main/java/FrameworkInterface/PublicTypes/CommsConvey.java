/**
 * Created by tej on 24/06/18.
 */
package FrameworkInterface.PublicTypes;

public interface CommsConvey {

    public void serialStataChanged(FrameworkInterface.PublicTypes.Constants.CommsStates State);

    public void newDeviceDiscovered(FrameworkInterface.PublicTypes.Device Device);

    public void stringRecieved(String Data);
}
