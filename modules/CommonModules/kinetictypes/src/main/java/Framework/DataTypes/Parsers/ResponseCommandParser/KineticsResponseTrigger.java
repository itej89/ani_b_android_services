//
//  KineticsResponseTrigger.swift
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

//TRG#O:
public class KineticsResponseTrigger extends  KineticsResponse
{
    
    public KineticsResponseTrigger(String response) {
        super(response);
        if(ResponseType == CommandLabels.CommandTypes.TRG)
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

    protected KineticsResponseTrigger(Parcel in) {
        super(in);
    }

    public static final Creator<KineticsResponseTrigger> CREATOR = new Creator<KineticsResponseTrigger>() {
        @Override
        public KineticsResponseTrigger createFromParcel(Parcel source) {
            return new KineticsResponseTrigger(source);
        }

        @Override
        public KineticsResponseTrigger[] newArray(int size) {
            return new KineticsResponseTrigger[size];
        }
    };
}

