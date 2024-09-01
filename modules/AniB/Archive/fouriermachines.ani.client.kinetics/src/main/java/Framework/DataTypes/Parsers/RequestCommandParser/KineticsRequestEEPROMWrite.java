//
//  KineticRequestEEPROMWrite.swift
//  FourierMachines.Ani.Client.Kinetics
//
//  Created by Tej Kiran on 07/03/18.
//  Copyright © 2018 FourierMachines. All rights reserved.
//

package Framework.DataTypes.Parsers.RequestCommandParser;

import java.util.ArrayList;

import Framework.DataTypes.Constants.CommandLabels;
import FrameworkInterface.PublicTypes.EEPROMDetails;

public class KineticsRequestEEPROMWrite extends KineticsRequest
{
    EEPROMDetails MemoryLocation;
    Integer Value;
    
    //noOfBytes should always be one
    public KineticsRequestEEPROMWrite(EEPROMDetails memoryLocation, Integer value)
    {
        super(CommandLabels.CommandTypes.EEPW);
        
        MemoryLocation = memoryLocation;
        Value = value;
        
        String Command = super.formCommand(new String[]{(MemoryLocation.Address.toString()), (MemoryLocation.NoOfBytes.toString()), (Value.toString())});
        
        Request = super.addDelimiters(Command);
    }
    
    public KineticsRequestEEPROMWrite(String command)
    {
        super(CommandLabels.CommandTypes.EEPW);
        String _command = super.removeDelimiters( command);
        ArrayList<String> contents = super.decomposeCommand( _command);
        if(contents.size() == 3)
        {
            Integer Address = Integer.parseInt(contents.get(1));
            Integer NoOfBytes = Integer.parseInt(contents.get(2));
            
            MemoryLocation = new EEPROMDetails( Address,  NoOfBytes);
            
            Value = Integer.parseInt(contents.get(3));
            
        }
    }
}
