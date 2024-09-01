//
//  KineticRequestInOutCommand.swift
//  FourierMachines.Ani.Client.Kinetics
//
//  Created by Tej Kiran on 07/03/18.
//  Copyright © 2018 FourierMachines. All rights reserved.
//

package Framework.DataTypes.Parsers.RequestCommandParser;

import android.location.Address;
import android.os.Parcel;

import java.util.ArrayList;

import Framework.DataTypes.Constants.CommandLabels;
import FrameworkInterface.PublicTypes.Config.MachineConfig;
import FrameworkInterface.PublicTypes.Constants.Actuator;

public class KineticsRequestEasingInOut extends KineticsRequestForActuator
{
    public CommandLabels.EasingType EasingType;

    public void PostConstructor()
    {
        String Command = super.formCommand(new String[]{MachineConfig.Instance.MachineActuatorList.get(ActuatorType).
                Address.toString(), EasingType.toString()});

        Request = super.addDelimiters(Command);
    }

    public KineticsRequestEasingInOut(CommandLabels.EasingType easingType,Actuator actuatorType)
    {
        super(CommandLabels.CommandTypes.INO);
        
        EasingType = easingType;
        ActuatorType = actuatorType;

        PostConstructor();
    }
    
    public KineticsRequestEasingInOut(String command)
    {
        super(CommandLabels.CommandTypes.INO);
        String _command = super.removeDelimiters( command);
        ArrayList<String> contents = super.decomposeCommand( _command);

        if(contents.size() == 2)
        {
            Integer address = Integer.parseInt(contents.get(1));
            if(address != null)
            {
                ActuatorType = MachineConfig.Instance.getActuatorWith( address);
                EasingType = CommandLabels.EasingType.valueOf(contents.get(2));
            }
        }
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeInt(this.EasingType == null ? -1 : this.EasingType.ordinal());
    }

    protected KineticsRequestEasingInOut(Parcel in) {
        super(in);
        int tmpEasingType = in.readInt();
        this.EasingType = tmpEasingType == -1 ? null : CommandLabels.EasingType.values()[tmpEasingType];

        PostConstructor();
    }

    public static final Creator<KineticsRequestEasingInOut> CREATOR = new Creator<KineticsRequestEasingInOut>() {
        @Override
        public KineticsRequestEasingInOut createFromParcel(Parcel source) {
            return new KineticsRequestEasingInOut(source);
        }

        @Override
        public KineticsRequestEasingInOut[] newArray(int size) {
            return new KineticsRequestEasingInOut[size];
        }
    };
}
