package Framework.DataTypes.Delegates;

import java.util.ArrayList;

import FrameworkInterface.PublicTypes.Constants.Actuator;
import FrameworkInterface.PublicTypes.EEPROMDetails;

public interface ServoEEPromReadConvey {
    public  void  ServoEEPROMReadRecieved(Actuator actuator, EEPROMDetails Details, ArrayList<Integer> Data);
}
