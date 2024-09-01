//
//  KineticsResponseServoMotionCheck.swift
//  FourierMachines.Ani.Client.Kinetics
//
//  Created by Tej Kiran on 07/03/18.
//  Copyright © 2018 FourierMachines. All rights reserved.
//

package Framework.DataTypes.Parsers.ResponseCommandParser;


import android.os.Parcel;

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


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeInt(this.ActuatorType == null ? -1 : this.ActuatorType.ordinal());
        dest.writeInt(this.ActuatorState == null ? -1 : this.ActuatorState.ordinal());
        dest.writeInt(this.ResponseErrorCondition == null ? -1 : this.ResponseErrorCondition.ordinal());
    }

    protected KineticsResponseServoMotionCheck(Parcel in) {
        super(in);
        int tmpActuatorType = in.readInt();
        this.ActuatorType = tmpActuatorType == -1 ? null : Actuator.values()[tmpActuatorType];
        int tmpActuatorState = in.readInt();
        this.ActuatorState = tmpActuatorState == -1 ? null : ActuatorMotionSymbols.values()[tmpActuatorState];
        int tmpResponseErrorCondition = in.readInt();
        this.ResponseErrorCondition = tmpResponseErrorCondition == -1 ? null : KineticsResponseAcknowledgement.values()[tmpResponseErrorCondition];
    }

    public static final Creator<KineticsResponseServoMotionCheck> CREATOR = new Creator<KineticsResponseServoMotionCheck>() {
        @Override
        public KineticsResponseServoMotionCheck createFromParcel(Parcel source) {
            return new KineticsResponseServoMotionCheck(source);
        }

        @Override
        public KineticsResponseServoMotionCheck[] newArray(int size) {
            return new KineticsResponseServoMotionCheck[size];
        }
    };
}
