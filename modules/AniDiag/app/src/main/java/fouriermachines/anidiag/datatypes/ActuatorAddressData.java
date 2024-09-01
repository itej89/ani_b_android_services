package fouriermachines.anidiag.datatypes;

import FrameworkInterface.PublicTypes.Constants.Actuator;
import FrameworkInterface.PublicTypes.Constants.KineticsEEPROM;

public  class ActuatorAddressData
{
    public Actuator Type;
    public  String ActuatorName;
    public  Integer EEPromAddress;
    public  Integer EEPromByteCount;
    public  Byte Value = null;
    public  String DataInfo;
    public KineticsEEPROM.EEPROMParameter Parameter;
}