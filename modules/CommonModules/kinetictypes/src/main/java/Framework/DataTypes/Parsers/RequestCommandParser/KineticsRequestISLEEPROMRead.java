//
//  KineticRequestISLEEPROMREAD.swift
//  FourierMachines.Ani.Client.Kinetics
//
//  Created by Tej Kiran on 07/03/18.
//  Copyright © 2018 FourierMachines. All rights reserved.
//

package Framework.DataTypes.Parsers.RequestCommandParser;

import android.os.Parcel;

import java.util.ArrayList;

import Framework.DataTypes.Constants.CommandLabels;

public class KineticsRequestISLEEPROMRead extends KineticsRequest
{
    Integer NoOfBytes;
    Integer Address;


    public void PostConstructor()
    {
        String Command = super.formCommand(new String[]{(Address.toString()), (NoOfBytes.toString())});
        Request = super.addDelimiters(Command);
    }

    public KineticsRequestISLEEPROMRead(Integer noOfBytes, Integer address)
    {
        super( CommandLabels.CommandTypes.ISLER);
        
        NoOfBytes = noOfBytes;
        Address = address;

        PostConstructor();
    }
    
    public KineticsRequestISLEEPROMRead(String command)
    {
        super(CommandLabels.CommandTypes.ISLER);
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

    protected KineticsRequestISLEEPROMRead(Parcel in) {
        super(in);
        this.NoOfBytes = (Integer) in.readValue(Integer.class.getClassLoader());
        this.Address = (Integer) in.readValue(Integer.class.getClassLoader());

        PostConstructor();
    }

    public static final Creator<KineticsRequestISLEEPROMRead> CREATOR = new Creator<KineticsRequestISLEEPROMRead>() {
        @Override
        public KineticsRequestISLEEPROMRead createFromParcel(Parcel source) {
            return new KineticsRequestISLEEPROMRead(source);
        }

        @Override
        public KineticsRequestISLEEPROMRead[] newArray(int size) {
            return new KineticsRequestISLEEPROMRead[size];
        }
    };
}
