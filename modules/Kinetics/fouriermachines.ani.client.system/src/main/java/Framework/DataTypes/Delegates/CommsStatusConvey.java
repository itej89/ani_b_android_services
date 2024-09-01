package Framework.DataTypes.Delegates;

import FrameworkInterface.PublicTypes.Constants.MachineCommsStates;
import FrameworkInterface.PublicTypes.Machine;

public interface CommsStatusConvey {
    public void commsStateChanged(MachineCommsStates State);
    public void newMachineFound(Machine Device);
}
