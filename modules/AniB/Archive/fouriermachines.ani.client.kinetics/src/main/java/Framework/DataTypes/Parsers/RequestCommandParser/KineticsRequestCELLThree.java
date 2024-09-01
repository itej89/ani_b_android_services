//
//  KimneticRequestCELLThreeCommand.swift
//  FourierMachines.Ani.Client.Kinetics
//
//  Created by Tej Kiran on 07/03/18.
//  Copyright © 2018 FourierMachines. All rights reserved.
//

package Framework.DataTypes.Parsers.RequestCommandParser;

import Framework.DataTypes.Constants.CommandLabels;

public class KineticsRequestCELLThree extends KineticsRequest
{
    
    public KineticsRequestCELLThree()
    {
        super(CommandLabels.CommandTypes.CELL3);
    }
    
    public KineticsRequestCELLThree(String command)
    {
        super(CommandLabels.CommandTypes.CELL3);
        
        super.removeDelimiters(command);
        
    }
}
