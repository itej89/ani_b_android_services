//
//  KimneticRequestDettachServoCommand.swift
//  FourierMachines.Ani.Client.Kinetics
//
//  Created by Tej Kiran on 07/03/18.
//  Copyright © 2018 FourierMachines. All rights reserved.
//

package Framework.DataTypes.Parsers.RequestCommandParser;

import android.location.Address;

import java.util.ArrayList;

import Framework.DataTypes.Constants.CommandLabels;
import FrameworkInterface.PublicTypes.Config.MachineConfig;
import FrameworkInterface.PublicTypes.Constants.Actuator;

public class KineticsRequestDettachServo extends KineticsRequestForActuator
{
    
    public KineticsRequestDettachServo(Actuator actuatorType)
    {
        super(CommandLabels.CommandTypes.DTC);
        
        ActuatorType = actuatorType;
        
        String Command = super.formCommand(new String[]{ MachineConfig.Instance.MachineActuatorList.get(ActuatorType).
        Address.toString()});
        
        Request = super.addDelimiters( Command);
    }
    
    public KineticsRequestDettachServo(String command)
    {
        super(CommandLabels.CommandTypes.DTC);
        
        String _command = super.removeDelimiters( command);
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
}
