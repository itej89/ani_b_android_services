//
//  KineticsRequestLockPowerStatus.swift
//  FourierMachines.Ani.Client.Kinetics
//
//  Created by Tej Kiran on 28/04/18.
//  Copyright © 2018 FourierMachines. All rights reserved.
//

package Framework.DataTypes.Parsers.RequestCommandParser;


import android.location.Address;
import android.os.Parcel;
import android.widget.ArrayAdapter;

import java.util.ArrayList;

import Framework.DataTypes.Constants.CommandLabels;
import Framework.MachineCommandHelper;
import FrameworkInterface.PublicTypes.Config.MachineConfig;
import FrameworkInterface.PublicTypes.Constants.Actuator;

public class KineticsRequestLockPowerStatus extends KineticsRequestForActuator
{
    public void PostConstructor()
    {
        String Command = super.formCommand(new String[]{MachineConfig.Instance.MachineActuatorList.get(ActuatorType).Address.toString()});
        Request = super.addDelimiters(Command);
    }

    public KineticsRequestLockPowerStatus(Actuator actuatorType)
    {
        super(CommandLabels.CommandTypes.LPW);
        
        ActuatorType = actuatorType;

        PostConstructor();
    }
    
    public KineticsRequestLockPowerStatus(String command)
    {
        super(CommandLabels.CommandTypes.LPW);
        
        String _command = super.removeDelimiters(command);
        ArrayList<String> contents = super.decomposeCommand(_command);
        
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

    protected KineticsRequestLockPowerStatus(Parcel in) {
        super(in);

        PostConstructor();
    }

    public static final Creator<KineticsRequestLockPowerStatus> CREATOR = new Creator<KineticsRequestLockPowerStatus>() {
        @Override
        public KineticsRequestLockPowerStatus createFromParcel(Parcel source) {
            return new KineticsRequestLockPowerStatus(source);
        }

        @Override
        public KineticsRequestLockPowerStatus[] newArray(int size) {
            return new KineticsRequestLockPowerStatus[size];
        }
    };
}
