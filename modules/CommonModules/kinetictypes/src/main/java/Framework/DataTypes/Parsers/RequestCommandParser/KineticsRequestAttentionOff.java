//
//  KimneticRequestAttentionOffCommand.swift
//  FourierMachines.Ani.Client.Kinetics
//
//  Created by Tej Kiran on 07/03/18.
//  Copyright © 2018 FourierMachines. All rights reserved.
//

package Framework.DataTypes.Parsers.RequestCommandParser;

import android.os.Parcel;

import Framework.DataTypes.Constants.CommandLabels;
import FrameworkInterface.PublicTypes.Config.MachineConfig;

public class KineticsRequestAttentionOff extends KineticsRequest
{
    public void PostConstructor()
    {
        String Command = super.formCommand(new String[]{});

        Request = super.addDelimiters(Command);
    }

    public KineticsRequestAttentionOff()
    {
        super(CommandLabels.CommandTypes.VNO);

        PostConstructor();
    }
    
    public KineticsRequestAttentionOff(String command)
    {
        super(CommandLabels.CommandTypes.VNO);
        
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

    protected KineticsRequestAttentionOff(Parcel in) {
        super(in);

        PostConstructor();
    }

    public static final Creator<KineticsRequestAttentionOff> CREATOR = new Creator<KineticsRequestAttentionOff>() {
        @Override
        public KineticsRequestAttentionOff createFromParcel(Parcel source) {
            return new KineticsRequestAttentionOff(source);
        }

        @Override
        public KineticsRequestAttentionOff[] newArray(int size) {
            return new KineticsRequestAttentionOff[size];
        }
    };
}
