//
//  KimneticRequestDelayCommand.swift
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

public class KineticsRequestDelay extends KineticsRequestForActuator
{
    public Integer Delay;
    
    public KineticsRequestDelay(Integer delay, Actuator actuatorType)
    {
        super(CommandLabels.CommandTypes.DEL);
        
        Delay = delay;
        ActuatorType = actuatorType;
        
        String Command = super.formCommand(new String[]{MachineConfig.Instance.MachineActuatorList.get(ActuatorType).
        Address.toString(), Delay.toString()});
        
        Request = super.addDelimiters( Command);
    }
    
    public KineticsRequestDelay(String command)
    {
        super(CommandLabels.CommandTypes.DEL);
        
        String _command = super.removeDelimiters( command);
        ArrayList<String> contents = super.decomposeCommand( _command);
        if(contents.size() == 2)
        {
            Integer address = Integer.parseInt(contents.get(1));
            if(address != null)
            {
                ActuatorType = MachineConfig.Instance.getActuatorWith( address);
                Delay = Integer.parseInt(contents.get(2));
            }
        }
    }
}
