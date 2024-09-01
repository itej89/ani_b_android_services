//
//  KineticsRequestForActuator.swift
//  FourierMachines.Ani.Client.Kinetics
//
//  Created by Tej Kiran on 06/05/18.
//  Copyright © 2018 FourierMachines. All rights reserved.
//

package Framework.DataTypes.Parsers.RequestCommandParser;

import Framework.DataTypes.Constants.CommandLabels;
import FrameworkInterface.PublicTypes.Constants.Actuator;

public class KineticsRequestForActuator extends KineticsRequest
{
   public Actuator ActuatorType;
    
    public KineticsRequestForActuator(CommandLabels.CommandTypes requestType)
    {
        super(requestType);
    }
}
