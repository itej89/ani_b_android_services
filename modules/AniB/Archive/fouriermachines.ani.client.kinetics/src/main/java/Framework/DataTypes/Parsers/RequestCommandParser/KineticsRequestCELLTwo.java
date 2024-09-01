//
//  KimneticRequestCELLTwoCommand.swift
//  FourierMachines.Ani.Client.Kinetics
//
//  Created by Tej Kiran on 07/03/18.
//  Copyright © 2018 FourierMachines. All rights reserved.
//

package Framework.DataTypes.Parsers.RequestCommandParser;

import Framework.DataTypes.Constants.CommandLabels;

public class KineticsRequestCELLTwo extends KineticsRequest
{
    
    public KineticsRequestCELLTwo()
    {
        super(CommandLabels.CommandTypes.CELL2);
    }
    
    public KineticsRequestCELLTwo(String command)
    {
        super(CommandLabels.CommandTypes.CELL2);
        
        super.removeDelimiters(command);
        
    }
}
