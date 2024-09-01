//
//  KineticsResponseLockSignalStatus.swift
//  FourierMachines.Ani.Client.Kinetics
//
//  Created by Tej Kiran on 28/04/18.
//  Copyright © 2018 FourierMachines. All rights reserved.
//

package Framework.DataTypes.Parsers.ResponseCommandParser;


import android.os.Parcel;

import java.util.ArrayList;

import Framework.DataTypes.Constants.ActuatorSignalStatusSymbols;
import Framework.DataTypes.Constants.CommandLabels;
import Framework.DataTypes.Constants.KineticsResponseAcknowledgement;
import FrameworkInterface.PublicTypes.Config.MachineConfig;
import FrameworkInterface.PublicTypes.Constants.Actuator;


//LSA#ADR#O:LSA#ADR#DATA:
public class KineticsResponseLockSignalStatus extends  KineticsResponse
{
     Actuator ActuatorType;
    public ActuatorSignalStatusSymbols SignalState;
    KineticsResponseAcknowledgement ResponseErrorCondition;
    
    public KineticsResponseLockSignalStatus(String response) {
        super(response);
        if(ResponseType == CommandLabels.CommandTypes.LSA)
        {
            ArrayList<String> RequestParts = DecomposedResponse.get(0);
            ArrayList<String> ResponseParts = DecomposedResponse.get(1);
            
            if(RequestParts.size() == 3)
            {
                RequestRecievedAck =  KineticsResponseAcknowledgement.ConvertFromString(RequestParts.get(2));
            }
            
            if(RequestRecievedAck != null && RequestRecievedAck == KineticsResponseAcknowledgement.OK)
            {
                if(ResponseParts.size() == 3)
                {
                    Integer Address = Integer.parseInt(ResponseParts.get(1));

                    ActuatorType = MachineConfig.Instance.getActuatorWith(Address);
                    
                    ResponseErrorCondition = KineticsResponseAcknowledgement.ConvertFromString(ResponseParts.get(2));
                    
                    if(ResponseErrorCondition == null)
                    {
                        SignalState = ActuatorSignalStatusSymbols.ConvertFromString(ResponseParts.get(2));
                    }
                    
                }
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
        dest.writeInt(this.SignalState == null ? -1 : this.SignalState.ordinal());
        dest.writeInt(this.ResponseErrorCondition == null ? -1 : this.ResponseErrorCondition.ordinal());
    }

    protected KineticsResponseLockSignalStatus(Parcel in) {
        super(in);
        int tmpActuatorType = in.readInt();
        this.ActuatorType = tmpActuatorType == -1 ? null : Actuator.values()[tmpActuatorType];
        int tmpSignalState = in.readInt();
        this.SignalState = tmpSignalState == -1 ? null : ActuatorSignalStatusSymbols.values()[tmpSignalState];
        int tmpResponseErrorCondition = in.readInt();
        this.ResponseErrorCondition = tmpResponseErrorCondition == -1 ? null : KineticsResponseAcknowledgement.values()[tmpResponseErrorCondition];
    }

    public static final Creator<KineticsResponseLockSignalStatus> CREATOR = new Creator<KineticsResponseLockSignalStatus>() {
        @Override
        public KineticsResponseLockSignalStatus createFromParcel(Parcel source) {
            return new KineticsResponseLockSignalStatus(source);
        }

        @Override
        public KineticsResponseLockSignalStatus[] newArray(int size) {
            return new KineticsResponseLockSignalStatus[size];
        }
    };
}
