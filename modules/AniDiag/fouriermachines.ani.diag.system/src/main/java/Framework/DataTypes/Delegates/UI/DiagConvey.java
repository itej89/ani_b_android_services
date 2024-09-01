package Framework.DataTypes.Delegates.UI;

import java.util.ArrayList;

import FrameworkInterface.PublicTypes.Constants.Actuator;
import FrameworkInterface.PublicTypes.EEPROMDetails;

public interface DiagConvey {
    public void RecievedEEPROMBytes(EEPROMDetails details, ArrayList<Integer> Data);
    public void ServoEEPROMBytes(Actuator actuator, EEPROMDetails details, ArrayList<Integer> Data);
    public  void  EEPROMWriteStatus(Boolean Status);
    public  void  ISLWriteStatus(Boolean Status);
    public void RecievedISLBytes(EEPROMDetails details, ArrayList<Integer> Data);
}
