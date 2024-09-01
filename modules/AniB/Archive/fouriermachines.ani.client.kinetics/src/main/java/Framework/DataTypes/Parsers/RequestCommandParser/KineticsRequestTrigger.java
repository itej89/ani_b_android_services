//
//  KimneticRequestTriggerCommand.swift
//  FourierMachines.Ani.Client.Kinetics
//
//  Created by Tej Kiran on 07/03/18.
//  Copyright © 2018 FourierMachines. All rights reserved.
//

package Framework.DataTypes.Parsers.RequestCommandParser;

import Framework.DataTypes.Constants.CommandLabels;

public class KineticsRequestTrigger extends KineticsRequest
{
    
    public KineticsRequestTrigger()
    {
        super(CommandLabels.CommandTypes.TRG);
        String Command  = super.formCommand(new String[]{});
        Request = super.addDelimiters(Command);
    }
    
    public KineticsRequestTrigger(String command)
    {
        super(CommandLabels.CommandTypes.TRG);
        super.removeDelimiters(command);
        
    }
}
