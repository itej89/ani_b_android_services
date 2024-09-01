//
//  KimneticRequestInstantTriggerCommand.swift
//  FourierMachines.Ani.Client.Kinetics
//
//  Created by Tej Kiran on 07/03/18.
//  Copyright © 2018 FourierMachines. All rights reserved.
//

package Framework.DataTypes.Parsers.RequestCommandParser;

import Framework.DataTypes.Constants.CommandLabels;

public class KineticsRequestInstantTrigger extends KineticsRequest
{
    
    public KineticsRequestInstantTrigger()
    {
        super(CommandLabels.CommandTypes.ITRG);
        String Command  = super.formCommand(new String[]{});
        Request = super.addDelimiters(Command);
    }
    
    public KineticsRequestInstantTrigger(String command)
    {
        super(CommandLabels.CommandTypes.ITRG);
        super.removeDelimiters(command);
        
    }
}
