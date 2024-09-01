//
//  KimneticRequestCELLTwoCommand.swift
//  FourierMachines.Ani.Client.Kinetics
//
//  Created by Tej Kiran on 07/03/18.
//  Copyright © 2018 FourierMachines. All rights reserved.
//

package Framework.DataTypes.Parsers.RequestCommandParser;

import android.os.Parcel;

import Framework.DataTypes.Constants.CommandLabels;

public class KineticsRequestCELLTwo extends KineticsRequest
{
    
    public KineticsRequestCELLTwo()
    {
        super(CommandLabels.CommandTypes.CELL2);
    }
    
    public KineticsRequestCELLTwo(String command)
    {
        super(CommandLabels.CommandTypes.CELL2);
        
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

    protected KineticsRequestCELLTwo(Parcel in) {
        super(in);
    }

    public static final Creator<KineticsRequestCELLTwo> CREATOR = new Creator<KineticsRequestCELLTwo>() {
        @Override
        public KineticsRequestCELLTwo createFromParcel(Parcel source) {
            return new KineticsRequestCELLTwo(source);
        }

        @Override
        public KineticsRequestCELLTwo[] newArray(int size) {
            return new KineticsRequestCELLTwo[size];
        }
    };
}
