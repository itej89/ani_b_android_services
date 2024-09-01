//
//  KimneticRequestCELLThreeCommand.swift
//  FourierMachines.Ani.Client.Kinetics
//
//  Created by Tej Kiran on 07/03/18.
//  Copyright © 2018 FourierMachines. All rights reserved.
//

package Framework.DataTypes.Parsers.RequestCommandParser;

import android.os.Parcel;

import Framework.DataTypes.Constants.CommandLabels;

public class KineticsRequestCELLThree extends KineticsRequest
{
    
    public KineticsRequestCELLThree()
    {
        super(CommandLabels.CommandTypes.CELL3);
    }
    
    public KineticsRequestCELLThree(String command)
    {
        super(CommandLabels.CommandTypes.CELL3);
        
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

    protected KineticsRequestCELLThree(Parcel in) {
        super(in);
    }

    public static final Creator<KineticsRequestCELLThree> CREATOR = new Creator<KineticsRequestCELLThree>() {
        @Override
        public KineticsRequestCELLThree createFromParcel(Parcel source) {
            return new KineticsRequestCELLThree(source);
        }

        @Override
        public KineticsRequestCELLThree[] newArray(int size) {
            return new KineticsRequestCELLThree[size];
        }
    };
}
