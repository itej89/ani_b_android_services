//
//  KineticsRequest.swift
//  FourierMachines.Ani.Client.Kinetics
//
//  Created by Tej Kiran on 07/03/18.
//  Copyright © 2018 FourierMachines. All rights reserved.
//
package Framework.DataTypes.Parsers.RequestCommandParser;

import java.util.ArrayList;

import Framework.DataTypes.Constants.CommandLabels;

public class KineticsRequest
{
   public CommandLabels.CommandTypes RequestType;
   
   public String Request;
    
    
    public KineticsRequest(CommandLabels.CommandTypes requestType)
    {
        RequestType = requestType;
    }
    
     String addDelimiters(String command)
    {
        return "~"+command+":";
    }
    
     String formCommand(String[] items)
    {
        String command = RequestType.toString();
        for(String val : items)
        {
            command = command+"#"+val;
        }
        return command;
    }
    
     String removeDelimiters(String command)
    {
        return command.replaceAll("~$", "").replaceAll("^:", "");
    }



     ArrayList<String> decomposeCommand(String command)
    {
        ArrayList<String> data = new ArrayList<String>();
        
        String[] parts = command.split("#");
        if(parts.length > 0)
        {
            RequestType = CommandLabels.CommandTypes.valueOf(parts[0]);
            if(RequestType != null && parts.length > 1)
            {
                for(int i=1; i<parts.length;i++)
                {
                    data.add(parts[i]);
                }
            }
        }
        
        return data;
    }
}
