//
//  KimneticRequestFrequencyCommand.swift
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

public class KineticsRequestFrequency extends KineticsRequestForActuator
{
    public Integer Frequency;


    public void PostConstructor()
    {
        String Command = super.formCommand(new String[]{MachineConfig.Instance.MachineActuatorList.get(ActuatorType).
                Address.toString(), (Frequency.toString())});

        Request = super.addDelimiters( Command);
    }

    public KineticsRequestFrequency(Integer frequency, Actuator actuatorType)
    {
        super(CommandLabels.CommandTypes.FRQ);

        Frequency = frequency;
        ActuatorType = actuatorType;

        PostConstructor();
    }
    
    public KineticsRequestFrequency(String command)
    {
        super(CommandLabels.CommandTypes.FRQ);
        
        String _command = super.removeDelimiters( command);
        ArrayList<String> contents = super.decomposeCommand( _command);
        if(contents.size() == 2)
        {
            Integer address = Integer.parseInt(contents.get(1));
            if(address != null)
            {
                ActuatorType = MachineConfig.Instance.getActuatorWith( address);
                Frequency = Integer.parseInt(contents.get(2));
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
        dest.writeValue(this.Frequency);
    }

    protected KineticsRequestFrequency(Parcel in) {
        super(in);
        this.Frequency = (Integer) in.readValue(Integer.class.getClassLoader());

        PostConstructor();
    }

    public static final Creator<KineticsRequestFrequency> CREATOR = new Creator<KineticsRequestFrequency>() {
        @Override
        public KineticsRequestFrequency createFromParcel(Parcel source) {
            return new KineticsRequestFrequency(source);
        }

        @Override
        public KineticsRequestFrequency[] newArray(int size) {
            return new KineticsRequestFrequency[size];
        }
    };
}
