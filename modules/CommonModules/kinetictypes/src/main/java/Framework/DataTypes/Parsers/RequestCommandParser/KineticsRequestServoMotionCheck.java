//
//  KineticRequestServoMotionCheckCommand.swift
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

public class KineticsRequestServoMotionCheck extends KineticsRequestForActuator
{
    public void PostConstructor()
    {
        String Command = super.formCommand(new String[]{MachineConfig.Instance.MachineActuatorList.get(ActuatorType).Address.toString()});

        Request = super.addDelimiters(Command);
    }

    public KineticsRequestServoMotionCheck(Actuator actuatorType)
    {
        super(CommandLabels.CommandTypes.SMV);
        
        ActuatorType = actuatorType;
        PostConstructor();
    }
    
    public KineticsRequestServoMotionCheck(String command)
    {
        super(CommandLabels.CommandTypes.SMV);
        
        String _command = super.removeDelimiters(command);
        ArrayList<String> contents = super.decomposeCommand( _command);
        
        if(contents.size() == 1)
        {
            Integer address = Integer.parseInt(contents.get(0));
            if(address != null)
            {
                ActuatorType = MachineConfig.Instance.getActuatorWith( address);
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
    }

    protected KineticsRequestServoMotionCheck(Parcel in) {
        super(in);
        PostConstructor();
    }

    public static final Creator<KineticsRequestServoMotionCheck> CREATOR = new Creator<KineticsRequestServoMotionCheck>() {
        @Override
        public KineticsRequestServoMotionCheck createFromParcel(Parcel source) {
            return new KineticsRequestServoMotionCheck(source);
        }

        @Override
        public KineticsRequestServoMotionCheck[] newArray(int size) {
            return new KineticsRequestServoMotionCheck[size];
        }
    };
}
