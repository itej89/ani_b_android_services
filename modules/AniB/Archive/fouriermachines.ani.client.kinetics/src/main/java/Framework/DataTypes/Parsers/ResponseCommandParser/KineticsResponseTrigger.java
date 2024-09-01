//
//  KineticsResponseTrigger.swift
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

//TRG#O:
public class KineticsResponseTrigger extends  KineticsResponse
{
    
    public KineticsResponseTrigger(String response) {
        super(response);
        if(ResponseType == CommandLabels.CommandTypes.TRG)
        {
            ArrayList<String> RequestAckParts = DecomposedResponse.get(0);
            
            
            if(RequestAckParts.size() == 2)
            {
                RequestRecievedAck =  KineticsResponseAcknowledgement.ConvertFromString(RequestAckParts.get(1));
            }
        }
    }
    
}

