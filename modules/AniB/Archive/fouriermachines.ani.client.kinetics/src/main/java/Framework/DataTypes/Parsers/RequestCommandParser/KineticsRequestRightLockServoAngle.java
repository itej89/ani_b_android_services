//
//  KineticsRequestRightLockServoAngle.swift
//  FourierMachines.Ani.Client.Kinetics
//
//  Created by Tej Kiran on 08/03/18.
//  Copyright © 2018 FourierMachines. All rights reserved.
//

package Framework.DataTypes.Parsers.RequestCommandParser;


import android.location.Address;

import java.util.ArrayList;

import Framework.DataTypes.Constants.CommandLabels;
import Framework.MachineCommandHelper;
import FrameworkInterface.PublicTypes.Config.MachineConfig;
import FrameworkInterface.PublicTypes.Constants.Actuator;

public class KineticsRequestRightLockServoAngle extends KineticsRequestForActuator
{
    Integer Angle;
    Integer PWM_VALUE;
    
    public KineticsRequestRightLockServoAngle(Integer angle, Actuator actuatorType)
    {
        super(CommandLabels.CommandTypes.RLK);
        
        Angle = angle;
        ActuatorType = actuatorType;

        MachineCommandHelper CommandHelper = new  MachineCommandHelper();
        PWM_VALUE = CommandHelper.ConvertAngleToPWMValue(Angle);
        String Command = super.formCommand(new String[]{MachineConfig.Instance.MachineActuatorList.get(ActuatorType).Address.toString(), PWM_VALUE.toString()});
        
        Request = super.addDelimiters(Command);
    }
    
    public KineticsRequestRightLockServoAngle(String command)
    {
        super(CommandLabels.CommandTypes.RLK);
        String _command = super.removeDelimiters(command);
        ArrayList<String> contents = super.decomposeCommand( _command);
        if(contents.size() == 2)
        {
            Integer address = Integer.parseInt(contents.get(1));
            if(address != null)
            {
                ActuatorType = MachineConfig.Instance.getActuatorWith(address);
                Angle = Integer.parseInt(contents.get(2));
            }
        }
    }
}
