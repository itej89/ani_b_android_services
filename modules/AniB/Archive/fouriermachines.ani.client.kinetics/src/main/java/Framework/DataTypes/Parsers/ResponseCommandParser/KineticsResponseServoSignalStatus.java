//
//  KinectResponseServoSignalStatus.swift
//  FourierMachines.Ani.Client.Kinetics
//
//  Created by Tej Kiran on 28/04/18.
//  Copyright © 2018 FourierMachines. All rights reserved.
//

package Framework.DataTypes.Parsers.ResponseCommandParser;

import java.util.ArrayList;

import Framework.DataTypes.Constants.ActuatorSignalStatusSymbols;
import Framework.DataTypes.Constants.CommandLabels;
import Framework.DataTypes.Constants.KineticsResponseAcknowledgement;
import FrameworkInterface.PublicTypes.Config.MachineConfig;
import FrameworkInterface.PublicTypes.Constants.Actuator;

//SAT#ADR#O:SAT#ADR#DATA:
public class KineticsResponseServoSignalStatus extends  KineticsResponse
{
     Actuator ActuatorType;
    public ActuatorSignalStatusSymbols SignalState;
    KineticsResponseAcknowledgement ResponseErrorCondition;
    
    public KineticsResponseServoSignalStatus(String response) {
        super(response);
        if(ResponseType == CommandLabels.CommandTypes.SAT)
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
                        SignalState = ActuatorSignalStatusSymbols.ConvertFromString(ResponseParts.get(2));
                    }
                    
                }
            }
        }
    }
    
    
    
}
