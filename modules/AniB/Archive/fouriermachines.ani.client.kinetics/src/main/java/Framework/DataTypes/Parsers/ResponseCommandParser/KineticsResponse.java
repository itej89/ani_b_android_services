//
//  KineticsResponse.swift
//  FourierMachines.Ani.Client.Kinetics
//
//  Created by Tej Kiran on 08/03/18.
//  Copyright © 2018 FourierMachines. All rights reserved.
//
package Framework.DataTypes.Parsers.ResponseCommandParser;

import java.util.ArrayList;
import java.util.Arrays;

import Framework.DataTypes.Constants.CommandLabels;
import Framework.DataTypes.Constants.KineticsResponseAcknowledgement;

import static Framework.DataTypes.Constants.CommandLabels.CommandTypes.SEPR;

public class KineticsResponse
{
    public CommandLabels.CommandTypes ResponseType;
    public KineticsResponseAcknowledgement RequestRecievedAck;
    public ArrayList<ArrayList<String>> DecomposedResponse;
    public KineticsResponse(String response)
    {
        String allResponses = removeDelimiters(response);
        String[] responseList = allResponses.split(":");
        if(responseList.length > 0)
        {
            DecomposedResponse = new ArrayList<ArrayList<String>>();
            ResponseType = CommandLabels.CommandTypes.UNKNOWN;

            for(String part_response : responseList)
            {

                String[] parts = removeDelimiters(part_response).split("#");
                if(parts != null && parts.length > 0)
                {
                    DecomposedResponse.add(new ArrayList<String>(Arrays.asList(parts)));

                    CommandLabels.CommandTypes responseType = CommandLabels.CommandTypes.valueOf(parts[0]);
                    if(responseType != null)
                    {
                        if(ResponseType != CommandLabels.CommandTypes.UNKNOWN && ResponseType != responseType)
                        {
                            ResponseType = CommandLabels.CommandTypes.UNKNOWN;
                            return;
                        }
                        
                        ResponseType = responseType;
                    }
                }
                else
                {
                    return;
                }
            }
            
            if(DecomposedResponse != null && ResponseType != null && (DecomposedResponse.size()) != (new CommandLabels().CommandResponseCount.get(ResponseType)))
            {
                ResponseType = CommandLabels.CommandTypes.UNKNOWN;
                return;
            }
            
        }
    }


    String removeDelimiters(String command)
    {
        return command.replaceAll("~$", "").replaceAll("^:", "");
    }
    
    private ArrayList<String> decomposeCommand(String command)
    {
        ArrayList<String> data = new ArrayList<String>();
        
        if(ResponseType != CommandLabels.CommandTypes.UNKNOWN)
        {
            String[] parts = command.split("#");
            if(parts.length > 0)
            {
                if(parts.length > 1)
                {
                    for(int i=0;i<parts.length;i++)
                    {
                        data.add(parts[i]);
                    }
                }
            }
        }
        return data;
    }
    
   
    public static KineticsResponse GetResponseObject(String response)
    {
        KineticsResponse kineticReponse = new  KineticsResponse(response);
        
        if(kineticReponse.ResponseType != CommandLabels.CommandTypes.UNKNOWN)
        {
            switch(kineticReponse.ResponseType) {
                case ANG:
                    return new KineticsResponseAngle(response);
                case TMG:
                    return new KineticsResponseTiming(response);
                case TRG:
                    return new KineticsResponseTrigger(response);
                case ITRG:
                    return new KineticsResponseInstantTrigger(response);
                case DEG:
                    return new KineticsResponseServoDegree(response);
                case ATC:
                    return new KineticsResponseAttachServo(response);
                case DTC:
                    return new KineticsResponseDettachServo(response);
                case LLK:
                    return new KineticsResponseLeftServoAngle(response);
                case RLK:
                    return new KineticsResponseRightServoAngle(response);
                case EAS:
                    return new KineticsResponseServoEasing(response);
                case INO:
                    return new KineticsResponseEasingInOut(response);
                case PRX:
                    return new KineticsResponseProximityRead(response);
                case CELL1:
                    return new KineticsResponseCELLOne(response);
                case CELL2:
                    return new KineticsResponseCELLTwo(response);
                case CELL3:
                    return new KineticsResponseCELLThree(response);
                case SLEEP:
                    return kineticReponse;
                case VEN:
                    return new KineticsResponseAttentionOn(response);
                case VNO:
                    return new KineticsResponseAttentionOff(response);
                case CPW:
                    return new KineticsResponseConnectPower(response);
                case DPW:
                    return new KineticsResponseDisconnectPower(response);
                case LAT:
                    return new KineticsResponseAttachLockServo(response);
                case LDT:
                    return new KineticsResponseDettachLockServo(response);
                case LON:
                    return new KineticsResponsePowerOnLockServo(response);
                case LOF:
                    return new KineticsResponsePowerOffLockServo(response);
                case SMV:
                    return new KineticsResponseServoMotionCheck(response);
                case FRQ:
                    return new KineticsResponseFrequency(response);
                case DEL:
                    return new KineticsResponseDelay(response);
                case DMP:
                    return new KineticsResponseDamp(response);
                case VEL:
                    return new KineticsResponseVelocity(response);
                case ISLR:
                    return new KineticsResponseISLRead(response);
                case ISLW:
                    return new KineticsResponseISLWrite(response);
                case ISLER:
                    return new KineticsResponseISLEEPROMRead(response);
                case ISLEW:
                    return new KineticsResponseISLEEPROMWrite(response);
                case EEPR:
                    return new KineticsResponseEEPROMRead(response);
                case EEPW:
                    return new KineticsResponseEEPROMWrite(response);
                case UNKNOWN:
                    return kineticReponse;
                case SAT:
                    return new KineticsResponseServoSignalStatus(response);
                case SPW:
                    return new KineticsResponseServoPowerStatus(response);
                case LSA:
                    return new KineticsResponseLockSignalStatus(response);
                case LPW:
                    return new KineticsResponseLockPowerStatus(response);
                case SEPR:
                    return new KineticsResponseServoEEPROMRead(response);
            }
        }
        
        return kineticReponse;
        
    }
    
    
    
}
