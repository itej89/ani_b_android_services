//
//  KimneticRequestVelocityCommand.swift
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

public class KineticsRequestVelocity extends KineticsRequestForActuator
{
    public Integer Velocity;

    public void PostConstructor()
    {
        String Command = super.formCommand(new String[]{MachineConfig.Instance.MachineActuatorList.get(ActuatorType).Address.toString(), (Velocity.toString())});

        Request = super.addDelimiters( Command);
    }


    public KineticsRequestVelocity(Integer velocity, Actuator actuatorType)
    {
        super(CommandLabels.CommandTypes.VEL);
        
        Velocity = velocity;
        ActuatorType = actuatorType;
        PostConstructor();
    }
    
    public KineticsRequestVelocity(String command)
    {
        super(CommandLabels.CommandTypes.VEL);
        
        String _command = super.removeDelimiters( command);
        ArrayList<String> contents = super.decomposeCommand( _command);
        if(contents.size() == 2)
        {
            Integer address = Integer.parseInt(contents.get(1));
            if(address != null)
            {
                ActuatorType = MachineConfig.Instance.getActuatorWith( address);
                Velocity = Integer.parseInt(contents.get(2));
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
        dest.writeValue(this.Velocity);
    }

    protected KineticsRequestVelocity(Parcel in) {
        super(in);
        this.Velocity = (Integer) in.readValue(Integer.class.getClassLoader());
        PostConstructor();
    }

    public static final Creator<KineticsRequestVelocity> CREATOR = new Creator<KineticsRequestVelocity>() {
        @Override
        public KineticsRequestVelocity createFromParcel(Parcel source) {
            return new KineticsRequestVelocity(source);
        }

        @Override
        public KineticsRequestVelocity[] newArray(int size) {
            return new KineticsRequestVelocity[size];
        }
    };
}
