//
//  KineticsResponsePowerOnLockServo.swift
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

//LON#ADR#O:
public class KineticsResponsePowerOnLockServo extends  KineticsResponse
{
    
     Actuator ActuatorType;
    
    public KineticsResponsePowerOnLockServo(String response) {
        super(response);
        if(ResponseType == CommandLabels.CommandTypes.LON)
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

    protected KineticsResponsePowerOnLockServo(Parcel in) {
        super(in);
        int tmpActuatorType = in.readInt();
        this.ActuatorType = tmpActuatorType == -1 ? null : Actuator.values()[tmpActuatorType];
    }

    public static final Creator<KineticsResponsePowerOnLockServo> CREATOR = new Creator<KineticsResponsePowerOnLockServo>() {
        @Override
        public KineticsResponsePowerOnLockServo createFromParcel(Parcel source) {
            return new KineticsResponsePowerOnLockServo(source);
        }

        @Override
        public KineticsResponsePowerOnLockServo[] newArray(int size) {
            return new KineticsResponsePowerOnLockServo[size];
        }
    };
}
