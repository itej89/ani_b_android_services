//
//  KimneticRequestFrequencyCommand.swift
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

public class KineticsRequestFrequency extends KineticsRequestForActuator
{
    public Integer Frequency;
    
    public KineticsRequestFrequency(Integer frequency, Actuator actuatorType)
    {
        super(CommandLabels.CommandTypes.FRQ);

        Frequency = frequency;
        ActuatorType = actuatorType;

        String Command = super.formCommand(new String[]{MachineConfig.Instance.MachineActuatorList.get(ActuatorType).
        Address.toString(), (Frequency.toString())});

        Request = super.addDelimiters( Command);
    }
    
    public KineticsRequestFrequency(String command)
    {
        super(CommandLabels.CommandTypes.FRQ);
        
        String _command = super.removeDelimiters( command);
        ArrayList<String> contents = super.decomposeCommand( _command);
        if(contents.size() == 2)
        {
            Integer address = Integer.parseInt(contents.get(1));
            if(address != null)
            {
                ActuatorType = MachineConfig.Instance.getActuatorWith( address);
                Frequency = Integer.parseInt(contents.get(2));
            }
        }
    }
}
