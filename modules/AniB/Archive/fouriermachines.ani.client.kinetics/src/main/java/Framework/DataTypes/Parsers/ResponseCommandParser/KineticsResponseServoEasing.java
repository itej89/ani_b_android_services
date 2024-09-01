//
//  KineticsResponseServoEasing.swift
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

//EAS#ADR#Func#O:
public class KineticsResponseServoEasing extends  KineticsResponse
{
     Actuator ActuatorType;
    CommandLabels.EasingFunction EasingFunction;
    
    public KineticsResponseServoEasing(String response) {
        super(response);
        if(ResponseType == CommandLabels.CommandTypes.EAS)
        {
            ArrayList<String> RequestAckParts = DecomposedResponse.get(0);
            
            if(RequestAckParts.size() == 4)
            {
                Integer Address = Integer.parseInt(RequestAckParts.get(1));

                ActuatorType = MachineConfig.Instance.getActuatorWith(Address);
                EasingFunction = CommandLabels.EasingFunction.valueOf(RequestAckParts.get(2));
                RequestRecievedAck =  KineticsResponseAcknowledgement.ConvertFromString(RequestAckParts.get(3));
            }
        }
    }
    
}

