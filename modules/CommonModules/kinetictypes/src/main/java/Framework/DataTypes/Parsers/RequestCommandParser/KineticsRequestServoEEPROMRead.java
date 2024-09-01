package Framework.DataTypes.Parsers.RequestCommandParser;

import android.os.Parcel;

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

    public void PostConstructor()
    {
        String Command = super.formCommand(new String[]{MachineConfig.Instance.MachineActuatorList.get(ActuatorType).
                Address.toString(), EEPROMAddress.toString(), (NoOfBytes.toString())});

        Request = super.addDelimiters(Command);
    }

    public KineticsRequestServoEEPROMRead(Actuator actuator, EEPROMDetails memoryLocation)
    {
        super(CommandLabels.CommandTypes.SEPR);

        ActuatorType = actuator;
        NoOfBytes = memoryLocation.NoOfBytes;
        EEPROMAddress = memoryLocation.Address;

        PostConstructor();
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


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeParcelable(this.MemoryLocation, flags);
        dest.writeInt(this.ActuatorType == null ? -1 : this.ActuatorType.ordinal());
        dest.writeValue(this.NoOfBytes);
        dest.writeValue(this.EEPROMAddress);
    }

    protected KineticsRequestServoEEPROMRead(Parcel in) {
        super(in);
        this.MemoryLocation = in.readParcelable(EEPROMDetails.class.getClassLoader());
        int tmpActuatorType = in.readInt();
        this.ActuatorType = tmpActuatorType == -1 ? null : Actuator.values()[tmpActuatorType];
        this.NoOfBytes = (Integer) in.readValue(Integer.class.getClassLoader());
        this.EEPROMAddress = (Integer) in.readValue(Integer.class.getClassLoader());

        PostConstructor();
    }

    public static final Creator<KineticsRequestServoEEPROMRead> CREATOR = new Creator<KineticsRequestServoEEPROMRead>() {
        @Override
        public KineticsRequestServoEEPROMRead createFromParcel(Parcel source) {
            return new KineticsRequestServoEEPROMRead(source);
        }

        @Override
        public KineticsRequestServoEEPROMRead[] newArray(int size) {
            return new KineticsRequestServoEEPROMRead[size];
        }
    };
}
