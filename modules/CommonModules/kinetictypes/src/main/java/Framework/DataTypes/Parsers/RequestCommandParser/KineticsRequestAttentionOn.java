//
//  KimneticRequestAttentionOnCommand.swift
//  FourierMachines.Ani.Client.Kinetics
//
//  Created by Tej Kiran on 07/03/18.
//  Copyright © 2018 FourierMachines. All rights reserved.
//

package Framework.DataTypes.Parsers.RequestCommandParser;

import android.os.Parcel;

import Framework.DataTypes.Constants.CommandLabels;

public class KineticsRequestAttentionOn extends KineticsRequest
{
    public void PostConstructor()
    {
        String Command = super.formCommand(new String[]{});

        Request = super.addDelimiters(Command);
    }

    public KineticsRequestAttentionOn()
    {
        super(CommandLabels.CommandTypes.VEN);
        PostConstructor();
    }
    
    public KineticsRequestAttentionOn(String command)
    {
        super(CommandLabels.CommandTypes.VEN);
        
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

    protected KineticsRequestAttentionOn(Parcel in) {
        super(in);
        PostConstructor();
    }

    public static final Creator<KineticsRequestAttentionOn> CREATOR = new Creator<KineticsRequestAttentionOn>() {
        @Override
        public KineticsRequestAttentionOn createFromParcel(Parcel source) {
            return new KineticsRequestAttentionOn(source);
        }

        @Override
        public KineticsRequestAttentionOn[] newArray(int size) {
            return new KineticsRequestAttentionOn[size];
        }
    };
}
