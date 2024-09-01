//
//  KineticsResponseServoMotionCheck.swift
//  FourierMachines.Ani.Client.Kinetics
//
//  Created by Tej Kiran on 07/03/18.
//  Copyright © 2018 FourierMachines. All rights reserved.
//

package Framework.DataTypes.Parsers.ResponseCommandParser;


import java.util.ArrayList;

import Framework.DataTypes.Constants.ActuatorMotionSymbols;
import Framework.DataTypes.Constants.CommandLabels;
import Framework.DataTypes.Constants.KineticsResponseAcknowledgement;
import FrameworkInterface.PublicTypes.Config.MachineConfig;
import FrameworkInterface.PublicTypes.Constants.Actuator;

//SMV#ADR#O:SMV#ADR#DATA:
public class KineticsResponseServoMotionCheck extends  KineticsResponse
{
     Actuator ActuatorType;
    ActuatorMotionSymbols ActuatorState;
    KineticsResponseAcknowledgement ResponseErrorCondition;
    
    public KineticsResponseServoMotionCheck(String response) {
        super(response);
        if(ResponseType == CommandLabels.CommandTypes.SMV)
        {
            ArrayList<String> RequestParts = DecomposedResponse.get(0);
            ArrayList<String> ResponseParts = DecomposedResponse.get(1);
            
            if(RequestParts.size() == 3)
            {
                RequestRecievedAck = KineticsResponseAcknowledgement.ConvertFromString(RequestParts.get(2));
            }
            
            if(RequestRecievedAck != null && RequestRecievedAck == KineticsResponseAcknowledgement.OK)
            {
                if(ResponseParts.size() == 3)
                {
                    Integer Address = Integer.parseInt(ResponseParts.get(1));

                    ActuatorType = MachineConfig.Instance.getActuatorWith(Address);

                    RequestRecievedAck =  KineticsResponseAcknowledgement.ConvertFromString(ResponseParts.get(2));
                    
                    if(ResponseErrorCondition == null)
                    {
                        ActuatorState = ActuatorMotionSymbols.ConvertFromString(ResponseParts.get(2));
                    }
                
                }
            }
        }
    }
    
    
    
}
