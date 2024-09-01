//
//  KineticRequestEasingCommand.swift
//  FourierMachines.Ani.Client.Kinetics
//
//  Created by Tej Kiran on 07/03/18.
//  Copyright © 2018 FourierMachines. All rights reserved.
//

package Framework.DataTypes.Parsers.RequestCommandParser;

import android.os.Parcel;

import java.util.ArrayList;

import Framework.DataTypes.Constants.CommandLabels;
import FrameworkInterface.PublicTypes.Config.MachineConfig;
import FrameworkInterface.PublicTypes.Constants.Actuator;

public class KineticsRequestServoEasing extends KineticsRequestForActuator
{
    public CommandLabels.EasingFunction EasingFunction;

    public void PostConstructor()
    {
        String Command = super.formCommand(new String[]{MachineConfig.Instance.MachineActuatorList.get(ActuatorType).Address.toString(), EasingFunction.toString()});

        Request = super.addDelimiters( Command);
    }

    public KineticsRequestServoEasing(CommandLabels.EasingFunction easing, Actuator actuatorType)
    {
        super(CommandLabels.CommandTypes.EAS);
        
        EasingFunction = easing;
        ActuatorType = actuatorType;

        PostConstructor();
    }
    
    public KineticsRequestServoEasing(String command)
    {
        super(CommandLabels.CommandTypes.EAS);
        String _command = super.removeDelimiters( command);
        ArrayList<String> contents = super.decomposeCommand( _command);
        if(contents.size() == 2)
        {
            Integer address = Integer.parseInt(contents.get(1));
            if(address != null)
            {
                ActuatorType = MachineConfig.Instance.getActuatorWith(address);
                EasingFunction = CommandLabels.EasingFunction.valueOf(contents.get(2));
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
        dest.writeInt(this.EasingFunction == null ? -1 : this.EasingFunction.ordinal());
    }

    protected KineticsRequestServoEasing(Parcel in) {
        super(in);
        int tmpEasingFunction = in.readInt();
        this.EasingFunction = tmpEasingFunction == -1 ? null : CommandLabels.EasingFunction.values()[tmpEasingFunction];
        PostConstructor();
    }

    public static final Creator<KineticsRequestServoEasing> CREATOR = new Creator<KineticsRequestServoEasing>() {
        @Override
        public KineticsRequestServoEasing createFromParcel(Parcel source) {
            return new KineticsRequestServoEasing(source);
        }

        @Override
        public KineticsRequestServoEasing[] newArray(int size) {
            return new KineticsRequestServoEasing[size];
        }
    };
}
