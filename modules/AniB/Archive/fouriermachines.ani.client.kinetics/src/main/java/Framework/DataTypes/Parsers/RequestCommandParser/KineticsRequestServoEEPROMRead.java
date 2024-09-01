package Framework.DataTypes.Parsers.RequestCommandParser;

import java.util.ArrayList;

import Framework.DataTypes.Constants.CommandLabels;
import FrameworkInterface.PublicTypes.Config.MachineConfig;
import FrameworkInterface.PublicTypes.Constants.Actuator;
import FrameworkInterface.PublicTypes.EEPROMDetails;

//~SEPR#6#0#2: Read 2 bytes from EEPROM address 0 of servo address 6,
public class KineticsRequestServoEEPROMRead extends KineticsRequest
{
    EEPROMDetails MemoryLocation;
    Actuator ActuatorType;
    Integer NoOfBytes;
    Integer EEPROMAddress;

    public KineticsRequestServoEEPROMRead(Actuator actuator, EEPROMDetails memoryLocation)
    {
        super(CommandLabels.CommandTypes.SEPR);

        ActuatorType = actuator;
        NoOfBytes = memoryLocation.NoOfBytes;
        EEPROMAddress = memoryLocation.Address;

        String Command = super.formCommand(new String[]{MachineConfig.Instance.MachineActuatorList.get(ActuatorType).
                Address.toString(), EEPROMAddress.toString(), (NoOfBytes.toString())});

        Request = super.addDelimiters(Command);
    }

    public KineticsRequestServoEEPROMRead(String command)
    {
        super(CommandLabels.CommandTypes.SEPR);
        String _command = super.removeDelimiters(command);
        ArrayList<String> contents = super.decomposeCommand( _command);
        if(contents.size() == 3)
        {
            Integer address = Integer.parseInt(contents.get(0));
            if(address != null)
            {
                ActuatorType = MachineConfig.Instance.getActuatorWith(address);
            }
            EEPROMAddress = Integer.parseInt(contents.get(1));
            NoOfBytes = Integer.parseInt(contents.get(2));

            MemoryLocation = new EEPROMDetails(EEPROMAddress, NoOfBytes);

        }
    }
}
