//
//  KineticRequestInOutCommand.swift
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

public class KineticsRequestEasingInOut extends KineticsRequestForActuator
{
    public CommandLabels.EasingType EasingType;
    
    public KineticsRequestEasingInOut(CommandLabels.EasingType easingType,Actuator actuatorType)
    {
        super(CommandLabels.CommandTypes.INO);
        
        EasingType = easingType;
        ActuatorType = actuatorType;
        
        String Command = super.formCommand(new String[]{MachineConfig.Instance.MachineActuatorList.get(ActuatorType).
        Address.toString(), EasingType.toString()});
        
        Request = super.addDelimiters(Command);
    }
    
    public KineticsRequestEasingInOut(String command)
    {
        super(CommandLabels.CommandTypes.INO);
        String _command = super.removeDelimiters( command);
        ArrayList<String> contents = super.decomposeCommand( _command);

        if(contents.size() == 2)
        {
            Integer address = Integer.parseInt(contents.get(1));
            if(address != null)
            {
                ActuatorType = MachineConfig.Instance.getActuatorWith( address);
                EasingType = CommandLabels.EasingType.valueOf(contents.get(2));
            }
        }
    }
}
