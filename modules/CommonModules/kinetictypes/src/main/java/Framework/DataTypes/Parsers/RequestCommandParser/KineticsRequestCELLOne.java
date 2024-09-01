//
//  KimneticRequestCELLOneCommand.swift
//  FourierMachines.Ani.Client.Kinetics
//
//  Created by Tej Kiran on 07/03/18.
//  Copyright © 2018 FourierMachines. All rights reserved.
//

package Framework.DataTypes.Parsers.RequestCommandParser;

import android.os.Parcel;

import Framework.DataTypes.Constants.CommandLabels;

public class KineticsRequestCELLOne extends KineticsRequest
{
    
    public KineticsRequestCELLOne()
    {
        super(CommandLabels.CommandTypes.CELL1);
    }
    
    public KineticsRequestCELLOne(String command)
    {
        super(CommandLabels.CommandTypes.CELL1);
        
        super.removeDelimiters( command);
        
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
    }

    protected KineticsRequestCELLOne(Parcel in) {
        super(in);
    }

    public static final Creator<KineticsRequestCELLOne> CREATOR = new Creator<KineticsRequestCELLOne>() {
        @Override
        public KineticsRequestCELLOne createFromParcel(Parcel source) {
            return new KineticsRequestCELLOne(source);
        }

        @Override
        public KineticsRequestCELLOne[] newArray(int size) {
            return new KineticsRequestCELLOne[size];
        }
    };
}
