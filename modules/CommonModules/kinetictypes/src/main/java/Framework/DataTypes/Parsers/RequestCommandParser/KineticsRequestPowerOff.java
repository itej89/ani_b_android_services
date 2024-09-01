package Framework.DataTypes.Parsers.RequestCommandParser;

import android.os.Parcel;

import java.util.ArrayList;

import Framework.DataTypes.Constants.CommandLabels;
import FrameworkInterface.PublicTypes.Config.MachineConfig;

public class KineticsRequestPowerOff extends KineticsRequest
        {

public void PostConstructor()
{
        String Command  = super.formCommand(new String[]{});
        Request = super.addDelimiters( Command);
}

public KineticsRequestPowerOff()
        {
        super(CommandLabels.CommandTypes.POFF);
        PostConstructor();
        }

public KineticsRequestPowerOff(String command)
        {
        super( CommandLabels.CommandTypes.POFF);

        super.removeDelimiters(command);

        }


@Override
public int describeContents() {
        return 0;
}

@Override
public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
}

protected KineticsRequestPowerOff(Parcel in) {
                super(in);
                PostConstructor();
        }

public static final Creator<KineticsRequestPowerOff> CREATOR = new Creator<KineticsRequestPowerOff>() {
                @Override
                public KineticsRequestPowerOff createFromParcel(Parcel source) {
                        return new KineticsRequestPowerOff(source);
                }

                @Override
                public KineticsRequestPowerOff[] newArray(int size) {
                        return new KineticsRequestPowerOff[size];
                }
        };

        }




