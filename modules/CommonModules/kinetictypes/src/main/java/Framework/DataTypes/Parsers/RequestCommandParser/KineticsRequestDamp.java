//
//  KimneticRequestDampCommand.swift
//  FourierMachines.Ani.Client.Kinetics
//
//  Created by Tej Kiran on 07/03/18.
//  Copyright © 2018 FourierMachines. All rights reserved.
//

package Framework.DataTypes.Parsers.RequestCommandParser;

import android.location.Address;
import android.os.Parcel;

import java.util.ArrayList;

import Framework.DataTypes.Constants.CommandLabels;
import FrameworkInterface.PublicTypes.Config.MachineConfig;
import FrameworkInterface.PublicTypes.Constants.Actuator;

public class KineticsRequestDamp extends KineticsRequestForActuator
{
    public Integer Damping;

    public void PostConstructor()
    {
        String Command = super.formCommand(new String[]{MachineConfig.Instance.MachineActuatorList.get(ActuatorType).
                Address.toString(), Damping.toString()});

        Request = super.addDelimiters(Command);
    }


    public KineticsRequestDamp(Integer damping, Actuator actuatorType)
    {
        super(CommandLabels.CommandTypes.DMP);
        
        Damping = damping;
        ActuatorType = actuatorType;

        PostConstructor();
    }
    
    public KineticsRequestDamp(String command)
    {
        super(CommandLabels.CommandTypes.DMP);
        
        String _command = super.removeDelimiters( command);
        ArrayList<String> contents = super.decomposeCommand( _command);
        if(contents.size() == 2)
        {
            Integer address = Integer.parseInt(contents.get(1));
            if(address != null)
            {
                ActuatorType = MachineConfig.Instance.getActuatorWith( address);
                Damping = Integer.parseInt(contents.get(2));
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
        dest.writeValue(this.Damping);
    }

    protected KineticsRequestDamp(Parcel in) {
        super(in);
        this.Damping = (Integer) in.readValue(Integer.class.getClassLoader());

        PostConstructor();
    }

    public static final Creator<KineticsRequestDamp> CREATOR = new Creator<KineticsRequestDamp>() {
        @Override
        public KineticsRequestDamp createFromParcel(Parcel source) {
            return new KineticsRequestDamp(source);
        }

        @Override
        public KineticsRequestDamp[] newArray(int size) {
            return new KineticsRequestDamp[size];
        }
    };
}
