//
//  KimneticRequestAttachLockServoCommand.swift
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
import Framework.MachineCommandHelper;
import FrameworkInterface.PublicTypes.Config.MachineConfig;
import FrameworkInterface.PublicTypes.Constants.Actuator;

public class KineticsRequestAttachLockServo extends KineticsRequestForActuator
{

    public void PostConstructor()
    {
        String Command = super.formCommand(new String[]{MachineConfig.Instance.MachineActuatorList.get(ActuatorType).
                Address.toString()});

        Request = super.addDelimiters(Command);
    }

    public KineticsRequestAttachLockServo(Actuator actuatorType)
    {
        super(CommandLabels.CommandTypes.LAT);
        
        ActuatorType = actuatorType;

        PostConstructor();

    }
    
    public KineticsRequestAttachLockServo(String command)
    {
        super(CommandLabels.CommandTypes.LAT);
        
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

    protected KineticsRequestAttachLockServo(Parcel in) {
        super(in);

        PostConstructor();
    }

    public static final Creator<KineticsRequestAttachLockServo> CREATOR = new Creator<KineticsRequestAttachLockServo>() {
        @Override
        public KineticsRequestAttachLockServo createFromParcel(Parcel source) {
            return new KineticsRequestAttachLockServo(source);
        }

        @Override
        public KineticsRequestAttachLockServo[] newArray(int size) {
            return new KineticsRequestAttachLockServo[size];
        }
    };
}
