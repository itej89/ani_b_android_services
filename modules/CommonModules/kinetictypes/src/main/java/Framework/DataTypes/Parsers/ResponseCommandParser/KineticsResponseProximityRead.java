//
//  KineticsResponseProximityRead.swift
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
import Framework.DataTypes.Constants.ProximityStateSymbols;
import FrameworkInterface.PublicTypes.Config.MachineConfig;
import FrameworkInterface.PublicTypes.Constants.Actuator;


//PRX#ADR#O:PRX#ADR#DATA:
public class KineticsResponseProximityRead extends  KineticsResponse
{
     Actuator ActuatorType;
    public ProximityStateSymbols MountState;
    KineticsResponseAcknowledgement ResponseErrorCondition;
    
    public KineticsResponseProximityRead(String response) {
        super(response);
        if(ResponseType == CommandLabels.CommandTypes.PRX)
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
                        MountState = ProximityStateSymbols.ConvertFromString(ResponseParts.get(2));
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
        dest.writeInt(this.MountState == null ? -1 : this.MountState.ordinal());
        dest.writeInt(this.ResponseErrorCondition == null ? -1 : this.ResponseErrorCondition.ordinal());
    }

    protected KineticsResponseProximityRead(Parcel in) {
        super(in);
        int tmpActuatorType = in.readInt();
        this.ActuatorType = tmpActuatorType == -1 ? null : Actuator.values()[tmpActuatorType];
        int tmpMountState = in.readInt();
        this.MountState = tmpMountState == -1 ? null : ProximityStateSymbols.values()[tmpMountState];
        int tmpResponseErrorCondition = in.readInt();
        this.ResponseErrorCondition = tmpResponseErrorCondition == -1 ? null : KineticsResponseAcknowledgement.values()[tmpResponseErrorCondition];
    }

    public static final Creator<KineticsResponseProximityRead> CREATOR = new Creator<KineticsResponseProximityRead>() {
        @Override
        public KineticsResponseProximityRead createFromParcel(Parcel source) {
            return new KineticsResponseProximityRead(source);
        }

        @Override
        public KineticsResponseProximityRead[] newArray(int size) {
            return new KineticsResponseProximityRead[size];
        }
    };
}
