//
//  KimneticRequestCELLOneCommand.swift
//  FourierMachines.Ani.Client.Kinetics
//
//  Created by Tej Kiran on 07/03/18.
//  Copyright © 2018 FourierMachines. All rights reserved.
//

package Framework.DataTypes.Parsers.RequestCommandParser;

import Framework.DataTypes.Constants.CommandLabels;

public class KineticsRequestCELLOne extends KineticsRequest
{
    
    public KineticsRequestCELLOne()
    {
        super(CommandLabels.CommandTypes.CELL1);
    }
    
    public KineticsRequestCELLOne(String command)
    {
        super(CommandLabels.CommandTypes.CELL1);
        
        super.removeDelimiters( command);
        
    }
}
