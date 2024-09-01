//
//  KineticsResponseEEPROMRead.swift
//  FourierMachines.Ani.Client.Kinetics
//
//  Created by Tej Kiran on 07/03/18.
//  Copyright © 2018 FourierMachines. All rights reserved.
//

package Framework.DataTypes.Parsers.ResponseCommandParser;

import java.util.ArrayList;

import Framework.DataTypes.Constants.CommandLabels;
import Framework.DataTypes.Constants.KineticsResponseAcknowledgement;
import FrameworkInterface.PublicTypes.Config.MachineConfig;
import FrameworkInterface.PublicTypes.Constants.Actuator;
import FrameworkInterface.PublicTypes.EEPROMDetails;

//EEPR#ADR#O:EEPR#ADR#COUNT#DATA1#DATA2#..#DATAn:
public class KineticsResponseEEPROMRead extends  KineticsResponse
{
    EEPROMDetails MemoryLocation;
    public ArrayList<Integer> Data = new ArrayList<Integer>();
    
    public KineticsResponseEEPROMRead(String response) {
        super(response);
        if(ResponseType == CommandLabels.CommandTypes.EEPR)
        {
            ArrayList<String> RequestParts = DecomposedResponse.get(0);
            ArrayList<String> ResponseParts = DecomposedResponse.get(1);
            
            if(RequestParts.size() == 3)
            {
                RequestRecievedAck =  KineticsResponseAcknowledgement.ConvertFromString(RequestParts.get(2));
            }
            
            if(RequestRecievedAck != null && RequestRecievedAck == KineticsResponseAcknowledgement.OK)
            {
                if(ResponseParts.size() >= 3)
                {
                    Integer Address = Integer.parseInt(ResponseParts.get(1));
                    Integer ByteCount = Integer.parseInt(ResponseParts.get(2));
                    
                    if(Address != null && ByteCount != null){
                        MemoryLocation = new EEPROMDetails(Address, ByteCount);
                        
                        if(MemoryLocation.NoOfBytes > 0 && ResponseParts.size() == 3+MemoryLocation.NoOfBytes)
                        {
                            for(int index=0; index< MemoryLocation.NoOfBytes; index++)
                            {
                                //EEPROM return value will be in hex String format
                                Data.add(Integer.parseInt(ResponseParts.get(3 + index), 16));
                            }
                        }
                    }
                }
            }
        }
    }
    
    

}
