//
//  KimneticRequestProximityReadCommand.swift
//  FourierMachines.Ani.Client.Kinetics
//
//  Created by Tej Kiran on 07/03/18.
//  Copyright © 2018 FourierMachines. All rights reserved.
//

package Framework.DataTypes.Parsers.RequestCommandParser;

import android.os.Parcel;

import java.util.ArrayList;

import Framework.DataTypes.Constants.CommandLabels;
import FrameworkInterface.PublicTypes.Config.MachineConfig;
import FrameworkInterface.PublicTypes.Constants.Actuator;

public class KineticsRequestProximityRead extends KineticsRequest
{
    Actuator ActuatorType;

    public void PostConstructor()
    {
        String Command = super.formCommand(new String[]{MachineConfig.Instance.MachineActuatorList.get(ActuatorType).Address.toString()});
        Request = super.addDelimiters(Command);
    }

    public KineticsRequestProximityRead(Actuator actuatorType)
    {
        super(CommandLabels.CommandTypes.PRX);
        
        ActuatorType = actuatorType;
        PostConstructor();
    }
    
    public KineticsRequestProximityRead(String command)
    {
        super(CommandLabels.CommandTypes.PRX);
        
        String _command = super.removeDelimiters( command);
        ArrayList<String> contents = super.decomposeCommand( _command);
        
        if(contents.size() == 1)
        {
            Integer address = Integer.parseInt(contents.get(0));
            if(address != null)
            {
                ActuatorType = MachineConfig.Instance.getActuatorWith( address);
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

    protected KineticsRequestProximityRead(Parcel in) {
        super(in);
        int tmpActuatorType = in.readInt();
        this.ActuatorType = tmpActuatorType == -1 ? null : Actuator.values()[tmpActuatorType];
        PostConstructor();
    }

    public static final Creator<KineticsRequestProximityRead> CREATOR = new Creator<KineticsRequestProximityRead>() {
        @Override
        public KineticsRequestProximityRead createFromParcel(Parcel source) {
            return new KineticsRequestProximityRead(source);
        }

        @Override
        public KineticsRequestProximityRead[] newArray(int size) {
            return new KineticsRequestProximityRead[size];
        }
    };
}
