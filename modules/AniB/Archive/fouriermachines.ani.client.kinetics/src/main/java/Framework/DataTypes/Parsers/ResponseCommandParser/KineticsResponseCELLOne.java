//
//  KineticsResponseCELLOne.swift
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

//CELL1#O:CELL1#ADC:
public class KineticsResponseCELLOne extends KineticsResponse
{
    
     Actuator ActuatorType;
    Integer ADC;
    KineticsResponseAcknowledgement ResponseErrorCondition;
    
    public KineticsResponseCELLOne(String response) {
        super(response);
        if(ResponseType == CommandLabels.CommandTypes.CELL1)
        {
            ArrayList<String> RequestParts = DecomposedResponse.get(0);
            ArrayList<String> ResponseParts = DecomposedResponse.get(1);
            
            if(RequestParts.size() == 2)
            {
                RequestRecievedAck =  KineticsResponseAcknowledgement.ConvertFromString(RequestParts.get(1));
            }
            
            if(RequestRecievedAck != null && RequestRecievedAck == KineticsResponseAcknowledgement.OK)
            {
                if(ResponseParts.size() == 2)
                {
                    ADC = Integer.parseInt(ResponseParts.get(1));
                }
            }
        }
    }
}
