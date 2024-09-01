//
//  KineticsResponseAttachServo.swift
//  FourierMachines.Ani.Client.Kinetics
//
//  Created by Tej Kiran on 07/03/18.
//  Copyright © 2018 FourierMachines. All rights reserved.
//

package Framework.DataTypes.Parsers.ResponseCommandParser;

import android.widget.ArrayAdapter;

import java.util.ArrayList;

import Framework.DataTypes.Constants.CommandLabels;
import Framework.DataTypes.Constants.KineticsResponseAcknowledgement;
import FrameworkInterface.PublicTypes.Config.MachineConfig;
import FrameworkInterface.PublicTypes.Constants.Actuator;
//ATC#ADR#O:
public class KineticsResponseAttachServo extends  KineticsResponse
{

    Actuator ActuatorType;
    
    public KineticsResponseAttachServo(String response) {
        super(response);
        if(ResponseType == CommandLabels.CommandTypes.ATC)
        {
            ArrayList<String> RequestParts = DecomposedResponse.get(0);
            
            if(RequestParts.size() == 3)
            {
                Integer Address = Integer.parseInt(RequestParts.get(1));
                
                ActuatorType = MachineConfig.Instance.getActuatorWith(Address);
                RequestRecievedAck =  KineticsResponseAcknowledgement.ConvertFromString(RequestParts.get(2));
            }
            
            
        }
    }
}
