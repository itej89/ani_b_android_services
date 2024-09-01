//
//  KineticsResponseDelay.swift
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

//DEL#ADR#ms#O:
public class KineticsResponseDelay extends  KineticsResponse
{
     Actuator ActuatorType;
     Integer Timing;
    
    public KineticsResponseDelay(String response) {
        super(response);
        if(ResponseType == CommandLabels.CommandTypes.DEL)
        {
            ArrayList<String> RequestAckParts = DecomposedResponse.get(0);
            
            if(RequestAckParts.size() == 4)
            {
                Integer Address = Integer.parseInt(RequestAckParts.get(1));

                ActuatorType = MachineConfig.Instance.getActuatorWith(Address);
                Timing = Integer.parseInt(RequestAckParts.get(2));
                RequestRecievedAck =  KineticsResponseAcknowledgement.ConvertFromString(RequestAckParts.get(3));
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
        dest.writeValue(this.Timing);
    }

    protected KineticsResponseDelay(Parcel in) {
        super(in);
        int tmpActuatorType = in.readInt();
        this.ActuatorType = tmpActuatorType == -1 ? null : Actuator.values()[tmpActuatorType];
        this.Timing = (Integer) in.readValue(Integer.class.getClassLoader());
    }

    public static final Creator<KineticsResponseDelay> CREATOR = new Creator<KineticsResponseDelay>() {
        @Override
        public KineticsResponseDelay createFromParcel(Parcel source) {
            return new KineticsResponseDelay(source);
        }

        @Override
        public KineticsResponseDelay[] newArray(int size) {
            return new KineticsResponseDelay[size];
        }
    };
}

