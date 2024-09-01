//
//  KineticsResponseVelocity.swift
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

//VEL#ADR#K#O:
public class KineticsResponseVelocity extends  KineticsResponse
{
     Actuator ActuatorType;
     Integer Velocity;
    
    public KineticsResponseVelocity(String response) {
        super(response);
        if(ResponseType == CommandLabels.CommandTypes.VEL)
        {
            ArrayList<String> RequestAckParts = DecomposedResponse.get(0);
            
            if(RequestAckParts.size() == 4)
            {
                Integer Address = Integer.parseInt(RequestAckParts.get(1));

                ActuatorType = MachineConfig.Instance.getActuatorWith(Address);
                Velocity = Integer.parseInt(RequestAckParts.get(2));
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
        dest.writeValue(this.Velocity);
    }

    protected KineticsResponseVelocity(Parcel in) {
        super(in);
        int tmpActuatorType = in.readInt();
        this.ActuatorType = tmpActuatorType == -1 ? null : Actuator.values()[tmpActuatorType];
        this.Velocity = (Integer) in.readValue(Integer.class.getClassLoader());
    }

    public static final Creator<KineticsResponseVelocity> CREATOR = new Creator<KineticsResponseVelocity>() {
        @Override
        public KineticsResponseVelocity createFromParcel(Parcel source) {
            return new KineticsResponseVelocity(source);
        }

        @Override
        public KineticsResponseVelocity[] newArray(int size) {
            return new KineticsResponseVelocity[size];
        }
    };
}

