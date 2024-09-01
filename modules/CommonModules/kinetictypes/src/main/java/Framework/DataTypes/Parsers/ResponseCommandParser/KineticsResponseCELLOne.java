//
//  KineticsResponseCELLOne.swift
//  FourierMachines.Ani.Client.Kinetics
//
//  Created by Tej Kiran on 07/03/18.
//  Copyright © 2018 FourierMachines. All rights reserved.
//

package Framework.DataTypes.Parsers.ResponseCommandParser;

import android.os.Parcel;

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


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeInt(this.ActuatorType == null ? -1 : this.ActuatorType.ordinal());
        dest.writeValue(this.ADC);
        dest.writeInt(this.ResponseErrorCondition == null ? -1 : this.ResponseErrorCondition.ordinal());
    }

    protected KineticsResponseCELLOne(Parcel in) {
        super(in);
        int tmpActuatorType = in.readInt();
        this.ActuatorType = tmpActuatorType == -1 ? null : Actuator.values()[tmpActuatorType];
        this.ADC = (Integer) in.readValue(Integer.class.getClassLoader());
        int tmpResponseErrorCondition = in.readInt();
        this.ResponseErrorCondition = tmpResponseErrorCondition == -1 ? null : KineticsResponseAcknowledgement.values()[tmpResponseErrorCondition];
    }

    public static final Creator<KineticsResponseCELLOne> CREATOR = new Creator<KineticsResponseCELLOne>() {
        @Override
        public KineticsResponseCELLOne createFromParcel(Parcel source) {
            return new KineticsResponseCELLOne(source);
        }

        @Override
        public KineticsResponseCELLOne[] newArray(int size) {
            return new KineticsResponseCELLOne[size];
        }
    };
}
