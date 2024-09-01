//
//  KineticsRequestLockSignalStatus.swift
//  FourierMachines.Ani.Client.Kinetics
//
//  Created by Tej Kiran on 28/04/18.
//  Copyright © 2018 FourierMachines. All rights reserved.
//

package Framework.DataTypes.Parsers.RequestCommandParser;


import android.location.Address;
import android.os.Parcel;

import java.util.ArrayList;

import Framework.DataTypes.Constants.CommandLabels;
import FrameworkInterface.PublicTypes.Config.MachineConfig;
import FrameworkInterface.PublicTypes.Constants.Actuator;

public class KineticsRequestLockSignalStatus extends KineticsRequestForActuator
{
    public void PostConstructor()
    {
        String Command = super.formCommand(new String[]{MachineConfig.Instance.MachineActuatorList.get(ActuatorType).Address.toString()});

        Request = super.addDelimiters(Command);
    }

    public KineticsRequestLockSignalStatus(Actuator actuatorType)
    {
        super(CommandLabels.CommandTypes.LSA);
        
        ActuatorType = actuatorType;
        PostConstructor();
    }
    
    public KineticsRequestLockSignalStatus(String command)
    {
        super(CommandLabels.CommandTypes.LSA);
        
        String _command = super.removeDelimiters(command);
        ArrayList<String> contents = super.decomposeCommand( _command);
        
        if(contents.size() == 1)
        {
            Integer address = Integer.parseInt(contents.get(0));
            if(address != null)
            {
                ActuatorType = MachineConfig.Instance.getActuatorWith(address);
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

    protected KineticsRequestLockSignalStatus(Parcel in) {
        super(in);
        PostConstructor();
    }

    public static final Creator<KineticsRequestLockSignalStatus> CREATOR = new Creator<KineticsRequestLockSignalStatus>() {
        @Override
        public KineticsRequestLockSignalStatus createFromParcel(Parcel source) {
            return new KineticsRequestLockSignalStatus(source);
        }

        @Override
        public KineticsRequestLockSignalStatus[] newArray(int size) {
            return new KineticsRequestLockSignalStatus[size];
        }
    };
}
