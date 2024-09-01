//
//  KimneticRequestAttentionOffCommand.swift
//  FourierMachines.Ani.Client.Kinetics
//
//  Created by Tej Kiran on 07/03/18.
//  Copyright © 2018 FourierMachines. All rights reserved.
//

package Framework.DataTypes.Parsers.RequestCommandParser;

import Framework.DataTypes.Constants.CommandLabels;

public class KineticsRequestAttentionOff extends KineticsRequest
{
    public KineticsRequestAttentionOff()
    {
        super(CommandLabels.CommandTypes.VNO);
        
        String Command = super.formCommand(new String[]{});
        
        Request = super.addDelimiters(Command);
    }
    
    public KineticsRequestAttentionOff(String command)
    {
        super(CommandLabels.CommandTypes.VNO);
        
         super.removeDelimiters(command);
        
    }
}
