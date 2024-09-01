package FrameworkInterface.PublicTypes.Delegates;

import FrameworkInterface.PublicTypes.Constants.MachineCommsStates;
import FrameworkInterface.PublicTypes.Machine;

public interface KineticsCommsConvey
{
    public void commsStateChanged(MachineCommsStates State);

    public void newMachineFound(Machine Device);
}
