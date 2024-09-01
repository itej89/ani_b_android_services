//
//  KineticsResponseInstantTrigger.swift
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

//ITRG#O:
public class KineticsResponseInstantTrigger extends  KineticsResponse
{
    
    public KineticsResponseInstantTrigger(String response) {
        super(response);
        if(ResponseType == CommandLabels.CommandTypes.ITRG)
        {
            ArrayList<String> RequestAckParts = DecomposedResponse.get(0);
            
            if(RequestAckParts.size() == 2)
            {
                RequestRecievedAck =  KineticsResponseAcknowledgement.ConvertFromString(RequestAckParts.get(1));
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
    }

    protected KineticsResponseInstantTrigger(Parcel in) {
        super(in);
    }

    public static final Creator<KineticsResponseInstantTrigger> CREATOR = new Creator<KineticsResponseInstantTrigger>() {
        @Override
        public KineticsResponseInstantTrigger createFromParcel(Parcel source) {
            return new KineticsResponseInstantTrigger(source);
        }

        @Override
        public KineticsResponseInstantTrigger[] newArray(int size) {
            return new KineticsResponseInstantTrigger[size];
        }
    };
}
