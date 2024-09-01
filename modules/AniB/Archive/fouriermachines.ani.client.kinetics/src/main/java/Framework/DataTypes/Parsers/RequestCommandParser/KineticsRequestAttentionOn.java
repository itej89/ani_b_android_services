//
//  KimneticRequestAttentionOnCommand.swift
//  FourierMachines.Ani.Client.Kinetics
//
//  Created by Tej Kiran on 07/03/18.
//  Copyright © 2018 FourierMachines. All rights reserved.
//

package Framework.DataTypes.Parsers.RequestCommandParser;

import Framework.DataTypes.Constants.CommandLabels;

public class KineticsRequestAttentionOn extends KineticsRequest
{
    
    public KineticsRequestAttentionOn()
    {
        super(CommandLabels.CommandTypes.VEN);
        
        String Command = super.formCommand(new String[]{});
        
        Request = super.addDelimiters(Command);
    }
    
    public KineticsRequestAttentionOn(String command)
    {
        super(CommandLabels.CommandTypes.VEN);
        
        super.removeDelimiters(command);
        
    }
}
