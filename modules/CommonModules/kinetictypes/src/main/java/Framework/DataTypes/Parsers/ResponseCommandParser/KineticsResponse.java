//
//  KineticsResponse.swift
//  FourierMachines.Ani.Client.Kinetics
//
//  Created by Tej Kiran on 08/03/18.
//  Copyright © 2018 FourierMachines. All rights reserved.
//
package Framework.DataTypes.Parsers.ResponseCommandParser;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import Framework.DataTypes.Constants.CommandLabels;
import Framework.DataTypes.Constants.KineticsResponseAcknowledgement;

import static Framework.DataTypes.Constants.CommandLabels.CommandTypes.SEPR;

public class KineticsResponse implements Parcelable {
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


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(ResponseType.toString());
        dest.writeInt(this.ResponseType == null ? -1 : this.ResponseType.ordinal());
        dest.writeInt(this.RequestRecievedAck == null ? -1 : this.RequestRecievedAck.ordinal());
    }

    protected KineticsResponse(Parcel in) {
        int tmpResponseType = in.readInt();
        this.ResponseType = tmpResponseType == -1 ? null : CommandLabels.CommandTypes.values()[tmpResponseType];
        int tmpRequestRecievedAck = in.readInt();
        this.RequestRecievedAck = tmpRequestRecievedAck == -1 ? null : KineticsResponseAcknowledgement.values()[tmpRequestRecievedAck];
    }

    public static final Creator<KineticsResponse> CREATOR = new Creator<KineticsResponse>() {
        @Override
        public KineticsResponse createFromParcel(Parcel source) {
            String derivedClassName = source.readString();
            switch (CommandLabels.CommandTypes.valueOf(derivedClassName))
            {
                case ANG:
                    return new KineticsResponseAngle(source);
                case TMG:
                    return new KineticsResponseTiming(source);
                case TRG:
                    return new KineticsResponseTrigger(source);
                case ITRG:
                    return new KineticsResponseInstantTrigger(source);
                case DEG:
                    return new KineticsResponseServoDegree(source);
                case ATC:
                    return new KineticsResponseAttachServo(source);
                case DTC:
                    return new KineticsResponseDettachServo(source);
                case LLK:
                    return new KineticsResponseLeftServoAngle(source);
                case RLK:
                    return new KineticsResponseRightServoAngle(source);
                case EAS:
                    return new KineticsResponseServoEasing(source);
                case INO:
                    return new KineticsResponseEasingInOut(source);
                case PRX:
                    return new KineticsResponseProximityRead(source);
                case CELL1:
                    return new KineticsResponseCELLOne(source);
                case CELL2:
                    return new KineticsResponseCELLTwo(source);
                case CELL3:
                    return new KineticsResponseCELLThree(source);
                case SLEEP:
                    return new KineticsResponse(source);
                case VEN:
                    return new KineticsResponseAttentionOn(source);
                case VNO:
                    return new KineticsResponseAttentionOff(source);
                case CPW:
                    return new KineticsResponseConnectPower(source);
                case DPW:
                    return new KineticsResponseDisconnectPower(source);
                case LAT:
                    return new KineticsResponseAttachLockServo(source);
                case LDT:
                    return new KineticsResponseDettachLockServo(source);
                case LON:
                    return new KineticsResponsePowerOnLockServo(source);
                case LOF:
                    return new KineticsResponsePowerOffLockServo(source);
                case SMV:
                    return new KineticsResponseServoMotionCheck(source);
                case FRQ:
                    return new KineticsResponseFrequency(source);
                case DEL:
                    return new KineticsResponseDelay(source);
                case DMP:
                    return new KineticsResponseDamp(source);
                case VEL:
                    return new KineticsResponseVelocity(source);
                case ISLR:
                    return new KineticsResponseISLRead(source);
                case ISLW:
                    return new KineticsResponseISLWrite(source);
                case ISLER:
                    return new KineticsResponseISLEEPROMRead(source);
                case ISLEW:
                    return new KineticsResponseISLEEPROMWrite(source);
                case EEPR:
                    return new KineticsResponseEEPROMRead(source);
                case EEPW:
                    return new KineticsResponseEEPROMWrite(source);
                case UNKNOWN:
                    return new KineticsResponse(source);
                case SAT:
                    return new KineticsResponseServoSignalStatus(source);
                case SPW:
                    return new KineticsResponseServoPowerStatus(source);
                case LSA:
                    return new KineticsResponseLockSignalStatus(source);
                case LPW:
                    return new KineticsResponseLockPowerStatus(source);
                case SEPR:
                    return new KineticsResponseServoEEPROMRead(source);
            }
            return new KineticsResponse(source);
        }

        @Override
        public KineticsResponse[] newArray(int size) {
            return new KineticsResponse[size];
        }
    };
}
