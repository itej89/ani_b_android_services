package Framework.DataTypes.Delegates.UI;

import FrameworkInterface.PublicTypes.Machine;

public interface SystemInitializationUIConvey {
    public void ClearScanDeatils();
    public void ResetInitializing();

    public void Scanning();
    public void Binding();

    public void PauseApplication();
    public void ShutdownApplication();
    public void ResumeApplication();
    public void NewMachineFound(Machine Device);

    public void MachinePoweredOff();
    public void MachineDisconnected();
    public void MachineConected();
    public void MachineConnectionTimeout();

    public void MachineIsMovingTOMountPosition();
    public void MachineCheckingInitialProximity();
    public void MachinWaitingForMounting();
    public void MachineLoadKineticsService();
    public void  ShowBlankScreen();
    public void ShutdownRequest();

}
