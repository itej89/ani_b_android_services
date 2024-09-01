//
//  KinectRequestServoSignalStatusCheck.swift
//  FourierMachines.Ani.Client.Kinetics
//
//  Created by Tej Kiran on 28/04/18.
//  Copyright © 2018 FourierMachines. All rights reserved.
//

package Framework.DataTypes.Parsers.RequestCommandParser;


import android.location.Address;
import android.widget.ArrayAdapter;

import java.util.ArrayList;

import Framework.DataTypes.Constants.CommandLabels;
import FrameworkInterface.PublicTypes.Config.MachineConfig;
import FrameworkInterface.PublicTypes.Constants.Actuator;

public class KineticsRequestServoSignalStatus extends KineticsRequestForActuator
{
    
    public KineticsRequestServoSignalStatus(Actuator actuatorType)
    {
        super(CommandLabels.CommandTypes.SAT);
        
        ActuatorType = actuatorType;
        
        String Command = super.formCommand(new String[]{MachineConfig.Instance.MachineActuatorList.get(ActuatorType).Address.toString()});
        
        Request = super.addDelimiters( Command);
    }
    
    public KineticsRequestServoSignalStatus(String command)
    {
        super(CommandLabels.CommandTypes.SAT);
        
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
}
