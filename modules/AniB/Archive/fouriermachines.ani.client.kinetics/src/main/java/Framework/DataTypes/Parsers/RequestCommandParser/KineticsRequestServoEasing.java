//
//  KineticRequestEasingCommand.swift
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

public class KineticsRequestServoEasing extends KineticsRequestForActuator
{
    public CommandLabels.EasingFunction EasingFunction;
    
    public KineticsRequestServoEasing(CommandLabels.EasingFunction easing, Actuator actuatorType)
    {
        super(CommandLabels.CommandTypes.EAS);
        
        EasingFunction = easing;
        ActuatorType = actuatorType;
        
        String Command = super.formCommand(new String[]{MachineConfig.Instance.MachineActuatorList.get(ActuatorType).Address.toString(), EasingFunction.toString()});
        
        Request = super.addDelimiters( Command);
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
}
