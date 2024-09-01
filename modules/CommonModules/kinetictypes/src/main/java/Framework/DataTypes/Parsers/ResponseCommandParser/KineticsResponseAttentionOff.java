//
//  KineticsResponseAttentionOff.swift
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
//VNO#O:
public class KineticsResponseAttentionOff extends KineticsResponse
{
    
    public KineticsResponseAttentionOff(String response) {
        super(response);
        if(ResponseType == CommandLabels.CommandTypes.VNO)
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

    protected KineticsResponseAttentionOff(Parcel in) {
        super(in);
    }

    public static final Creator<KineticsResponseAttentionOff> CREATOR = new Creator<KineticsResponseAttentionOff>() {
        @Override
        public KineticsResponseAttentionOff createFromParcel(Parcel source) {
            return new KineticsResponseAttentionOff(source);
        }

        @Override
        public KineticsResponseAttentionOff[] newArray(int size) {
            return new KineticsResponseAttentionOff[size];
        }
    };
}

