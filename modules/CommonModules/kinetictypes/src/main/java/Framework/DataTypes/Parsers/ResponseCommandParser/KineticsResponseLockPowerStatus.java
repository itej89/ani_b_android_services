//
//  KineticsResponseLockPowerStatus.swift
//  FourierMachines.Ani.Client.Kinetics
//
//  Created by Tej Kiran on 28/04/18.
//  Copyright © 2018 FourierMachines. All rights reserved.
//

package Framework.DataTypes.Parsers.ResponseCommandParser;

import android.os.Parcel;

import java.util.ArrayList;

import Framework.DataTypes.Constants.ActuatorPowerStatusSymbols;
import Framework.DataTypes.Constants.CommandLabels;
import Framework.DataTypes.Constants.KineticsResponseAcknowledgement;
import FrameworkInterface.PublicTypes.Config.MachineConfig;
import FrameworkInterface.PublicTypes.Constants.Actuator;


//LPW#ADR#O:LPW#ADR#DATA:
public class KineticsResponseLockPowerStatus extends  KineticsResponse
{
     Actuator ActuatorType;
    public ActuatorPowerStatusSymbols PowerState;
    KineticsResponseAcknowledgement ResponseErrorCondition;
    
    public KineticsResponseLockPowerStatus(String response) {
        super(response);
        if(ResponseType == CommandLabels.CommandTypes.LPW)
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
                    
                    ResponseErrorCondition =  KineticsResponseAcknowledgement.ConvertFromString(ResponseParts.get(2));
                    
                    if(ResponseErrorCondition == null)
                    {
                        PowerState = ActuatorPowerStatusSymbols.ConvertFromString(ResponseParts.get(2));
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
        dest.writeInt(this.PowerState == null ? -1 : this.PowerState.ordinal());
        dest.writeInt(this.ResponseErrorCondition == null ? -1 : this.ResponseErrorCondition.ordinal());
    }

    protected KineticsResponseLockPowerStatus(Parcel in) {
        super(in);
        int tmpActuatorType = in.readInt();
        this.ActuatorType = tmpActuatorType == -1 ? null : Actuator.values()[tmpActuatorType];
        int tmpPowerState = in.readInt();
        this.PowerState = tmpPowerState == -1 ? null : ActuatorPowerStatusSymbols.values()[tmpPowerState];
        int tmpResponseErrorCondition = in.readInt();
        this.ResponseErrorCondition = tmpResponseErrorCondition == -1 ? null : KineticsResponseAcknowledgement.values()[tmpResponseErrorCondition];
    }

    public static final Creator<KineticsResponseLockPowerStatus> CREATOR = new Creator<KineticsResponseLockPowerStatus>() {
        @Override
        public KineticsResponseLockPowerStatus createFromParcel(Parcel source) {
            return new KineticsResponseLockPowerStatus(source);
        }

        @Override
        public KineticsResponseLockPowerStatus[] newArray(int size) {
            return new KineticsResponseLockPowerStatus[size];
        }
    };
}
