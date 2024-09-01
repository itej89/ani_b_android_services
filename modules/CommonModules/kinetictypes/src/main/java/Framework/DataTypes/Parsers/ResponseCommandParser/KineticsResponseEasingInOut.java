//
//  KineticsResponseEasingInOut.swift
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

//INO#ADR#Type#O:
public class KineticsResponseEasingInOut extends  KineticsResponse
{
     Actuator ActuatorType;
    CommandLabels.EasingType EasingType;
    
    public KineticsResponseEasingInOut(String response) {
        super(response);
        if(ResponseType == CommandLabels.CommandTypes.EAS)
        {
            ArrayList<String> RequestAckParts = DecomposedResponse.get(0);
            
            if(RequestAckParts.size() == 4)
            {
                Integer Address = Integer.parseInt(RequestAckParts.get(1));

                ActuatorType = MachineConfig.Instance.getActuatorWith(Address);
                EasingType = CommandLabels.EasingType.valueOf(RequestAckParts.get(2));
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
        dest.writeInt(this.EasingType == null ? -1 : this.EasingType.ordinal());
    }

    protected KineticsResponseEasingInOut(Parcel in) {
        super(in);
        int tmpActuatorType = in.readInt();
        this.ActuatorType = tmpActuatorType == -1 ? null : Actuator.values()[tmpActuatorType];
        int tmpEasingType = in.readInt();
        this.EasingType = tmpEasingType == -1 ? null : CommandLabels.EasingType.values()[tmpEasingType];
    }

    public static final Creator<KineticsResponseEasingInOut> CREATOR = new Creator<KineticsResponseEasingInOut>() {
        @Override
        public KineticsResponseEasingInOut createFromParcel(Parcel source) {
            return new KineticsResponseEasingInOut(source);
        }

        @Override
        public KineticsResponseEasingInOut[] newArray(int size) {
            return new KineticsResponseEasingInOut[size];
        }
    };
}

