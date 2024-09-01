//
//  KimneticRequestTriggerCommand.swift
//  FourierMachines.Ani.Client.Kinetics
//
//  Created by Tej Kiran on 07/03/18.
//  Copyright © 2018 FourierMachines. All rights reserved.
//

package Framework.DataTypes.Parsers.RequestCommandParser;

import android.os.Parcel;

import Framework.DataTypes.Constants.CommandLabels;
import FrameworkInterface.PublicTypes.Config.MachineConfig;

public class KineticsRequestTrigger extends KineticsRequest
{
    public void PostConstructor()
    {
        String Command  = super.formCommand(new String[]{});
        Request = super.addDelimiters(Command);
    }

    public KineticsRequestTrigger()
    {
        super(CommandLabels.CommandTypes.TRG);
        PostConstructor();
    }
    
    public KineticsRequestTrigger(String command)
    {
        super(CommandLabels.CommandTypes.TRG);
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

    protected KineticsRequestTrigger(Parcel in) {
        super(in);
        PostConstructor();
    }

    public static final Creator<KineticsRequestTrigger> CREATOR = new Creator<KineticsRequestTrigger>() {
        @Override
        public KineticsRequestTrigger createFromParcel(Parcel source) {
            return new KineticsRequestTrigger(source);
        }

        @Override
        public KineticsRequestTrigger[] newArray(int size) {
            return new KineticsRequestTrigger[size];
        }
    };
}
