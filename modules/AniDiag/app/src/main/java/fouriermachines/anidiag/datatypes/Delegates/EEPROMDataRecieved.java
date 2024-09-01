package fouriermachines.anidiag.datatypes.Delegates;

import java.util.ArrayList;

import FrameworkInterface.PublicTypes.Constants.Actuator;
import FrameworkInterface.PublicTypes.EEPROMDetails;

public interface EEPROMDataRecieved {
    public  void  EEPROMDataRecieved(EEPROMDetails Details, ArrayList<Integer> Data);
    public  void  EEPROMWriteStatus(Boolean Status);
    public  void  ServoEEPROMDataRecieved(Actuator actuator, EEPROMDetails Details, ArrayList<Integer> Data);
}
