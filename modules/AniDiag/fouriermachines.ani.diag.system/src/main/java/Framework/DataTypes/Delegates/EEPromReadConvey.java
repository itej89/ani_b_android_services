package Framework.DataTypes.Delegates;

import java.util.ArrayList;

import FrameworkInterface.PublicTypes.EEPROMDetails;

public interface EEPromReadConvey {
    public  void  EEPROMReadRecieved(EEPROMDetails Details, ArrayList<Integer> Data);
}
