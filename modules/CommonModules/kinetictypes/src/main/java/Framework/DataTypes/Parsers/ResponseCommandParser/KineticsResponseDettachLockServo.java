//
//  KineticsResponseDettachLockServo.swift
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

//LDT#ADR#O:
public class KineticsResponseDettachLockServo extends  KineticsResponse
{
    
     Actuator ActuatorType;
    
    public KineticsResponseDettachLockServo(String response) {
        super(response);
        if(ResponseType == CommandLabels.CommandTypes.LDT)
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

    protected KineticsResponseDettachLockServo(Parcel in) {
        super(in);
        int tmpActuatorType = in.readInt();
        this.ActuatorType = tmpActuatorType == -1 ? null : Actuator.values()[tmpActuatorType];
    }

    public static final Creator<KineticsResponseDettachLockServo> CREATOR = new Creator<KineticsResponseDettachLockServo>() {
        @Override
        public KineticsResponseDettachLockServo createFromParcel(Parcel source) {
            return new KineticsResponseDettachLockServo(source);
        }

        @Override
        public KineticsResponseDettachLockServo[] newArray(int size) {
            return new KineticsResponseDettachLockServo[size];
        }
    };
}
