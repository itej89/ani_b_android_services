//
//  KimneticRequestTimingCommand.swift
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

public class KineticsRequestTiming extends KineticsRequestForActuator
{
    public Integer Timing;

    public void PostConstructor()
    {
        String Command = super.formCommand(new String[]{MachineConfig.Instance.MachineActuatorList.get(ActuatorType).Address.toString(), Timing.toString()});

        Request = super.addDelimiters( Command);
    }

    public KineticsRequestTiming(Integer timing, Actuator actuatorType)
    {
        super(CommandLabels.CommandTypes.TMG);
        
        Timing = timing;
        ActuatorType = actuatorType;
        PostConstructor();
    }
    
    public KineticsRequestTiming(String command)
    {
        super(CommandLabels.CommandTypes.TMG);
        
        String _command = super.removeDelimiters(command);
        ArrayList<String> contents = super.decomposeCommand(_command);
        if(contents.size() == 2)
        {
            Integer address = Integer.parseInt(contents.get(1));
            if(address != null)
            {
                ActuatorType = MachineConfig.Instance.getActuatorWith(address);
                Timing = Integer.parseInt(contents.get(2));
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
        dest.writeValue(this.Timing);
    }

    protected KineticsRequestTiming(Parcel in) {
        super(in);
        this.Timing = (Integer) in.readValue(Integer.class.getClassLoader());
        PostConstructor();
    }

    public static final Creator<KineticsRequestTiming> CREATOR = new Creator<KineticsRequestTiming>() {
        @Override
        public KineticsRequestTiming createFromParcel(Parcel source) {
            return new KineticsRequestTiming(source);
        }

        @Override
        public KineticsRequestTiming[] newArray(int size) {
            return new KineticsRequestTiming[size];
        }
    };
}
