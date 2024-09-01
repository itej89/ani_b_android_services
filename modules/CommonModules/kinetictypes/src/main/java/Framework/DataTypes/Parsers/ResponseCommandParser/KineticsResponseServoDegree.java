//
//  KineticsResponseServoDegree.swift
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

//DEG#ADR#O:DEG#ADR#ADC:
//ERROR Condition : DEG#ADR#O:DEG#ADR#E:
public class KineticsResponseServoDegree extends  KineticsResponse
{

    Actuator ActuatorType;
    public Integer ADC;
    KineticsResponseAcknowledgement ResponseErrorCondition;

public KineticsResponseServoDegree(String response) {
        super(response);
    if(ResponseType == CommandLabels.CommandTypes.DEG)
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
                    ADC = Integer.parseInt(ResponseParts.get(2));
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
        dest.writeValue(this.ADC);
        dest.writeInt(this.ResponseErrorCondition == null ? -1 : this.ResponseErrorCondition.ordinal());
    }

    protected KineticsResponseServoDegree(Parcel in) {
        super(in);
        int tmpActuatorType = in.readInt();
        this.ActuatorType = tmpActuatorType == -1 ? null : Actuator.values()[tmpActuatorType];
        this.ADC = (Integer) in.readValue(Integer.class.getClassLoader());
        int tmpResponseErrorCondition = in.readInt();
        this.ResponseErrorCondition = tmpResponseErrorCondition == -1 ? null : KineticsResponseAcknowledgement.values()[tmpResponseErrorCondition];
    }

    public static final Creator<KineticsResponseServoDegree> CREATOR = new Creator<KineticsResponseServoDegree>() {
        @Override
        public KineticsResponseServoDegree createFromParcel(Parcel source) {
            return new KineticsResponseServoDegree(source);
        }

        @Override
        public KineticsResponseServoDegree[] newArray(int size) {
            return new KineticsResponseServoDegree[size];
        }
    };
}
