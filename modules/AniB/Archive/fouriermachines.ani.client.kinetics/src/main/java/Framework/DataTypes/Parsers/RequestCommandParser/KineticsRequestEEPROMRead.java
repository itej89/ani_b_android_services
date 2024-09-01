//
//  KineticRequestEEPROMRead.swift
//  FourierMachines.Ani.Client.Kinetics
//
//  Created by Tej Kiran on 07/03/18.
//  Copyright © 2018 FourierMachines. All rights reserved.
//

package Framework.DataTypes.Parsers.RequestCommandParser;

import java.util.ArrayList;

import Framework.DataTypes.Constants.CommandLabels;
import FrameworkInterface.PublicTypes.EEPROMDetails;

public class KineticsRequestEEPROMRead extends KineticsRequest
{
    EEPROMDetails MemoryLocation;
    Integer NoOfBytes;
    Integer Address;
    
    public KineticsRequestEEPROMRead(EEPROMDetails memoryLocation)
    {
        super(CommandLabels.CommandTypes.EEPR);
        
        NoOfBytes = memoryLocation.NoOfBytes;
        Address = memoryLocation.Address;
        
        String Command = super.formCommand(new String[]{(Address.toString()), (NoOfBytes.toString())});
        
        Request = super.addDelimiters( Command);
    }
    
    public KineticsRequestEEPROMRead(String command)
    {
        super(CommandLabels.CommandTypes.EEPR);
        String _command = super.removeDelimiters(command);
        ArrayList<String> contents = super.decomposeCommand( _command);
        if(contents.size() == 2)
        {
            Address = Integer.parseInt(contents.get(1));
            NoOfBytes = Integer.parseInt(contents.get(2));
            
            MemoryLocation = new EEPROMDetails(Address, NoOfBytes);
            
        }
    }
}
