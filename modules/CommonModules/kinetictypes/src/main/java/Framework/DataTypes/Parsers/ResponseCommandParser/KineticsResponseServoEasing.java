//
//  KineticsResponseServoEasing.swift
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

//EAS#ADR#Func#O:
public class KineticsResponseServoEasing extends  KineticsResponse
{
     Actuator ActuatorType;
    CommandLabels.EasingFunction EasingFunction;
    
    public KineticsResponseServoEasing(String response) {
        super(response);
        if(ResponseType == CommandLabels.CommandTypes.EAS)
        {
            ArrayList<String> RequestAckParts = DecomposedResponse.get(0);
            
            if(RequestAckParts.size() == 4)
            {
                Integer Address = Integer.parseInt(RequestAckParts.get(1));

                ActuatorType = MachineConfig.Instance.getActuatorWith(Address);
                EasingFunction = CommandLabels.EasingFunction.valueOf(RequestAckParts.get(2));
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
        dest.writeInt(this.EasingFunction == null ? -1 : this.EasingFunction.ordinal());
    }

    protected KineticsResponseServoEasing(Parcel in) {
        super(in);
        int tmpActuatorType = in.readInt();
        this.ActuatorType = tmpActuatorType == -1 ? null : Actuator.values()[tmpActuatorType];
        int tmpEasingFunction = in.readInt();
        this.EasingFunction = tmpEasingFunction == -1 ? null : CommandLabels.EasingFunction.values()[tmpEasingFunction];
    }

    public static final Creator<KineticsResponseServoEasing> CREATOR = new Creator<KineticsResponseServoEasing>() {
        @Override
        public KineticsResponseServoEasing createFromParcel(Parcel source) {
            return new KineticsResponseServoEasing(source);
        }

        @Override
        public KineticsResponseServoEasing[] newArray(int size) {
            return new KineticsResponseServoEasing[size];
        }
    };
}

