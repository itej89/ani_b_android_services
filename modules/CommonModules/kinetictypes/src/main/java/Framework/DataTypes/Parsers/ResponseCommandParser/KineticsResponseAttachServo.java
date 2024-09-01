//
//  KineticsResponseAttachServo.swift
//  FourierMachines.Ani.Client.Kinetics
//
//  Created by Tej Kiran on 07/03/18.
//  Copyright © 2018 FourierMachines. All rights reserved.
//

package Framework.DataTypes.Parsers.ResponseCommandParser;

import android.os.Parcel;
import android.widget.ArrayAdapter;

import java.util.ArrayList;

import Framework.DataTypes.Constants.CommandLabels;
import Framework.DataTypes.Constants.KineticsResponseAcknowledgement;
import FrameworkInterface.PublicTypes.Config.MachineConfig;
import FrameworkInterface.PublicTypes.Constants.Actuator;
//ATC#ADR#O:
public class KineticsResponseAttachServo extends  KineticsResponse
{

    Actuator ActuatorType;
    
    public KineticsResponseAttachServo(String response) {
        super(response);
        if(ResponseType == CommandLabels.CommandTypes.ATC)
        {
            ArrayList<String> RequestParts = DecomposedResponse.get(0);
            
            if(RequestParts.size() == 3)
            {
                Integer Address = Integer.parseInt(RequestParts.get(1));
                
                ActuatorType = MachineConfig.Instance.getActuatorWith(Address);
                RequestRecievedAck =  KineticsResponseAcknowledgement.ConvertFromString(RequestParts.get(2));
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
    }

    protected KineticsResponseAttachServo(Parcel in) {
        super(in);
        int tmpActuatorType = in.readInt();
        this.ActuatorType = tmpActuatorType == -1 ? null : Actuator.values()[tmpActuatorType];
    }

    public static final Creator<KineticsResponseAttachServo> CREATOR = new Creator<KineticsResponseAttachServo>() {
        @Override
        public KineticsResponseAttachServo createFromParcel(Parcel source) {
            return new KineticsResponseAttachServo(source);
        }

        @Override
        public KineticsResponseAttachServo[] newArray(int size) {
            return new KineticsResponseAttachServo[size];
        }
    };
}
