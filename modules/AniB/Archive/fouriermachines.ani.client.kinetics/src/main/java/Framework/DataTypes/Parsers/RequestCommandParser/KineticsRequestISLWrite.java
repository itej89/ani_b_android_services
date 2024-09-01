//
//  KineticRequestISLWrite.swift
//  FourierMachines.Ani.Client.Kinetics
//
//  Created by Tej Kiran on 07/03/18.
//  Copyright © 2018 FourierMachines. All rights reserved.
//

package Framework.DataTypes.Parsers.RequestCommandParser;

import java.util.ArrayList;

import Framework.DataTypes.Constants.CommandLabels;

public class KineticsRequestISLWrite extends KineticsRequest
{
    Integer NoOfBytes;
    Integer Address;
    Integer Value;
    
    //noOfBytes should always be one
    public KineticsRequestISLWrite(Integer noOfBytes, Integer address, Integer value)
    {
        super(CommandLabels.CommandTypes.ISLW);
        
        NoOfBytes = noOfBytes;
        Address = address;
        Value = value;
        
        String Command = super.formCommand(new String[]{(Address.toString()), (NoOfBytes.toString()), (Value.toString())});
        
        Request = super.addDelimiters( Command);
    }
    
    public KineticsRequestISLWrite(String command)
    {
        super(CommandLabels.CommandTypes.ISLW);
        String _command = super.removeDelimiters( command);
        ArrayList<String> contents = super.decomposeCommand( _command);
        if(contents.size() == 3)
        {
            Address = Integer.parseInt(contents.get(1));
            NoOfBytes = Integer.parseInt(contents.get(2));
            Value = Integer.parseInt(contents.get(3));
            
        }
    }
}
