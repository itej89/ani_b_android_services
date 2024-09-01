package fouriermachines.anidiag.datatypes.Delegates;

import java.util.ArrayList;

import FrameworkInterface.PublicTypes.Constants.Actuator;
import FrameworkInterface.PublicTypes.EEPROMDetails;

public interface ISLDataRecieved {
    public  void  ISLReadRecieved(EEPROMDetails Details, ArrayList<Integer> Data);
    public  void  ISLWriteStatus(Boolean Status);
}
