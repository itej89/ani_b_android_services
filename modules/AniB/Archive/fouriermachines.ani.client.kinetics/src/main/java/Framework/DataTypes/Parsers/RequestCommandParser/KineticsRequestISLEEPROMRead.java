//
//  KineticRequestISLEEPROMREAD.swift
//  FourierMachines.Ani.Client.Kinetics
//
//  Created by Tej Kiran on 07/03/18.
//  Copyright © 2018 FourierMachines. All rights reserved.
//

package Framework.DataTypes.Parsers.RequestCommandParser;

import java.util.ArrayList;

import Framework.DataTypes.Constants.CommandLabels;

public class KineticsRequestISLEEPROMRead extends KineticsRequest
{
    Integer NoOfBytes;
    Integer Address;
    
    public KineticsRequestISLEEPROMRead(Integer noOfBytes, Integer address)
    {
        super( CommandLabels.CommandTypes.ISLER);
        
        NoOfBytes = noOfBytes;
        Address = address;
        
        String Command = super.formCommand(new String[]{(Address.toString()), (NoOfBytes.toString())});
        
        Request = super.addDelimiters(Command);
    }
    
    public KineticsRequestISLEEPROMRead(String command)
    {
        super(CommandLabels.CommandTypes.ISLER);
        String _command = super.removeDelimiters( command);
        ArrayList<String> contents = super.decomposeCommand( _command);
        if(contents.size() == 2)
        {
            Address = Integer.parseInt(contents.get(1));
            NoOfBytes = Integer.parseInt(contents.get(2));
            
        }
    }
}
