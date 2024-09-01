//
//  KimneticRequestVelocityCommand.swift
//  FourierMachines.Ani.Client.Kinetics
//
//  Created by Tej Kiran on 07/03/18.
//  Copyright © 2018 FourierMachines. All rights reserved.
//

package Framework.DataTypes.Parsers.RequestCommandParser;

import java.util.ArrayList;

import Framework.DataTypes.Constants.CommandLabels;
import FrameworkInterface.PublicTypes.Config.MachineConfig;
import FrameworkInterface.PublicTypes.Constants.Actuator;

public class KineticsRequestVelocity extends KineticsRequestForActuator
{
    public Integer Velocity;
    
    public KineticsRequestVelocity(Integer velocity, Actuator actuatorType)
    {
        super(CommandLabels.CommandTypes.VEL);
        
        Velocity = velocity;
        ActuatorType = actuatorType;
        
        String Command = super.formCommand(new String[]{MachineConfig.Instance.MachineActuatorList.get(ActuatorType).Address.toString(), (Velocity.toString())});
        
        Request = super.addDelimiters( Command);
    }
    
    public KineticsRequestVelocity(String command)
    {
        super(CommandLabels.CommandTypes.VEL);
        
        String _command = super.removeDelimiters( command);
        ArrayList<String> contents = super.decomposeCommand( _command);
        if(contents.size() == 2)
        {
            Integer address = Integer.parseInt(contents.get(1));
            if(address != null)
            {
                ActuatorType = MachineConfig.Instance.getActuatorWith( address);
                Velocity = Integer.parseInt(contents.get(2));
            }
        }
    }
}
