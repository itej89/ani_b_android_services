//
//  KineticsResponseProximityRead.swift
//  FourierMachines.Ani.Client.Kinetics
//
//  Created by Tej Kiran on 07/03/18.
//  Copyright © 2018 FourierMachines. All rights reserved.
//

package Framework.DataTypes.Parsers.ResponseCommandParser;

import java.util.ArrayList;

import Framework.DataTypes.Constants.CommandLabels;
import Framework.DataTypes.Constants.KineticsResponseAcknowledgement;
import Framework.DataTypes.Constants.ProximityStateSymbols;
import FrameworkInterface.PublicTypes.Config.MachineConfig;
import FrameworkInterface.PublicTypes.Constants.Actuator;


//PRX#ADR#O:PRX#ADR#DATA:
public class KineticsResponseProximityRead extends  KineticsResponse
{
     Actuator ActuatorType;
    public ProximityStateSymbols MountState;
    KineticsResponseAcknowledgement ResponseErrorCondition;
    
    public KineticsResponseProximityRead(String response) {
        super(response);
        if(ResponseType == CommandLabels.CommandTypes.PRX)
        {
            ArrayList<String> RequestParts = DecomposedResponse.get(0);
            ArrayList<String> ResponseParts = DecomposedResponse.get(1);
            
            if(RequestParts.size() == 3)
            {
                RequestRecievedAck =  KineticsResponseAcknowledgement.ConvertFromString(RequestParts.get(2));
            }
            
            if(RequestRecievedAck != null && RequestRecievedAck == KineticsResponseAcknowledgement.OK)
            {
                if(ResponseParts.size() == 3)
                {
                    Integer Address = Integer.parseInt(ResponseParts.get(1));

                    ActuatorType = MachineConfig.Instance.getActuatorWith(Address);
                    
                    ResponseErrorCondition = KineticsResponseAcknowledgement.ConvertFromString(ResponseParts.get(2));
                    
                    if(ResponseErrorCondition == null)
                    {
                        MountState = ProximityStateSymbols.ConvertFromString(ResponseParts.get(2));
                    }
                    
                }
            }
        }
    }
    
    
    
}
