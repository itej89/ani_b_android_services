//
//  KineticsRequestLeftLockServoAngle.swift
//  FourierMachines.Ani.Client.Kinetics
//
//  Created by Tej Kiran on 08/03/18.
//  Copyright © 2018 FourierMachines. All rights reserved.
//

package Framework.DataTypes.Parsers.RequestCommandParser;


import android.location.Address;
import android.os.Parcel;

import java.util.ArrayList;

import Framework.DataTypes.Constants.CommandLabels;
import Framework.MachineCommandHelper;
import FrameworkInterface.PublicTypes.Config.MachineConfig;
import FrameworkInterface.PublicTypes.Constants.Actuator;

public class KineticsRequestLeftLockServoAngle extends KineticsRequestForActuator
{
    Integer Angle;
    Integer PWM_VALUE;

    public void PostConstructor()
    {
        MachineCommandHelper CommandHelper = new MachineCommandHelper();
        PWM_VALUE = CommandHelper.ConvertAngleToPWMValue(Angle);
        String Command = super.formCommand(new String[]{MachineConfig.Instance.MachineActuatorList.get(ActuatorType).Address.toString(), PWM_VALUE.toString()});

        Request = super.addDelimiters(Command);
    }

    public KineticsRequestLeftLockServoAngle(Integer angle, Actuator actuatorType)
    {
        super(CommandLabels.CommandTypes.LLK);

        Angle = angle;
        ActuatorType = actuatorType;

        PostConstructor();
    }
    
    public KineticsRequestLeftLockServoAngle(String command)
    {
        super(CommandLabels.CommandTypes.LLK);
        String _command = super.removeDelimiters(command);
        ArrayList<String> contents = super.decomposeCommand(_command);
        if(contents.size() == 2)
        {
            Integer address = Integer.parseInt(contents.get(1));
            if(address != null)
            {
                ActuatorType = MachineConfig.Instance.getActuatorWith(address);
                Angle = Integer.parseInt(contents.get(2));
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
        dest.writeValue(this.Angle);
        dest.writeValue(this.PWM_VALUE);
    }

    protected KineticsRequestLeftLockServoAngle(Parcel in) {
        super(in);
        this.Angle = (Integer) in.readValue(Integer.class.getClassLoader());
        this.PWM_VALUE = (Integer) in.readValue(Integer.class.getClassLoader());

        PostConstructor();
    }

    public static final Creator<KineticsRequestLeftLockServoAngle> CREATOR = new Creator<KineticsRequestLeftLockServoAngle>() {
        @Override
        public KineticsRequestLeftLockServoAngle createFromParcel(Parcel source) {
            return new KineticsRequestLeftLockServoAngle(source);
        }

        @Override
        public KineticsRequestLeftLockServoAngle[] newArray(int size) {
            return new KineticsRequestLeftLockServoAngle[size];
        }
    };
}
