//
//  KimneticRequestConnectPowerCommand.swift
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

public class KineticsRequestConnectPower extends KineticsRequestForActuator
{
    public void PostConstructor()
    {
        String Command = super.formCommand(new String[]{MachineConfig.Instance.MachineActuatorList.get(ActuatorType).Address.toString()});
        Request = super.addDelimiters(Command);
    }

    public KineticsRequestConnectPower(Actuator actuatorType)
    {
        super(CommandLabels.CommandTypes.CPW);

        ActuatorType = actuatorType;

        PostConstructor();
    }
    
    public KineticsRequestConnectPower(String command)
    {
        super(CommandLabels.CommandTypes.CPW);
        
        String _command = super.removeDelimiters( command);
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

    protected KineticsRequestConnectPower(Parcel in) {
        super(in);
        PostConstructor();
    }

    public static final Creator<KineticsRequestConnectPower> CREATOR = new Creator<KineticsRequestConnectPower>() {
        @Override
        public KineticsRequestConnectPower createFromParcel(Parcel source) {
            return new KineticsRequestConnectPower(source);
        }

        @Override
        public KineticsRequestConnectPower[] newArray(int size) {
            return new KineticsRequestConnectPower[size];
        }
    };
}
