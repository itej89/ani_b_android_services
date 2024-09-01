//
//  KineticRequestISLRead.swift
//  FourierMachines.Ani.Client.Kinetics
//
//  Created by Tej Kiran on 07/03/18.
//  Copyright © 2018 FourierMachines. All rights reserved.
//

package Framework.DataTypes.Parsers.RequestCommandParser;

import android.os.Parcel;

import java.util.ArrayList;

import Framework.DataTypes.Constants.CommandLabels;

public class KineticsRequestISLRead extends KineticsRequest
{
    Integer NoOfBytes;
    Integer Address;

    public void PostConstructor()
    {
        String Command = super.formCommand(new String[]{(Address.toString()), (NoOfBytes.toString())});

        Request = super.addDelimiters( Command);
    }

    public KineticsRequestISLRead(Integer noOfBytes, Integer address)
    {
        super(CommandLabels.CommandTypes.ISLR);
        
        NoOfBytes = noOfBytes;
        Address = address;

        PostConstructor();
    }
    
    public KineticsRequestISLRead(String command)
    {
        super(CommandLabels.CommandTypes.ISLR);
        String _command = super.removeDelimiters( command);
        ArrayList<String> contents = super.decomposeCommand( _command);
        if(contents.size() == 2)
        {
            Address = Integer.parseInt(contents.get(1));
            NoOfBytes = Integer.parseInt(contents.get(2));
            
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
    }

    protected KineticsRequestISLRead(Parcel in) {
        super(in);
        this.NoOfBytes = (Integer) in.readValue(Integer.class.getClassLoader());
        this.Address = (Integer) in.readValue(Integer.class.getClassLoader());

        PostConstructor();
    }

    public static final Creator<KineticsRequestISLRead> CREATOR = new Creator<KineticsRequestISLRead>() {
        @Override
        public KineticsRequestISLRead createFromParcel(Parcel source) {
            return new KineticsRequestISLRead(source);
        }

        @Override
        public KineticsRequestISLRead[] newArray(int size) {
            return new KineticsRequestISLRead[size];
        }
    };
}
