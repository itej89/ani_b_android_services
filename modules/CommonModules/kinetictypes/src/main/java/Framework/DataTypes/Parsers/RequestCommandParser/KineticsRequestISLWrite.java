//
//  KineticRequestISLWrite.swift
//  FourierMachines.Ani.Client.Kinetics
//
//  Created by Tej Kiran on 07/03/18.
//  Copyright © 2018 FourierMachines. All rights reserved.
//

package Framework.DataTypes.Parsers.RequestCommandParser;

import android.os.Parcel;

import java.util.ArrayList;

import Framework.DataTypes.Constants.CommandLabels;

public class KineticsRequestISLWrite extends KineticsRequest
{
    Integer NoOfBytes;
    Integer Address;
    Integer Value;

    public void PostConstructor()
    {
        String Command = super.formCommand(new String[]{(Address.toString()), (NoOfBytes.toString()), (Value.toString())});

        Request = super.addDelimiters( Command);
    }

    //noOfBytes should always be one
    public KineticsRequestISLWrite(Integer noOfBytes, Integer address, Integer value)
    {
        super(CommandLabels.CommandTypes.ISLW);
        
        NoOfBytes = noOfBytes;
        Address = address;
        Value = value;

        PostConstructor();
    }
    
    public KineticsRequestISLWrite(String command)
    {
        super(CommandLabels.CommandTypes.ISLW);
        String _command = super.removeDelimiters( command);
        ArrayList<String> contents = super.decomposeCommand( _command);
        if(contents.size() == 3)
        {
            Address = Integer.parseInt(contents.get(1));
            NoOfBytes = Integer.parseInt(contents.get(2));
            Value = Integer.parseInt(contents.get(3));
            
        }
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeValue(this.NoOfBytes);
        dest.writeValue(this.Address);
        dest.writeValue(this.Value);
    }

    protected KineticsRequestISLWrite(Parcel in) {
        super(in);
        this.NoOfBytes = (Integer) in.readValue(Integer.class.getClassLoader());
        this.Address = (Integer) in.readValue(Integer.class.getClassLoader());
        this.Value = (Integer) in.readValue(Integer.class.getClassLoader());

        PostConstructor();
    }

    public static final Creator<KineticsRequestISLWrite> CREATOR = new Creator<KineticsRequestISLWrite>() {
        @Override
        public KineticsRequestISLWrite createFromParcel(Parcel source) {
            return new KineticsRequestISLWrite(source);
        }

        @Override
        public KineticsRequestISLWrite[] newArray(int size) {
            return new KineticsRequestISLWrite[size];
        }
    };
}
