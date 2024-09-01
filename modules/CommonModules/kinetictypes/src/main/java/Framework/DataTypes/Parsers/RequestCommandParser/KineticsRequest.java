//
//  KineticsRequest.swift
//  FourierMachines.Ani.Client.Kinetics
//
//  Created by Tej Kiran on 07/03/18.
//  Copyright © 2018 FourierMachines. All rights reserved.
//
package Framework.DataTypes.Parsers.RequestCommandParser;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

import Framework.DataTypes.Constants.CommandLabels;

public class KineticsRequest implements Parcelable {
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


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(RequestType.toString());
        dest.writeInt(this.RequestType == null ? -1 : this.RequestType.ordinal());
        dest.writeString(this.Request);
    }

    protected KineticsRequest(Parcel in) {
        int tmpRequestType = in.readInt();
        this.RequestType = tmpRequestType == -1 ? null : CommandLabels.CommandTypes.values()[tmpRequestType];
        this.Request = in.readString();
    }

    public static final Creator<KineticsRequest> CREATOR = new Creator<KineticsRequest>() {
        @Override
        public KineticsRequest createFromParcel(Parcel source) {
            String strRequestType = source.readString();
            switch (CommandLabels.CommandTypes.valueOf(strRequestType))
            {
                case ANG:
                    return new KineticsRequestAngle(source);
                case TMG:
                    return new KineticsRequestTiming(source);
                case TRG:
                    return new KineticsRequestTrigger(source);
                case ITRG:
                    return new KineticsRequestInstantTrigger(source);
                case DEG:
                    return new KineticsRequestServoDegree(source);
                case ATC:
                    return new KineticsRequestAttachServo(source);
                case DTC:
                    return new KineticsRequestDettachServo(source);
                case LLK:
                    return new KineticsRequestLeftLockServoAngle(source);
                case RLK:
                    return new KineticsRequestRightLockServoAngle(source);
                case EAS:
                    return new KineticsRequestServoEasing(source);
                case INO:
                    return new KineticsRequestEasingInOut(source);
                case PRX:
                    return new KineticsRequestProximityRead(source);
                case CELL1:
                    return new KineticsRequestCELLOne(source);
                case CELL2:
                    return new KineticsRequestCELLTwo(source);
                case CELL3:
                    return new KineticsRequestCELLThree(source);
                case SLEEP:
                    return new KineticsRequest(source);
                case VEN:
                    return new KineticsRequestAttentionOn(source);
                case VNO:
                    return new KineticsRequestAttentionOff(source);
                case CPW:
                    return new KineticsRequestConnectPower(source);
                case DPW:
                    return new KineticsRequestDisconnectPower(source);
                case LAT:
                    return new KineticsRequestAttachLockServo(source);
                case LDT:
                    return new KineticsRequestDettachLockServo(source);
                case LON:
                    return new KineticsRequestPowerOnLockServo(source);
                case LOF:
                    return new KineticsRequestPowerOffLockServo(source);
                case SMV:
                    return new KineticsRequestServoMotionCheck(source);
                case FRQ:
                    return new KineticsRequestFrequency(source);
                case DEL:
                    return new KineticsRequestDelay(source);
                case DMP:
                    return new KineticsRequestDamp(source);
                case VEL:
                    return new KineticsRequestVelocity(source);
                case ISLR:
                    return new KineticsRequestISLRead(source);
                case ISLW:
                    return new KineticsRequestISLWrite(source);
                case ISLER:
                    return new KineticsRequestISLEEPROMRead(source);
                case ISLEW:
                    return new KineticsRequestISLEEPROMWrite(source);
                case EEPR:
                    return new KineticsRequestEEPROMRead(source);
                case EEPW:
                    return new KineticsRequestEEPROMWrite(source);
                case UNKNOWN:
                    return new KineticsRequest(source);
                case SAT:
                    return new KineticsRequestServoSignalStatus(source);
                case SPW:
                    return new KineticsRequestServoPowerStatus(source);
                case LSA:
                    return new KineticsRequestLockSignalStatus(source);
                case LPW:
                    return new KineticsRequestLockPowerStatus(source);
                case SEPR:
                    return new KineticsRequestServoEEPROMRead(source);
            }
            return new KineticsRequest(source);
        }

        @Override
        public KineticsRequest[] newArray(int size) {
            return new KineticsRequest[size];
        }
    };
}
