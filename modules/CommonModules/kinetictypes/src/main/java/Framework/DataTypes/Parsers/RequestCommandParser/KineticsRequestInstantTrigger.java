//
//  KimneticRequestInstantTriggerCommand.swift
//  FourierMachines.Ani.Client.Kinetics
//
//  Created by Tej Kiran on 07/03/18.
//  Copyright © 2018 FourierMachines. All rights reserved.
//

package Framework.DataTypes.Parsers.RequestCommandParser;

import android.os.Parcel;

import Framework.DataTypes.Constants.CommandLabels;
import FrameworkInterface.PublicTypes.Config.MachineConfig;

public class KineticsRequestInstantTrigger extends KineticsRequest
{

    public void PostConstructor()
    {
        String Command  = super.formCommand(new String[]{});
        Request = super.addDelimiters(Command);
    }

    public KineticsRequestInstantTrigger()
    {
        super(CommandLabels.CommandTypes.ITRG);

        PostConstructor();
    }
    
    public KineticsRequestInstantTrigger(String command)
    {
        super(CommandLabels.CommandTypes.ITRG);
        super.removeDelimiters(command);
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
    }

    protected KineticsRequestInstantTrigger(Parcel in) {
        super(in);

        PostConstructor();
    }

    public static final Creator<KineticsRequestInstantTrigger> CREATOR = new Creator<KineticsRequestInstantTrigger>() {
        @Override
        public KineticsRequestInstantTrigger createFromParcel(Parcel source) {
            return new KineticsRequestInstantTrigger(source);
        }

        @Override
        public KineticsRequestInstantTrigger[] newArray(int size) {
            return new KineticsRequestInstantTrigger[size];
        }
    };
}
