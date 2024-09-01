package Framework.DataTypes.Delegates;

import java.util.ArrayList;

import FrameworkInterface.PublicTypes.EEPROMDetails;

public interface ISLReadConvey {
    public  void  ISLReadRecieved(EEPROMDetails Details, ArrayList<Integer> Data);
}
